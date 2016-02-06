package fr.maxcraft.player.menu.game;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.game.GameInstance;
import fr.maxcraft.server.game.InstanceStatus;
import fr.maxcraft.server.game.StartSign;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;


/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class Instance extends Menu{

    private final StartSign startsign;
    private GameInstance gameInstance;

    public Instance(User u, GameInstance g, StartSign s) {
        super(u);
        this.gameInstance = g;
        this.startsign = s;
    }

    @Override
    public void execute(User u) {
        if (!startsign.isOpen() || gameInstance.getStatus().equals(InstanceStatus.CLOSE) || (gameInstance.getStatus().equals(InstanceStatus.FACTION) && !gameInstance.getFaction().equals(u.getFaction()))) {
            popup(Arrays.asList(ChatColor.RED + "Vous ne pouvez pas rejoindre cette instance."));
            return;
        }
        if (gameInstance.getStatus().equals(InstanceStatus.END)){
            Inventory i = super.getInventory("Restriction");
            i.setItem(0, new InstanceFaction(u,this.gameInstance).getItem(u));
            i.setItem(3, new InstancePublic(u,this.gameInstance).getItem(u));
            u.getPlayer().openInventory(i);
        }

        if (gameInstance.getStatus().equals(InstanceStatus.OPEN)||((gameInstance.getStatus().equals(InstanceStatus.FACTION)&&gameInstance.getFaction().equals(u.getFaction()))))
            gameInstance.Teleport(u.getPlayer());
    }

    @Override
    public ItemStack getItem(User u) {
        if (!startsign.isOpen()||gameInstance.getStatus().equals(InstanceStatus.CLOSE)||(gameInstance.getStatus().equals(InstanceStatus.FACTION)&&!gameInstance.getFaction().equals(u.getFaction())))
            return new ItemStackCreator(Material.STAINED_CLAY,ChatColor.GOLD+ gameInstance.getGame().getName(), Arrays.asList("Vous ne pouvez pas rejoindre cette instance."),1,14);
        if (gameInstance.getStatus().equals(InstanceStatus.END))
            return new ItemStackCreator(Material.STAINED_CLAY,ChatColor.GOLD+ gameInstance.getGame().getName(), Arrays.asList("Cette instance est libre."),1,5);
        if (gameInstance.getStatus().equals(InstanceStatus.OPEN)||(gameInstance.getStatus().equals(InstanceStatus.FACTION)&&gameInstance.getFaction().equals(u.getFaction())))
            return new ItemStackCreator(Material.STAINED_CLAY,ChatColor.GOLD+ gameInstance.getGame().getName(), Arrays.asList("Vous pouvez rejoindre cette instance."),1,4);
        return null;
    }
}
