package net.nathem.script.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.md_5.bungee.api.ChatColor;
import net.nathem.script.core.Map;
import net.nathem.script.core.NSCore;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Utils;
import net.nathem.script.core.sign.OutputSignal;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditorCommand {
	
	private CommandSender sender;
	private String[] args;
	private Editor editor;
	
	public EditorCommand(Editor editor, CommandSender sender, String[] args) {
		
		this.sender = sender;
		this.args = args;
		this.editor = editor;
		
		if(this.args == null);
		else if(this.args.length == 0) this.editorInfos();
		else
		{
			switch(args[0])
			{
				case "open": case "o":
					this.open();
					break;
				case "close": case "c":
					this.close();
					break;
				case "tp":
					this.tp();
					break;
				case "i": case "info":
					this.info();
					break;
				case "sign": case "s":
						this.sign();
						break;
			    case "signs":
					this.signs();
					break;
			    case "find": case "f":
					this.find();
					break;
			    case "go": case "g":
					this.go();
					break;
			    case "test": case "t":
					this.test();
					break;
			}
		}
			
	}
	
	public static void message(CommandSender sender, String message)
	{
		sender.sendMessage(ChatColor.DARK_AQUA + "[Editor]" + ChatColor.GRAY + " " + message);
	}
	
	public void message(String message)
	{
		EditorCommand.message(this.sender, message);
	}
	
	public void editorInfos()
	{
		message(ChatColor.DARK_AQUA +"Nathem Script editor");
		message(ChatColor.DARK_AQUA +"********************");
		if(this.editor.getWorlds().isEmpty())
		{
			message("There is no world oppened in editor");
		}
		else
		{
			message("Oppened worlds :");
			for(World world : this.editor.getWorlds().keySet())
			{
				message("- "+ world.getName());
			}
		}
	}
	
	public void open()
	{
		if(this.args.length == 2)
		{
			String worldName = this.args[1];
			World world = Bukkit.getWorld(worldName);
			if(world == null)
			{
				message("World <"+ worldName +"> was not loaded. Loading world...");
				world = Bukkit.createWorld(WorldCreator.name(worldName));
				message("World <"+ worldName +"> loaded");
			}
			if(this.editor.getWorlds().keySet().contains(world))
			{
				message("World <"+ worldName +"> is already openned in editor");
				return;
			}
			
			try {
				this.editor.addWorld(world);
				
			} catch (IOException e) {
				message("Error loading script map for world <"+ worldName +">...");
				e.printStackTrace();
				return;
			}
			
			this.editor.buildSigns(world);
			
			message("World <"+ worldName +"> openned in editor");
			NSCore.log("World <"+ worldName +"> openned in editor by "+this.sender.getName());
		}
	}

	public void close()
	{
		if(this.args.length == 2)
		{
			String worldName = this.args[1];
			World world = Bukkit.getWorld(worldName);
			
			if(!this.editor.getWorlds().keySet().contains(world))
			{
				message("World <"+ worldName +"> is not oppened in editor");
				return;
			}
			
			if(world != null)
			{
				this.editor.destroySigns(world);
				this.editor.getWorlds().remove(world);
				Bukkit.unloadWorld(worldName, true);
			}
			
			
			message("World <"+ worldName +"> is now closed");
			NSCore.log("World <"+ worldName +"> closed in editor by "+this.sender.getName());
		}
	}
	
	public void tp()
	{
		if(this.args.length == 2)
		{
			String worldName = this.args[1];
			World world = Bukkit.getWorld(worldName);
			
			if(!this.editor.getWorlds().keySet().contains(world))
			{
				message("World <"+ worldName +"> is not oppened in editor");
				return;
			}
			
			if(world == null) return;
			
			Player p = (Player) this.sender;
			p.teleport(world.getSpawnLocation());
			message(p.getName() + " teleported to script map <"+ worldName +">");
		}
	}
	
	public void info()
	{
		
		World world = null;
		
		if(this.args.length == 1 && this.sender instanceof Player)
		{
			world = ((Player) this.sender).getLocation().getWorld();
		}
		else if(this.args.length > 1)
		{
			world = Bukkit.getWorld(this.args[1]);
			
			if(world == null)
			{
				message("World <"+ this.args[1] +"> does not exist");
				return;
			}
		}
		else return;
		
		Map map = this.editor.getMap(world);
		
		if(map == null)
		{
			message("World <"+ world.getName() +"> is not oppened in editor");
			return;
		}
		
		// Informations sur la map
		message(ChatColor.DARK_AQUA +"Map <"+map.getWorldName()+">");
		message("Object count : "+ map.getSigns().size());
		message("Available objects ("+ObjectType.getObjectTypeNames().size()+") : "+ ObjectType.getObjectTypeNames());
	}
	
	public void signs()
	{
		if(this.args.length == 1) this.info();
		else
		{
			Player p = (Player) this.sender;
			Map map = this.editor.getMap(p.getWorld());
			if(map == null) return;
			
			switch(args[1])
			{
				case "show": case "s":
					this.showSigns(map, p.getWorld());
					break;
				case "hide": case "h":
					this.hideSigns(map, p.getWorld());
					break;
				case "reload": case "r":
					this.reloadSigns(map, p.getWorld());
					break;
			}
		}
	}
	
	public void showSigns(Map map, World world)
	{
		message("Showing signs in world <"+ world.getName() +">...");
		this.editor.buildSigns(world);
	}
	
	public void hideSigns(Map map, World world)
	{
		message("Hiding signs in world <"+ world.getName() +">...");
		this.editor.destroySigns(world);
	}
	
	public void reloadSigns(Map map, World world)
	{
		message("Reloading signs in world <"+ world.getName() +">...");
		this.editor.destroySigns(world);
		this.editor.buildSigns(world);
	}
	
	public void find()
	{
		if(!(this.sender instanceof Player)) return;
		
		Player p = (Player) this.sender;
		Map map = this.editor.getMap(p.getWorld());
		if(map == null) return;
		
		ArrayList<Sign> results;
		
		if(this.args.length == 1)
		{
			message("Searching for signs...");
			results = map.getSigns();
		}
		else
		{
			results = new ArrayList<Sign>();
			for(Sign s : map.getSigns())
			{
				if(s.getObjectName().contains(this.args[1]))
				{
					results.add(s);
				}
			}
		}
		
		message(results.size()+" signs found :");
		
		for(Sign sign : results)
		{
			
			int n = map.getSignNumber(sign);
			int distance = (int) Math.round(sign.getRealLocation(p.getWorld()).distance(p.getLocation()));
			message("#"+ n + " " + ChatColor.WHITE + sign.getTypeName() + ChatColor.GRAY+ " ("+distance+"m)"  );
		}
	}
	
	public void go()
	{
		if(!(this.sender instanceof Player)) return;
		
		Player p = (Player) this.sender;
		Map map = this.editor.getMap(p.getWorld());
		if(map == null) return;
		if(this.args.length == 1) return;
		if(! Utils.isInt(this.args[1])) return;
		int n = Integer.parseInt(this.args[1]);
		if(n < 1) return;
		Sign sign = map.getSign(n);
		
		if(sign == null)
		{
			message("Sign not found, check /editor find...");
			return;
		}
		
		p.teleport(sign.getRealLocation(p.getWorld()));
		message("Teleported to sign #"+n);
		
	}
	
	public void sign()
	{
		Player p = (Player) this.sender;
		Map map = this.editor.getMap(p.getWorld());
		Sign sign = this.editor.getSelections().get(p);
		
		if(sign == null)
		{
			message("You must select a sign first...");
			return;
		}
		
		if(this.args.length == 1)
		{
			this.signInfo(sign, map);
		}
		else
		{
			switch(args[1])
			{
				case "infos": case "info":
					this.signInfo(sign, map);
					break;
				case "input": case "inputs": case "i":
					this.signInput(sign, map);
					break;
				case "output": case "outputs": case "o":
					this.signOutput(sign, map);
					break;
				case "set": case "s": case "option":
					this.signSet(sign, map);
					break;
				case "copy": case "c":
					this.signCopy(sign, map);
					break;
				case "paste": case "p":
					this.signPaste(sign, map);
					break;
			}
		}
	}
	
	public void signInfo(Sign sign, Map map)
	{
		int number = map.getSignNumber(sign);
		message("*******************************");
		message(ChatColor.WHITE + "" + ChatColor.BOLD + sign.getTypeName() + " #"+number);
		message("*******************************");
		
		message("Inputs : "+ this.editor.displayInputs(sign));
		message("Outputs ON : "+ this.editor.displayOutputs(sign, true));
		message("Outputs OFF : "+ this.editor.displayOutputs(sign, false));
		
		message(ChatColor.YELLOW +""+ ChatColor.BOLD +"Options :");
		
		for(String key : sign.getOptions().keySet())
		{
			message(ChatColor.WHITE + key + " : " + ChatColor.GRAY  + sign.getOptions().get(key));
		}
		
		for(String key : sign.getDefaultOptions().keySet())
		{
			if(sign.getOptions().containsKey(key)) continue;
			message(ChatColor.GRAY + "" + ChatColor.ITALIC + key + " : " + ChatColor.GRAY  + sign.getDefaultOptions().get(key).getValue() +" ("+sign.getDefaultOptions().get(key).getOptionType().name().toLowerCase()+")");
		}
	}
	
	public void signPaste(Sign sign, Map map)
	{
		if(!this.editor.getClipBoards().containsKey((Player) sender))
		{
			message("Use /editor sign copy first !");
			return;
		}
		
		Sign source = this.editor.getClipBoards().get((Player) sender);
		sign.copyData(source);
		this.editor.buildSign(sign, map);
		
		try {
			map.writeSigns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		message(ChatColor.YELLOW +"Sign "+ sign.getTypeName() +" pasted");
		
	}
	
	public void signCopy(Sign sign, Map map)
	{
		this.editor.getClipBoards().put((Player) sender, sign);
		message(ChatColor.YELLOW +"Sign "+ sign.getTypeName() +" copied in clipboard");
	}
	
	public void signInput(Sign sign, Map map)
	{
		if(this.args.length <= 3) return;
		
		int flux;
		if(this.args[3].equalsIgnoreCase("n"))
		{
			flux = map.getNewSignal();
		}
		else if(!Utils.isInt(this.args[3])) return;
		else
		{
			flux = Integer.parseInt(this.args[3]);
		}
		
		switch(this.args[2])
		{
			case "add": case "a": case "+":
				this.signAddInput(sign, map, flux);
				break;
			case "remove": case "r": case "-":
				this.signRemoveInput(sign, map, flux);
				break;
		}
	}
	
	public void signAddInput(Sign sign, Map map, int flux)
	{
		sign.getInputsSignals().add(flux);
		this.editor.buildSign(sign, map);
		
		try {
			map.writeSigns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.signInfo(sign, map);
	}
	
	public void signRemoveInput(Sign sign, Map map, int flux)
	{
		
		ArrayList<Integer> newInputs = new ArrayList<Integer>();
		
		Iterator<Integer> it = sign.getInputsSignals().iterator();
		
		while(it.hasNext())
		{
			int i = it.next();
			if(i == flux) continue;
			else newInputs.add(i);
			
		}
		
		sign.setInputsSignals(newInputs);
		
		this.editor.buildSign(sign, map);
		
		try {
			map.writeSigns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.signInfo(sign, map);
	}
	
	
	public void signOutput(Sign sign, Map map)
	{
		if(this.args.length <= 4) return;
		int flux;
		if(this.args[3].equalsIgnoreCase("n"))
		{
			flux = map.getNewSignal();
		}
		else if(!Utils.isInt(this.args[3])) return;
		else
		{
			flux = Integer.parseInt(this.args[3]);
		}
		boolean fluxValue;
		if(this.args[4].equalsIgnoreCase("true") || this.args[4].equalsIgnoreCase("t") || this.args[4].equalsIgnoreCase("on")) fluxValue = true;
		else if(this.args[4].equalsIgnoreCase("false") || this.args[4].equalsIgnoreCase("f") || this.args[4].equalsIgnoreCase("off")) fluxValue = false;
		else return;
		
		switch(this.args[2])
		{
			case "add": case "a": case "+":
				this.signAddOutput(sign, map, flux, fluxValue);
				break;
			case "remove": case "r": case "-":
				this.signRemoveOutput(sign, map, flux, fluxValue);
				break;
		}
	}
	
	public void signAddOutput(Sign sign, Map map, int flux, boolean fluxValue)
	{
		sign.getOutputSignals().add(new OutputSignal(flux, fluxValue));
		this.editor.buildSign(sign, map);
		
		try {
			map.writeSigns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.signInfo(sign, map);
	}
	
	public void signRemoveOutput(Sign sign, Map map, int flux, boolean fluxValue)
	{
		ArrayList<OutputSignal> newOutputs = new ArrayList<OutputSignal>();
		
		
		for(OutputSignal os: sign.getOutputSignals())
		{
			if(os.getSignal() == flux && os.isOn() == fluxValue) continue;
			newOutputs.add(os);
		}
		
		sign.setOutputSignals(newOutputs);
		
		this.editor.buildSign(sign, map);
		
		try {
			map.writeSigns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.signInfo(sign, map);
	}
	
	public void signSet(Sign sign, Map map)
	{
		if(this.args.length < 4) return;
		
		String key = this.args[2];
		String value = Utils.multiArgs(this.args, 3);
		
		if(!sign.getDefaultOptions().containsKey(key) && !sign.getObjectType().equals(ObjectType.CUSTOM))
		{
			message("Option <"+key+"> is not defined for object "+ sign.getTypeName());
			return;
		}
		
		// Option value reset
		if(value.equals("default") && sign.getOptions().containsKey(key)){
			sign.getOptions().remove(key);
			message("Option <"+key+"> has now it default value "+ ChatColor.WHITE + sign.getDefaultOptions().get(key).getValue());
		}
		else
		{
			sign.getOptions().put(key, value);
			message("Option <"+key+"> has now value "+ ChatColor.WHITE + value);
		}
		
		try {
			map.writeSigns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
	}
	
	public void test()
	{
		if(!(this.sender instanceof Player)) return;
		Player p = (Player) this.sender;
		World world = p.getWorld();
		
		if(!this.editor.getWorlds().containsKey(world))
		{
			Instance currentInstance = this.editor.getInstance(world);
			if(currentInstance == null) return;
			world = currentInstance.getSourceWorld();
		}
		
		Instance instance = this.editor.getTestWorlds().get(world);
		
		
		
		if(this.args.length == 1)
		{
			this.testInfos(world, instance);
		}
		else
		{
			switch(this.args[1])
			{
				case "i": case "infos": case "info":
					this.testInfos(world, instance);
					break;
				case "build": case "create": case "b":
					this.testBuild(world, instance, p);
					break;
				case "tp": case "teleport": case "go":
					this.testTp(world, instance, p);
					break;
				case "remove": case "delete": case "destroy": case "r": case "d":
					this.testDestroy(world, instance, p);
					break;
				case "leave": case "l":
					this.testLeave(world, instance, p);
					break;
			}
		}
	}
	
	public void testInfos(World world, Instance instance)
	{
		message(ChatColor.BOLD + "World <"+world.getName()+"> test world infos");
		
		if(instance == null)
		{
			message("There is no test world for world <"+world.getName()+"> actualy, '/editor test build' to create one...");
			return;
		}
		
		message("Test world : "+instance.getInstanceWorld().getName());
		message("Players online : "+ instance.getInstanceWorld().getPlayers().size());
		
	}
	
	public void testBuild(World world, Instance instance, Player p)
	{
		message("Building test word  : NW_"+world.getName()+"_TEST");
		world.save();
		if(instance != null) instance.destroy();
		instance = new Instance(this.editor, world.getName(), "NW_"+world.getName()+"_TEST");
		this.editor.getTestWorlds().put(world, instance);
		NathemWorld nw = instance.build();
		
		if(nw == null)
		{
			message("An error occured in compiler !");
			return;
		}
		
		Location spawn = p.getLocation().clone();
		spawn.setWorld(instance.getInstanceWorld());
		p.teleport(spawn);
	}
	
	public void testDestroy(World world, Instance instance, Player p)
	{
		if(instance != null) instance.destroy();
		message("Test world destroyed...");
	}
	
	public void testTp(World world, Instance instance, Player p)
	{
		if(instance == null){
			message("There is no test world yet... '/editor test build' to create one.");
			return;
		}
		
		message("Teleporting to test world...");
		p.teleport(instance.getNathemWorld().getSpawnLocation());
		
	}
	
	public void testLeave(World world, Instance instance, Player p)
	{
		if(instance == null)
		{
			message("There is no test world yet... '/editor test build' to create one.");
			return;
		}
		
		if(instance.getInstanceWorld().getPlayers().size() <= 1)
		{
			instance.destroy();
			message("You have been teleported back and test world has been destroyed...");
		}
		else
		{
			instance.teleportBack(p);
			message("You have been teleported back...");
		}

	}
		

}
