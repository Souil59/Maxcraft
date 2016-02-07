package fr.maxcraft.server.game;

import fr.maxcraft.Main;
import fr.maxcraft.player.faction.Faction;
import net.nathem.script.core.Map;
import net.nathem.script.core.NSCore;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.editor.Editor;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Crevebedaine on 23/01/2016.
 */
public class GameInstance {

    private static ArrayList<GameInstance> instances = new ArrayList<GameInstance>();
    private Faction faction;
    private String sourceWorldName;
    private String instanceWorldName;
    private World instanceWorld;
    private NathemWorld nathemWorld;
    private Game game;
    private HashMap<Player, Location> backLocations;
    private HashMap<Player, Integer> life= new HashMap<Player, Integer>();
    private InstanceStatus status;

    public GameInstance(Game game, String instanceWorldName) {
        this.setStatus(InstanceStatus.END);
        this.sourceWorldName = game.getSource();
        this.instanceWorldName = "[game]"+instanceWorldName;
        this.game = game;
        this.instanceWorld = null;
        this.nathemWorld = null;
        this.backLocations = new HashMap<Player, Location>();
        instances.add(this);
    }

    public static ArrayList<GameInstance> getInstances() {
        return instances;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public NathemWorld build() {
        File sourceWorldFile = new File(this.sourceWorldName);
        File instanceFile = new File(this.instanceWorldName);

        if (!sourceWorldFile.exists()) {

            NSCore.log("World <" + instanceWorldName + "> could not be loaded because the source world <" + sourceWorldName + "> not exists in files");
            return null;
        }

        World oldInstance = Bukkit.getWorld(this.instanceWorldName);
        if (oldInstance != null) {
            NSCore.log("World <" + instanceWorldName + "> is already loaded, unloading world...");
            Bukkit.unloadWorld(oldInstance, false);
        }

        if (instanceFile.exists()) {
            NSCore.log("World <" + instanceWorldName + "> already exists in files, removing files...");
            instanceFile.delete();
        }

        // Copy

        try {
            FileUtils.copyDirectory(sourceWorldFile, instanceFile);
        } catch (IOException e) {

            NSCore.log("Error copying file to create instance <" + instanceWorldName + ">");
            return null;
        }

        // Removing uid.dat

        File uidFile = new File(instanceFile, "uid.dat");
        uidFile.delete();

        // World loading
        this.instanceWorld = Bukkit.createWorld(WorldCreator.name(this.instanceWorldName));

        if (this.instanceWorld == null) {

            NSCore.log("World <" + instanceWorldName + "> cannot be loaded...");
            return null;
        }


        // Map loading
        Map map = null;

        try {
            map = new Map(this.sourceWorldName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (map == null) return null;

        // NathemWorld loading
        this.nathemWorld = new NathemWorld(Main.getNSCore(), this.instanceWorldName, map);

        this.setStatus(InstanceStatus.OPEN);
        if (this.getFaction()!=null)
            this.setStatus(InstanceStatus.FACTION);

        new Task(this).runTaskTimer(Main.getPlugin(),120,20);
        return this.nathemWorld;

    }

    public void destroy() {
        // Teleport back
        for (Player p : this.getInstanceWorld().getPlayers()) {
            this.teleportBack(p);
        }

        boolean unload = Bukkit.unloadWorld(this.getInstanceWorld(), true);

        if (unload == false) {
            NSCore.log("Error unloading world <" + instanceWorldName + ">...");
        }

        World sourceWorld = Bukkit.getWorld(this.sourceWorldName);
        File testWorldFile = new File(this.instanceWorldName);

        try {
            FileUtils.deleteDirectory(testWorldFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.setStatus(InstanceStatus.END);
    }

    public void teleportBack(Player p) {
        Location backLocation = this.getBackLocations().get(p);
        if (backLocation == null) backLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
        p.teleport(backLocation);

    }

    @Override
    public String toString() {
        return "Instance [sourceWorldName=" + sourceWorldName
                + ", instanceWorldName=" + instanceWorldName + "]";
    }

    public String getSourceWorldName() {
        return sourceWorldName;
    }

    public World getInstanceWorld() {
        return instanceWorld;
    }

    public NathemWorld getNathemWorld() {
        return nathemWorld;
    }

    public World getSourceWorld() {
        return Bukkit.getWorld(this.sourceWorldName);
    }

    public HashMap<Player, Location> getBackLocations() {
        return backLocations;
    }

    public void teleport(Player p){
        int i;
        for(i = 0;i<100;i++) {
            if (this.getNathemWorld().getMarkers().containsKey(this.game.getEntrance() + ":" + i))
                for (Player pl : this.getInstanceWorld().getPlayers())
                    if (this.getNathemWorld().getMarkers().get(this.game.getEntrance() + ":" + i).distance(pl.getLocation()) < 2) {
                        break;
                    } else {
                        this.backLocations.put(p,p.getLocation());
                        p.teleport((this.getNathemWorld().getMarkers().get(this.game.getEntrance() + ":" + i)));
                        if (this.life.containsKey(p))
                            this.life.put(p, game.getLife());
                        return;
                    }
        }
        if (this.getNathemWorld().getMarkers().containsKey(this.game.getEntrance())){
            this.backLocations.put(p,p.getLocation());
            p.teleport(this.getNathemWorld().getMarkers().get(this.game.getEntrance()));
            if (!this.life.containsKey(p))
                this.life.put(p, game.getLife());
            return;
        }
        p.sendMessage(ChatColor.RED+"L'entrée n'existe pas!");
    }

    public InstanceStatus getStatus() {
        if (status.equals(InstanceStatus.OPEN)||status.equals(InstanceStatus.FACTION))
            if (this.inGame().size()>=this.getGame().getMax())
                return InstanceStatus.CLOSE;
        return status;
    }

    public void setStatus(InstanceStatus status) {
        this.status = status;
    }

    public void close(){
        this.setStatus(InstanceStatus.CLOSE);
    }

    public HashMap<Player, Integer> getLife() {
        return life;
    }

    public Game getGame(){
        return this.game;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public ArrayList<Player> inGame()
    {
        ArrayList<Player> ps= new ArrayList<Player>();
        for (Player p : this.instanceWorld.getPlayers())
            if (p.getGameMode().equals(GameMode.ADVENTURE)||p.getGameMode().equals(GameMode.SURVIVAL))
                ps.add(p);
        return ps;
    }
    public ArrayList<Player> ModoInGame()
    {
        ArrayList<Player> ps= new ArrayList<Player>();
        for (Player p : this.instanceWorld.getPlayers())
            if (!p.getGameMode().equals(GameMode.ADVENTURE)||p.getGameMode().equals(GameMode.SURVIVAL))
                if (p.hasPermission("maxcraft.modo"))
                    ps.add(p);
        return ps;
    }




    public class Task extends BukkitRunnable {
        private GameInstance instance;

        public Task(GameInstance instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            if (instance.inGame().isEmpty()&&instance.ModoInGame().isEmpty()){
                NSCore.log("Destroy instance");
                instance.destroy();
                this.cancel();
            }
        }
    }
}