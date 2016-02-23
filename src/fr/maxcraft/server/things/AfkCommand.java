package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Created by admin on 14/02/16.
 */
public class AfkCommand extends Command {

    public AfkCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").setAliases(Arrays.asList("away")).register();
    }

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
        if (!User.get(arg0.getName()).getPerms().hasPerms("maxcraft.modo")) return false;
        if (arg2.length==0) {
            if (!User.get(arg0.getName()).isAfk()) {
                User.get(arg0.getName()).setAfk(true);
                arg0.sendMessage(ChatColor.GRAY+"Vous êtes désormais marqué absent");
                return true;
            }
            else{
                User.get(arg0.getName()).setAfk(false);
                arg0.sendMessage(ChatColor.GRAY + "Vous n'êtes désormais plus marqué absent");
                return true;
            }
        }
        User u = User.get(arg2[0]);
        if (u==null) {
            arg0.sendMessage(ChatColor.RED+"Joueur non trouvé !");
            return true;
        }
        else if(!u.isAfk()){
            u.setAfk(true);
            arg0.sendMessage(ChatColor.GRAY+u.getName()+" est désormais marqué absent");
            u.sendMessage(ChatColor.GRAY + "Vous avez été marqué absent par " + arg0 + getName());
            return true;
        }
        else {
            u.setAfk(false);
            arg0.sendMessage(ChatColor.GRAY + u.getName() + " n'est plus marqué absent");
            u.sendMessage(ChatColor.GRAY+"Vous avez été marqué présent par "+arg0+getName());
            return true;
        }

    }
}
