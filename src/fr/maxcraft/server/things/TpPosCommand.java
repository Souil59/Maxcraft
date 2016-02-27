package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;


public class TpPosCommand extends Command {

    public TpPosCommand(String name) {
        super(name);
        this.setPerms("maxcraft.guide").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length!= 3){
            sender.sendMessage(ChatColor.RED+"Nombre de paramètres incorrect !");
            return true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du joueur !");
            return true;
        }
        int x = 1;
        int y = 1;
        int z = 1;
        try {
            x =Integer.parseInt(args[0]);
            y =Integer.parseInt(args[1]);
            z =Integer.parseInt(args[2]);
        }
        catch (ClassCastException e){
            sender.sendMessage(ChatColor.RED+"Erreur dans la conversion des positions...");
            return true;
        }
        if (x > 30000000 || y > 30000000 || z > 30000000 || x < -30000000 || y < -30000000 || z < -30000000){
           sender.sendMessage(ChatColor.RED+"Coordonnées incohérentes !");
            return true;
        }
        Location pLocation = u.getPlayer().getLocation();
        Location l = new Location(u.getPlayer().getWorld(), (double)x, (double)y, (double)z);
        u.getPlayer().teleport(l);
        u.setLastLocation(pLocation);
        u.sendMessage(Things.message()+"Vous avez été tp aux coordonnées "+x+" "+y+" "+z+" de la map "+u.getPlayer().getWorld().getName());
        return true;
    }
}
