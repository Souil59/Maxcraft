package fr.maxcraft.server.shop.events;

import fr.maxcraft.player.User;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class ShopCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private User owner;
    private float price;
    private int amount;
    private Location location;
    private String type;
    private boolean admin;
    private boolean cancelled;
 
    public ShopCreateEvent(User owner, float price, int amount, Location location, String type, boolean admin) {
        this.owner = owner;
        this.price = price;
        this.amount = amount;
        this.location = location;
        this.type = type;
        this.admin = admin;
        this.cancelled = false;
    }
 
  
 
    public User getOwner() {
		return owner;
	}



	public void setOwner(User owner) {
		this.owner = owner;
	}



	public float getPrice() {
		return price;
	}



	public void setPrice(float price) {
		this.price = price;
	}



	public int getAmount() {
		return amount;
	}



	public void setAmount(int amount) {
		this.amount = amount;
	}



	public Location getLocation() {
		return location;
	}



	public void setLocation(Location location) {
		this.location = location;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public boolean isAdmin() {
		return admin;
	}



	public void setAdmin(boolean admin) {
		this.admin = admin;
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