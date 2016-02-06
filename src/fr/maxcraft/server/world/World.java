package fr.maxcraft.server.world;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Crevebedaine on 27/01/2016.
 */
public class World {

    public static void loadAll(){
        Main.log("chargement");
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `world`",false);
        try {
            while (r.next()){
                Bukkit.createWorld(new WorldCreator(r.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
