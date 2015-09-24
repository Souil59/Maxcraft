package fr.maxcraft.player.menu.faction;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.utils.ItemStackCreator;

public class SpawnSetter extends Menu{

	public SpawnSetter(User u) {
		super(u);
	}

	@Override
	public void execute(User u) {
		if (Zone.getZone(u.getPlayer().getLocation())==null)
			popup(Arrays.asList(ChatColor.RED+"Cette zone n'appartient pas à ",ChatColor.RED+"votre faction"));
		if (!Zone.getZone(u.getPlayer().getLocation()).getOwner().equals(u.getFaction()));
			popup(Arrays.asList(ChatColor.RED+"Cette zone n'appartient pas à ",ChatColor.RED+"votre faction"));
		if (Zone.getZone(u.getPlayer().getLocation()).getOwner().equals(u.getFaction()));
			u.getFaction().setSpawn(u.getPlayer().getLocation());
			popup(Arrays.asList(ChatColor.GREEN+"Le nouvel emplacement de spawn ",ChatColor.GREEN+"est pris en compte."));
		
	}

	@Override
	public ItemStack getItem(User u) {
		return new ItemStackCreator(Material.BED,"Definir spawn", Arrays.asList("Definir ce lieu comme spawn pour","les membres de la faction"), 1, 0);
	}

}
