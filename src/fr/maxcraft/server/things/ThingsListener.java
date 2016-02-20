package fr.maxcraft.server.things;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


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
    }


    public void onDamagePlayer(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        User u = User.get(p);
        if (!u.isGod()) return;
        e.setCancelled(true);
    }

    /*@EventHandler(priority = EventPriority.NORMAL) //TODO fnr
    public void onPlayerTeleportEvent(PlayerTeleportEvent e){
        if (!User.get(e.getPlayer()).getPerms().hasPerms("maxcraft.guide")) return;
        for (Player p : Bukkit.getOnlinePlayers()){
            p.hidePlayer(e.getPlayer());
        }

    }*/

}
