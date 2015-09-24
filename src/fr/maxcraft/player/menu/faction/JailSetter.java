package fr.maxcraft.player.menu.faction;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.utils.ItemStackCreator;

public class JailSetter extends Menu{

	public JailSetter(User u) {
		super(u);
	}

	@Override
	public void execute(User u) {
		if (Zone.getZone(u.getPlayer().getLocation())==null)
			popup(Arrays.asList(ChatColor.RED+"Cette zone n'appartient pas à ",ChatColor.RED+"votre faction"));
		if (!Zone.getZone(u.getPlayer().getLocation()).getOwner().equals(u.getFaction()));
			popup(Arrays.asList(ChatColor.RED+"Cette zone n'appartient pas à ",ChatColor.RED+"votre faction"));
		if (Zone.getZone(u.getPlayer().getLocation()).getOwner().equals(u.getFaction()));
			u.getFaction().setJail(u.getPlayer().getLocation());
			popup(Arrays.asList(ChatColor.GREEN+"Le nouvel emplacement de la prison ",ChatColor.GREEN+"est pris en compte."));
		
	}

	@Override
	public ItemStack getItem(User u) {
		return new ItemStackCreator(Material.IRON_FENCE,"Definir prison", Arrays.asList("Definir ce lieu comme prison","pour vos prisonniers"), 1, 0);
	}

}
