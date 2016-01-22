package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Strike extends NathemObject {

	
	private Location destination;

	private boolean activated;
	private int amountStriked;
	
	public Strike() {
		super();
	}

	public Strike(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.destination = (Location) this.getOption("location");
		if(this.destination == null) this.destination = this.location;
		this.activated = false;
		this.amountStriked = 0;
	}

	@Override
	public void activate() {
		
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("amount", new Option("amount", "1", OptionType.INTEGER));
		optionsList.put("frequency", new Option("frequency", "0t", OptionType.DURATION));
		optionsList.put("delay", new Option("delay", "0t", OptionType.DURATION));
		optionsList.put("location", new Option("location", "null", OptionType.LOCATION));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SPAWNER;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		this.activated = value;
		this.amountStriked = 0;
		if(value == false) return;
		new StrikeTask(this).runTaskLater(this.getCore().plugin, this.getDelay());
		
	}
	
	
	// Options

	public Location getDestination()
	{
		return this.destination;
	}
	
	
	public int getDelay()
	{
		return (int) this.getOption("delay");
	}
	
	public int getFrequency()
	{
		return (int) this.getOption("frequency");
	}
	
	public int getAmount()
	{
		return (int) this.getOption("amount");
	}
	
	
	public boolean isActivated()
	{
		return this.activated;
	}
	
	public void strike()
	{
		this.getNathemWorld().getWorld().strikeLightning(this.destination);
		this.amountStriked++;
	}
	
	public class StrikeTask extends BukkitRunnable
	{

		private Strike strike;
	
		public StrikeTask(Strike strike) {
			super();
			this.strike = strike;
		}

		@Override
		public void run() {
			if(!strike.isActivated()) return;
			if(strike.amountStriked >= strike.getAmount()) return;
			strike.strike();
			new StrikeTask(strike).runTaskLater(strike.getCore().plugin, strike.getFrequency());
		}
		
	}
	

}
