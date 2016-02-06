package fr.maxcraft.player.menu.faction;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.utils.ItemStackCreator;

public class FactionMenu extends Menu{

	public FactionMenu(User u) {
		super(u);
	}

	@Override
	public void execute(User u) {
		if (u.getFaction()==null)
			return;
		if (!u.getFaction().getOwner().equals(u))
			nonOwner(u);
		if (u.getFaction().getOwner().equals(u))
			Owner(u);
	}

	private void Owner(User u) {
		Inventory i = super.getInventory("Faction");
		i.setItem(2, new JailSetter(u).getItem(u));
		i.setItem(3, new SpawnSetter(u).getItem(u));
		i.setItem(5, new BannerSetter(u).getItem(u));
		u.getPlayer().openInventory(i);
	}

	private void nonOwner(User u) {
		ItemStack i = getItem(u);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName("");
		m.setLore(Arrays.asList(ChatColor.RED+"Seul le proprietaire de la faction",ChatColor.RED+"peux acceder à ce menu"));
		i.setItemMeta(m);
		u.getPlayer().getOpenInventory().setItem(4, i);
	}

	@Override
	public ItemStack getItem(User u) {
        if (u.getFaction() == null)
            return new ItemStackCreator(Material.IRON_SWORD, "Vous n'avez pas de faction", Arrays.asList("Vous devez etre dans une faction pour", "pour acceder à ce menu"), 1, 0);
        return new ItemStackCreator(Material.IRON_SWORD, ChatColor.GRAY + "*** <" + ChatColor.GOLD + u.getFaction().getName() + ChatColor.GRAY + "> ***", Arrays.asList(
                ChatColor.RESET+"Faction : " + ChatColor.GREEN + u.getFaction().getName()
                ,ChatColor.RESET+ "Tag : " + ChatColor.GREEN + u.getFaction().getTAG()
                ,ChatColor.RESET+ "Chef : " + ChatColor.GREEN + u.getFaction().getOwner().getName()
                ,ChatColor.RESET+ "Argent : " + ChatColor.GREEN + u.getFaction().getBalance() + "POs"), 1, 0);

    }

}
