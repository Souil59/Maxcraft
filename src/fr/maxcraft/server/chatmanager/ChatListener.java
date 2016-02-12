package fr.maxcraft.server.chatmanager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;

import java.util.HashMap;

public class ChatListener implements Listener{

	public ChatListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}

    private HashMap<Player,String> lastmessage = new HashMap<Player,String>();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
        if (lastmessage.get(e.getPlayer()).equals(e.getMessage()))
            return;
        lastmessage.put(e.getPlayer(),e.getMessage());
		User u = User.get(e.getPlayer());
		if (u.getModeration().isMute()){
			u.sendMessage("Vous ne pouvez pas parler");
			e.setCancelled(true);
			return;
		}
		if (e.getPlayer().hasPermission("maxcraft.guide"))
			e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		if (e.getMessage().startsWith("!"))
			new AdminChat(e);
		else if (e.getMessage().startsWith("*")&&!e.getMessage().endsWith("*"))
			new FactionChat(e);
		else if (e.getMessage().startsWith("*")&&e.getMessage().endsWith("*"))
			new Me(e);
		else
			new Chat(e);
		e.setCancelled(true);
		
	}

	public static void register(Main main) {
		main.getCommand("msg").setExecutor(new Private());
		main.getCommand("r").setExecutor(new Respond());
		
	}
}
