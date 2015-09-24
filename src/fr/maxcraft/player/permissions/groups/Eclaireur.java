package fr.maxcraft.player.permissions.groups;


import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import fr.maxcraft.player.permissions.Group;

public class Eclaireur implements Group{
	
	public Eclaireur(){
		
	}

	@Override
	public String getPrefix() {
		return "Eclaireur  ";
	}

	@Override
	public List<String> getPermissions() {
		return Arrays.asList("maxcraft.guide","maxcraft.base");
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_AQUA;
	}

}
