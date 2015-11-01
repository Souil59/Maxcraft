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

	public ItemStackCreator(Material material) {
		this.setType(material);
	}
	
	public ItemStackCreator setamount(int i){
		this.setAmount(i);
		return this;
	}
	public ItemStackCreator setdata(int i){
		this.setDurability((short) i);
		return this;
	}
	public ItemStackCreator setName(String name){
		ItemMeta meta =this.getItemMeta();
		meta.setDisplayName(name);
		this.setItemMeta(meta);
		return this;
	}
	public ItemStackCreator setDesc(List<String> desc){
		ItemMeta meta =this.getItemMeta();
		meta.setLore(desc);
		this.setItemMeta(meta);
		return this;
	}

}
