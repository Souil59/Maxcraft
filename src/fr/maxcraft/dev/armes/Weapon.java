package fr.maxcraft.dev.armes;

import java.util.Date;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public abstract class Weapon {
	
	Date nextShot;
	
	abstract ItemStack item();
	abstract int damage();
	abstract EntityType bullet();
	abstract int reloadTime();
	abstract int fireRate();
	abstract int ammunition();
	abstract int loader();
	abstract int loaderSize();
	
	public boolean canFire(){
		Date d = new Date();
		if (this.ammunition()<0&&d.getTime()>nextShot.getTime())
			return true;
		return false;
	}
	public String reload(){
		if (this.ammunition()==this.loaderSize())
			return "Votre arme est deja chargée";
		if (this.loader()==0)
			return "Vous n'avez plus de munitions sur vous";
		Date d = new Date();
		nextShot.setTime(d.getTime()+this.reloadTime());
		return "Rechargement";	
	}
	
}
