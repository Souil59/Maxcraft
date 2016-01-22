package net.nathem.script.core;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {
	
	public static boolean isInt(String s)
	{
		  try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    return true;
	}
	
	public static boolean isShort(String s)
	{
		  try { 
		        Short.parseShort(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    return true;
	}
	
	public static boolean isDouble(String s)
	{
		  try { 
		        Double.parseDouble(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    return true;
	}
	
	public static int[] getLocation(String loc)
	{
	
		Pattern p = Pattern.compile("^(-?[0-9]+);(-?[0-9]+);(-?[0-9]+)$");
		Matcher m = p.matcher(loc);
		
		if(m.find())
		{
				int[] locCoords = new int[3];
				locCoords[0] = Integer.parseInt(m.group(1));
				locCoords[1] = Integer.parseInt(m.group(2));
				locCoords[2] = Integer.parseInt(m.group(3));
				return locCoords;
		}
		return null;
		
	
	}
	
	public static Integer getDuration(String duration)
	{
		if(duration.equals("-1")) return -1;
		Pattern p = Pattern.compile("^([0-9]+)([t,s,m])$");
		Matcher m = p.matcher(duration);
		
		if(m.find())
		{
			String amount = m.group(1);
			String type = m.group(2);
			
			switch(type)
			{
			case "s":
				return Integer.parseInt(amount)*20;
			case "t":
				return Integer.parseInt(amount);
			case "m":
				return Integer.parseInt(amount)*20*60;
			}
		}
		
		return null;
		
		
	
	}
	
	public static ArrayList<Integer> readSignIO(String line, Map map)
	{
		Pattern p = Pattern.compile("([0-9,n]+)");
		Matcher m = p.matcher(line);
		ArrayList<Integer> io = new ArrayList<Integer>();
		while(m.find())
		{
			int flux;
			if(m.group(1).equalsIgnoreCase("n"))
			{
				flux = map.getNewSignal();
			}
			else
			{
				flux = Integer.parseInt(m.group(1));
			}
			io.add(flux);
		}
		
		return io;
		
	}
	
	public static String multiArgs(String[] args, int start)
	{
		
		int length = args.length;
		int nlength = length-start;
		String[] nametable = new String[nlength];
		int nid;
		for(int i = start; i < args.length; i++)
		{
			nid = i-start;
			nametable[nid] = args[i];
		}
		
		String name = StringUtils.join(nametable, " ");
		return name;
	}
	
	public static ItemStack getMaterial(String value)
	{
		String mat, meta;
		short metaValue;
		if(!value.contains(":"))
		{
			mat = value;
			meta = "";
		}
		else
		{
			mat = value.split(":")[0];
			meta = value.split(":")[1];
		}
		Material m = Material.getMaterial(mat.toUpperCase());
		if(m == null) return null;
		
		if(!isShort(meta)) metaValue = 0;
		else metaValue = Short.parseShort(meta);
		
		
		return new ItemStack(m, 1, metaValue);
		
	}
	
	public static void clearInventory(Player p)
	{
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.updateInventory();
	}
	
	public static EntityType getMobType(String name)
	{
		for(EntityType et: EntityType.values())
		{
			if(!et.isSpawnable()) continue;
			if(et.name().equalsIgnoreCase(name))
			{
				return et;
			}
		}
		return null;
	}
	
	public static double getRandom(double max)
	{
		Random r = new Random();
		return max * r.nextDouble();
	}
	
	public static void copyMergeInventory(Inventory source, Inventory target)
	{
		ListIterator<ItemStack> it = source.iterator();
		while(it.hasNext())
		{
			ItemStack stack = it.next();
			if(stack != null) target.addItem(stack.clone());
		}
	}
}
