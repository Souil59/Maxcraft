package fr.maxcraft.player.menu.zone;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Louis on 21/01/2016.
 */
public class ZoneDisplay extends Menu {

    public ZoneDisplay(User u) {
        super(u);
    }

    @Override
    public void execute(User u) {

    }

    @Override
    public ItemStack getItem(User u) {
        if (Zone.getZone(u.getPlayer().getLocation())==null)
            return new ItemStackCreator(Material.SPONGE,"Zone", Arrays.asList("Vous n'etes pas sur une zone"),1,0);
        List<String> l = Arrays.asList(Zone.getZone(u.getPlayer().getLocation()).description().split("\n"));
        return new ItemStackCreator(Material.SPONGE,l.get(0),l.subList(1,l.size()),1,0);

    }
}


