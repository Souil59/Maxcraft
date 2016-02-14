package fr.maxcraft.player.classes.magicraft;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.maxcraft.Main;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagicListener implements Listener {

	public MagicListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if (e.getPlayer().getItemInHand()==null)
			return;

		//Parchemin
		if (e.getPlayer().getItemInHand().getType()== Material.PAPER){

		}

	}
}

