package fr.maxcraft.player.menu.game;

import fr.maxcraft.player.User;
import fr.maxcraft.player.faction.Faction;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.game.GameInstance;
import fr.maxcraft.server.game.InstanceStatus;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class InstancePublic extends Menu{
    private final GameInstance gameInstance;

    public InstancePublic(User u, GameInstance g) {
        super(u);
        this.gameInstance = g;
    }

    @Override
    public void execute(User u) {
        this.gameInstance.build();
        this.gameInstance.setStatus(InstanceStatus.OPEN);
        this.gameInstance.teleport(u.getPlayer());
    }

    @Override
    public ItemStack getItem(User u) {
        return new ItemStackCreator(Material.SPONGE,"Publique", Arrays.asList("Cette instance sera accessible","à tous le monde."),1,1);
    }
}
