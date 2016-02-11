package fr.maxcraft.player.menu.quest;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.quester.Note;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Crevebedaine on 10/02/2016.
 */
public class QuestBook extends Menu {

    public QuestBook(User u) {
        super(u);
    }

    @Override
    public void execute(User u) {
        Inventory i = Menu.getInventory("Livre de quêtes");
        Note n = Note.get(u);
        int j=0;
        for (String q : n.getQuest()){
            i.setItem(j,new QuestNote(u).getItem(q,n.getNote().get(n.getQuest().indexOf(q)),n.getTarget().get(n.getQuest().indexOf(q))));
            j++;
        }

        u.getPlayer().openInventory(i);
    }

    @Override
    public ItemStack getItem(User u) {
        ItemStackCreator i = new ItemStackCreator(Material.BOOK);
        i.setName("Livre de quêtes");
        i.setDesc(Arrays.asList("Cliquez ici pour acceder à votre livre des quêtes"));
        return i;
    }
}
