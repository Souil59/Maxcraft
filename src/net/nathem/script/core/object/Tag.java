package net.nathem.script.core.object;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.Option;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Tag extends NathemObject {
	
	private boolean activated;
	
	public Tag() {
		super();
	}
	
	public Tag(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.activated = true;
	}
	
	
	
	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		
		optionsList.put("radius", new Option("radius", "5", OptionType.DOUBLE));
		optionsList.put("min-y", new Option("min-y", "0", OptionType.INTEGER));
		optionsList.put("max-y", new Option("max-y", "256", OptionType.INTEGER));
		optionsList.put("type", new Option("type", "no-damage", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.TAG;
	}
	
	
	public void playerCrossLine(Player p, boolean enter)
	{
		switch(this.getType())
		{
			case "normal":
				if(enter)
				{
					this.sendAllSignals(p);
					this.activated = false;
				}
				break;
			case "switch":
				if(enter) this.sendSignals(true, true, p);
				else this.sendSignals(false,  false, p);
				break;
		}
	}
	
	
	
	public boolean isActivated() {
		return activated;
	}

	public boolean isInCaptorArea(Location l)
	{
		if(l.distance(this.location) < this.getRadius())
		{
			if(l.getBlockY() < this.getMinY() || l.getBlockY() > this.getMaxY())
			{
				return false;
			}
			return true;
		}
		
		return false;
			
	}
	// Options
	
	public Double getRadius()
	{
		return (Double) this.getOption("radius");
	}
	
	public int getMaxY()
	{
		return (int) this.getOption("max-y");
	}
	
	public int getMinY()
	{
		return (int) this.getOption("min-y");
	}
	
	public String getType()
	{
		return (String) this.getOption("type");
	}

	@Override
	public void activate() {
	
		
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		this.activated = value;
	}





}
