package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LightningStrike;

import java.util.Arrays;
import java.util.Set;


public class LightningCommand extends Command {

    public LightningCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").setAliases(Arrays.asList("thor")).register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")) return false;
        if (args.length != 0 && args.length!= 1){
            sender.sendMessage(ChatColor.RED+"Il y a trop de paramètres !");
            return true;
        }
        if (args.length ==1){
            User u = User.get(args[0]);
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            u.getPlayer().getWorld().strikeLightning(u.getPlayer().getLocation());
            u.sendMessage(ChatColor.GRAY + "Vous avez été foudroyé !");
            sender.sendMessage(Things.message()+u.getName()+" a été foudroyé !");
            return true;
        }
        else {
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            u.getPlayer().getWorld().strikeLightning(u.getPlayer().getTargetBlock((Set<Material>)null, 600).getLocation());
            return true;
        }
    }
}
