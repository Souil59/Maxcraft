package net.nathem.script.editor.event;

import net.nathem.script.editor.Editor;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;

public class SignPlaceEvent extends Event implements Cancellable{

	private SignChangeEvent handler;
	private Editor editor;
	private static final HandlerList handlers = new HandlerList();
	
	public SignPlaceEvent(SignChangeEvent handler, Editor editor) {
		this.handler = handler;
		this.editor = editor;
	}


	@Override
	public boolean isCancelled() {
		return false;
	}


	@Override
	public void setCancelled(boolean cancelled) {
		this.handler.setLine(0, "CANCELLED");
		this.handler.setLine(1, "");
		this.handler.setLine(2, "");
		this.handler.setLine(3, "");
		
	}


	public SignChangeEvent getHandler() {
		// TODO Auto-generated method stub
		return this.handler;
	}


	 
	@Override //La classe Event nous oblige à surcharger la méthode getHandler()
		public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}


	public Editor getEditor() {
		return editor;
	}
	

}
