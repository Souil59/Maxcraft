package fr.maxcraft.server.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.maxcraft.Main;

public class WorldListener implements Listener {

	public WorldListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		if (!e.getFrom().getWorld().equals(e.getTo().getWorld())){
			WorldInventories.save(e.getPlayer(),e.getFrom().getWorld());
			e.getPlayer().getInventory().clear();
			e.getPlayer().updateInventory();
			WorldInventories.load(e.getPlayer(),e.getTo().getWorld());
		}
	}

}
