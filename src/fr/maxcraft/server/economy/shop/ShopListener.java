package fr.maxcraft.server.economy.shop;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.economy.shop.events.*;
import fr.maxcraft.server.zone.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class ShopListener implements Listener {

    private Main plugin;
    private ShopManager manager;

    public ShopListener(Main pl, ShopManager manager){
        this.plugin = pl;
        this.manager = manager;
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
            e.getUser().sendMessage(Shop.message() + "Prix : " + ChatColor.GOLD + e.getShop().getPrice() + " POs pour " + e.getShop().getAmount() + " unité(s)");

        }
        else
        {
            e.getUser().sendMessage(Shop.message() + "Etat : " + ChatColor.RED + "Shop non activé");
        }


    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent e){

        User u = User.get(e.getPlayer());
        if(u == null) return;

        if(! (e.getLine(0).equalsIgnoreCase("shop") || e.getLine(0).equalsIgnoreCase("[shop]") )) return;


        //Position du sign
        if(!e.getBlock().getType().equals(Material.WALL_SIGN))
        {
            u.sendMessage(Shop.message() + "Le panneau doit être posé contre un coffre pour créer un shop !");
            return;
        }

        Sign s = (Sign) e.getBlock().getState();
        Chest chest = this.manager.getRelativeChest(s);
        if(chest == null)
        {
            u.sendMessage(Shop.message() + "Le panneau doit être posé contre un coffre pour créer un shop !");
            return;
        }

        //Déjà un shop
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getChest().equals(chest))
            {
                u.sendMessage(Shop.message() + "Ce coffre est déjà utilisé pour un autre shop !");
                return;
            }
        }

        //Paramètres
        int amount;
        float price;
        boolean admin = false;
        String type = "SELL";


        try
        {
            amount =  Integer.parseInt(e.getLine(1));

            if(amount <= 0 || amount > 64*9)
            {
                u.sendMessage(Shop.message() + "Le nombre d'item à vendre par clic (ligne 2) n'est pas valide, il doit être compris entre 1 et 576 !");
                return;
            }
        }
        catch(Exception ex)
        {
            u.sendMessage(Shop.message() + "La deuxième ligne doit comporter le nombre d'items à vendre à chaque clic.");
            return;
        }

        try
        {
            price =  Float.parseFloat(e.getLine(2));

            if(price <= 0 || price > 10000)
            {
                u.sendMessage(Shop.message() + "Le prix (ligne 3) n'est pas valide, il doit être compris entre 0 et 10 000 POs !");
                return;
            }
        }
        catch(Exception ex)
        {
            u.sendMessage(Shop.message() + "La troisième ligne doit comporter le prix !");
            return;
        }

        if(e.getLine(3).contains("admin") && u.getPerms().hasPerms("maxcraft.modo"))
        {
            admin = true;
        }

        if(e.getLine(3).contains("achat"))
        {
            type = "BUY";
        }

        ShopCreateEvent sce = new ShopCreateEvent(u, price, amount, e.getBlock().getLocation(), type, admin);
        Bukkit.getServer().getPluginManager().callEvent(sce);

        if(!sce.isCancelled())
        {


            Shop shop = new Shop(this.manager, this.manager.getNewId(), sce.getLocation(), sce.getOwner(), sce.getType(), sce.getAmount(), sce.getPrice(), sce.isAdmin(), null);
            shop.save();
            e.getPlayer().sendMessage(Shop.message()+"Vous avez créé un shop !");

            Bukkit.getServer().getPluginManager().callEvent(new ShopCreatedEvent(shop, u));

            if(!shop.canWork())
            {
                e.getPlayer().sendMessage(Shop.message()+"Le shop ne peut pas encore fonctionner, vous devez placer un coffre et un item frame (cadre à item) avec un item dedans!");

            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignBreak(BlockBreakEvent e){

        Shop s = null;
        for(Shop shop : this.manager.getShops())
        {
            if(e.getBlock().equals(shop.getSign().getBlock()))
            {
                s = shop;
                if (s.hasAccess(User.get(e.getPlayer()))) continue;
                e.setCancelled(true);
                return;
            }
        }

        if(s == null) return;

        User u = User.get(e.getPlayer());

        ShopRemoveEvent sre = new ShopRemoveEvent(s, u);

        Bukkit.getServer().getPluginManager().callEvent(sre);

        if(!sre.isCancelled())
        {
            sre.getShop().remove();
            e.getPlayer().sendMessage(Shop.message()+"Vous avez retiré le shop !");

        }
        else
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent e){

        for(Shop shop : this.manager.getShops())
        {
            if(shop.getChest() != null && shop.getChest().getBlock().equals(e.getBlock()))
            {
                e.setCancelled(true);
            }

            if (shop.getSign() != null && shop.getSign().equals(e.getBlock())) e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemFrameRightClick(PlayerInteractEntityEvent e){

        if(!( e.getRightClicked() instanceof ItemFrame))
        {
            return;
        }

        User u = User.get(e.getPlayer());
        if (u==null){
            e.setCancelled(true);
            return;
        }

        for(Shop shop : this.manager.getShops())
        {
            if(shop.getItemFrame() != null && shop.getItemFrame().equals(e.getRightClicked()))
            {
                if(!shop.hasAccess(u))
                {
                    //Pas le droit
                    e.setCancelled(true);
                    Bukkit.getServer().getPluginManager().callEvent(new ShopInfoEvent(shop, u));

                }

                else
                {
                    // On a le droit

                    ItemFrame itemf = (ItemFrame) e.getRightClicked();

                    // Nouvel item !

                    if((itemf.getItem() == null || itemf.getItem().getType().equals(Material.AIR)) && e.getPlayer().getItemInHand() != null)
                    {
                        e.setCancelled(true);
                        ItemStack itemshowcase = e.getPlayer().getItemInHand().clone();
                        itemshowcase.setAmount(1);

                        itemf.setItem(itemshowcase);

                        Bukkit.getServer().getPluginManager().callEvent(new ShopChoseItemEvent(shop, u, e.getPlayer().getItemInHand()));
                    }
                }

                return;
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemFrameLeftClick(EntityDamageByEntityEvent e)
    {
        if(!(e.getEntity() instanceof ItemFrame)) return;


        ItemFrame itemf = (ItemFrame) e.getEntity();


        Shop s = null;
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getItemFrame() != null && shop.getItemFrame().equals(itemf))
            {
                s = shop;
            }
        }

        if(s == null) return;

        if(!(e.getDamager() instanceof Player))
        {
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getDamager();

        User u = User.get(p);

        if (u==null){
            e.setCancelled(true);
            return;
        }

        if(!s.hasAccess(u))
        {
            e.setCancelled(true);
            return;
        }

        // Item retiré !
        if(itemf.getItem() != null)
        {
            itemf.setItem(new ItemStack(Material.AIR,1));
            e.setCancelled(true);
            Bukkit.getServer().getPluginManager().callEvent(new ShopRemoveItemEvent(s, u));
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemFrameBreak(HangingBreakEvent e)
    {
        if(!(e.getEntity() instanceof ItemFrame))
        {
            return;
        }


        ItemFrame itemf = (ItemFrame) e.getEntity();

        Shop s = null;
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getItemFrame() != null && shop.getItemFrame().equals(itemf))
            {
                s = shop;
            }
        }

        if(s == null) return;

        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestOpen(InventoryOpenEvent e)
    {
        if(!(e.getInventory().getHolder() instanceof Chest)) return;
        if(! (e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();

        Chest chest = (Chest) e.getInventory().getHolder();

        User u = User.get(p);

        Shop s = null;
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getChest() != null && shop.getChest().equals(chest))
            {
                s = shop;
            }
        }

        if(s == null) return;


        if(!s.hasAccess(u))
        {
            u.sendMessage(Shop.message()+"Vous n'avez pas accès au contenu de ce shop !");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestOpen(PlayerInteractEvent e)
    {

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Shop s = null;
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getChest() != null && shop.getChest().getBlock().equals(e.getClickedBlock()))
            {
                s = shop;
            }
        }

        if(s == null) return;


        if(!s.hasAccess(User.get(e.getPlayer())))
        {
            e.getPlayer().sendMessage(Shop.message() + "Vous n'avez pas accès au contenu de ce shop !");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignClicked(PlayerInteractEvent e)
    {
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Shop s = null;
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getSign() != null && shop.getSign().getBlock().equals(e.getClickedBlock()))
            {
                s = shop;
            }
        }

        if(s == null) return;

        Bukkit.getServer().getPluginManager().callEvent(new ShopClickEvent(s, User.get(e.getPlayer())));
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHopperMoveItemFromShop(InventoryMoveItemEvent e){
        for(Shop shop : this.manager.getShops())
        {
            if(shop.getChest() != null && shop.getChest().getInventory().equals(e.getSource()))
            {
                e.setCancelled(true);
                return;
            }
            if (shop.getChest()!=null && shop.getChest().getInventory().equals(e.getDestination())){
                e.setCancelled(true);
                e.getSource().remove(e.getItem());
                return;
            }
        }
    }
}
