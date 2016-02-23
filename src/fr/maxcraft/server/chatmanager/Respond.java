package fr.maxcraft.server.chatmanager;

import java.util.HashMap;

import fr.maxcraft.server.things.Things;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxcraft.player.User;

public class Respond implements CommandExecutor {

	public static HashMap<Player,Player> reply = new HashMap<Player,Player>();

	
	public Respond() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		if (!reply.containsKey((Player)sender)){
			sender.sendMessage("Vous n'avez personne à qui répondre");
			return true;
		}
		Player j = (Player) sender;
		if (args.length<1){
			sender.sendMessage("/r <message>");
			return true;
		}
		User u = User.get(reply.get((Player)sender));
		if (u == null){
			sender.sendMessage("Ce joueur n'est pas connecté.");
			return true;
		}
		if (u.isAfk()) sender.sendMessage(org.bukkit.ChatColor.DARK_PURPLE+"Ce joueur est afk, il risque de ne pas de répondre");

		String p = ChatColor.GOLD+"["+j.getName()+ChatColor.GOLD+" -> Moi] :";
		String p2 = ChatColor.GOLD+"[Moi -> "+u.getName()+"] :";
		String m =ChatColor.WHITE+"";
		for (int i = 0; i<args.length ;i++)
			m += " "+args[i];
		TextComponent message = new TextComponent(p+m);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour repondre.").color(ChatColor.BLUE).create() ));
		message.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/msg "+j.getName()));
		u.sendNotifMessage(message);
		j.sendMessage(p2+m);
		Respond.reply.put(u.getPlayer() , (Player) sender);
        for (Player pl : Bukkit.getOnlinePlayers()){
            if (User.get(pl)==null) continue;
            if (!User.get(pl).isSocialspy())continue;
            pl.sendMessage(Things.socialspyMessage(j, u.getPlayer(), m));
        }
		return true;
	}

}
