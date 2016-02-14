package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by admin on 14/02/16.
 */
public class TpCommand extends Command {

    protected TpCommand(String name) {
        super(name);
        this.setPerms("maxcraft.guide").register();
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] arg2) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.guide")) return true;
        if (arg2.length>1){
            User u = User.get(arg2[0]);
            User toU = User.get(arg2[1]);
            if (u==null || toU==null || !toU.isConnect() || !u.isConnect()){
                sender.sendMessage(ChatColor.RED+"Joueur(s) non trouvé(s) !");
                return true;
            }
            u.getPlayer().teleport(toU.getPlayer(), PlayerTeleportEvent.TeleportCause.COMMAND);
            sender.sendMessage(Things.message() + "Tp réussi : " + u.getName() + " ---> " + toU.getName());
            u.sendMessage(ChatColor.GRAY+"Vous avez été tp à "+toU.getName());
            return true;
        }
        else if (arg2.length==1){
            User u = User.get(sender.getName()), toU = User.get(arg2[0]);
            if (u == null|| toU == null || !u.isConnect() || !toU.isConnect()){
                sender.sendMessage(ChatColor.RED+"Joueur(s) non trouvé(s) !");
                return true;
            }
            u.getPlayer().teleport(toU.getPlayer(), PlayerTeleportEvent.TeleportCause.COMMAND);
            sender.sendMessage(Things.message() + "Tp réussi : " + u.getName() + " ---> " + toU.getName());
            return true;
        }
        return false;
    }
}
