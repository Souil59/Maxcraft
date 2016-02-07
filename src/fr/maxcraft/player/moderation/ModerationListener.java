package fr.maxcraft.player.moderation;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Lu29600 on 07/02/16.
 */
public class ModerationListener implements Listener {

    public ModerationListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAttempsToJoin(PlayerLoginEvent e){
        User u = User.get(e.getPlayer());
        if (!u.getModeration().isBan()) {
            e.allow();
            return;
        }
        e.disallow(e.getResult(), u.getModeration().getBanReason());
        return;
    }
}
