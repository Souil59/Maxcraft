package fr.maxcraft.server.marker;

import fr.maxcraft.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Crevebedaine on 07/02/2016.
 */
public class MarkerListener implements Listener{
    public MarkerListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }
}
