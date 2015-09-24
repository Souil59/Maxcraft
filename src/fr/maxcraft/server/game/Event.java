package fr.maxcraft.server.game;

import java.util.ArrayList;

import fr.maxcraft.player.User;

@SuppressWarnings("unused")
public class Event {
	
	private String name;
	private String tag;
	private int maxslot;
	private ArrayList<User> players = new ArrayList<User>();
	private boolean open;

	public Event(String name,String tag,int maxslot,ArrayList<User> players, boolean open){
		this.name = name;
		this.tag = tag;
		this.maxslot = maxslot;
		this.players = players;
		this.open = open;
		
	}
	
	
	

}
