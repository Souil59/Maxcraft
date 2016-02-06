package fr.maxcraft.player.menu;

import fr.maxcraft.player.User;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Crevebedaine on 27/01/2016.
 */
public class UnClickable extends Menu{
    private ItemStack i;

    public UnClickable(User u, ItemStack i) {
        super(u);
        this.i = i;
    }

    @Override
    public void execute(User u) {
    }

    @Override
    public ItemStack getItem(User u) {
        return i;
    }
}
