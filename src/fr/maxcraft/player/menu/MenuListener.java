package fr.maxcraft.player.menu;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;

public class MenuListener implements Listener {
	
	public static HashMap<User,InventoryClickEvent> events = new HashMap<User,InventoryClickEvent>();
	
	public MenuListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onclic(InventoryClickEvent e){
		events.put(User.get((Player)e.getWhoClicked()),e);
		if (!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
			Menu.clic(e,e.getCurrentItem(),User.get((Player)e.getWhoClicked()));
		events.remove(User.get((Player)e.getWhoClicked()));
	}
	@EventHandler
	public void onclose(InventoryCloseEvent e){
		Menu.menulist.remove(User.get(e.getPlayer().getUniqueId()));
		new Fill(e.getPlayer()).runTaskLater(Main.getPlugin(), 1);
	}
	@EventHandler
	public void onco(PlayerJoinEvent e){
		new Fill(e.getPlayer()).runTaskLater(Main.getPlugin(), 1);
	}
	public class Fill extends BukkitRunnable{

		private Player p;

		public Fill(HumanEntity player) {
			this.p = (Player) player;
		}

		@Override
		public void run() {
			if (this.p.getOpenInventory().getType().equals(InventoryType.CHEST))
				return;
			this.p.getOpenInventory ().getTopInventory ().setItem (0,Menu.mainitem);
			this.p.updateInventory();
		}
		
	}
}
