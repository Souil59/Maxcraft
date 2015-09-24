package fr.maxcraft.player.menu.faction;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;

public class BannerSetter extends Menu{

	public BannerSetter(User u) {
		super(u);
	}

	@Override
	public void execute(User u) {
		if (u.getPlayer().getItemInHand()==null)
			popup(Arrays.asList(ChatColor.RED+"Vous devez tenir la nouvelle",ChatColor.RED+"banniere en main"));
		else if (!u.getPlayer().getItemInHand().getType().equals(Material.BANNER))
			popup(Arrays.asList(ChatColor.RED+"Vous devez tenir la nouvelle",ChatColor.RED+"banniere en main"));
		else {
			u.getFaction().setBanner(u.getPlayer().getItemInHand());
			popup(Arrays.asList(ChatColor.GREEN+"Le changement de banniere ",ChatColor.GREEN+"est pris en compte."));
		}
		
	}

	@Override
	public ItemStack getItem(User u) {ItemStack i = u.getFaction().getBanner();
	ItemMeta m = i.getItemMeta();
	m.setDisplayName(ChatColor.RED+"Definir la baniniere");
	m.setLore(Arrays.asList("Definir une nouvelle banniere","comme banniere de la faction"));
	i.setItemMeta(m);
	return i;
	}

}
