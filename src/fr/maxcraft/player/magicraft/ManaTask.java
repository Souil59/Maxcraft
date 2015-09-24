package fr.maxcraft.player.magicraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.maxcraft.player.User;

public class ManaTask extends BukkitRunnable {

	public ManaTask() {
	}

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers())
			if (User.get(p).getMage().getMana()<User.get(p).getMage().getMaxMana()){
				ManaDisplay.sendPacket(p,User.get(p).getMage());
				User.get(p).getMage().setMana(User.get(p).getMage().getMana()+0.5);
			}
	}
} 
