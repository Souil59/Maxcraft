package fr.maxcraft.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
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
	public static String itemstackToString(ItemStack is) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final OutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagCompound outputObject = new NBTTagCompound();
		CraftItemStack craft = CraftItemStack.asCraftCopy(is);
		if (craft != null) 
		   CraftItemStack.asNMSCopy(craft).save(outputObject);
		try {
			NBTCompressedStreamTools.a(outputObject, dataOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
		}

	public static ItemStack stringToItemStack(String str) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(str, 32).toByteArray());
		NBTTagCompound item = null;
		try {
			item = NBTCompressedStreamTools.a(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		net.minecraft.server.v1_8_R3.ItemStack stack = net.minecraft.server.v1_8_R3.ItemStack.createStack(item);
		return  CraftItemStack.asCraftMirror(stack);
	}
}
