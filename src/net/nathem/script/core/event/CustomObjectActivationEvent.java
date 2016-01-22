package net.nathem.script.core.event;

import net.nathem.script.core.object.Custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomObjectActivationEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	private Custom customObject;
	
	
	public CustomObjectActivationEvent(Custom customObject) {
		super();
		this.customObject = customObject;
		
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


	public String getType()
	{
		return this.customObject.getType();
	}
	
	

}
