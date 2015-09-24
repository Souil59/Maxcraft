package fr.maxcraft.player.jobs.jobs;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

import fr.maxcraft.player.jobs.Jobs;
import fr.maxcraft.player.jobs.jobstype.Farmer;

public class Mineur extends Jobs implements Farmer{
	public Mineur(UUID uuid,double xp){
		super.uuid = uuid;
		super.xp = xp;
		super.name = "Mineur";
		super.addBlocks(blockList());
	}

	@Override
	public List<Material> blockList() {
		return Arrays.asList(Material.IRON_ORE,Material.GOLD_ORE,Material.COAL_ORE,Material.REDSTONE_ORE,Material.LAPIS_ORE
				,Material.DIAMOND_ORE,Material.QUARTZ_ORE);
	}

	@Override
	public void xp() {
		super.xp = super.xp+20;
		
	}
	@Override
	public int blockRate() {
		if (this.getLvl()<=5)
			return 100+this.getLvl()*10;
		else
			return 150+(this.getLvl()-5)*5;
	}

	@Override
	public void xp(Material m) {
		
	}
}
