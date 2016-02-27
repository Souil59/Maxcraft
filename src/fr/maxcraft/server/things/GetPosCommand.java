package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class GetPosCommand extends Command {

    public GetPosCommand(String name) {
        super(name);
        this.setPerms("maxcraft.guide").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length != 0 && args.length!= 1){
            sender.sendMessage(ChatColor.RED+"Il y a trop de paramètres !");
            return true;
        }
        if (args.length==1){
            User u = User.get(args[0]);
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Erreur, joueur introuvable !");
                return true;
            }
            String name = u.getName();
            String worldName = u.getPlayer().getWorld().getName();
            double x = u.getPlayer().getLocation().getBlockX();
            double y = u.getPlayer().getLocation().getBlockY();
            double z = u.getPlayer().getLocation().getBlockZ();
            boolean flying = u.getPlayer().isFlying();
            boolean sneaking = u.getPlayer().isSneaking();
            boolean sprinting = u.getPlayer().isSprinting();
            boolean insideVehicule = u.getPlayer().isInsideVehicle();
            this.positionMessage(sender, name, worldName, x, y, z, flying, sneaking, sprinting, insideVehicule);
            return true;
        }
        else {
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Erreur, joueur introuvable !");
                return true;
            }
            String name = u.getName();
            String worldName = u.getPlayer().getWorld().getName();
            double x = u.getPlayer().getLocation().getBlockX();
            double y = u.getPlayer().getLocation().getBlockY();
            double z = u.getPlayer().getLocation().getBlockZ();
            boolean flying = u.getPlayer().isFlying();
            boolean sneaking = u.getPlayer().isSneaking();
            boolean sprinting = u.getPlayer().isSprinting();
            boolean insideVehicule = u.getPlayer().isInsideVehicle();
            this.positionMessage(sender, name, worldName, x, y, z, flying, sneaking, sprinting, insideVehicule);
            return true;
        }
    }

    private void positionMessage(CommandSender sender, String name, String worldName, double x, double y, double z, boolean flying, boolean sneaking, boolean sprinting, boolean
                                   insideVehicule){
        sender.sendMessage(Things.message()+ChatColor.GOLD+"Joueur : "+ChatColor.RED+name);
        sender.sendMessage(Things.message()+ChatColor.GOLD+"Dans la map "+ChatColor.RED+worldName);
        sender.sendMessage(Things.message()+ChatColor.GOLD+"X: "+ChatColor.RED+x+ChatColor.GOLD+"; Y: "+ChatColor.RED+y+ChatColor.GOLD+"; Z: "+ChatColor.RED+z);
        if (flying) sender.sendMessage(Things.message()+ChatColor.GOLD+"En train de voler :"+ChatColor.GREEN+" OUI"); else sender.sendMessage(Things.message()+ChatColor.GOLD+"En train de voler :"+ChatColor.RED+" NON");
        if (sneaking) sender.sendMessage(Things.message()+ChatColor.GOLD+"En sneak :"+ChatColor.GREEN+" OUI"); else sender.sendMessage(Things.message()+ChatColor.GOLD+"En sneak :"+ChatColor.RED+" NON");
        if (sprinting) sender.sendMessage(Things.message()+ChatColor.GOLD+"En train de courir :"+ChatColor.GREEN+" OUI"); else sender.sendMessage(Things.message()+ChatColor.GOLD+"En train de courir :"+ChatColor.RED+" NON");
        if (insideVehicule) sender.sendMessage(Things.message()+ChatColor.GOLD+"Dans un véhicule :"+ChatColor.GREEN+" OUI"); else sender.sendMessage(Things.message()+ChatColor.GOLD+"Dans un véhicule :"+ChatColor.RED+" NON");
    }
}
