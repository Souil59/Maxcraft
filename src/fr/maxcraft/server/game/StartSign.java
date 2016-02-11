package fr.maxcraft.server.game;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.player.menu.game.Instance;
import fr.maxcraft.player.menu.game.SwitchUsable;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;
import org.bukkit.inventory.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class StartSign {

    public static ArrayList<StartSign> sslist = new ArrayList<StartSign>();
    private final Game game;
    private final int nbrInstances;
    private ArrayList<GameInstance> instances = new ArrayList<GameInstance>();
    private final UUID npcUUID;
    private boolean open;

    public StartSign(UUID pnj,int instances,boolean open,String name, String source, String entrance, int max,int life){
        this.npcUUID = pnj;
        this.open = open;
        this.game = new Game(name,source,entrance,max,life);
        this.nbrInstances = instances;
        for (int i = 1;i<=instances;i++)
            this.instances.add(new GameInstance(game,source+i));
        sslist.add(this);
    }

    public static void load(){
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `startsign`;",false);
        try {
            while (r.next()){
                new StartSign(UUID.fromString(r.getString("uuid")),r.getInt("nbrInstances"),r.getBoolean("open"),r.getString("name")
                        ,r.getString("source"),r.getString("entrance"),r.getInt("max"),r.getInt("life"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Main.log("StartSigns chargés ("+sslist.size()+" StartSigns).");
    }

    public void clic(User u){
        Inventory i = Menu.getInventory("Instances");
        int j = 0;
        for (GameInstance g : instances) {
            i.setItem(j, new Instance(u, g,this).getItem(u));
            j++;
        }
        if (u.getPerms().hasPerms("maxcraft.modo"))
            i.setItem(8,new SwitchUsable(u,this).getItem(u));
        u.getPlayer().openInventory(i);
    }

    public UUID getNPCUUID(){
        return npcUUID;
    }

    public void insert(){
        MySQLSaver.mysql_update("INSERT INTO `startsign` (`uuid`, `nbrInstances`,`open`, `name`, `source`,  `entrance`, `max`, `life`) VALUES"
                + " ('"+this.npcUUID+"',"+this.nbrInstances+","+this.open+",'"+this.game.getName()+"','"+this.game.getSource()+"','"+this.game.getEntrance()+"',"+this.game.getMax()+","+this.game.getLife()+");");
    }

    public boolean isOpen(){
        return open;
    }

    public void switchAccess() {
        this.open = !this.open;
        this.save();
    }

    private void save() {
        MySQLSaver.mysql_update("UPDATE `startsign` SET `open` ="+this.isOpen()+" WHERE `uuid` = '"+this.getNPCUUID().toString()+"'");

    }
}
