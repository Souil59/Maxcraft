package fr.maxcraft.server.chatmanager;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.maxcraft.player.User;

public class FactionChat {

	public FactionChat(AsyncPlayerChatEvent e) {
		for (Player p : Bukkit.getOnlinePlayers())
			if (User.get(p).getFaction()!=null)
				if (User.get(p).getFaction().equals(User.get(e.getPlayer()).getFaction()))
					User.get(p).sendNotifMessage(ChatColor.BLUE+"[Faction] "+e.getPlayer().getName()+" "+e.getMessage());
	}

}
