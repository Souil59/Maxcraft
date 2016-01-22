package net.nathem.script.core.event;

import net.nathem.script.core.object.Custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomObjectSignalEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	private Custom customObject;
	private boolean signal;
	private Player player;
	
	
	public CustomObjectSignalEvent(Custom customObject, boolean signal, Player player) {
		super();
		this.customObject = customObject;
		this.signal = signal;
		this.player = player;
		
	}


	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}


	public Custom getCustomObject() {
		return customObject;
	}


	public boolean getSignal() {
		return signal;
	}


	public Player getPlayer() {
		return player;
	}

	public String getType()
	{
		return this.customObject.getType();
	}

	
	

}
