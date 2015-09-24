package fr.maxcraft.player.permissions;

import java.util.List;

import org.bukkit.ChatColor;

public interface Group {

	abstract List<String> getPermissions();
	abstract String getPrefix();
	abstract ChatColor getColor();
}
