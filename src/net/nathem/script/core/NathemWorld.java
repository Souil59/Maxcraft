package net.nathem.script.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.nathem.script.core.object.Stuff;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.LogType;
import net.nathem.script.enums.ObjectType;
import sun.java2d.pipe.RenderBuffer;

public class NathemWorld {

	private ArrayList<NathemObject> objects;
	private String worldName;
	private Map map;
	private HashMap<String, Object> variables;
	private World world;
	private boolean loaded;
	private NSCore core;
	private ArrayList<Player> spectators;
	private HashMap<String, Location> markers;
    private HashMap<Player, Location> checkPoints;

    public NathemWorld(NSCore core, String worldName, Map map) {
		this.loaded = false;
		NSCore.log("Loading script for world <"+ worldName + "> from map <"+ map.getWorldName() + ">");
		this.objects = new ArrayList<NathemObject>();
		this.variables = new HashMap<String, Object>();
		this.worldName = worldName;
		this.map = map;
		this.core = core;
		this.spectators = new ArrayList<Player>();
		this.world = Bukkit.getServer().getWorld(this.worldName);
		this.markers = new HashMap<String, Location>();
        this.checkPoints = new HashMap<Player, Location>();
		
		if(this.world != null)
		{
			this.compile();
			this.loaded = true;
			NSCore.log("Script for world <"+ worldName + "> loaded with "+ this.objects.size() + " objects");
		}
		else
		{
			NSCore.log("Script for world <"+ worldName + "> cannot be loaded because the world isn't loaded");
		}
	}
	
	public void compile()
	{
		// Objects registering
		for(Sign sign : this.map.getSigns())
		{
			ObjectType objectType = ObjectType.fromObjectName(sign.getTypeName().toLowerCase());
			if(objectType == null)
			{
				NSCore.log("Object type <"+sign.getTypeName().toLowerCase()+"> not valid on sign " +sign.getUniqueId());
				continue;
			}
			else
			{
				NathemObject newObject = objectType.createNew(sign, this);
			}
		}
		
		NSCore.log(this.objects.size()+" objects registered, destroying signs...", LogType.LOG);
		
		for(NathemObject basicObject : this.objects)
		{
			basicObject.destroySign();
		}
		
		NSCore.log("Starting activation...", LogType.LOG);

		// Init signals
		for(NathemObject basicObject : this.objects)
		{
			NSCore.log("Signals init of object "+basicObject.getObjectType().name()+" "+basicObject.getSign().getUniqueId(), LogType.LOG);
			basicObject.initSignals();
		}
		
		// Objects activation
		for(NathemObject basicObject : this.objects)
		{
			NSCore.log("Activation of object "+basicObject.getObjectType().name()+" "+basicObject.getSign().getUniqueId(), LogType.LOG);
			basicObject.activate();
		}
		
		this.core.registeredWorlds.add(this);
		
		// Global init signals
		this.sendGlobalSignal(1, true, null);
		this.sendGlobalSignal(0, false, null);
		
		this.loaded = true;
	}

	public void destroy()
	{
		this.core.registeredWorlds.remove(this);
		this.loaded = false;
	}
	
	public ArrayList<NathemObject> getObjects() {
		return objects;
	}

	public String getWorldName() {
		return worldName;
	}

	public Map getMap() {
		return map;
	}

	public HashMap<String, Object> getVariables() {
		return variables;
	}

	public World getWorld() {
		return world;
	}

	public boolean isLoaded() {
		return loaded;
	}
	
	public Location getSpawnLocation()
	{
		// TODO 
		return this.getWorld().getSpawnLocation();
	}

	public void sendGlobalSignal(int signal, boolean value, Player p)
	{
		for(NathemObject no : this.objects)
		{
			if(no.getSign().getInputsSignals().contains(signal))
			{
				no.onSignal(value, p);
			}
		}
	}

	public NSCore getCore() {
		return core;
	}
	
	
	public ArrayList<Player> getSpectators() {
		return spectators;
	}

	public ArrayList<NathemObject> getObjects(ObjectType type)
	{
		ArrayList<NathemObject> objects = new ArrayList<NathemObject>();
		
		for(NathemObject no : this.objects)
		{
			if(no.getObjectType().equals(type)) objects.add(no);
		}
		
		return objects;
		
	}

	public HashMap<String, Location> getMarkers() {
		return markers;
	}
	
	public Inventory getStuff(String name)
	{
		ArrayList<Stuff> stuffs = new ArrayList<Stuff>();
		ArrayList<Stuff> selectedStuffs = new ArrayList<Stuff>();
		
		for(NathemObject no : this.objects)
		{
			if(no.getObjectType().equals(ObjectType.STUFF)) stuffs.add((Stuff) no);
		}
		
		for(Stuff stuff : stuffs)
		{
			if(stuff.getName().equals("null")) continue;
			if(!stuff.getName().equalsIgnoreCase(name)) continue;
			selectedStuffs.add(stuff);
		}
		
		double total = 0;
		for(Stuff stuff : selectedStuffs)
		{
			total += stuff.getChance();
		}
		
		if(total <= 0) return null;
		double rand = Utils.getRandom(total);
		double value = 0;
		int i = 0;
		Stuff myStuff = null;
		while(value < rand)
		{
			myStuff= selectedStuffs.get(i);
			value += myStuff.getChance();
			i++;
		}
		return myStuff.getInventory();
	}
	
	public Player getNearestPlayer(Location loc)
	{
		Player nearest = null;
		Double minDistance = 100000.;
		for(Player player : this.getWorld().getPlayers())
		{
			Double distance = player.getLocation().distance(loc);
			if(distance < minDistance)
			{
				minDistance = distance;
				nearest = player;
			}
		}
		return nearest;
	}


    public HashMap<Player, Location> getCheckPoints() {
        return checkPoints;
    }
}
