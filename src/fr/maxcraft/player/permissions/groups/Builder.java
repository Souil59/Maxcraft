package fr.maxcraft.player.permissions.groups;


import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import fr.maxcraft.player.permissions.Group;

public class Builder implements Group{
	
	public Builder(){
		
	}

	@Override
	public String getPrefix() {
		return "Builder ";
	}

	@Override
	public List<String> getPermissions() {
		return Arrays.asList("maxcraft.guide","maxcraft.modo","maxcraft.base");
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_PURPLE;
	}

}
