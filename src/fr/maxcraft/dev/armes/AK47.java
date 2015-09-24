package fr.maxcraft.dev.armes;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class AK47 extends Weapon{
	
	int damage;
	EntityType bullet;
	int reloadTime;
	int fireRate;
	int ammunition;
	int loader;
	int loaderSize;
	
	public AK47(){
		this.damage = 10;
		this.bullet = EntityType.ARROW;
		this.reloadTime = 2000;
		this.fireRate = 120;
		this.ammunition = 31;
		this.loader = 3;
		this.loaderSize = 31;
		
	}
	

	@Override
	public ItemStack item() {
		return null;
	}

	@Override
	public int damage() {
		return damage;
	}

	@Override
	public EntityType bullet() {
		return bullet;
	}

	@Override
	public int reloadTime() {
		return reloadTime;
	}

	@Override
	public int fireRate() {
		return fireRate;
	}

	@Override
	public int ammunition() {
		return ammunition;
	}

	@Override
	public int loader() {
		return loader;
	}

	@Override
	public int loaderSize() {
		return loaderSize;
	}

}
