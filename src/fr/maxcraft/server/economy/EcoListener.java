package fr.maxcraft.server.economy;

import org.bukkit.event.Listener;
import fr.maxcraft.Main;

public class EcoListener implements Listener {

	public EcoListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
}
