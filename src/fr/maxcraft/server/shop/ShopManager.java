package fr.maxcraft.server.shop;


import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShopManager {

    private Main plugin;
    private ArrayList<Shop> shops = new ArrayList<>();

    public ShopManager(Main plugin) {
        this.plugin = plugin;
        this.loadAll();
    }


    public ArrayList<Shop> getShops() {
        return shops;
    }

    public void saveConfig(){
        for (Shop s : this.shops){
            int id, zoneId, amount, x, y, z, admin;
            String owner, type, world, item;
            double price;

            id = s.getId();
            zoneId = s.getZone().getId();
            amount = s.getAmount();
            x = s.getLocation().getBlockX();
            y = s.getLocation().getBlockY();
            z = s.getLocation().getBlockZ();
            owner = s.getOwner().getUuid().toString();
            type = s.getType();
            world = s.getLocation().getWorld().getName();
            item = s.getItem().name();
            price = s.getPrice();
            if (s.isAdmin()) admin = 1; else admin = 0;


            MySQLSaver.mysql_update("INSERT INTO 'shop' ('id', 'zone_id', 'owner', 'type', 'price', 'admin', 'amount', 'world', 'x', 'y', 'z', 'item') VALUES ('"+id+"', '"+zoneId+"', '"+owner+"','"+type+"', '"+price+"', '"+admin+"', '"+amount+"', '"+world+"', '"+x+"', '"+y+"', '"+z+"', '"+item+"')");
        }
    }

    public void loadAll(){
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM 'shop';", false);

        try{
            while (r.next()){

                int id, x, y, z, amount;
                String worldName, ownerUuid, type;
                boolean admin;
                float price;
                Zone zone;
                Material item;

                try {
                    id = r.getInt("id");
                    x = r.getInt("x");
                    y = r.getInt("y");
                    z = r.getInt("z");
                    amount = r.getInt("amount");

                    worldName = r.getString("world");
                    ownerUuid = r.getString("owner");
                    type = r.getString("type");

                    admin = r.getBoolean("admin");
                    price = r.getFloat("price");
                    zone = Zone.getZone(r.getInt("zone_id"));
                    item = Material.getMaterial(r.getString("item"));

                }catch (SQLException e){
                    e.printStackTrace();
                    continue;
                }
                World w = this.plugin.getServer().getWorld(worldName);

                Location l = new Location(w, x, y, z);
                User owner = User.get(ownerUuid);

                new Shop(this, id, l, owner, type, amount, price, admin, zone, item);

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public boolean sameItem(ItemStack item1, ItemStack item2)
    {
        //Type
        if(! item1.getType().equals(item2.getType())) return false;
        if(item1.getDurability() != item2.getDurability()) return false;
        if(!item1.getItemMeta().equals(item2.getItemMeta())) return false;
        if(!item1.getData().equals(item2.getData())) return false;
        return true;
    }
}
