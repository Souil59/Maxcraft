package fr.maxcraft.server.shop;

import fr.maxcraft.Main;
import fr.maxcraft.server.shop.events.ShopTransactionEvent;
import fr.maxcraft.server.zone.Zone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class ShopListener implements Listener {

    private Main plugin;

    public ShopListener(Main pl){
        this.plugin = pl;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopTransaction(ShopTransactionEvent e)
    {
        Zone z = Zone.getZone(e.getShop().getChest().getLocation());
        if(z != null){
            if(!z.hasTag("shop")){
                e.setCancelled(true);
                e.setReason(Shop.message() + "Tag \"shop\" manquant !");
            }
        }
        else{
            e.setCancelled(true);
            e.setReason(Shop.message()+"Tag \"shop\" manquant !");
        }
    }
}
