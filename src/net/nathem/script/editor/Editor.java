package net.nathem.script.editor;

import java.io.IOException;
import java.util.HashMap;

import net.nathem.script.core.Map;
import net.nathem.script.core.NSCore;
import net.nathem.script.core.sign.OutputSignal;
import net.nathem.script.core.sign.Sign;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Editor {

	
	private NSCore plugin;
	private EditorListener listener;
	private HashMap<World, Map> worlds;
	private HashMap<Player, Sign> selections;
	private HashMap<Player, Sign> clipBoards;
	private HashMap<World, Instance> testWorlds;
	
	public Editor(NSCore plugin) {
		this.plugin = plugin;
		this.worlds = new HashMap<World, Map>();
		this.listener = new EditorListener(this);
		this.selections = new HashMap<Player, Sign>();
		this.testWorlds = new HashMap<World, Instance>();
		this.clipBoards = new HashMap<Player, Sign>();
	}


	public void addWorld(World world) throws IOException 
	{
		this.worlds.put(world, new Map(world.getName()));
	}
	
	public HashMap<World, Map> getWorlds() {
		return worlds;
	}
	
	public Map getMap(World world)
	{
		if(!this.getWorlds().containsKey(world)) return null;
		return this.getWorlds().get(world);
	}


	public NSCore getPlugin() {
		return plugin;
	}

	public EditorListener getListener() {
		return listener;
	}
	
	public void buildSign(Sign sign, World world)
	{
		Location l = sign.getRealLocation(world);
		Block b = l.getBlock();
		
		if(sign.isWallSign()) b.setType(Material.WALL_SIGN);
		else b.setType(Material.SIGN_POST);
		b.getState().update();
		try {
			
		org.bukkit.block.Sign signBlock = (org.bukkit.block.Sign) b.getState();
		
		org.bukkit.material.Sign signMaterial = (org.bukkit.material.Sign) signBlock.getData();
		signMaterial.setFacingDirection(sign.getFacing());
	
		
		String[] lines = this.buildLines(sign);
		signBlock.setLine(0, lines[0]);
		signBlock.setLine(1, lines[1]);
		signBlock.setLine(2, lines[2]);
		signBlock.setLine(3, lines[3]);
		signBlock.update();
	}catch(Exception e){
		e.printStackTrace();
		NSCore.log("Error while creating sign at "+l);
	}
		
	}
	
	public void buildSign(Sign sign, Map map)
	{
		World w = map.getWorld();
		if(w == null) return;
		this.buildSign(sign, w);
	}
	
	
	public String[] buildLines(Sign sign)
	{
		String[] lines = new String[4];
		String objectName = sign.getObjectName().substring(1).toUpperCase();
		lines[0] = ChatColor.RED + "" + ChatColor.BOLD + "["+ objectName +"]";
		lines[1] = this.displayInputs(sign);
		lines[2] = this.displayOutputs(sign, true);
		lines[3] = this.displayOutputs(sign, false);
		return lines;
		
	}
	
	public String displayInputs(Sign sign)
	{
		String display = "" + ChatColor.DARK_AQUA;
		for(int i : sign.getInputsSignals())
		{
			display += i + " ";
		}
		
		return display;
	}
	
	public String displayOutputs(Sign sign, boolean on)
	{
		String display;
		if(on) display = "" + ChatColor.GREEN;
		else display = "" + ChatColor.DARK_RED;
		
		for(OutputSignal os : sign.getOutputSignals())
		{
			if(os.isOn() == on)
			{
				display += os.getSignal() + " ";
			}
		}
		
		return display;
	}
	
	public void buildSigns(World world)
	{
		Map map = this.getMap(world);
		if(map == null) return;
		
		for(Sign sign : map.getSigns())
		{
			this.buildSign(sign, world);
		}
		
	}
	
	public void destroySigns(World world)
	{
		Map map = this.getMap(world);
		if(map == null) return;
		
		for(Sign sign : map.getSigns())
		{
			Location loc = sign.getRealLocation(world);
			loc.getBlock().setType(Material.AIR);
		}
	}


	public HashMap<Player, Sign> getSelections() {
		return selections;
	}


	public HashMap<World, Instance> getTestWorlds() {
		return testWorlds;
	}
	
	
	public Instance getInstance(World world)
	{
		for(World editorWorld : this.testWorlds.keySet())
		{
			Instance i = this.testWorlds.get(editorWorld);
			if(i.getInstanceWorld() == null) continue;
			if(i.getInstanceWorld().equals(world)) return i;
		}
		
		return null;
	}


	public HashMap<Player, Sign> getClipBoards() {
		return clipBoards;
	}
	

	

	
}
