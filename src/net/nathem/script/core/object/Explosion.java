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

public class Explosion extends NathemObject{
	
	private double power;
	private boolean fire;
	
	public Explosion() {
		super();
	}

	public Explosion(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.power = (double) this.getOption("power");
		this.fire = (boolean) this.getOption("fire");
	}

	@Override
	public void activate() {
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("power", new Option("power", "4", OptionType.DOUBLE));
		optionsList.put("fire", new Option("fire", "false", OptionType.BOOLEAN));

		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.EXPLOSION;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		if(value == false) return;
		this.explosion();
		
	}
	
	public void explosion()
	{
		this.location.getWorld().createExplosion(this.location, (float) this.power, this.fire);
	}

	// Options
	
	public double getPower() {
		return power;
	}

	public boolean isFire() {
		return fire;
	}
	
	

}
