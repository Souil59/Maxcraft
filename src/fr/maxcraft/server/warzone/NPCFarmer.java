package fr.maxcraft.server.warzone;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.text.html.parser.Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Crevebedaine on 11/02/2016.
 */
public class NPCFarmer {

    public final UUID entityUUID;
    private final ItemStack item;
    private final ItemStack period;
    private Inventory inv = Bukkit.createInventory(null,54);

    public static ArrayList<NPCFarmer> farmers = new ArrayList<NPCFarmer>();

    public NPCFarmer(UUID n, ItemStack is, int period, int amount){
        this.entityUUID = n;
        this.item = is;
        this.period = is;
        for (int i = amount;i>0;i--)
            this.add();
        this.farmers.add(this);
        new Task(this).runTaskTimer(Main.getPlugin(),period,period);
    }

    public void add(){
        this.inv.addItem(this.item);
    }

    public void open(Player p){
        p.openInventory(this.inv);
        this.save();
    }

    public void insert(){
        int i = 0;
        for (ItemStack is : this.inv.getContents())
            if(is!=null)
                i+=is.getAmount();
        MySQLSaver.mysql_update("INSERT INTO `warzone` (`npc`, `item`, `period`,`amount` ) VALUES ('"+this.entityUUID.toString()+"', '"+Serialize.itemstackToString(this.item)+"', "+this.period+", "+i+");");
    }

    public void save(){
        int i = 0;
        for (ItemStack is : this.inv.getContents())
            if(is!=null)
                if (is.getType().equals(this.item))
                    i+=is.getAmount();
        MySQLSaver.mysql_update("UPDATE `warzone` SET `amount` = "+i+" WHERE 'npc' = '"+this.entityUUID.toString()+"';");

    }

    public static void load() {
        ResultSet r = MySQLSaver.mysql_query("SELECT * FROM  `warzone` ",false);
        try {
            while (r.next())
                new NPCFarmer(UUID.fromString(r.getString("npc")),Serialize.stringToItemStack(r.getString("item")),r.getInt("period"),r.getInt("amount"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private class Task extends BukkitRunnable {

        private final NPCFarmer farmer;

        public Task(NPCFarmer npcFarmer) {
            this.farmer = npcFarmer;
        }

        @Override
        public void run() {
            this.farmer.add();
            this.farmer.save();
        }
    }
}
