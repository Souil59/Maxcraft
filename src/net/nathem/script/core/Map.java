package net.nathem.script.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.yaml.snakeyaml.Yaml;

import net.nathem.script.core.sign.OutputSignal;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.LogType;

public class Map {

	private String worldName;
	private ArrayList<Sign> signs;
	
	public Map(String worldName) throws IOException {
		
		NSCore.log("Loading map <" + worldName + ">");
		this.worldName = worldName;
		this.readSigns();
		
	}
	
	public void readSigns() throws IOException
	{
		// Lecture des signs du YAML
		this.signs = new ArrayList<Sign>();
		
		try 
		{
			InputStream input = new FileInputStream(this.getScriptFile());
			Yaml yaml = new Yaml();
			java.util.Map<String, Object> data = (java.util.Map<String, Object>) yaml.load(input);
			
			
			java.util.Map<String, Object> signsMap = (java.util.Map<String, Object>) data.get("signs");
		
		
			
			if(signsMap != null)
			{
				
			for(Entry<String, Object> sign : signsMap.entrySet())
			{
				// UUID
				UUID uuid = UUID.fromString(sign.getKey());
				java.util.Map<String, Object> signParams = (java.util.Map<String, Object>) sign.getValue();
				
				// Location
				String coords[] = ((String) signParams.get("location")).split(";");
				if(coords.length != 3) continue;
				Location location = new Location(null, Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));
				
				
				// ObjectName
				String objectName = ((String) signParams.get("objectName"));
				
				// Facing
				BlockFace facing = BlockFace.valueOf(((String) signParams.get("facing")));
				
				// WallSign
				boolean wallSign = ((boolean) signParams.get("wallSign"));
				
				// Input Signals
				ArrayList<Integer> inputSignals = (ArrayList<Integer>) signParams.get("inputSignals");
				
				// Output Signals
				ArrayList<HashMap<Integer,Boolean>> outputSignalsMap = (ArrayList<HashMap<Integer,Boolean>>) signParams.get("outputSignals");
				ArrayList<OutputSignal> outputSignals = new ArrayList<OutputSignal>();
				
				for(HashMap<Integer,Boolean> output : outputSignalsMap)
				{
					for(Entry<Integer, Boolean> oneOutput : output.entrySet())
					{
						outputSignals.add(new OutputSignal(oneOutput.getKey(), oneOutput.getValue()));
					}
				}
				
				// Options
				HashMap<String, String> options = new HashMap<String, String>();
				HashMap<String, Object> optionsMap = (HashMap<String, Object>) signParams.get("options");
				for(String optionKey : optionsMap.keySet())
				{
					options.put(optionKey, ""+ optionsMap.get(optionKey));
				}
				
				
				
				Sign newSign = new Sign(uuid, location, objectName, facing, wallSign);
				newSign.setInputsSignals(inputSignals);
				newSign.setOutputSignals(outputSignals);
				newSign.setOptions(options);
				this.signs.add(newSign);
				NSCore.log("Sign loaded : "+ newSign.toString(), LogType.LOG);
				
				
			}
			}
	
		} 
		catch (FileNotFoundException e)
		{
			this.getScriptFile().createNewFile();
			this.writeSigns();
			NSCore.log("Script file created for world " + this.worldName);
		}
		
		NSCore.log("Map <" + this.worldName + "> loaded with "+this.signs.size()+" signs");
		
	}
	
	public void writeSigns() throws IOException
	{
		// Ecriture des signs dans le YAML
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		data.put("signs", new HashMap<String, Object>());
		HashMap<String, Object> signsMap = (HashMap<String, Object>) data.get("signs");
		
		
			for(Sign sign : this.signs)
			{
				 
			    signsMap.put(sign.getUniqueId().toString(), new HashMap<String, Object>());
				
				HashMap<String, Object> signMap = (HashMap<String, Object>) signsMap.get(sign.getUniqueId().toString());
				signMap.put("location", sign.getLocation().getBlockX() +";"+ sign.getLocation().getBlockY() + ";" + sign.getLocation().getBlockZ());
				signMap.put("objectName", sign.getObjectName());
				signMap.put("facing", sign.getFacing().name());
				signMap.put("wallSign", sign.isWallSign());
				signMap.put("inputSignals", sign.getInputsSignals());
				signMap.put("options", sign.getOptions());
				signMap.put("outputSignals", new ArrayList<HashMap<Integer,Boolean>>());

				ArrayList<HashMap<Integer,Boolean>> outputSignalsMap = (ArrayList<HashMap<Integer,Boolean>>) signMap.get("outputSignals");
				for(OutputSignal output : sign.getOutputSignals())
				{
					HashMap<Integer,Boolean> outputMap = new HashMap<Integer,Boolean>();
					outputMap.put(output.getSignal(), output.isOn());
					outputSignalsMap.add(outputMap);
				}
			}
			
		Yaml yaml = new Yaml();
		String output = yaml.dump(data);
		FileWriter fw = new FileWriter(this.getScriptFile(), false);
		BufferedWriter buffer = new BufferedWriter(fw);
		buffer.write(output);
		buffer.flush();
		buffer.close();
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public ArrayList<Sign> getSigns() {
		return signs;
	}

	public void setSigns(ArrayList<Sign> signs) {
		this.signs = signs;
	}
	
	public File getScriptFile()
	{
		return new File(this.worldName + "/nathemscript.yml");
		
	}
	
	public Sign getSign(Location l)
	{
		if(!l.getWorld().getName().equals(this.getWorldName())) return null;
		
		for(Sign s : this.signs)
		{
			if(l.getBlockX() == s.getLocation().getBlockX() && l.getBlockY() == s.getLocation().getBlockY() && l.getBlockZ() == s.getLocation().getBlockZ())
			{
				return s;
			}
		}
		
		return null;
			
	}
	
	public int getSignNumber(Sign sign)
	{
		if(sign == null) return -1;
		
		for(int i = 0 ; i < this.signs.size() ; i++)
		{
			if(sign.getUniqueId().equals(this.signs.get(i).getUniqueId()))
			{
				return i+1;
			}
		}
		
		return -1;
	}
	
	public Sign getSign(int number)
	{
		if(number > this.getSigns().size()) return null;
		number--;
		return this.signs.get(number);
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(this.worldName);
	}

	@Override
	public String toString() {
		return "Map [worldName=" + worldName + ", signs=" + signs + "]";
	}
	
	
	public int getNewSignal()
	{
		int s = 2;
		while(true)
		{
			if(!this.isSignalUsed(s)) return s;
			s++;
		}
	}
	
	public boolean isSignalUsed(int signal)
	{
		for(Sign sign : this.signs)
		{
			if(sign.getInputsSignals().contains(signal)) return true;
			
			for(OutputSignal os : sign.getOutputSignals())
			{
				if(os.getSignal() == signal) return true;
			}
		}
		
		return false;
	}
	
}
