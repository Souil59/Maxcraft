package fr.maxcraft.dev;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;

public class MaxcraftDataConverter {

	public static void convertUser() throws SQLException {
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `user`;",false);
		while (r.next()){
			int id = r.getInt("id");
			String falseuuid = r.getString("uuid");
			String trueuuid  = falseuuid.substring(0, 8)+"-"+falseuuid.substring(8,12)+"-"+falseuuid.substring(12, 16)+"-"+falseuuid.substring(16, 20)+"-"+falseuuid.substring(20, 32);
			Main.log("ca donne "+r.getInt("id")+" : "+trueuuid);
			MySQLSaver.mysql_update("UPDATE `maxcraft`.`user` SET `uuid` = '"+trueuuid+"' WHERE `user`.`id` = "+id+";");
		}
	}

}
