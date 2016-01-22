package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.nathem.script.core.NSCore;
import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Random extends NathemObject {

	
	private boolean global;
	public Random() {
		super();
	}

	public Random(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
	}

	@Override
	public void activate() {
		
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("random_outputs", new Option("text", "output|output", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.RANDOM;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(value == false) return;
		int random = (int) (Math.random() * getOutputslist().length);
		int output = Integer.parseInt(getOutputslist()[random]);
		this.getNathemWorld().sendGlobalSignal(output, true, p);

	}
	
	// Options

	public String[] getOutputslist()
	{
		String o = (String) this.getOption("random_outputs");
		String[] outputs = o.split("\\|");
		return outputs;
	}

}
