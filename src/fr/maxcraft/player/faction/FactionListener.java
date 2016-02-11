package fr.maxcraft.player.faction;

import fr.maxcraft.player.User;
import fr.maxcraft.server.game.GameInstance;
import fr.maxcraft.server.zone.Zone;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.maxcraft.Main;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;

public class FactionListener implements Listener {
	
	public FactionListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onco(PlayerJoinEvent e){
		User u = User.get(e.getPlayer());
        Scoreboard s = e.getPlayer().getScoreboard();
        for (Faction f : Faction.factionlist)
            updateTeam(e.getPlayer(),f,ChatColor.WHITE );
        if (u.getFaction()!=null) {
            u.getFaction().load(u);
            for (Faction f : u.getFaction().getAllies())
                updateTeam(e.getPlayer(),f,ChatColor.GREEN );

            for (Faction f : u.getFaction().getEnnemies())
                updateTeam(e.getPlayer(),f,ChatColor.RED);
            updateTeam(e.getPlayer(),u.getFaction(),ChatColor.GREEN );
        }
		u.getPlayer().setScoreboard(s);
        u.getPlayer().setPlayerListName(u.getPerms().dysplayName());
	}

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        for (GameInstance g : GameInstance.getInstances())
            if (e.getPlayer().getWorld().equals(g.getInstanceWorld()))
                    return;
        if (Zone.getZone(e.getPlayer().getLocation())!=null)
        if (Zone.getZone(e.getPlayer().getLocation()).getHighestOwner() instanceof Faction)
            if (!Zone.getZone(e.getPlayer().getLocation()).getHighestOwner().equals(User.get(e.getPlayer()).getFaction())) {
                e.setRespawnLocation(((Faction)Zone.getZone(e.getPlayer().getLocation()).getHighestOwner()).getJail());
                e.getPlayer().sendMessage("Vous avez était fait prisonniers!");
                new Task(User.get(e.getPlayer())).runTaskLater(Main.getPlugin(),12000);
                return;
            }
        if (User.get(e.getPlayer()).getFaction()==null)
            return;
        e.setRespawnLocation(User.get(e.getPlayer()).getFaction().getSpawn());
    }


    public void updateTeam(Player p, Faction f,ChatColor c){
        ScoreboardTeam st = ((CraftScoreboard) p.getScoreboard()).getHandle().getTeam(f.getTeam().getName());
        PacketPlayOutScoreboardTeam ppost = new PacketPlayOutScoreboardTeam(st,2);
        try {
            Field fi = ppost.getClass().getDeclaredField("c");
            fi.setAccessible(true);
            fi.set(ppost,c+f.getTAG()+" ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppost);
    }

    public class Task extends BukkitRunnable {

        private final User user;

        public Task(User u){
            this.user = u;
        }

        @Override
        public void run() {
            if (user.getFaction()!=null)
                if (user.getFaction().getSpawn()!=null)
                    user.getPlayer().teleport(user.getFaction().getSpawn());
            user.getPlayer().teleport(user.getPlayer().getLocation().getWorld().getSpawnLocation());
        }
    }
}
