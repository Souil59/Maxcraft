package fr.maxcraft.server.protect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import fr.maxcraft.player.User;
import fr.maxcraft.server.zone.Zone;

public class Lock {

	public static boolean canUse(Player player, Block block,
			Location location) {
	User u = User.get(player);
	if (!getTags(block).contains("[private]")){
		//Zone checker
	Zone z = Zone.getZone(location);
	if (z!=null){
		if (z.canBuild(player))
			return true;
		if (z.hasDirectlyTag("lock-public"))
			return true;
	}
	else
		return true;
	}
		//TAG Checker
	if (getTags(block).contains(u.getName()))
		return true;
	if (getTags(block).contains("public"))
		return true;
	if (getTags(block).contains("publique"))
		return true;
	return false;
	}

	private static List<String> getTags(Block block) {
		ArrayList<String> tags = new ArrayList<String>();
		tags.addAll(getDirectlyTags(block));
		if (getTwin(block)!=null)
			tags.addAll(getDirectlyTags(getTwin(block)));
		if (block.getType().toString().contains("DOOR")){
			tags.addAll(getDirectlyTags(block.getRelative(BlockFace.NORTH)));
			tags.addAll(getDirectlyTags(block.getRelative(BlockFace.EAST)));
			tags.addAll(getDirectlyTags(block.getRelative(BlockFace.SOUTH)));
			tags.addAll(getDirectlyTags(block.getRelative(BlockFace.WEST)));
			tags.addAll(getDirectlyTags(block.getRelative(BlockFace.UP)));
			tags.addAll(getDirectlyTags(block.getRelative(BlockFace.DOWN)));
			if (getTwin(block)!=null){
				tags.addAll(getDirectlyTags(getTwin(block).getRelative(BlockFace.NORTH)));
				tags.addAll(getDirectlyTags(getTwin(block).getRelative(BlockFace.EAST)));
				tags.addAll(getDirectlyTags(getTwin(block).getRelative(BlockFace.SOUTH)));
				tags.addAll(getDirectlyTags(getTwin(block).getRelative(BlockFace.WEST)));
				tags.addAll(getDirectlyTags(getTwin(block).getRelative(BlockFace.UP)));
				tags.addAll(getDirectlyTags(getTwin(block).getRelative(BlockFace.DOWN)));
			}
		}
		return tags;
	}

	private static Block getTwin(Block block) {
		Block b = null;
		if (block.getRelative(BlockFace.EAST).getType().equals(block.getType()))
			b = (block.getRelative(BlockFace.EAST));
		if (block.getRelative(BlockFace.WEST).getType().equals(block.getType()))
			b = (block.getRelative(BlockFace.WEST));
		if (block.getRelative(BlockFace.NORTH).getType().equals(block.getType()))
			b = (block.getRelative(BlockFace.NORTH));
		if (block.getRelative(BlockFace.SOUTH).getType().equals(block.getType()))
			b = (block.getRelative(BlockFace.SOUTH));
		return b;
	}

	private static List<String> getDirectlyTags(Block block) {
		ArrayList<String> tags = new ArrayList<String>();
		if (block.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN))
			for (String s :((Sign) block.getRelative(BlockFace.EAST).getState()).getLines())
				tags.add(s);
		if (block.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN))
			for (String s :((Sign) block.getRelative(BlockFace.WEST).getState()).getLines())
				tags.add(s);
		if (block.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN))
			for (String s :((Sign) block.getRelative(BlockFace.NORTH).getState()).getLines())
				tags.add(s);
		if (block.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN))
			for (String s :((Sign) block.getRelative(BlockFace.SOUTH).getState()).getLines())
				tags.add(s);
		return tags;
	}


	public static boolean isProtected(Material type) {
		ArrayList<Material> lb = new ArrayList<Material>(Arrays.asList(Material.CHEST,Material.ENCHANTMENT_TABLE,Material.FURNACE,Material.BURNING_FURNACE,Material.JUKEBOX
				,Material.DROPPER,Material.DISPENSER,Material.WORKBENCH,Material.HOPPER,Material.ANVIL,Material.BREWING_STAND,Material.TRAP_DOOR,Material.IRON_TRAPDOOR));
		for (Material s:Material.values())
			if (s.toString().contains("DOOR")||s.toString().contains("FENCE"))
				lb.add(s);
		if (lb.contains(type))
			return true;
		return false;
	}

	public static boolean canAdmin(Player player) {
		if (player.hasPermission("maxcraft.guide"))
			return true;
		return false;
	}

}
