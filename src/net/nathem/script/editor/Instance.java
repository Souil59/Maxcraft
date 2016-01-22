package net.nathem.script.editor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.nathem.script.core.Map;
import net.nathem.script.core.NSCore;
import net.nathem.script.core.NathemWorld;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class Instance {
	
	private String sourceWorldName;
	private String instanceWorldName;
	private World instanceWorld;
	private NathemWorld nathemWorld;
	private Editor editor;
	private HashMap<Player, Location> backLocations;
	
	public Instance(Editor editor, String sourceWorldName, String instanceWorldName) {
		this.sourceWorldName = sourceWorldName;
		this.instanceWorldName = instanceWorldName;
		this.editor = editor;
		this.instanceWorld = null;
		this.nathemWorld = null;
		this.backLocations = new HashMap<Player, Location>();
	}
	
	public NathemWorld build()
	{
		File sourceWorldFile = new File(this.sourceWorldName);
		File instanceFile = new File(this.instanceWorldName);
		
		if(!sourceWorldFile.exists())
		{
			
			NSCore.log("World <"+instanceWorldName+"> could not be loaded because the source world <"+sourceWorldName+"> not exists in files");
			return null;
		}
		
		World oldInstance = Bukkit.getWorld(this.instanceWorldName);
		if(oldInstance != null)
		{
			NSCore.log("World <"+instanceWorldName+"> is already loaded, unloading world...");
			Bukkit.unloadWorld(oldInstance, false);
		}
		
		if(instanceFile.exists())
		{
			NSCore.log("World <"+instanceWorldName+"> already exists in files, removing files...");
			instanceFile.delete();
		}
		
		// Copy
		
		try {
			FileUtils.copyDirectory(sourceWorldFile, instanceFile);
		} catch (IOException e) {
		
			NSCore.log("Error copying file to create instance <"+instanceWorldName+">");
			return null;
		}
		
		// Removing uid.dat

		File uidFile = new File(instanceFile, "uid.dat");
        uidFile.delete();
        
        // World loading
        this.instanceWorld = Bukkit.createWorld(WorldCreator.name(this.instanceWorldName));
        
        if(this.instanceWorld == null)
        {
        	
			NSCore.log("World <"+instanceWorldName+"> cannot be loaded...");
        	return null;
        }
        
        
        // Map loading
        Map map = null;
        
        try {
        	map = new Map(this.sourceWorldName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(map == null) return null;
        
        // NathemWorld loading
        this.nathemWorld = new NathemWorld(this.editor.getPlugin(), this.instanceWorldName, map);
        
        return this.nathemWorld;
        
	}
	
	public void destroy()
	{
		// Teleport back
		for(Player p : this.getInstanceWorld().getPlayers())
		{
			this.teleportBack(p);
		}
		
		boolean unload = Bukkit.unloadWorld(this.getInstanceWorld(), true);
		
		if(unload == false)
		{
			NSCore.log("Error unloading world <"+instanceWorldName+">...");
		}
		
		World sourceWorld = Bukkit.getWorld(this.sourceWorldName);
		this.editor.getTestWorlds().put(sourceWorld, null);
		File testWorldFile = new File(this.instanceWorldName);

		try {
			FileUtils.deleteDirectory(testWorldFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void teleportBack(Player p)
	{
		Location backLocation = this.getBackLocations().get(p);
		if(backLocation == null) backLocation = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
		p.teleport(backLocation);
		
	}
	
	@Override
	public String toString() {
		return "Instance [sourceWorldName=" + sourceWorldName
				+ ", instanceWorldName=" + instanceWorldName + "]";
	}

	public String getSourceWorldName() {
		return sourceWorldName;
	}

	public World getInstanceWorld() {
		return instanceWorld;
	}

	public NathemWorld getNathemWorld() {
		return nathemWorld;
	}
	
	public World getSourceWorld()
	{
		return Bukkit.getWorld(this.sourceWorldName);
	}

	public HashMap<Player, Location> getBackLocations() {
		return backLocations;
	}
	

	

}
