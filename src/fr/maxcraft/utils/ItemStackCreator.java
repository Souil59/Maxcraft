package fr.maxcraft.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackCreator extends ItemStack {

	public ItemStackCreator(Material material, String name,
			List<String> desc, int amount, int data) {
		this.setType(material);
		this.setAmount(amount);
		this.setDurability((short) data);
		ItemMeta meta =this.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(desc);
		this.setItemMeta(meta);
	}

	public ItemStackCreator(Material material, ItemMeta meta, int amount,
			int data) {
		this.setType(material);
		this.setAmount(amount);
		this.setDurability((short) data);
		this.setItemMeta(meta);
	}

}
