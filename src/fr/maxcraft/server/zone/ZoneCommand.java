package fr.maxcraft.server.zone;

import java.awt.Point;
import java.awt.Polygon;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import fr.maxcraft.utils.MySQLSaver;

public class ZoneCommand extends Command{

	public ZoneCommand(String s) {
		super(s);
		this.setAliases(Arrays.asList("z")).register();
	}

	@Override
	public boolean execute(CommandSender sender, String label,
			String[] args) {
	{
		if(args.length >= 1)
		{
		
			switch(args[0])
			{
		
			case "infos": case "info": case "i": case "information": case "informations":
				return zoneInfo(sender);
			case "tp":
				return tp(sender, args[1]);
			case "inactive": case "in": case "dead": case "d":
				return inactiveList(sender);
			case "new": case "n": case "add": case "create": case "nouvelle":
				return this.createZone(sender, args);
			case "remove": case "supprimer": case "r": case "delete": case "del":
				return this.removeZone(sender, args[1]);
			case "resize": case "rs": case "s": case "redimensionner":
				return this.resizeZone(sender, args[1]);
			case "reload": case "load": case "l": case "rl": case "recharger":
				return this.loadZone(sender, args[1]);
			case "set":
				return this.set(sender, args);
			case "addtag": case "tag": case "newtag":
				return this.addTag(sender, args[1], args[2]);
			case "removetag": case "deletetag":
				return this.removeTag(sender, args[1], args[2]);
			case "reset": case "reinitialiser":
				return this.reset(sender, args[1]);
			default:
				sender.sendMessage("Il manque des param�tres...");
				return true;
			}
		}
		
		sender.sendMessage("Il manque des param�tres...");
		}
	return true;
	}

	private boolean reset(CommandSender sender, String string) {
		if(!isInt(string)){
			sender.sendMessage(message("Ce n'est pas une zone !"));
			return true;
		}
		int id = Integer.parseInt(string);
		Zone zone = Zone.getZone(id);
		if (!sender.hasPermission("maxcraft.modo")){
			sender.sendMessage(message("Vous ne pouvez pas faire cela."));
			return true;
		}
		zone.reset();
		zone.save();
		sender.sendMessage(message("Cette zone a été reset."));
		return true;
		
	}

	private boolean removeTag(CommandSender sender, String string, String string2) {
		if(!isInt(string)){
			sender.sendMessage(message("Ce n'est pas une zone !"));
			return true;
		}
		int id = Integer.parseInt(string);
		Zone zone = Zone.getZone(id);
		if (!sender.hasPermission("maxcraft.modo")){
			sender.sendMessage(message("Vous ne pouvez pas faire cela."));
			return true;
		}
		zone.getTags().remove(string2);
		zone.save();
		sender.sendMessage(message("Le TAG "+string2+" a été retiré."));
		return true;
	}

	private boolean addTag(CommandSender sender, String string, String string2) {
		if(!isInt(string)){
			sender.sendMessage(message("Ce n'est pas une zone !"));
			return true;
		}
		int id = Integer.parseInt(string);
		Zone zone = Zone.getZone(id);
		if (!sender.hasPermission("maxcraft.modo")){
			sender.sendMessage(message("Vous ne pouvez pas faire cela."));
			return true;
		}
		zone.getTags().add(string2);
		zone.save();
		sender.sendMessage(message("Le TAG "+string2+" a été ajouté."));
		return true;
	}

	private boolean set(CommandSender sender, String[] args) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean loadZone(CommandSender sender, String string) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean resizeZone(CommandSender sender, String string) {
		User u = User.get(((Player)sender).getUniqueId());
		if(!isInt(string)){
			sender.sendMessage(message("Ce n'est pas une zone !"));
			return true;
		}
		int id = Integer.parseInt(string);
		Zone zone = Zone.getZone(id);
		if(!ZoneListener.selections.containsKey(u)){
			u.sendMessage(this.message("Vous devez faire une sélection !"));
			return true;
		}
		if (!zone.canCubo(u.getPlayer())){
			u.sendMessage(this.message("Vous ne pouvez faire cela!"));
			return true;
		}
		Polygon p = ZoneListener.selections.get(u);
		if (p.npoints<2)
			u.sendMessage(this.message("Vous devez faire une sélection d'au moins 2 points !"));
		if (p.npoints==2){
			int[] x = p.xpoints.clone();
			int[] y = p.ypoints.clone();
			p.reset();
			p.addPoint(x[0], y[0]);
			p.addPoint(x[0], y[1]);
			p.addPoint(x[1], y[1]);
			p.addPoint(x[1], y[0]);
		}
		zone.setPolygon(p);
		zone.save();
		sender.sendMessage(message("La zone a été redéfinie."));
		return true;
	}

	private boolean removeZone(CommandSender sender, String string) {
		User u = User.get(((Player)sender).getUniqueId());
		if(!isInt(string)){
			sender.sendMessage(message("Ce n'est pas une zone !"));
			return true;
		}
		int id = Integer.parseInt(string);
		Zone zone = Zone.getZone(id);
		if (!zone.canCubo(u.getPlayer())){
			u.sendMessage(this.message("Vous ne pouvez faire cela!"));
			return true;
		}
		Zone.zonelist.remove(zone);
		MySQLSaver.mysql_update("DELETE FROM `zone` WHERE `id`= "+id);
		sender.sendMessage(message("La zone a été suprimée."));
		return true;
	}

	private boolean createZone(CommandSender sender, String[] args) {
		User u = User.get(((Player)sender).getUniqueId());
		if(!ZoneListener.selections.containsKey(u))
			u.sendMessage(this.message("Vous devez faire une sélection !"));
		else
			try {
				sender.sendMessage(message(Zone.create(ZoneListener.selections.get(u),u,args[1])));
			} catch (Exception e) {
				Main.log("Erreur à la creation de zone");
				e.printStackTrace();
			}
			ZoneListener.selections.remove(u);
		return true;
	}

	private boolean inactiveList(CommandSender sender) {
		Player p = (Player) sender;
		if(!p.hasPermission("maxcraft.guide"))
		{
			sender.sendMessage(message("Vous n'avez pas accès à cette commande !"));
			return true;
		}
		Zone zone = Zone.getZone(p.getLocation());
		sender.sendMessage(message("LISTE DES PARCELLES MORTES SUR "+ zone.getName()));
		for (Zone z : Zone.zonelist)
			if (z.getOwner()!=null)
				if (z.getParent()!=null)
				if (z.getParent().equals(zone) && !z.getOwner().isActive())
						sender.sendMessage(message(ChatColor.GOLD + "#" +z.getId()+ ChatColor.WHITE + " " + z.getName()));
		return true;
		
	}

	private boolean tp(CommandSender sender, String string) {

		if(!isInt(string)){
			sender.sendMessage(message("Ce n'est pas une zone !"));
			return true;
		}
		int id = Integer.parseInt(string);
		Zone zone = Zone.getZone(id);
		if(zone == null){
			sender.sendMessage(message("Cette zone n'existe pas !"));
			return true;
		}
		Player p = (Player) sender;
		if(!p.hasPermission("maxcraft.guide")){
			sender.sendMessage(message("Vous n'avez pas accès à cette commande !"));
			return true;
		}
		Point po = zone.getCenter();
		Location loc = p.getLocation();	
		loc.setX(po.getX());
		loc.setZ(po.getY());
		loc.setWorld(zone.getWorld());
		loc.setY(loc.getWorld().getHighestBlockAt(loc).getLocation().getBlockY()+1);
		p.teleport(loc);
		sender.sendMessage(message("Téléportation vers la zone <"+zone.getName()+"> ..."));
		return true;

		
	}

	private boolean zoneInfo(CommandSender sender) {
		Player p = (Player) sender;
		Zone zone = Zone.getZone(p.getLocation());
		if(zone == null){
			sender.sendMessage(message("Vous n'êtes pas sur une zone !"));
			return true;
		}
		sender.sendMessage(ChatColor.GRAY+zone.description());
		return true;
	}

	private String message(String message){
		return ChatColor.AQUA + "[Zones] " + ChatColor.GRAY + message;
	}
	private boolean isInt(String string) {

		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	
}
