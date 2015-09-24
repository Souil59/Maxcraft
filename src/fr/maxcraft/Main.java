package fr.maxcraft;

import java.lang.reflect.Field;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxcraft.player.User;
import fr.maxcraft.player.faction.Faction;
import fr.maxcraft.player.faction.FactionListener;
import fr.maxcraft.player.jobs.JobsListener;
import fr.maxcraft.player.magicraft.MagicListener;
import fr.maxcraft.player.magicraft.ManaTask;
import fr.maxcraft.player.menu.MenuListener;
import fr.maxcraft.player.moderation.ModeratorCommand;
import fr.maxcraft.player.permissions.PermsCommand;
import fr.maxcraft.player.permissions.PermsListener;
import fr.maxcraft.server.chatmanager.ChatListener;
import fr.maxcraft.server.command.HelpManager;
import fr.maxcraft.server.economy.EcoListener;
import fr.maxcraft.server.game.GameListener;
import fr.maxcraft.server.protect.ProtectListener;
import fr.maxcraft.server.world.Marker;
import fr.maxcraft.server.world.Travel;
import fr.maxcraft.server.world.World;
import fr.maxcraft.server.world.WorldListener;
import fr.maxcraft.server.zone.Zone;
import fr.maxcraft.server.zone.ZoneCommand;
import fr.maxcraft.server.zone.ZoneListener;
import fr.maxcraft.utils.MySQLSaver;

public class Main extends JavaPlugin {


	private static Plugin plugin;
	private static CommandMap cmap;
	public void onEnable() {
		plugin = this;
		
		try {
			
		//MySql
		this.saveDefaultConfig();
		MySQLSaver.connect();

		//Entity Loader
		User.loadActive();
		Faction.load();
		Zone.load();
		//Marker.load();
		//ZoneVente.load();if(Bukkit.getServer() instanceof CraftServer){
		
		
		
		
		//Task
		new ManaTask().runTaskTimer(this, 0, 10);
		
		//CommandRegister
        final Field f = CraftServer.class.getDeclaredField("commandMap");
        f.setAccessible(true);
        cmap=(CommandMap)f.get(Bukkit.getServer());
		new ModeratorCommand();
		new HelpManager();
		new ZoneCommand("zone");
		new PermsCommand("perms");
		World.register(this);
		Marker.register(this);
		Travel.register(this);
		ChatListener.register(this);
		
		
		//Listeners
		new JobsListener(this);
		new PermsListener(this);
		new ZoneListener(this);
		new WorldListener(this);
		new EcoListener(this);
		new ChatListener(this);
		new ProtectListener(this);
		new FactionListener(this);
		new MenuListener(this);
		new MagicListener(this);
		new GameListener(this);
		
		
		
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED+"===================");
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED+"= Maxcraft charg� =");
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED+"===================");
		} catch (Exception e) {
			e.printStackTrace();
			logError("Erreur au chargement!");
			//this.getServer().shutdown();
		}
		
	}
	public static void log(String s){
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE+"[Maxcraft]"+s);
	}
	public static void logError(String s){
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED+"[Maxcraft]"+s);
	}
	public static Main getPlugin() {
		return (Main)plugin ;
	}
	public static CommandMap getCmap() {
		return cmap;
	}
}
