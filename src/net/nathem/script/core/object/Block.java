package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.Option;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Block extends NathemObject {

	private boolean value;
	private boolean activated;
	protected ItemStack material;
	
	public Block() {
		super();
		
	}
	
	public Block(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.value = false;
		this.activated = false;
		if(this.getObjectType() == ObjectType.BLOCK)
		{
			this.material = (ItemStack) this.getOption("block");
		}
	}
	
	
	@Override
	public void activate() {
	
		
	}


	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("pulsor", new Option("pulsor", "-1", OptionType.DURATION));
		optionsList.put("delay", new Option("delay", "0t", OptionType.DURATION));
		optionsList.put("block", new Option("block", "STONE", OptionType.MATERIAL));
		optionsList.put("data", new Option("data", "0", OptionType.INTEGER));
		return optionsList;
	}


	@Override
	public ObjectType getObjectType() {
		// TODO Auto-generated method stub
		return ObjectType.BLOCK;
	}

	@Override
	public void onSignal(boolean value, Player p) {

		new ActivateTask(this, value).runTaskLater(this.getCore().plugin, this.getDelay());
	}
	
	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}
	
	public boolean isActivated()
	{
		return this.activated;
	}
	
	public void setState(boolean state)
	{
		this.value = state;
		if(state){
			this.location.getBlock().setType(this.material.getType());
			this.location.getBlock().setData((byte) this.getData());
			Bukkit.getLogger().info(""+this.location.getBlock().getData());

			Bukkit.getLogger().info(""+this.getData());
		}
		else this.location.getBlock().setType(Material.AIR);
	}
	
	public void switchState()
	{
		this.setState(!this.value);
	}
	

	// Options
	
	public int getPulsor()
	{
		return (int) this.getOption("pulsor");
	}
	
	public int getDelay()
	{
		return (int) this.getOption("delay");
	}
	
	public ItemStack getBlock()
	{
		return this.material;
	}
	
	public int getData()
	{
		return (int) this.getOption("data");
	}
	public class PulsorTask extends BukkitRunnable{

		private Block block;

		
		public PulsorTask(Block block)
		{
			this.block = block;
			
		}
		
		 public void run() {
		      
			 if(!block.isActivated()) return;
			 block.switchState();
			 new PulsorTask(block).runTaskLater(this.block.getCore().plugin, block.getPulsor());

		 }
	}
	
	public class ActivateTask extends BukkitRunnable{

		private Block block;
		private boolean activated;
		
		public ActivateTask(Block block, boolean activated)
		{
			this.block = block;
			this.activated = activated;
			
		}
		
		 public void run() {
		      this.block.setActivated(activated);
		      this.block.setState(activated);
		      int pulsor = block.getPulsor();
		      if(pulsor != -1)
		      {
		    	  new PulsorTask(block).runTaskLater(this.block.getCore().plugin, pulsor);
		      }

		 }
	}
	

}
