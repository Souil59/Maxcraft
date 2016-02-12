package fr.maxcraft.server.anticheat;

import fr.maxcraft.Main;
import fr.maxcraft.server.chatmanager.AdminChat;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lu27600 on 12/02/16.
 */
public class AntiCheatListener implements Listener {

    public AntiCheatListener(Main main) {

        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void hasPlayerNegativeItem(PlayerInteractEvent e){
        if ( !(e.getItem().getAmount() < 0)) return;
        e.getItem().setAmount(0);
        AdminChat.sendMessageToStaffs(AntiCheat.alertMessage() + e.getPlayer().getName() + " a utilisé un item négatif, c'est " + e.getItem().getItemMeta().getDisplayName() + " qui était au nombre de " + (e.getItem().getAmount() + 1));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void openInventoryEvent(InventoryOpenEvent e){
        for (ItemStack item : e.getInventory().getContents()){
            if ( !(item.getAmount() < 0)) return;
            item.setAmount(0);
            AdminChat.sendMessageToStaffs(AntiCheat.alertMessage()+e.getPlayer().getName()+" a possédé un item négatif, c'est "+item.getItemMeta().getDisplayName()+" qui était au nombre de "+(item.getAmount()+1));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        for (ItemStack item : e.getPlayer().getInventory().getContents()){
            if (!(item.getAmount() < 0))return;
            item.setAmount(0);
            AdminChat.sendMessageToStaffs(AntiCheat.alertMessage() + e.getPlayer().getName() + " a possédé un item négatif, c'est " + item.getItemMeta().getDisplayName() + " qui était au nombre de " + (item.getAmount() + 1));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        if ( !(e.getFrom().getBlockY() - e.getTo().getBlockY() >3)) return;
        if ( !(e.getPlayer().getLastDamageCause().equals(EntityDamageEvent.DamageCause.FALL))) return;
        if ((e.getPlayer().getEquipment().getBoots().getEnchantments().containsKey(Enchantment.PROTECTION_FALL))) return;

    }

}
