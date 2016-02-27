package fr.maxcraft.server.chatmanager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.maxcraft.player.User;

public class Chat {

	public Chat(AsyncPlayerChatEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()){
			User u = User.get(e.getPlayer());
			TextComponent player;
			if (u.getFaction()==null||u.getPerms().hasPerms("maxcraft.guide"))
				player = new TextComponent(u.getPerms().dysplayName()+ChatColor.WHITE+" : ");
			else
				player = new TextComponent(ChatColor.AQUA+u.getFaction().getTAG()+" "+u.getName()+ChatColor.WHITE+" : ");
			if (u.isAfk()){
                u.setAfk(false);
                u.sendMessage(org.bukkit.ChatColor.GRAY+"Vous n'êtes plus marqué absent");
            }
			player.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour ajouter ce joueur en ami.").color(ChatColor.BLUE).create() ));
			player.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/friends add "+e.getPlayer().getName()));
			TextComponent message = new TextComponent(e.getMessage());
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour repondre en privé.").color(ChatColor.BLUE).create() ));
			message.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/msg "+e.getPlayer().getName()));
			player.addExtra(message);
			p.spigot().sendMessage(player);
		}
	}

}
