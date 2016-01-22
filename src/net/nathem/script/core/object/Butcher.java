package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Butcher extends NathemObject{
	
	public Butcher() {
		super();
	}

	public Butcher(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("radius", new Option("radius", "30", OptionType.DOUBLE));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.BUTCHER;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		if(value == false) return;
		butcher();
		
	}
	
	public void butcher()
	{
		for(Entity entity: this.getLocation().getWorld().getEntities())
		{
			if(!(entity instanceof Monster)) continue;
			if(entity.getLocation().distance(this.getLocation()) > this.getRadius()) continue;
			entity.remove();
		}
	}
	
	// Options
	
	public double getRadius()
	{
		return (double) this.getOption("radius");
	}
	

}
