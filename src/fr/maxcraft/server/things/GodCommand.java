package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GodCommand extends Command {

    public GodCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length!= 0 && args.length!= 1){
            sender.sendMessage(ChatColor.RED+"Il y a trop de paramètres !");
            return true;
        }
        if (args.length==1){
            User u = User.get(args[0]);
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            if (!u.isGod()){
                u.setGod(true);
                u.sendMessage(ChatColor.GRAY + "God mode " + ChatColor.GREEN + "activé");
                sender.sendMessage(Things.message()+ "God mode " + ChatColor.GREEN + "activé "+ChatColor.GRAY+ "pour "+u.getName());
                return true;
            }
            else{
                u.setGod(false);
                u.sendMessage(ChatColor.GRAY + "God mode " + ChatColor.RED + "désactivé");
                sender.sendMessage(Things.message() + "God mode " + ChatColor.RED + "désactivé " + ChatColor.GRAY + "pour " + u.getName());
                return true;
            }
        }
        else{
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            if (!u.isGod()){
                u.setGod(true);
                u.sendMessage(Things.message() + "God mode " + ChatColor.GREEN + "activé");
                return true;
            }
            else{
                u.setGod(false);
                u.sendMessage(Things.message() + "God mode " + ChatColor.RED + "désactivé");
                return true;
            }
        }
    }
}
