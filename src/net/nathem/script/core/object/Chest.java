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
import net.nathem.script.enums.LogType;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Chest extends NathemObject{

	private String suffName;
	private Inventory source;
	private org.bukkit.block.Chest chest;
	
	public Chest() {
		super();
	}

	public Chest(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.suffName = (String) this.getOption("stuff");
		
		// Chest detection
		if(!this.sign.isWallSign()) return;
		org.bukkit.block.Block b = this.getLocation().getBlock().getRelative(this.sign.getFacing().getOppositeFace());
		if(!(b.getState() instanceof org.bukkit.block.Chest))
		{
			NSCore.log("Error loading CHEST, there is no chest on "+b.getX()+","+b.getY()+","+b.getZ()+" !", LogType.CONSOLE);
			return;
		}
		this.chest = (org.bukkit.block.Chest) b.getState();
		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("stuff", new Option("stuff", "null", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.CHEST;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(this.chest == null)
		{
			NSCore.log("Error with CHEST. There is no chest !");
			return;
		}
		
		if(value)
		{
			this.source = this.nathemWorld.getStuff(this.suffName);
			if(this.source == null)
			{
				NSCore.log("Error with CHEST. There is no stuff named <"+this.suffName+"> !");
				return;
			}
		
			
			Utils.copyMergeInventory(this.source, this.chest.getInventory());
		
		}
		else
		{
			this.chest.getInventory().clear();
		}
		
		this.chest.update();
		
	}
	


	
	// OPTIONS
	public String getSuffName() {
		return suffName;
	}

}
