package fr.maxcraft.server.merchant;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.merchant.Buy;

public class MerchantListener implements Listener {

	public MerchantListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e){
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (e.getClickedBlock()==null) return;
		if (!e.getClickedBlock().getType().equals(Material.SIGN_POST)) return;
		Merchant m = new Merchant();
		m.add(e.getPlayer().getItemInHand(),5);
		m.setPlayer(e.getPlayer());
		m.open();
		e.getPlayer().getOpenInventory().setItem(0,new Buy(User.get(e.getPlayer())).getItem(User.get(e.getPlayer())));
		
	}
}
