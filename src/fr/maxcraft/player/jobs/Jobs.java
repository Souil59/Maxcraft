package fr.maxcraft.player.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;

public abstract class Jobs {
	
protected double xp;
protected UUID uuid;
protected String name;
List<Material> blocks =  Arrays.asList();
	public Jobs(){
		
	}
	
	public int getLvl() {
		if((Math.sqrt(this.xp)/30)<=10)
			return (int) (Math.sqrt(this.xp)/30);
		return 10;
	}
	
	public static Jobs buildJob(UUID uuid,String metier,double xp){
		try {
		return (Jobs) Class.forName("fr.maxcraft.jobs.jobs."+metier).getConstructor(UUID.class,double.class).newInstance(uuid,xp);
	} catch (Exception e) {
		e.printStackTrace();
		Main.logError("Erreur au chargement des metier!");
	}
	return null;
		
	}

	public static Jobs load(UUID uuid) {
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `jobs` WHERE `jobs`.`id` = '"+uuid.toString()+"'",true);
		try {
			if (!r.isFirst())
				return null;
		return buildJob(uuid,r.getString("metier"),r.getDouble("xp"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void insert() {
		MySQLSaver.mysql_update("INSERT INTO `jobs` (`id`, `metier`, `xp`) VALUES ('"+this.uuid.toString()+"', '"+this.name+"', '"+this.xp+"');");
		
	}
	
	public List<Material> blockList() {
		return blocks;
	}

	public void addBlocks(List<Material> blockList) {
		// TODO Auto-generated method stub
		
	}
	

}