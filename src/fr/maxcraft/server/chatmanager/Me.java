package fr.maxcraft.server.chatmanager;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Me {

	public Me(AsyncPlayerChatEvent e) {
		e.setMessage(e.getMessage().substring(1, e.getMessage().length()-1));
		for (Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(ChatColor.GRAY+e.getPlayer().getName()+" "+e.getMessage());
	}

}
