package fr.maxcraft.server.warzone;

import fr.maxcraft.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Created by Crevebedaine on 11/02/2016.
 */
public class WarzoneListener implements Listener {
    public WarzoneListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void clic(PlayerInteractEntityEvent e){
        for (NPCFarmer n : NPCFarmer.farmers)
            if (n.entityUUID.equals(e.getRightClicked().getUniqueId()))
                n.open(e.getPlayer());
    }
}
