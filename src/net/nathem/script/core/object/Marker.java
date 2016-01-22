package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Marker extends NathemObject{

	private String name;
	
	public Marker() {
		super();
	}

	public Marker(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.name = (String) this.getOption("name");
	}

	@Override
	public void activate() {
		if(this.name != "null")
		{
			this.nathemWorld.getMarkers().put(this.name, this.getLocation().clone().add(0.5,0,0.5));
		}
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("name", new Option("name", "null", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.MARKER;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
	}

	
	// OPTIONS
	
	public String getName() {
		return name;
	}

}
