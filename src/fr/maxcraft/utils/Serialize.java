package fr.maxcraft.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import fr.maxcraft.player.User;
import fr.maxcraft.player.faction.Faction;

public class Serialize {

	public static String locationToString(Location l){
		String s = "";
		s+=l.getWorld().getName()+";";
		s+=l.getBlockX()+";";
		s+=l.getBlockY()+";";
		s+=l.getBlockZ()+";";
		s+=l.getYaw()+";";
		s+=l.getPitch()+";";
		return s;
	}
	public static Location locationFromString(String s){
		if (s==null)
			return null;
		String[] arg = s.split(";");
		if (Bukkit.getWorld(arg[0])==null)
			return null;
		Location l = new Location(Bukkit.getWorld(arg[0]),Double.parseDouble(arg[1]), Double.parseDouble(arg[2]), Double.parseDouble(arg[3])
				, Float.parseFloat(arg[4]), Float.parseFloat(arg[5]));
		return l;
	}
	


	public static String usersToString(ArrayList<User> ul){
		String s = "";
		for (User u : ul )
			s+=u.getUuid()+";";
		return s;
	}
	public static ArrayList<User> usersFromString(String str){
		if (str == null)
			return new ArrayList<User>();
		if (str.length()<2)
			return new ArrayList<User>();
		String[] arg = str.split(";");
		ArrayList<User> ul = new ArrayList<User>();
		for (String s:arg){
			ul.add(User.get(UUID.fromString(s)));
		}
		return ul;
	}
	public static ArrayList<Faction> factionsFromString(String str){
		if (str.length()<2)
			return new ArrayList<Faction>();
		String[] arg = str.split(";");
		ArrayList<Faction> ul = new ArrayList<Faction>();
		for (String s:arg){
			ul.add(Faction.get(UUID.fromString(s)));
		}
		return ul;
	}
	public static String ArrayStringToString(ArrayList<String> ul){
		String s = "";
		for (String u : ul )
			s+=u+";";
		return s;
	}
	public static ArrayList<String> ArrayStringFromString(String str){
		if (str.length()<2)
			return new ArrayList<String>();
		String[] arg = str.split(";");
		ArrayList<String> ul = new ArrayList<String>();
		for (String s:arg){
			ul.add(s);
		}
		return ul;
	}
}
