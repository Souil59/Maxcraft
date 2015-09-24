package fr.maxcraft.player.magicraft;

import java.util.ArrayList;
import java.util.UUID;

public class Mage {
	
	private static ArrayList<Mage> mage = new ArrayList<Mage>();
	private UUID uuid;
	private double mana;
	private int level;

	
	public Mage (UUID uuid,int mana,int level){
		this.setUuid(uuid);
		this.mana = mana;
		this.level = level;
		mage.add(this);
	}


	public static Mage load(UUID uuid) {
		return new Mage(uuid,0,1);
	}


	public double getMana() {
		return mana;
	}


	public void setMana(double mana) {
		this.mana = mana;
	}


	public int getLevel() {
		return level;
	}


	public UUID getUuid() {
		return uuid;
	}


	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public int getMaxMana(){
		return 100+this.getLevel()*10;
	}

}
