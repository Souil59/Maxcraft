package fr.maxcraft.server.quester;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public class QuesterListener implements Listener {
    public QuesterListener(Main main) {
        main.getServer().getPluginManager().registerEvents(this, main);

    }

    @EventHandler
    public void onRClic(PlayerInteractEntityEvent e) {
        for (Quester q : Quester.questers) {
            if (e.getRightClicked().getUniqueId().equals(q.getNpc().getUniqueId())){
                q.rightClic(User.get(e.getPlayer()));
        }
    }
    }
    @EventHandler
    public void onAnswer(PlayerCommandPreprocessEvent e){
        e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        if(!e.getMessage().contains("/quest"))
            return;
        UUID uuid = UUID.fromString(e.getMessage().split(" ")[1]);
        for (Quester q : Quester.questers)
            if (uuid.equals(q.getNpc().getUniqueId()))
                q.answer(User.get(e.getPlayer()),Integer.parseInt(e.getMessage().split(" ")[2]));
        e.setCancelled(true);
    }

}
