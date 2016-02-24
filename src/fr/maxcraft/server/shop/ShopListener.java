package fr.maxcraft.server.shop;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.shop.events.*;
import fr.maxcraft.server.zone.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.sql.SQLException;


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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopCreated(ShopCreatedEvent e){
            e.getShop().save();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopRemoved(ShopRemoveEvent e){

        if(!e.getShop().hasAccess(e.getUser()))
        {
            e.getUser().sendMessage(Shop.message()+"Vous n'avez pas la permission de détruire ce shop !");
            e.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShopRemovedLast(ShopRemoveEvent e){

        if(!e.isCancelled())
        {

            if(e.getShop().canWork())
            {
                e.getShop().getItemFrame().setItem(null);
            }
            e.getShop().remove();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopChoseItem(ShopChoseItemEvent e){

        e.getUser().sendMessage(Shop.message()+"Ce shop est désormais activé avec l'item <"+ e.getShop().getItem().name()+">.");

        //SQL
        e.getShop().save();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopRemoveItem(ShopRemoveItemEvent e){

        e.getUser().sendMessage(Shop.message()+"Ce shop est désormais désactivé.");


        //SQL
            e.getShop().remove();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopClick(ShopClickEvent e){


        if(e.getShop().getOwner().getName().equals(e.getUser().getName()))
        {
            //INFO
            Bukkit.getServer().getPluginManager().callEvent(new ShopInfoEvent(e.getShop(), e.getUser()));
        }
        else
        {
            e.getShop().transaction(e.getUser());
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShopInfo(ShopInfoEvent e){

        e.getShop().loadSign();
        String type = "Vente";

        if(e.getShop().getType().equals("BUY")) type = "Achat";

        e.getUser().sendMessage(Shop.message()+"*** INFORMATION SHOP ("+type+") ***");
        e.getUser().sendMessage(Shop.message()+"Stock : " + ChatColor.GOLD + e.getShop().getStockAmount());
        e.getUser().sendMessage(Shop.message()+"Propriétaire : " + ChatColor.GOLD + e.getShop().getOwner().getName());

        if(e.getShop().canWork())
        {
            e.getUser().sendMessage(Shop.message()+"Etat : "+ ChatColor.GREEN + "En marche");
            e.getUser().sendMessage(Shop.message()+"Item : "+ ChatColor.GOLD + e.getShop().getItem().name());
            e.getUser().sendMessage(Shop.message()+"Prix : "+ ChatColor.GOLD + e.getShop().getPrice()+" POs pour "+e.getShop().getAmount() +" unité(s)");

        }
        else
        {
            e.getUser().sendMessage(Shop.message()+"Etat : "+ ChatColor.RED + "Shop non activé");
        }


    }
}
