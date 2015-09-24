package fr.maxcraft.player.menu.player;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;

public class PlayerMenu extends Menu{

	public PlayerMenu(User u) {
		super(u);
	}

	@Override
	public void execute(User u) {
		popup(Arrays.asList(ChatColor.RED+"Il n'y a encore rien ici",ChatColor.RED+"repasse plus tard"));
	}

	@Override
	public ItemStack getItem(User u) {
		ItemStack i = new ItemStack(Material.SKULL_ITEM,1 , (byte)3);
		SkullMeta  m = (SkullMeta) i.getItemMeta();
		m.setOwner(u.getName());
		i.setItemMeta(m);
		return i;
	}

}
