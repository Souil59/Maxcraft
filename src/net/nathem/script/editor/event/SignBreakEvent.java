package net.nathem.script.editor.event;

import net.nathem.script.core.Map;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.editor.Editor;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class SignBreakEvent extends Event implements Cancellable{

	private BlockBreakEvent handler;
	private Editor editor;
	private static final HandlerList handlers = new HandlerList();
	private Sign sign;
	private Map map;
	
	public SignBreakEvent(BlockBreakEvent handler, Editor editor, Map map, Sign sign) {
		this.handler = handler;
		this.editor = editor;
		this.sign = sign;
		this.map = map;
	}


	@Override
	public boolean isCancelled() {
		return false;
	}


	@Override
	public void setCancelled(boolean cancelled) {
		this.handler.setCancelled(true);
	}


	public BlockBreakEvent getHandler() {
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


	public Sign getSign() {
		return sign;
	}


	public Map getMap() {
		return map;
	}
	
	

}
