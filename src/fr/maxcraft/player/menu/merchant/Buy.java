package fr.maxcraft.player.menu.merchant;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.utils.ItemStackCreator;

public class Buy extends Menu{

	public Buy(User u) {
		super(u);
	}

	@Override
	public void execute(User u) {
		u.sendMessage("acheté!");
	}

	@Override
	public ItemStack getItem(User u) {
		return new ItemStackCreator(Material.SLIME_BALL).setName("Acheter").setamount(1);
	}

}
