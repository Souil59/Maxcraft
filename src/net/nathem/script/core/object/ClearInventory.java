package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.Utils;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class ClearInventory extends NathemObject{

	private boolean global;

	
	public ClearInventory() {
		super();
	}

	public ClearInventory(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.global = (boolean) this.getOption("global");
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("global", new Option("global", "false", OptionType.BOOLEAN));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.CLEAR_INVENTORY;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		if(value == false) return;
		
		if(this.global)
		{
			for(Player player : this.getNathemWorld().getWorld().getPlayers())
			{
				Utils.clearInventory(player);
			}
		}
		else if(p != null)
		{
			Utils.clearInventory(p);
		}
	}
	
	// Options
	
	public boolean isGlobal()
	{
		return this.global;
	}
	

}
