package fr.maxcraft.server.economy.shop.events;

import fr.maxcraft.player.User;
import fr.maxcraft.server.economy.shop.Shop;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShopChoseItemEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private Shop shop;
    private User user;
    private ItemStack item;
 
    public ShopChoseItemEvent(Shop shop, User u, ItemStack is) {
    	this.shop = shop;
    	this.user = u;
    	this.item = is;
    }
 
 




	public ItemStack getItem() {
		return item;
	}






	public void setItem(ItemStack item) {
		this.item = item;
	}






	public User getUser() {
		return user;
	}






	public void setUser(User user) {
		this.user = user;
	}






	public Shop getShop() {
		return shop;
	}


	public void setShop(Shop shop) {
		this.shop = shop;
	}




	public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}