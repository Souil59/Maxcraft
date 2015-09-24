package fr.maxcraft.player.permissions.groups;


import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import fr.maxcraft.player.permissions.Group;

public class Souverain implements Group{
	
	public Souverain(){
		
	}

	@Override
	public String getPrefix() {
		return "Souverain ";
	}

	@Override
	public List<String> getPermissions() {
		return Arrays.asList("minecraft","craftbukkit","maxcraft.guide","maxcraft.modo","maxcraft.admin","maxcraft.base");
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.RED;
	}

}
