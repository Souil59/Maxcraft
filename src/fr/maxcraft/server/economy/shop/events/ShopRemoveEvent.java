package fr.maxcraft.server.economy.shop.events;

import fr.maxcraft.player.User;
import fr.maxcraft.server.economy.shop.Shop;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class ShopRemoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private Shop shop;
    private boolean cancelled;
    private User user;
 
    public ShopRemoveEvent(Shop shop, User user) {
    	this.shop = shop;
        this.cancelled = false;
        this.user = user;
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


	public boolean isCancelled() {
		return cancelled;
	}


	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


	public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}