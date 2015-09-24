package fr.maxcraft.server.game;

import org.bukkit.event.Listener;

import fr.maxcraft.Main;

public class GameListener implements Listener{
	
	public GameListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}

}
