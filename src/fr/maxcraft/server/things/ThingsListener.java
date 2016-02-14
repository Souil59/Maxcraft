package fr.maxcraft.server.things;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


/**
 * Created by admin on 14/02/16.
 */
public class ThingsListener implements Listener {

    public ThingsListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        User u = User.get(e.getPlayer());
        if (!u.isAfk()) return;
        u.setAfk(false);
        u.sendMessage(ChatColor.GRAY + "Vous n'êtes plus marqué absent");
        return;
    }

}
