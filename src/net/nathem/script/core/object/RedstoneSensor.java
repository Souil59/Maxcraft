package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.Option;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class RedstoneSensor extends NathemObject {

	public boolean switchValue;
	public boolean activated;
	
	public RedstoneSensor() {
		super();
	}

	public RedstoneSensor(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.switchValue = false;
		this.activated = true;
	}

	@Override
	public void activate() {
	
		
	}
	
	public void switchSignal()
	{
		Player nearest = this.getNearestPlayer();
		this.switchValue = !this.switchValue;
		this.sendSignals(this.switchValue, this.switchValue, nearest);
	}
	
	public void invSwitchSignal()
	{
		Player nearest = this.getNearestPlayer();
		this.switchValue = !this.switchValue;
		this.sendSignals(!this.switchValue, !this.switchValue, nearest);
	}
	
	public void normalSignal(boolean value)
	{
		Player nearest = this.getNearestPlayer();
		this.switchValue = value;
		this.sendAllSignals(nearest);
		this.activated = false;
	}
	
	public void powered(boolean value)
	{
		
		switch(this.getType())
		{
		case "normal": default:
			this.normalSignal(value);
			break;
		case "switch":
			this.switchSignal();
			break;
		case "!switch":
			this.invSwitchSignal();
			break;
		}
	}
	
	
	
	public boolean getSwitchValue() {
		return switchValue;
	}
	
	

	public boolean isActivated() {
		return activated;
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("type", new Option("type", "normal", OptionType.TEXT));
		
		return optionsList;
	}


	@Override
	public ObjectType getObjectType() {
		return ObjectType.REDSTONE_SENSOR;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		this.activated = value;		
	}

	// Options

	public String getType()
	{
		return (String) this.getOption("type");
	}

}
