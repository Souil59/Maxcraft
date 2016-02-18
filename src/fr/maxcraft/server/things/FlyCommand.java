package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.chatmanager.Chat;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by admin on 18/02/16.
 */
public class FlyCommand extends Command {

    public FlyCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length!=0 && args.length!=1){
            sender.sendMessage(ChatColor.RED+"Erreur: joueur introuvable !");
            return true;
        }

        if (args.length ==1){
            User u = User.get(args[0]);
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            if (!u.getPlayer().isFlying()){
                u.getPlayer().setAllowFlight(true);
                u.sendMessage(ChatColor.GRAY + "Fly activé !");
                sender.sendMessage(Things.message()+"Fly activé pour "+u.getName());
                return true;
            }
            else {
                u.getPlayer().setAllowFlight(false);
                u.sendMessage(ChatColor.GRAY + "Fly désactivé !");
                sender.sendMessage(Things.message()+" Fly désactivé pour "+u.getName());
                return true;
            }
        }
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"Vous devez être un joueur pour soumettre cette commande avec ces paramètres !");
            return true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        if (!u.getPlayer().isFlying()){
            u.getPlayer().setAllowFlight(true);
            u.sendMessage(Things.message() + "Fly activé !");
            return true;
        }
        else {
            u.getPlayer().setAllowFlight(false);
            u.sendMessage(Things.message() + "Fly désactivé !");
            return true;
        }
    }
}
