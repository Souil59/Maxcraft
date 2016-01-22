package net.nathem.script.core.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.nathem.script.core.Option;
import net.nathem.script.enums.ObjectType;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;


public class Sign {
 
	private UUID uniqueId;
	private Location location;
	private String objectName;
	private ArrayList<Integer> inputsSignals;
	private ArrayList<OutputSignal> outputSignals;
	private HashMap<String, String> options;
	private BlockFace facing;
	private boolean wallSign;

	public Sign(UUID uniqueId, Location location, String objectName, BlockFace facing, boolean wallSign) {
		super();
		this.uniqueId = uniqueId;
		this.location = location;
		this.objectName = objectName;
		this.inputsSignals = new ArrayList<Integer>();
		this.outputSignals = new ArrayList<OutputSignal>();
		this.options = new HashMap<String, String>();
		this.facing = facing;
		this.wallSign = wallSign;
	}
	
	public void copyData(Sign sign)
	{
		this.objectName = sign.getObjectName();
		this.inputsSignals = (ArrayList<Integer>) sign.getInputsSignals().clone();
		this.outputSignals = (ArrayList<OutputSignal>) sign.getOutputSignals().clone();
		this.options = (HashMap<String, String>) sign.getOptions().clone();
	}

	public UUID getUniqueId() {
		return uniqueId;
	}


	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public String getObjectName() {
		return objectName;
	}


	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}


	public ArrayList<Integer> getInputsSignals() {
		return inputsSignals;
	}


	public void setInputsSignals(ArrayList<Integer> inputsSignals) {
		this.inputsSignals = inputsSignals;
	}


	public ArrayList<OutputSignal> getOutputSignals() {
		return outputSignals;
	}


	public void setOutputSignals(ArrayList<OutputSignal> outputSignals) {
		this.outputSignals = outputSignals;
	}


	public HashMap<String, String> getOptions() {
		return options;
	}


	public void setOptions(HashMap<String, String> options) {
		this.options = options;
	}


	public BlockFace getFacing() {
		return facing;
	}


	public void setFacing(BlockFace facing) {
		this.facing = facing;
	}


	public boolean isWallSign() {
		return wallSign;
	}


	public void setWallSign(boolean wallSign) {
		this.wallSign = wallSign;
	}
	
	public Location getRealLocation(World world)
	{
		Location loc = this.location.clone();
		loc.setWorld(world);
		return loc;
	}
	
	public String getTypeName()
	{
		return this.objectName.substring(1).toUpperCase();
	}
	
	
	public ObjectType getObjectType()
	{
		return ObjectType.fromObjectName(this.objectName.substring(1));
	}
	
	public HashMap<String, Option> getDefaultOptions() {
		
		ObjectType type = this.getObjectType();
		if(type == null) return new HashMap<String, Option>();
		
		return type.getOptionsList();
		
	}
	
	public Boolean getOutputSignal(int i)
	{
		for(OutputSignal os : this.getOutputSignals())
		{
			if(os.getSignal() == i) return os.isOn();
		}
		
		return null;
	}


	@Override
	public String toString() {
		return "Sign [uniqueId=" + uniqueId + ", location=" + location
				+ ", objectName=" + objectName + ", inputsSignals="
				+ inputsSignals + ", outputSignals=" + outputSignals
				+ ", options=" + options + ", facing=" + facing + ", wallSign="
				+ wallSign + "]";
	}

	
	
	
}
