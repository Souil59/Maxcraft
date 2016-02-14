package fr.maxcraft.player.classes.magicraft.spells;

import fr.maxcraft.player.classes.classes.Mage;
import fr.maxcraft.player.classes.magicraft.Spell;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fireball extends Spell{

	Mage mage;
	PlayerInteractEvent event;

	public Fireball(Mage m,PlayerInteractEvent event){
		this.mage = m;
		this.event = event;
	}

	@Override
	public boolean isCastable() {
		if (this.mage.getMana()<10)
			return false;
		return true;
	}

	@Override
	public void cast() {
		org.bukkit.entity.Fireball f = (org.bukkit.entity.Fireball) this.mage.getUser().getPlayer().getWorld().spawnEntity(this.mage.getUser().getPlayer().getLocation(),EntityType.FIREBALL);
		f.setVelocity(this.mage.getUser().getPlayer().getVelocity());
		this.mage.manaConsume(10);
	}

}
