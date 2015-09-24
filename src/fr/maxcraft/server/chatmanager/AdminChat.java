package fr.maxcraft.server.chatmanager;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AdminChat {

	public AdminChat(AsyncPlayerChatEvent e) {
		e.getPlayer().sendMessage(ChatColor.RED + "[A] " +ChatColor.GRAY+ e.getPlayer().getName()+ " : " +ChatColor.WHITE+   e.getMessage().substring(1,e.getMessage().length()));
		for (Player p : Bukkit.getOnlinePlayers())
			if (p.hasPermission("maxcraft.guide")&& !p.equals(e.getPlayer()))
				p.sendMessage(ChatColor.RED + "[A] " +ChatColor.GRAY+ e.getPlayer().getName()+ " : " +ChatColor.WHITE+   e.getMessage().substring(1,e.getMessage().length()));
	}

}
