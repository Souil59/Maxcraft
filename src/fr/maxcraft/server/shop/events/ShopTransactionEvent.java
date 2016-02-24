package fr.maxcraft.server.shop.events;


import fr.maxcraft.player.User;
import fr.maxcraft.server.shop.Shop;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopTransactionEvent extends Event{

    private Shop shop;
    private User user;
    private boolean cancelled;
    private String reason;


    public ShopTransactionEvent(Shop shop, User u) {
        this.shop = shop;
        this.user = u;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
