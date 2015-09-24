package fr.maxcraft.utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;

import fr.maxcraft.Main;

public class MySQLSaver {
	public static java.sql.Connection sql;
	public static FileConfiguration config = Main.getPlugin().getConfig();
	
	/*
	 * Connexion MySQL
	 */
	public static void connect(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			sql = DriverManager.getConnection("jdbc:mysql://"+config.getString("adresse")+"/"+config.getString("table"),config.getString("user"), config.getString("pass"));
			Main.log("Connexion sql etablie !"); //$NON-NLS-1$
		} catch (SQLException e) {
			e.printStackTrace();
			Main.log("Connexion sql echouée !");
			Main.getPlugin().getServer().shutdown();
		}
	}
	
	
	/*
	 * Requete MySQL
	 */
	public static ResultSet mysql_query(String query, boolean singleResult)
	{
		try {
			if(sql.isClosed())
			{
				connect();
			}
		} catch (SQLException e1) {
		}
		
		try {
			Statement state = (Statement) MySQLSaver.sql.createStatement();
			ResultSet result = state.executeQuery(query);
	
		if(singleResult)
		{
			result.next();
		}
		return result;
				
		} catch (SQLException e) {
			return null;
		}
		
	}
	
	public static void mysql_update(String query)
	{
		try {
			if(sql.isClosed())
			{
				connect();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Statement state = (Statement) MySQLSaver.sql.createStatement();
		
			state.executeUpdate(query);
			state.close();
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}	
	}
}
