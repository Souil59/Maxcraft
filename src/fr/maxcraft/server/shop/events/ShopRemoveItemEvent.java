package fr.maxcraft.server.shop.events;

import fr.maxcraft.player.User;
import fr.maxcraft.server.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class ShopRemoveItemEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private Shop shop;
    private User user;
 
    public ShopRemoveItemEvent(Shop shop, User u) {
    	this.shop = shop;
    	this.user = u;
    }
 




	public Shop getShop() {
		return shop;
	}


	public void setShop(Shop shop) {
		this.shop = shop;
	}




	public User getUser() {
		return user;
	}





	public void setUser(User user) {
		this.user = user;
	}





	public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}