package fr.maxcraft.player.menu.settings;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Louis on 21/01/2016.
 */
public class Settings extends Menu {
    public Settings(User u) {
        super(u);
    }

    @Override
    public void execute(User u) {
        popup(Arrays.asList(ChatColor.RED+"A venir"));
    }

    @Override
    public ItemStack getItem(User u) {
        return new ItemStackCreator(Material.COMMAND,"Parametre", Arrays.asList("Accés au parametres"),1,0);
    }
}
