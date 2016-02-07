package fr.maxcraft.server.marker;

import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Crevebedaine on 07/02/2016.
 */
public class Marker extends Location{
    private Boolean GPS;
    private String name;
    private static ArrayList<Marker> markerlist = new ArrayList<Marker>();

    public Marker(Location l, String name, Boolean onGPS) {
        super(l.getWorld(),l.getX(),l.getY(),l.getZ(),l.getYaw(),l.getPitch());
        this.name = name;
        this.GPS = onGPS;
        markerlist.add(this);
    }

    public static void load() {
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM  `marker` ",false);
        try {
            while (r.next())
                new Marker(Serialize.locationFromString(r.getString("location")),r.getString("name"),r.getBoolean("gps"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(){
        MySQLSaver.mysql_update("INSERT INTO `marker` (`name`, `location`, `gps`) VALUES ('"+this.name+"', '"+Serialize.locationToString(this)+"', "+this.GPS+");");
    }

    public void save() {
        MySQLSaver.mysql_update("UPDATE `marker` SET `location ='"+Serialize.locationToString(this)+"',`gps` ='"+this.GPS+"' WHERE 'name' = "+this.name);

    }

    public void remove(){
        markerlist.remove(this);
        MySQLSaver.mysql_update("DELETE FROM `marker` WHERE `name`= "+this.name);

    }

    public static ArrayList<Marker> getMarkerlist() {
        return markerlist;
    }

    public String getName() {
        return name;
    }

    public static Marker getMarker(String name){
        for (Marker m : markerlist)
            if(m.getName().equals(name))
                return m;
        return null;
    }
}
