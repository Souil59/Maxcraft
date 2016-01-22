package net.nathem.script.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.maxcraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.nathem.script.editor.Editor;
import net.nathem.script.editor.EditorCommand;
import net.nathem.script.enums.LogType;


public class NSCore {

	public Main plugin;
	public static Editor Editor;
	private DevListener DevListener;
	private NathemListener NathemListener;
	public ArrayList<NathemWorld> registeredWorlds;


	public NSCore(Main main){
		this.plugin = main;
		this.onEnable();
	}

	public void onEnable() {
		this.registeredWorlds = new ArrayList<NathemWorld>();
		
		this.Editor = new Editor(this);
		this.DevListener = new DevListener(this);
		this.NathemListener = new NathemListener(this);
		
		// LISTENERS
		this.plugin.getServer().getPluginManager().registerEvents(this.Editor.getListener(), this.plugin);
		this.plugin.getServer().getPluginManager().registerEvents(this.DevListener, this.plugin);
		this.plugin.getServer().getPluginManager().registerEvents(this.NathemListener, this.plugin);
		
		NSCore.log("Nathem Script Core loaded");
		
		
	}



	public static void log(String message, LogType logType)
	{
		switch(logType)
		{
			//case CONSOLE:
			//	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
			//	console.sendMessage(ChatColor.YELLOW+"NS> " + ChatColor.WHITE+message);
			//	break;
			//case LOG:
			//	System.out.println(new SimpleDateFormat("dd/MM/yyyy H:m").format(new Date()) + " | " + message);
			//	break;
			case CHAT:
				Bukkit.broadcastMessage("[NS]" + message);
				break;
			
		}
	}
	
	public static void log(String message)
	{
		//NSCore.log(message, LogType.CONSOLE);
	}
	
	public NathemWorld getNathemWorld(World world)
	{
		for(NathemWorld nw : this.registeredWorlds)
		{
			if(nw.getWorld().equals(world)) return nw;
		}
		return null;
	}

}
