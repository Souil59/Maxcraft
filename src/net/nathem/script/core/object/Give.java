package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.nathem.script.core.NSCore;
import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.Utils;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Give extends NathemObject{

	private String suffName;
	private boolean global;
	private Inventory source;
	
	
	public Give() {
		super();
	}

	public Give(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.suffName = (String) this.getOption("stuff");
		this.global = (boolean) this.getOption("global");
		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("stuff", new Option("stuff", "null", OptionType.TEXT));
		optionsList.put("global", new Option("global", "false", OptionType.BOOLEAN));

		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.GIVE;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(!value) return;
		
		this.source = this.nathemWorld.getStuff(this.suffName);
		if(this.source == null)
		{
			NSCore.log("Error with GIVE. There is no stuff named <"+this.suffName+"> !");
		}
		
		if(this.global)
		{
			for(Player player : this.getNathemWorld().getWorld().getPlayers())
			{
				this.give(player);
			}
		}
		else if(p != null)
		{
			this.give(p);
		}
	}
	
	public void give(Player p)
	{
		if(this.source == null) return;
		Utils.copyMergeInventory(this.source, p.getInventory());
		p.updateInventory();
	}

	
	// OPTIONS
	public String getSuffName() {
		return suffName;
	}

	public boolean isGlobal() {
		return global;
	}
	
}
