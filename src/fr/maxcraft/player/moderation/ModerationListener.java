package fr.maxcraft.player.moderation;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.world.marker.Marker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Date;

public class ModerationListener implements Listener {

    public ModerationListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerAttempsToJoin(AsyncPlayerPreLoginEvent e){
        User u = User.get(e.getName());
        if (u.getModeration().isBan()) {
            if ( !(u.getModeration().getBanend() > new Date().getTime()) && u.getModeration().getBanend() != -1){
                u.getModeration().setBan(false, -1, null);
                u.getModeration().save();
                e.allow();
                return;
            }
            String reason;
            if (u.getModeration().getBanend() > 0){
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, u.getModeration().getBanReason());
                return;
            }
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED+"Vous êtes banni : "+ChatColor.GOLD+u.getModeration().getBanReason());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        User u = User.get(e.getPlayer());
        if (!u.getModeration().isJailBoolean()) return;
        if (u.getModeration().getJailend() <= new Date().getTime() && u.getModeration().getJailend() != -1){
            u.getModeration().setJail(false, -1);
            u.getPlayer().setGameMode(GameMode.SURVIVAL);
            u.getPlayer().teleport(Marker.getMarker("spawn"));
            u.sendNotifMessage(ChatColor.GREEN + "Vous avez été libéré, le temps de votre peine s'est écoulé!");
            Moderation.sendMessageToStaffs(Moderation.message() + u.getName() + " a été libéré ! (Fin du temps imparti)");
        }
    }
}
