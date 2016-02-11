package fr.maxcraft.server.zone.sale;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Crevebedaine on 27/01/2016.
 */
public class Sale {
    private final Zone zone;
    private final int price;
    private final SaleType type;
    private Sign sign;
    private static ArrayList<Sale> saleliste = new ArrayList<Sale>();

    public Sale(Zone z, int price, SaleType type, String s){
        this.zone = z;
        this.price = price;
        this.type = type;
        Block b = Serialize.locationFromString(s).getBlock();
        if (!b.getType().equals(Material.SIGN_POST)||!b.getType().equals(Material.WALL_SIGN))
            b.setType(Material.SIGN_POST);
        this.sign = (Sign)b.getState();
        saleliste.add(this);
        this.zone.setSale(this);
        this.initialize();
    }

    private void initialize() {
        if (type.equals(SaleType.SELL))
            this.sign.setLine(0, ChatColor.AQUA + "A VENDRE");
        if (type.equals(SaleType.RENT))
            this.sign.setLine(0, ChatColor.AQUA + "A LOUER");
        if (this.zone.getOwner()!=null)
            this.sign.setLine(1,this.zone.getOwner().getName());
        this.sign.setLine(2,"clique = achat");
        this.sign.setLine(3, ChatColor.DARK_RED +""+ this.price + " POs");
        sign.update();
        sign.getChunk().unload();
        sign.getChunk().load();
    }

    public void insert() {
        this.zone.setSale(this);
        MySQLSaver.mysql_update("INSERT INTO `sale` (`id`, `price`,`type`, `sign`) VALUES"
                + " ("+this.zone.getId()+", "+this.price+", '"+this.type.name()+"', '"+Serialize.locationToString(this.sign.getLocation())+"');");
    }

    public static ArrayList<Sale> getSaleliste() {
        return saleliste;
    }

    public  Sign getSign() {
        return this.sign;
    }

    public void sell(User user) {
        if(this.zone.getOwner()!=null)
            if (!user.pay(this.price,this.zone.getOwner()))
            return;
        if(this.zone.getOwner()==null)
            if (!user.take(this.price))
                return;
        MySQLSaver.mysql_update("DELETE FROM `sale` WHERE `id` = "+this.zone.getId());
        saleliste.remove(this);
        this.zone.reset();
        this.zone.setOwner(user);
        this.sign.getBlock().breakNaturally();
    }

    public int getPrice() {
        return price;
    }
}
