package fr.maxcraft.player.menu.game;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.game.GameInstance;
import fr.maxcraft.server.game.InstanceStatus;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class InstanceFaction extends Menu{
    private final GameInstance gameInstance;

    public InstanceFaction(User u, GameInstance g) {
        super(u);
        this.gameInstance = g;
    }

    @Override
    public void execute(User u) {
        this.gameInstance.build();
        this.gameInstance.setStatus(InstanceStatus.FACTION);
        this.gameInstance.setFaction(u.getFaction());
        this.gameInstance.teleport(u.getPlayer());
    }

    @Override
    public ItemStack getItem(User u) {
        return u.getFaction().getBanner();
    }
}
