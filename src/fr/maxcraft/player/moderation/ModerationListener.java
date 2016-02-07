package fr.maxcraft.player.moderation;

import fr.maxcraft.Main;
import org.bukkit.event.Listener;

/**
 * Created by admin on 07/02/16.
 */
public class ModerationListener implements Listener {

    public ModerationListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }


}
