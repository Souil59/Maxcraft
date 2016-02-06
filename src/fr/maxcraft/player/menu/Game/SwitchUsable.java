package fr.maxcraft.player.menu.game;

import fr.maxcraft.player.User;
import fr.maxcraft.player.menu.Menu;
import fr.maxcraft.server.game.StartSign;
import fr.maxcraft.utils.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Crevebedaine on 26/01/2016.
 */
public class SwitchUsable extends Menu{
    private final StartSign startsign;

    public SwitchUsable(User u, StartSign startSign) {
        super(u);
        this.startsign = startSign;
    }

    @Override
    public void execute(User u) {
        this.startsign.switchAccess();
        this.startsign.clic(u);
    }

    @Override
    public ItemStack getItem(User u) {
        if (this.startsign.isOpen())
          return new ItemStackCreator(Material.REDSTONE,"Desactiver ce jeu", Arrays.asList("Cliquez ici pour desactiver ce jeu"),1,0);
        return new ItemStackCreator(Material.SLIME_BALL,"Activer ce jeu", Arrays.asList("Cliquez ici pour activer ce jeu"),1,0);
    }
}
