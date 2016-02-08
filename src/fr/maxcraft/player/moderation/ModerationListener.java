package fr.maxcraft.player.moderation;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Lu27600 on 07/02/16.
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
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMutedPlayerChat(AsyncPlayerChatEvent e){
        User u = User.get(e.getPlayer());
        if (!u.getModeration().isMute()) return;
        e.setCancelled(true);
        u.sendNotifMessage(ChatColor.RED+"Vous êtes muet, vous ne pouvez pas parler !");
    }
}
