package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Heal extends NathemObject{

	private boolean global;
	private Type type;
	private double health;
	
	public Heal() {
		super();
	}

	public Heal(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.global = (boolean) this.getOption("global");
		this.health = (double) this.getOption("health");
		
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
		optionsList.put("health", new Option("health", "20", OptionType.DOUBLE));
		optionsList.put("type", new Option("type", "=", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.HEAL;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(value == false) return;
		if(this.global)
		{
			for(Player player : this.getNathemWorld().getWorld().getPlayers())
			{
				this.heal(player);
			}
		}
		else if(p != null)
		{
			this.heal(p);
		}
	}
	
	public void heal(Player p)
	{
		double newHealth = 20.;
		switch(this.type)
		{
		case ADD:
			newHealth = p.getHealth() + this.health;
			break;
		case SUB:
			newHealth = p.getHealth() - this.health;
			break;
		case SET:
			newHealth = this.health;
			break;
		}
		
		if(newHealth > p.getMaxHealth()) newHealth = p.getMaxHealth();
		if(newHealth < 0) newHealth = 0.;
		p.setHealth(newHealth);
	}
	
	// Options
	
	public boolean isGlobal()
	{
		return this.global;
	}
	
	
	
	public Type getType() {
		return type;
	}

	public double getHealth() {
		return health;
	}



	public enum Type
	{
		SET,
		ADD,
		SUB,
		;
	}

}
