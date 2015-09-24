package fr.maxcraft.player.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import fr.maxcraft.Main;
import fr.maxcraft.player.User;


public class PermsListener implements Listener {


	public PermsListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onco(PlayerJoinEvent e) {
		User u = User.get(e.getPlayer());
		for (String p : u.getPerms().perms)
			if (p.startsWith("!"))
				u.getPlayer().addAttachment(Main.getPlugin(), p.substring(1) ,false);
			else
				u.getPlayer().addAttachment(Main.getPlugin(), p ,true);
	}
}
