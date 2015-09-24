package fr.maxcraft.player.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.faction.FactionMenu;
import fr.maxcraft.player.menu.player.PlayerMenu;
import fr.maxcraft.utils.ItemStackCreator;

public abstract class Menu {
	
	static HashMap<User,ArrayList<Menu>> menulist = new HashMap<User,ArrayList<Menu>>();
	public static ItemStack voiditem = new ItemStackCreator(Material.STAINED_GLASS_PANE, " ", null, 1, 7);
	public static ItemStack mainitem = new ItemStackCreator(Material.EXP_BOTTLE, "Menu", Arrays.asList("Cliquez pour acceder au menu"), 1, 0);
	private User u;
	

	
	public Menu(User u){
		this.u = u ;
		if (!menulist.containsKey(u))
			menulist.put(u,new ArrayList<Menu>());
		menulist.get(u).add(this);
	}
	
	public abstract void execute(User u);
	public abstract ItemStack getItem(User u);
	
	public static Inventory getInventory(String string) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD+string);
		for (int s = 0 ; s<inv.getSize();s++)
			inv.setItem(s, voiditem);
		return inv;
	}
	public static Inventory getMainInventory(User u) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD+"Menu");
		for (int s = 0 ; s<inv.getSize();s++)
			inv.setItem(s, voiditem);
		inv.setItem(4, new FactionMenu(u).getItem(u));
		inv.setItem(0, new PlayerMenu(u).getItem(u));
		return inv;
	}
	public void popup(List<String> list){
		ItemStack i = MenuListener.events.get(this.u).getCurrentItem();
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(" ");
		m.setLore(list);
		i.setItemMeta(m);
		MenuListener.events.get(this.u).setCancelled(true);
	}
	
	public static void clic(InventoryClickEvent e, ItemStack item, User u) {
		if (item == null)
			return;
		if (mainitem.getItemMeta().equals(item.getItemMeta())){
			e.setCancelled(true);
			u.getPlayer().openInventory(getMainInventory(u));
		}
		for (Menu m : menulist.get(u))
			if (m.getItem(u).getItemMeta().equals(item.getItemMeta())){
				e.setCancelled(true);
				m.execute(u);
			}
		if (voiditem.getItemMeta().equals(item.getItemMeta()))
			e.setCancelled(true);
	}
}
