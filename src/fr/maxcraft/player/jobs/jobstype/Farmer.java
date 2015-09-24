package fr.maxcraft.player.jobs.jobstype;

import java.util.List;

import org.bukkit.Material;

public interface Farmer {
	public abstract List<Material> blockList();
	public abstract void xp(Material m);
	public abstract void xp();
	public abstract int blockRate();
}
