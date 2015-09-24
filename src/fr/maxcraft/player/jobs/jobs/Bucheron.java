package fr.maxcraft.player.jobs.jobs;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

import fr.maxcraft.player.jobs.Jobs;
import fr.maxcraft.player.jobs.jobstype.Farmer;

public class Bucheron extends Jobs implements Farmer{
	public Bucheron(UUID uuid,double xp){
		super.uuid = uuid;
		super.xp = xp;
		super.name = "Bucheron";
		super.addBlocks(blockList());
	}

	@Override
	public List<Material> blockList() {
		return Arrays.asList(Material.LOG,Material.LOG_2,Material.VINE,Material.LEAVES,Material.LEAVES_2);
	}

	@Override
	public int blockRate() {
		if (this.getLvl()<=5)
			return 50+this.getLvl()*10;
		else
			return 100+(this.getLvl()-5)*5;
	}

	@Override
	public void xp(Material m) {
		
	}

	@Override
	public void xp() {
		this.xp += 3;
		
	}
}
