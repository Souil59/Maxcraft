package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Spawner extends NathemObject {

	
	private Location destination;
	private EntityType mobType;
	private boolean activated;
	private int amountSpawned;
	
	public Spawner() {
		super();
	}

	public Spawner(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.destination = (Location) this.getOption("location");
		if(this.destination == null) this.destination = this.location;
		this.destination.add(0.5, 0.1, 0.5);
		this.mobType = (EntityType) this.getOption("mob");
		this.activated = false;
		this.amountSpawned = 0;
	}

	@Override
	public void activate() {
		
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("mob", new Option("mob", "zombie", OptionType.MOB));
		optionsList.put("amount", new Option("amount", "1", OptionType.INTEGER));
		optionsList.put("frequency", new Option("frequency", "0t", OptionType.DURATION));
		optionsList.put("delay", new Option("delay", "0t", OptionType.DURATION));
		optionsList.put("stuff", new Option("stuff", "null", OptionType.TEXT));
		optionsList.put("effects", new Option("effects", "null", OptionType.TEXT));
		optionsList.put("location", new Option("location", "null", OptionType.LOCATION));
		optionsList.put("mob-name", new Option("mob-name", "null", OptionType.TEXT));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SPAWNER;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		this.activated = value;
		this.amountSpawned = 0;
		if(value == false) return;
		
		new SpawnMobTask(this).runTaskLater(this.getCore().plugin, this.getDelay());
		
	}
	
	
	// Options

	public Location getDestination()
	{
		return this.destination;
	}
	
	public String getMobName()
	{
		return (String) this.getOption("mob-name");
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
	
	public EntityType getMobType()
	{
		return this.mobType;
	}
	
	public boolean isActivated()
	{
		return this.activated;
	}
	
	public void spawnMob()
	{
		this.getNathemWorld().getWorld().spawnEntity(this.destination, this.mobType);
		this.amountSpawned++;
	}
	
	public class SpawnMobTask extends BukkitRunnable
	{

		private Spawner spawner;
	
		public SpawnMobTask(Spawner spawner) {
			super();
			this.spawner = spawner;
		}

		@Override
		public void run() {
			if(!spawner.isActivated()) return;
			if(spawner.amountSpawned >= spawner.getAmount()) return;
			spawner.spawnMob();
			new SpawnMobTask(spawner).runTaskLater(spawner.getCore().plugin, spawner.getFrequency());
		}
		
	}
	

}
