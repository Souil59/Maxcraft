package fr.maxcraft.server.world;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;

public class Marker implements CommandExecutor {

	public static HashMap<String,Marker> marker = new HashMap<String,Marker>();
	public static HashMap<String,Marker> gps = new HashMap<String,Marker>();
	private Location location;
	
	public Marker(String name, String world, int x, int y,int z, float yaw, Boolean bool) {
		this.location=new Location(Bukkit.getWorld(world),x,y,z);
		this.location.setYaw(yaw);
		if (bool)
			gps.put(name, this);
		marker.put(name, this);
	}

	public Marker() {
		
	}

	public static void register(Main main) {
		main.getCommand("marker").setExecutor(new Marker());
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,
			String[] args) {
			Player p = (Player) sender;
			if (args.length==0){
				for (String w : marker.keySet())
					sender.sendMessage(w);
				return true;
			}
			switch(args[0]){
			case "new": case "create":{
				if(!createMarker(args,p))
					sender.sendMessage("/marker create <name> [onGPS?]");
				else
					sender.sendMessage("Nouveau marker crée");
				break;
			}
			case "tp":{
				if(!teleport(args,p))
					sender.sendMessage("/marker tp <name>");
				else
					sender.sendMessage("Teleportation vers "+args[1]);
				break;
			}
			case "delete": case "remove":{
				if(!remove(args,p))
					sender.sendMessage("/marker remove <name>");
				else
					sender.sendMessage("Supression du marker "+args[1]);
				break;
			}
			default :
				break;
			}
		return true;
	}

	public Location getLocation() {
		return location;
	}

	private boolean remove(String[] args, Player p) {
		if (args.length<2)
			return false;
		if (!marker.containsKey(args[1]))
			return false;
		marker.remove(args[1]);
		MySQLSaver.mysql_update("DELETE FROM `marker` WHERE `name`= '"+args[1]+"'");
		return true;
	}

	private boolean teleport(String[] args, Player p) {
		if (args.length<2)
			return false;
		if (!marker.containsKey(args[1]))
			return false;
		p.teleport(marker.get(args[1]).location);
		return true;
	}

	private boolean createMarker(String[] args, Player p) {
		if (args.length<2)
			return false;
		if (marker.containsKey(args[1]))
			return false;
		Boolean bool = false;
		if (args[2]!=null)
			bool=Boolean.valueOf(args[2]);
		Location l = p.getLocation();
		MySQLSaver.mysql_update("INSERT INTO `marker` (`name`,`world`,`x`,`y`,`z`,`yaw`,`onGPS`) VALUES "
				+ "('"+args[1]+"','"+l.getWorld().getName()+"', "+l.getBlockX()+", "+l.getBlockY()+", "+l.getBlockZ()+", "+l.getYaw()+","+bool+");");
		return true;
	}

	public static void load() {
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `marker`", false);
		try {
			while (r.next())
				new Marker(r.getString("name"),r.getString("world"),r.getInt("x"),r.getInt("y"),r.getInt("z"),r.getInt("yaw"),r.getBoolean("onGPS"));
		} catch (SQLException e) {
			e.printStackTrace();
			Main.logError("Erreur au chargement des markers");
		}
		
	}

}
