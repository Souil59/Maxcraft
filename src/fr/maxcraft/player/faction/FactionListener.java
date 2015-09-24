package fr.maxcraft.player.faction;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.maxcraft.Main;

public class FactionListener implements Listener {
	
	public FactionListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onco(PlayerJoinEvent e){
	}
}
