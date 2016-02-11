package fr.maxcraft.server.economy.merchant;
import fr.maxcraft.Main;

import org.bukkit.event.Listener;

public class MerchantListener implements Listener {

	//CONSTRUCTOR
	public MerchantListener(Main plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

	}

}