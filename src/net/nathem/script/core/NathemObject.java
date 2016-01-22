package net.nathem.script.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.nathem.script.core.sign.OutputSignal;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.LogType;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public abstract class NathemObject {
	
	protected ArrayList<Signal> inputs;
	protected ArrayList<Signal> outputs;
	protected Location location;
	protected Sign sign;
	protected NathemWorld nathemWorld;
	protected HashMap<String, Option> options;
	
	public NathemObject() {
	}
	
	public NathemObject(Sign sign, NathemWorld nathemWorld)
	{
		NSCore.log("Loading object "+ this.getObjectType().name() + " from sign " + sign.getUniqueId(), LogType.LOG);
		this.sign = sign;
		this.nathemWorld = nathemWorld;
		this.location = this.sign.getLocation();
		this.location.setWorld(this.nathemWorld.getWorld());
		this.inputs = new ArrayList<Signal>();
		this.outputs = new ArrayList<Signal>();
		this.options = (HashMap<String, Option>) this.getOptionsList().clone();
		this.importOptions();
		this.nathemWorld.getObjects().add(this);
	}
	
	abstract public void activate();
	abstract public HashMap<String, Option> getOptionsList();
	abstract public ObjectType getObjectType();
	abstract public void onSignal(boolean value, Player p);
	
	final public void initSignals()
	{
		// Input first
		for(NathemObject no : this.getNathemWorld().getObjects())
		{
			for(int input : this.getSign().getInputsSignals())
			{
				for(OutputSignal os : no.getSign().getOutputSignals())
				{
					if(os.getSignal() == input)
					{
						Signal newSignal = new Signal(no, this, os.isOn());
						NSCore.log("Signal <"+newSignal.isOn()+"> between "+newSignal.getEmitter()+" and "+ newSignal.getReceiver() +" created", LogType.LOG);
					}
				}
				
			}
		}
	}

	
	final void importOptions()
	{
		// Import des options du sign (merge)
		for(String optionKey : this.getOptions().keySet())
		{
			String optionValue = this.sign.getOptions().get(optionKey);
			if(optionValue != null) this.options.get(optionKey).setValue(optionValue);;
		}
		
		for(String key : this.sign.getOptions().keySet())
		{
			if(this.options.containsKey(key)) continue;
			else
			{
				this.options.put(key, new Option(key,this.sign.getOptions().get(key), OptionType.TEXT));
			}
		}
	}

	
	public ArrayList<Signal> getInputs() {
		return inputs;
	}

	public ArrayList<Signal> getOutputs() {
		return outputs;
	}
	
	public ArrayList<Signal> getOutputs(boolean on)
	{
		ArrayList<Signal> outputs = new ArrayList<Signal>();
		for(Signal output : this.outputs)
		{
			if(output.isOn() == on) outputs.add(output);
		}
		return outputs;
	}
	
	public NathemWorld getNathemWorld() {
		return nathemWorld;
	}
	

	public Location getLocation() {
		return location;
	}

	public Sign getSign() {
		return sign;
	}
	
	

	public HashMap<String, Option> getOptions() {
		return options;
	}
	
	protected final Object getCustomOption(String key, OptionType optionType, boolean defaultOptionType)
	{
		// Option format
				if(!this.options.containsKey(key))
				{
					NSCore.log("No option <"+key+"> for " + this.getObjectType().name(), LogType.LOG);
					return null;
				}
				
				// Formatage de l'option
				Option option = this.options.get(key);
				
				if(defaultOptionType) optionType = option.getOptionType();
					
				
				switch(optionType)
				{
				case BOOLEAN:
					if(option.getValue().equalsIgnoreCase("true")) return true;
					else if(option.getValue().equalsIgnoreCase("false")) return false;
					else
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Boolean required.");
						return false;
					}
				case INTEGER:
					if(!Utils.isInt(option.getValue()))
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Integer required.");
						return 0;
					}
					else return Integer.parseInt(option.getValue());
				case DURATION:
					Integer duration = Utils.getDuration(option.getValue());
					if(duration == null)
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Duration required.");
						return 0;
					}
					return duration;
				case DOUBLE:
					if(!Utils.isDouble(option.getValue()))
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Double required.");
						return 0.0;
					}
					else return Double.parseDouble(option.getValue());
				case LOCATION:
					if(option.getValue().equals("null"))
					{
						return null;
					}
					int[] coords = Utils.getLocation(option.getValue());
					if(coords == null)
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Location required (format x;y;z).");
						return new Location(this.getNathemWorld().getWorld(),0,0,0);
					}
					else return new Location(this.getNathemWorld().getWorld(),coords[0],coords[1],coords[2]);
				case MATERIAL:
					ItemStack m = Utils.getMaterial(option.getValue());
					if(m == null)
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Material required.");
						return new ItemStack(Material.STONE);
					}
					else return m;
				case MOB:
					EntityType et = Utils.getMobType(option.getValue());
					if(et == null)
					{
						NSCore.log("Value '" + option.getValue() +"' for option <"+option.getKey()+"> is invalid. Mob type required.");
						return EntityType.ZOMBIE;
					}
					else return et;
					
				default: case TEXT:
					return option.getValue();
					
							
				}
	}

	protected final Object getOption(String key)
	{
		return this.getCustomOption(key, null, true);
		
	}
	
	protected final void sendAllSignals(Player p)
	{
		for(Signal s : this.getOutputs())
		{
			// Signal sending...
			s.getReceiver().onSignal(s.isOn(), p);
			if(p != null) NSCore.log(this + " sending <"+s.isOn()+", "+p.getName()+"> signal to " + s.getReceiver(), LogType.LOG);
			else NSCore.log(this + " sending <"+s.isOn()+"> signal to " + s.getReceiver(), LogType.LOG);
		}
	}
	
	protected final void sendSignals(boolean outputValue, boolean value, Player p)
	{
		for(Signal s : this.getOutputs())
		{
			if(s.isOn() != outputValue) continue;
			// Signal sending...
			s.getReceiver().onSignal(value, p);
			if(p != null) NSCore.log(this + " sending <"+value+", "+p.getName()+"> signal to " + s.getReceiver(), LogType.LOG);
			else NSCore.log(this + " sending <"+value+"> signal to " + s.getReceiver(), LogType.LOG);
		}
	}
	
	protected final void sendSignals(boolean outputValue, boolean value)
	{
		this.sendSignals(outputValue, value, null);
	}
	
	protected final void sendAllSignals()
	{
		this.sendAllSignals(null);
	}
	

	@Override
	public String toString() {
		return this.getObjectType().name() + " ["+this.location.getBlockX()+","+this.location.getBlockY()+","+this.location.getBlockZ()+"]";
	}

	public final UUID getUniqueID()
	{
		return this.getSign().getUniqueId();
	}
	
	public void destroySign()
	{
		this.location.getBlock().setType(Material.AIR);
	}
	
	public NSCore getCore()
	{
		return this.nathemWorld.getCore();
	}
	
	public Player getNearestPlayer()
	{
		return this.nathemWorld.getNearestPlayer(this.location);
	}
	
}
