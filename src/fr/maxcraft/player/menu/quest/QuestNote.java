package fr.maxcraft.player.menu.quest;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Crevebedaine on 10/02/2016.
 */
public class QuestNote extends Menu{

    private Location target;

    public QuestNote(User u) {
        super(u);
    }

    @Override
    public void execute(User u) {
        u.getPlayer().setCompassTarget(this.target);
        popup(Arrays.asList("Votre boussole pointe desormais vers cette quete"));
    }

    @Override
    public ItemStack getItem(User u) {
        return null;
    }
    public ItemStack getItem(String quete, List<String> desc, Location target) {
        this.target = target;
        ItemStackCreator i = new ItemStackCreator(Material.PAPER);
        i.setName(quete);
        i.setDesc(desc);
        return i;
    }
}
