package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;


public class BackCommand extends Command {

    protected BackCommand(String name) {
        super(name);
        this.setPerms("maxcraft.guide").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length>0){
            User u = User.get(args[0]);
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur non trouvé !");
                return true;
            }
            if (u.getLastLocation()==null) return false;
            Location lastLocation = u.getPlayer().getLocation();
            u.getPlayer().teleport(u.getLastLocation());
            u.setLastLocation(lastLocation);
            sender.sendMessage(Things.message() + u.getName() + " a bien été back!");
            u.sendMessage(ChatColor.GRAY+"Vous avez été back");
            return true;
        }
        else {
            User u = User.get(sender.getName());
            if (u.getLastLocation()==null)return true;
            Location lastLocation = u.getPlayer().getLocation();
            u.getPlayer().teleport(u.getLastLocation());
            u.setLastLocation(lastLocation);
            sender.sendMessage(Things.message()+"Vous avez été back correctement !");
            return true;
        }


    }
}
