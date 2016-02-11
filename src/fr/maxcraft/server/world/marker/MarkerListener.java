package fr.maxcraft.server.world.marker;

import fr.maxcraft.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Crevebedaine on 07/02/2016.
 */
public class MarkerListener implements Listener{
    public MarkerListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        for(Travel t :Travel.travelslist)
            if (t.isReadyToTravel(e))
                t.travel(e.getPlayer());
    }
}
