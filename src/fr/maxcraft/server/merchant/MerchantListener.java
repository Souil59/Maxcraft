package fr.maxcraft.server.merchant;
import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.command.CommandSender;

import fr.maxcraft.server.command.Command;
import org.bukkit.event.Listener;

import java.awt.*;
import java.util.HashMap;

public class MerchantListener implements Listener {

	//CONSTRUCTOR
	public MerchantListener(Main plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

	}

}