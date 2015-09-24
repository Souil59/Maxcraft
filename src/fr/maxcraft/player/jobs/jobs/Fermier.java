package fr.maxcraft.player.jobs.jobs;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

import fr.maxcraft.player.jobs.Jobs;
import fr.maxcraft.player.jobs.jobstype.Farmer;

public class Fermier extends Jobs implements Farmer{
	public Fermier(UUID uuid,double xp){
		super.uuid = uuid;
		super.xp = xp;
		super.name = "Fermier";
		super.addBlocks(blockList());
	}

	@Override
	public List<Material> blockList() {
		return Arrays.asList(Material.WHEAT,Material.CROPS,Material.PUMPKIN,Material.MELON_BLOCK,Material.POTATO,Material.CARROT
				,Material.SUGAR_CANE_BLOCK,Material.NETHER_WARTS,Material.CACTUS,Material.BROWN_MUSHROOM,Material.RED_MUSHROOM);
	}

	@Override
	public void xp() {
		super.xp = super.xp+3;
		
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
		// TODO Auto-generated method stub
		
	}
}
