package fr.maxcraft.server.world;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TravelTask extends BukkitRunnable {
		private Player player;
		private Location marker;
		private String message;

		public TravelTask(Player player,Location location,String s) {
			this.player = player;
			this.marker = location;
			this.message = s;
		}

		@Override
		public void run() {
			player.teleport(marker);
			player.sendMessage(message);
		}
	
}
