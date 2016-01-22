package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Teleporter extends NathemObject {

	
	private boolean global;
	private Location destination;
	
	public Teleporter() {
		super();
	}

	public Teleporter(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.global = (boolean) this.getOption("global");
		this.destination = (Location) this.getOption("destination");
	}

	@Override
	public void activate() {
		
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("destination", new Option("destination", "null", OptionType.LOCATION));
		optionsList.put("global", new Option("global", "false", OptionType.BOOLEAN));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.TELEPORTER;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(value == false ) return;
		Location loc;
		if(this.destination == null) loc = this.location.clone();
		else loc = this.destination.clone();
		
		loc.add(0.5, 0.1, 0.5);
		
		if(this.global)
		{
			for(Player player : this.getNathemWorld().getWorld().getPlayers())
			{
				player.teleport(loc);
			}
		}
		else if(p != null)
		{
			p.teleport(loc);
		}
		
	}
	
	
	// Options

	public Location getDestination()
	{
		return this.destination;
	}
	
	public boolean isGlobal()
	{
		return this.global;
	}

}
