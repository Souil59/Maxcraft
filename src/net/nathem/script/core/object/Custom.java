package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.event.CustomObjectActivationEvent;
import net.nathem.script.core.event.CustomObjectSignalEvent;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Custom extends NathemObject{

	
	public Custom() {
		super();
	}

	public Custom(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
	}

	@Override
	public void activate() {
		
		Bukkit.getPluginManager().callEvent(new CustomObjectActivationEvent(this));
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("type", new Option("type", "none", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		
		return ObjectType.CUSTOM;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		Bukkit.getPluginManager().callEvent(new CustomObjectSignalEvent(this, value, p));
		
	}
	
	public String getType()
	{
		return (String) this.getOption("type");
	}

	
}
