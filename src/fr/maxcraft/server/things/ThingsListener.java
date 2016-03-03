package fr.maxcraft.server.things;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    /*@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerAkfChat(AsyncPlayerChatEvent e){
        User u = User.get(e.getPlayer());
        if (!u.isAfk()) return;
        u.setAfk(false);
        u.sendMessage(ChatColor.GRAY+"Vous n'êtes plus marqué absent");
    }*/

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamagePlayer(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        User u = User.get(p);
        if (u.isGod()){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerInteractPowertool(PlayerInteractEvent e){
        if (!e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        User u = User.get(e.getPlayer());
        if (u==null) return;
        if ( u.getPowertool() == null || u.getPowertool().isEmpty() || !u.getPowertool().containsKey(e.getMaterial()) ) return;
        u.getPlayer().performCommand(u.getPowertool().get(e.getMaterial()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void backPlayerDeath(PlayerDeathEvent e){
        User u = User.get(e.getEntity());
        if (!u.getPerms().hasPerms("maxcraft.guide")) return;
        Location l = e.getEntity().getLocation();
        u.setLastLocation(l);
        u.sendMessage(ChatColor.GOLD+"Pour revenir à votre emplacement initial tapez "+ChatColor.RED+"/back");
    }


}
