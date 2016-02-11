package fr.maxcraft.server.quester;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public class Quester {

    private final Entity npc;
    private Quest quest = null;
    private HashMap<User,Integer> playerStats = new HashMap<User,Integer>();
    public static ArrayList<Quester> questers =new ArrayList<Quester>();

    public Quester(Entity npc, String q, ArrayList<User> p, ArrayList<String> pi) {
        this.npc =npc;
        try {
            this.quest = (Quest) Class.forName(q).getConstructor(Quester.class).newInstance(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0;i<p.size();i++)
            playerStats.put(p.get(i),Integer.parseInt(pi.get(i)));
        questers.add(this);
    }

    public Entity getNpc(){
        return this.npc;
    }

    public void rightClic(User user) {
        if(!playerStats.containsKey(user))
            playerStats.put(user,0);
        playerStats.put(user,this.quest.rightclic(user,playerStats.get(user)));
        this.save();
    }

    public void answer(User user, int i) {
        if(!playerStats.containsKey(user))
            playerStats.put(user,0);
        playerStats.put(user,this.quest.answer(user,playerStats.get(user),i));
        this.save();
    }

    public static void load() {
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM  `quester` ",false);
        try {
            while (r.next()) {
                for (org.bukkit.World world : Main.getPlugin().getServer().getWorlds())
                    for (Entity entity : world.getEntities())
                        if (entity.getUniqueId().equals(UUID.fromString(r.getString("uuid"))))
                            new Quester(entity, r.getString("quest"), Serialize.usersFromString(r.getString("player")), Serialize.ArrayStringFromString(r.getString("stats")));
            }
            Main.log("Questers chargés : "+questers.size()+" Questers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(){
        ArrayList<User> us = new ArrayList<User>();
        ArrayList<String> is = new ArrayList<String>();
        for (User u : playerStats.keySet()){
            us.add(u);
            is.add(""+playerStats.get(u));
        }
        MySQLSaver.mysql_update("INSERT INTO `quester` (`uuid`, `quest`, `player`,`stats` ) VALUES ('"+this.npc.getUniqueId().toString()+"'" +
                ", '"+this.quest.getClass().getName()+"', '"+Serialize.usersToString(us)+"', '"+Serialize.ArrayStringToString(is)+"');");
    }

    public void save() {
        ArrayList<User> us = new ArrayList<User>();
        ArrayList<String> is = new ArrayList<String>();
        for (User u : playerStats.keySet()){
            us.add(u);
            is.add(""+playerStats.get(u));
        }
        MySQLSaver.mysql_update("UPDATE `quester` SET `player` = '"+Serialize.usersToString(us)+"',`stats` = '"+Serialize.ArrayStringToString(is)+"' WHERE 'uuid' = '"+this.npc.getUniqueId().toString()+"';");

    }
}
