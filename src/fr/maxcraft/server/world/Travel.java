package fr.maxcraft.server.world;

import fr.maxcraft.Main;
import fr.maxcraft.server.world.marker.Marker;
import fr.maxcraft.utils.MySQLSaver;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public class Travel {

    private final Marker m1;
    private final Marker temp;
    private final Marker m2;
    public static ArrayList<Travel> travelslist = new ArrayList<Travel>();

    public Travel(Marker m1, Marker temp, Marker m2){
        this.m1 = m1;
        this.temp = temp;
        this.m2 = m2;
        travelslist.add(this);
    }

    public boolean contains(Marker m){
        if (m1.equals(m)||m2.equals(m)||temp.equals(m))
            return true;
        return false;
    }

    public static void load() {
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM  `travel` ",false);
        try {
            while (r.next())
                new Travel(Marker.getMarker(r.getString("marker1")),Marker.getMarker(r.getString("tempmarker")),Marker.getMarker(r.getString("marker2")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(){
        MySQLSaver.mysql_update("INSERT INTO `travel` (`marker1`, `tempmarker`, `marker2`) VALUES ('"+this.m1.getName()+"', '"+this.temp.getName()+"', '"+this.m2.getName()+"');");
    }

    public void remove(){
        travelslist.remove(this);
        MySQLSaver.mysql_update("DELETE FROM `travel` WHERE `marker1`= '"+this.m1.getName()+"'");
    }
    public boolean isReadyToTravel(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if (m1.getWorld().equals(p.getLocation().getWorld()))
            if (e.getTo().distance(m1)<5&&e.getFrom().distance(m1)>5)
                return true;
        if (m2.getWorld().equals(p.getLocation().getWorld()))
            if (e.getTo().distance(m2)<5&&e.getFrom().distance(m2)>5)
                return true;
        return false;
    }

    public void travel(Player p){
        p.sendMessage(ChatColor.GRAY+"Patientez ici quelques secondes le temps de larguer les ammarres");
        if (m1.getWorld().equals(p.getLocation().getWorld()))
            if (p.getLocation().distance(m1)<   5)
                p.sendMessage(ChatColor.GRAY + "Connard!");
                //new Task(m1,temp,m2,p,this).runTaskLater(Main.getPlugin(),100);
        if (m2.getWorld().equals(p.getLocation().getWorld()))
            if (p.getLocation().distance(m2)<5)
                p.sendMessage(ChatColor.GRAY + "Connard!");
                //new Task(m2,temp,m1,p,this).runTaskLater(Main.getPlugin(),100);
    }

    public class Task extends BukkitRunnable{

        private final Marker temp;
        private final Player p;
        private final Marker to;
        private final Marker from;
        private final Travel travel;

        public Task(Marker from,Marker temp, Marker to, Player p,Travel t){
            p.sendMessage(ChatColor.GRAY + "1");
            this.from = from;
            this.temp = temp;
            this.p = p;
            this.to = to;
            this.travel = t;
            p.sendMessage(ChatColor.GRAY + "2");
        }

        @Override
        public void run() {
            p.sendMessage(ChatColor.GRAY + "Connard!");

            if (p.getLocation().distance(from)<5) {
                p.sendMessage(ChatColor.GRAY + "Profitez du voyage pour admirer la vue !");
                p.teleport(temp);
                new Task(from,temp,to,p,travel).runTaskLater(Main.getPlugin(),600);
                return;
            }
            if (p.getLocation().distance(temp)<20){
                p.sendMessage(ChatColor.GRAY + "Vous pouvez descendre du bateau, allez y avant qu'il ne reparte !");
                p.teleport(to);
                return;
            }
        }
    }


}
