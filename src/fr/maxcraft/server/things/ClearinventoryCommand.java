package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

/**
 * Created by admin on 16/02/16.
 */
public class ClearinventoryCommand extends Command {

    public ClearinventoryCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").setAliases(Arrays.asList("ci", "clearinv")).register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (cmd.length()>0){
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur non trouvé !");
                return true;
            }
            u.getPlayer().getInventory().clear();
            sender.sendMessage(Things.message() + "Inventaire de " + args[0] + " vidé !");
            u.sendMessage(Things.message() + "Votre inventaire a été vidé par " + sender.getName());
            return true;
        }
        if (cmd.length()==0){
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.DARK_RED+"ERREUR !");
                return true;
            }
            u.getPlayer().getInventory().clear();
            u.sendMessage(Things.message()+"Inventaire vidé !");
            return true;
        }
        sender.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande...");
        return false;
    }
}
