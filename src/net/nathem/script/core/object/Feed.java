package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Feed extends NathemObject{

	private boolean global;
	private Type type;
	private int food;
	
	public Feed() {
		super();
	}

	public Feed(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.global = (boolean) this.getOption("global");
		this.food = (int) this.getOption("food");
		
		switch((String) this.getOption("type"))
		{
		case "add": case "+":
			this.type = Type.ADD;
			break;
		case "sub": case "-":
			this.type = Type.SUB;
			break;
		default:
			this.type = Type.SET;
			break;
		}
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("global", new Option("global", "false", OptionType.BOOLEAN));
		optionsList.put("food", new Option("food", "20", OptionType.INTEGER));
		optionsList.put("type", new Option("type", "=", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.FEED;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(value == false) return;
		if(this.global)
		{
			for(Player player : this.getNathemWorld().getWorld().getPlayers())
			{
				this.feed(player);
			}
		}
		else if(p != null)
		{
			this.feed(p);
		}
	}
	
	public void feed(Player p)
	{
		int newFood = 20;
		switch(this.type)
		{
		case ADD:
			newFood = p.getFoodLevel() + this.food;
			break;
		case SUB:
			newFood = p.getFoodLevel() - this.food;
			break;
		case SET:
			newFood = this.food;
			break;
		}
		
		if(newFood > 20) newFood = 20;
		if(newFood < 0) newFood = 0;
		p.setFoodLevel(newFood);
	}
	
	// Options
	
	public boolean isGlobal()
	{
		return this.global;
	}
	
	
	
	public Type getType() {
		return type;
	}


	public int getFood() {
		return food;
	}

	public enum Type
	{
		SET,
		ADD,
		SUB,
		;
	}

}
