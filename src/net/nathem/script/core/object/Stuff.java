package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.nathem.script.core.NSCore;
import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.LogType;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Stuff extends NathemObject{

	private String name;
	private Chest chest;
	private double chance;
	private boolean openable;


	
	public Stuff() {
		super();
	}

	public Stuff(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.name = (String) this.getOption("name");
		this.chance = (double) this.getOption("chance");
		this.openable = !((boolean) this.getOption("protected"));
		this.chest = null;
		
		// Chest detection
		if(!this.sign.isWallSign()) return;
		org.bukkit.block.Block b = this.getLocation().getBlock().getRelative(this.sign.getFacing().getOppositeFace());
		if(!(b.getState() instanceof Chest))
		{
			NSCore.log("Error loading STUFF <"+this.name+">, there is no chest on "+b.getX()+","+b.getY()+","+b.getZ()+" !", LogType.CONSOLE);
			return;
		}
		this.chest = (Chest) b.getState();
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("name", new Option("name", "null", OptionType.TEXT));
		optionsList.put("chance", new Option("chance", "1", OptionType.DOUBLE));
		optionsList.put("protected", new Option("protected", "true", OptionType.BOOLEAN));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.STUFF;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
	}


	// Options
	
	public String getName() {
		return name;
	}

	public Chest getChest() {
		return chest;
	}

	public double getChance() {
		return chance;
	}
	
	public Inventory getInventory()
	{
		if(this.chest == null) return null;
		else return this.chest.getInventory();
	}

	public boolean isOpenable() {
		return openable;
	}
	
		
	

}
