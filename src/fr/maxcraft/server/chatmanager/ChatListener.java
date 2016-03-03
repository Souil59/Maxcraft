package fr.maxcraft.server.chatmanager;

import fr.maxcraft.dev.chess.ChessGame;
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
        if (lastmessage.containsKey(e.getPlayer()))
            if (lastmessage.get(e.getPlayer()).equals(e.getMessage())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED+"Vous ne pouvez pas répéter un même message !");
                return;
            }
        lastmessage.put(e.getPlayer(),e.getMessage());
		User u = User.get(e.getPlayer());
		if (u.getModeration().isMute()){
			u.sendMessage("Vous ne pouvez pas parler");
			e.setCancelled(true);
			return;
		}
		if (u.getModeration().getMuteend() == 0){
            u.getModeration().setMuteend(-1);
            u.getModeration().save();
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
