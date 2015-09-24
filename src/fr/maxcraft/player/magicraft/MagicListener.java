package fr.maxcraft.player.magicraft;

import org.bukkit.event.Listener;

import fr.maxcraft.Main;

public class MagicListener implements Listener {


	public MagicListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
}
