package fr.maxcraft.server.world;

import java.io.File;
import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WorldInventories {

	public static void save(Player player,World w){
		File file = new File(w.getWorldFolder().getAbsolutePath()+"/inventories/"+player.getUniqueId().toString()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		for (int i=0;i<player.getInventory().getSize();i++)
			if (player.getInventory().getItem(i)!=null)
				config.set("inv."+i,player.getInventory().getItem(i));
		config.set("health", player.getHealth());
		config.set("hunger", player.getFoodLevel());
		config.set("xp", player.getTotalExperience());
		config.set("gamemode", player.getGameMode().toString());
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load(Player player, World w) {
		File file = new File(w.getWorldFolder().getAbsolutePath()+"/inventories/"+player.getUniqueId().toString()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (!config.contains("inv"))
			player.getInventory().clear();
		for (int i=0;i<player.getInventory().getSize();i++)
			if (config.contains("inv."+i))
				player.getInventory().setItem((int) i, config.getItemStack("inv."+i));

		if (!config.contains("health"))
			player.setHealth(20);
		else player.setHealth(config.getDouble("health"));

		if (!config.contains("hunger"))
			player.setFoodLevel(20);
		else player.setFoodLevel(config.getInt("hunger"));
		
		if (!config.contains("xp"))
			player.setExp(0);
		else player.setExp(config.getInt("xp"));

		if (!config.contains("gamemode"))
			player.setGameMode(GameMode.SURVIVAL);
		else player.setGameMode(GameMode.valueOf(config.getString("gamemode")));
		
		if (player.getHealth()==0)
			player.setHealth(20);
	}
}
