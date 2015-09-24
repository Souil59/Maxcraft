package fr.maxcraft.player.jobs.jobs;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

import fr.maxcraft.player.jobs.Jobs;
import fr.maxcraft.player.jobs.jobstype.Farmer;

public class Excavateur extends Jobs implements Farmer{
	public Excavateur(UUID uuid,double xp){
		super.uuid = uuid;
		super.xp = xp;
		super.name = "Excavateur";
		super.addBlocks(blockList());
	}

	@Override
	public List<Material> blockList() {
		return Arrays.asList(Material.STONE,Material.DIRT,Material.GRASS,Material.GRAVEL,Material.SAND
				,Material.RED_SANDSTONE,Material.SANDSTONE,Material.CLAY,Material.MYCEL,Material.SANDSTONE
				,Material.PRISMARINE,Material.HARD_CLAY,Material.SNOW);
	}
	@Override
	public void xp() {
		super.xp = super.xp+0.5;
		
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
