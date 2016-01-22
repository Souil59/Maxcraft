package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;
import net.nathem.script.enums.OptionType;

public class Message extends NathemObject {

	
	private boolean global;
	public Message() {
		super();
	}

	public Message(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.global = (boolean) this.getOption("global");
	}

	@Override
	public void activate() {
		
		
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		
		HashMap<String, Option> optionsList = new HashMap<String, Option>();
		optionsList.put("text", new Option("text", "Text message here !", OptionType.TEXT));
		optionsList.put("global", new Option("global", "false", OptionType.BOOLEAN));
		return optionsList;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.MESSAGE;
	}

	@Override
	public void onSignal(boolean value, Player p) {
		
		if(value == false) return;
		
		if(this.global)
		{
			for(Player player : this.getNathemWorld().getWorld().getPlayers())
			{
				player.sendMessage(this.getColoredMessage());
			}
		}
		else if(p != null)
		{
			p.sendMessage(this.getColoredMessage());
		}
		
	}
	
	public String getColoredMessage()
	{
		return ChatColor.translateAlternateColorCodes('&', this.getText());
	}
	
	// Options

	public String getText()
	{
		return (String) this.getOption("text");
	}
	
	public boolean isGlobal()
	{
		return this.global;
	}

}
