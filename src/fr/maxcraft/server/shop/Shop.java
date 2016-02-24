package fr.maxcraft.server.shop;


import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.shop.events.ShopTransactionEvent;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Shop {

    //variables

    private ShopManager manager;

    private int id;
    private User owner;
    private Sign sign;
    private Material item;
    private double price;
    private int amount;
    private Chest chest;
    private String type;
    private boolean admin;
    private Zone zone;
    private Location location;

    //constructor
    public Shop(ShopManager manager, int id, Location location, User owner, String type, int amount, float price, boolean admin, Zone zone, Material item) {
        this.manager = manager;
        this.id=id;

        try {
            this.sign = (Sign) location.getBlock().getState();
        }
        catch (Exception e){
            this.remove();
            return;
        }

        this.owner = owner;
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.admin = admin;
        this.zone = zone;
        this.item = item;
        this.location = location;

        this.manager.getShops().add(this);
        this.loadSign();
    }

    public static String message(){
        return ChatColor.AQUA+"[Commerce]"+ChatColor.GRAY;
    }

    public void loadSign(){
        ChatColor color = ChatColor.GREEN;

        if (!this.canWork()){
            color = ChatColor.RED;
        }

        this.sign.setLine(0, org.bukkit.ChatColor.BOLD + "[SHOP]");
        this.sign.setLine(1, org.bukkit.ChatColor.DARK_AQUA + "" + org.bukkit.ChatColor.BOLD + this.amount + org.bukkit.ChatColor.BLACK + org.bukkit.ChatColor.RESET + " pour");
        this.sign.setLine(2, org.bukkit.ChatColor.DARK_AQUA + " " + org.bukkit.ChatColor.BOLD + this.price + "PO's");

        if(this.type.equals("SELL"))
        {
            this.sign.setLine(3, color + "" + ChatColor.BOLD + "- ACHETER -");
        }
        else if(this.type.equals("BUY"))
        {
            this.sign.setLine(3,  color + "" + ChatColor.BOLD + "- VENDRE -");
        }

        this.reloadSign();
    }

    public void reloadSign(){

        this.sign.update();
        this.sign.getChunk().unload();
        this.sign.getChunk().load();
    }

    public boolean canWork(){
        if (this.getSign() == null) return false;
        if (this.getChest() == null) return false;
        if (this.getItemFrame()==null) return false;

        if(this.getItemFrame().getItem() == null || this.getItemFrame().getItem().getType().equals(Material.AIR)) return false;

        return true;

    }

    public ItemFrame getItemFrame(){
        for (Entity e : this.sign.getLocation().getWorld().getEntities()){
            if ( !(e instanceof ItemFrame)) continue;
            if (e.getLocation().getBlock().equals(this.getChest().getBlock().getRelative(0, 1, 0))) return (ItemFrame) e;
        }

        return null;
    }

    public void remove(){
        this.manager.getShops().remove(this);
        this.manager.saveConfig();
    }

    public boolean hasAccess(User u){

        if (u==null) return false;
        if(u.getPerms().hasPerms("maxcraft.modo")) return true;
        if(this.owner.getName().equals(u.getName())) return true;

        return false;
    }



    //Getters ans Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Material getItem() {
        return item;
    }

    public void setItem(Material item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return amount;
    }

    public void setQuantity(int quantity) {
        this.amount = quantity;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Chest getChest() {
        return chest;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public ShopManager getManager() {
        return manager;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void save(){
        MySQLSaver.mysql_update("INSERT INTO 'shop' ('id', 'zone_id', 'owner', 'type', 'price', 'admin', 'amount', 'world', 'x', 'y', 'z', 'item') VALUES ('" + this.id + "', '" + this.zone.getId() + "', '" + this.owner + "','" + this.type + "', '" + this.price + "', '" + this.admin + "', '" + this.amount + "', '" + this.location.getWorld().getName() + "', '" + this.location.getBlockX() + "', '" + this.location.getBlockY() + "', '" + this.location.getBlockZ() + "', '" + this.item.name() + "')");
    }

    public void transaction(User u){
        if (!this.canWork()){
            u.sendNotifMessage(message()+"Ce shop n'est pas activé !");
            return;
        }
        switch (type){
            case "SELL":
                this.sellTransaction(u);
            case "BUY":
                this.buyTransaction(u);
            default:
                u.sendMessage(message()+"Erreur dans la transaction !");
                return;
        }
    }

    public void sellTransaction(User u){

        //VERIFICATIONS
        if (this.getStockAmount() < this.amount && !this.admin){
            u.sendMessage(message()+"Il n'y a pas assez de stock dans ce shop pour cet achat ("+this.getStockAmount()+") !");
            return;
        }
        if (u.getBalance() < this.price){
            u.sendMessage(message() + "Vous n'avez pas assez d'argent pour cet achat ! Il vous faut au moins " + this.price + " POs !");
            return;
        }

        ItemStack is = this.getItemStack().clone();
        is.setAmount(this.getAmount());

        if(!this.canGiveItem(u.getPlayer().getInventory(), is))
        {
            u.sendMessage(message() + "Vous n'avez pas assez de place dans votre inventaire pour cet achat (" + this.getAmount() + " slots requis) !");
            return;
        }

        //TRANSACTION
        ShopTransactionEvent ste = new ShopTransactionEvent(this, u);
        Bukkit.getServer().getPluginManager().callEvent(ste);

        if(ste.isCancelled())
        {
            u.sendMessage(ste.getReason());
            return;
        }

        this.giveItem(u.getPlayer().getInventory(), is);
        u.getPlayer().updateInventory();

        if(!this.admin) this.removeFromChest(this.getAmount());

        //ECONOMIE
        if (!this.admin) u.pay(this.price, this.owner);
        else u.take(this.price);

        //Messages
        if(!this.admin)
        {
            u.sendMessage(message()+"Vous avez acheté "+this.amount+" "+this.getItemStack().getType().name()+" pour "+this.price+" POs à "+this.owner.getName()+".");
        }
        else
        {
            u.sendMessage(message()+"Vous avez acheté "+this.amount+" "+this.getItemStack().getType().name()+" pour "+this.price+" POs à l'administration.");
        }

        if(this.owner.getPlayer().isOnline() && !this.admin )
        {
            this.owner.sendMessage(message()+"Vous avez vendu "+this.amount+" "+this.getItemStack().getType().name()+" pour "+this.price+" POs à "+u.getName()+".");
        }
    }

    public void buyTransaction(User u){

        //VERIFICATIONS
        if(this.getStock(u.getPlayer().getInventory()) < this.getAmount())
        {
            u.sendMessage(message() + "Vous n'avez pas " + this.amount + " item(s) de ce type dans votre inventaire.");
            return;
        }

        if(!this.admin && this.owner.getBalance() < this.price)
        {
            u.sendMessage(message() + "Le propriétaire de ce shop d'achat (" + this.owner.getName() + ") n'a pas assez d'argent pour vous acheter ces items !");
            return;
        }

        ItemStack is = this.getItemStack().clone();
        is.setAmount(this.getAmount());

        //Ajout dans le coffre
        if(!this.admin && !this.canGiveItem(this.getChest().getInventory(), is))
        {
            u.sendMessage(message() + "Il n'y a plus de place dans le coffre pour stocker vos items (" + this.getFreeSpace(is, this.getChest().getInventory()) + " places restantes) !");
            return;
        }

        //TRANSACTION
        ShopTransactionEvent ste = new ShopTransactionEvent(this, u);
        Bukkit.getServer().getPluginManager().callEvent(ste);

        if(ste.isCancelled())
        {
            u.sendMessage(ste.getReason());
            return;
        }

        if(!this.admin)
        {
            this.giveItem(this.getChest().getInventory(), is);
        }

        //Remove de l'inventaire
        this.remove(u.getPlayer().getInventory(), this.getAmount());
        u.getPlayer().updateInventory();

        //Economie

        if (!this.admin){
            this.owner.pay(this.price, u);
        }
        else u.give(this.price);

        //Messages
        if(!this.admin)
        {
            u.sendMessage(message()+"Vous avez acheté "+this.amount+" "+this.getItemStack().getType().name()+" pour "+this.price+" à "+this.owner.getName()+".");
        }
        else
        {
            u.sendMessage(message()+"Vous avez acheté "+this.amount+" "+this.getItemStack().getType().name()+" pour "+this.price+" POs à l'administration.");
        }

        if(this.owner.getPlayer().isOnline() && !this.admin )
        {
            this.owner.sendMessage(message()+"Vous avez vendu "+this.amount+" "+this.getItemStack().getType().name()+" pour "+this.price+" POs à "+u.getName()+".");
        }

    }

    public void removeFromChest(int amount)
    {
        this.remove(this.getChest().getInventory(), amount);
    }

    public void remove(Inventory inventory, int amount)
    {
        if(amount > this.getStock(inventory)) return;

        for(ItemStack is : inventory.getContents())
        {
            if(is == null) continue;
            if(!(this.manager.sameItem(is, this.getItemStack())))
            {
                continue;
            }

            if(amount > 0)
            {
                if(is.getAmount() <= amount)
                {
                    inventory.removeItem(is);
                    amount -= is.getAmount();
                }
                else
                {
                    is.setAmount(is.getAmount()-amount);
                    amount = 0;
                }
            }
        }

    }

    public int getStock(Inventory inventory)
    {
        if(this.getItemStack() == null) return 0;

        int stock = 0;

        for(ItemStack is : inventory.getContents())
        {
            if(is == null) continue;
            if(this.manager.sameItem(this.getItemStack(), is))
            {
                stock = stock + is.getAmount();
            }
        }

        return stock;
    }

    public boolean canGiveItem(Inventory inventory, ItemStack is){
        if(this.getFreeSpace(is, inventory) >= is.getAmount()) return true;

        else return false;

    }

    public void giveItem(Inventory inventory, ItemStack is)
    {
        int amount = is.getAmount();

        while(amount > 0)
        {
            if(amount >= is.getMaxStackSize())
            {
                ItemStack istack = is.clone();
                istack.setAmount(is.getMaxStackSize());
                inventory.addItem(istack);
                amount -= is.getMaxStackSize();
            }
            else
            {
                ItemStack istack = is.clone();
                istack.setAmount(amount);
                inventory.addItem(is);
                amount = 0;
            }

        }

        return;
    }

    public int getFreeSpace(ItemStack is, Inventory inventory)
    {
        int free = 0;

        for(ItemStack slot : inventory.getContents())
        {
            if(slot == null)
            {
                free += is.getMaxStackSize();
            }
            else if(slot.isSimilar(is))
            {
                free += is.getMaxStackSize() - slot.getAmount();
            }
        }

        return free;
    }

    public ItemStack getItemStack()
    {
        ItemStack is = null;

        if(this.getItemFrame() != null)
        {
            is = this.getItemFrame().getItem();
        }

        return is;
    }

    public int getStockAmount(){
        if(this.getItemStack() == null) return 0;

        int stock = 0;

        for(ItemStack is : this.getChest().getInventory().getContents())
        {
            if(is == null) continue;
            if(this.manager.sameItem(this.getItemStack(), is))
            {
                stock = stock + is.getAmount();
            }
        }

        return stock;
    }
}
