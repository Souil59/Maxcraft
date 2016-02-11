package fr.maxcraft.server.game;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Maxcraft.fr
 * <p/>
 * Created by Crevebedaine on 23/01/2016.
 * <p/>
 * Please, keep this header if you pick some code.
 * <p/>
 * Email me!
 */
public class GameListener implements Listener {

    //CONSTRUCTOR
    public GameListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onReSpawn(PlayerRespawnEvent e){
        for (GameInstance g : GameInstance.getInstances())
            if (e.getPlayer().getWorld().equals(g.getInstanceWorld())) {
                if (g.getLife().get(e.getPlayer()) > 0) {
                    g.getLife().put(e.getPlayer(), g.getLife().get(e.getPlayer()) - 1);
                    if (g.getNathemWorld().getCheckPoints().containsKey(e.getPlayer()))
                        e.setRespawnLocation(g.getNathemWorld().getCheckPoints().get(e.getPlayer()));
                    else
                        g.teleport(e.getPlayer());
                    e.getPlayer().sendMessage("Il vous reste "+ g.getLife().get(e.getPlayer()) +" vie(s).");
                    return;
                }
                e.setRespawnLocation(g.getBackLocations().get(e.getPlayer()));
            }
    }

    @EventHandler
    public void onClic(PlayerInteractEntityEvent e){
        for (StartSign s : StartSign.sslist)
            if (s.getNPCUUID().equals(e.getRightClicked().getUniqueId())) {
                s.clic(User.get(e.getPlayer()));
                e.setCancelled(true);
            }
    }

}
