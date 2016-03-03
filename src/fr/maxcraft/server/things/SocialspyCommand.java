package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by admin on 22/02/16.
 */
public class SocialspyCommand extends Command{

    public SocialspyCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")) return false;
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du Joueur !");
            return true;
        }
        if (!u.isSocialspy()){
            u.setSocialspy(true);
            u.sendNotifMessage(Things.message()+"Socialspy activé !");
            return true;
        }
        else {
            u.setSocialspy(false);
            u.sendNotifMessage(Things.message()+"Socialspy désactivé !");
            return true;
        }
    }
}
