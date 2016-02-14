package fr.maxcraft.server.world;

import fr.maxcraft.server.command.Command;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WorldCommand extends Command {

    public WorldCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo");
        this.register();
        this.tabComplete(name, Arrays.asList("create [name] [type]","tp [world]","delete [world]"));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
		Player p = (Player) sender;
        sender.sendMessage("testx");
		if (args.length==0){
			for (org.bukkit.World w : Bukkit.getWorlds())
				sender.sendMessage(w.getName());
			return true;
		}
		switch(args[0]){
		case "new": case "create":{
			if(!createWorld(args,p))
				sender.sendMessage("/world create <name> [type]");
			else
				sender.sendMessage("Nouveau monde créé");
			break;
		}
		case "tp":{
			if(!teleport(args,p))
				sender.sendMessage("/world tp <name>");
			else
				sender.sendMessage("Teleportation vers "+args[1]);
			break;
		}
		case "delete": case "remove":{
			if(!remove(args,p))
				sender.sendMessage("/world remove <name>");
			else
				sender.sendMessage("Supression du monde "+args[1]);
			break;
		}
		default :
			break;
		}
		return true;
	}

	private boolean remove(String[] args, Player p) {
		if (args.length<1)
			return false;
		if (Bukkit.getWorld(args[1]) == null)
			return false;
		org.bukkit.World w = Bukkit.getWorld(args[1]);
		Bukkit.unloadWorld(args[1], false);
		w.getWorldFolder().delete();
		MySQLSaver.mysql_update("DELETE FROM `world` WHERE `name` = '"+args[1]+"'");
		return true;
	}

	private boolean teleport(String[] args, Player sender) {
		if (args[1]==null)
			return false;
		if (Bukkit.getWorld(args[1]) == null)
			return false;
		sender.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
		return true;
	}

	private boolean createWorld(String[] args, CommandSender sender) {
		if (args.length==1)
			return false;
		WorldCreator wc = new WorldCreator(args[1]);
		if (args.length>2)
			wc.type(WorldType.valueOf(args[2].toUpperCase()));
		org.bukkit.World w = Bukkit.createWorld(wc);
		Location l = w.getSpawnLocation();
		w.setSpawnLocation(l.getBlockX(), w.getHighestBlockYAt(l), l.getBlockZ());
		MySQLSaver.mysql_update("INSERT INTO `world` (`name`, `group`) VALUES ('"+args[1]+"', '1');");
		return true;
	}
}
