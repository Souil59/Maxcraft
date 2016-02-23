package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

/**
 * Created by admin on 20/02/16.
 */
public class HealCommand extends Command {

    public HealCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").register();
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
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            if (u.getPlayer().getGameMode().equals(GameMode.CREATIVE) || u.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                sender.sendMessage(ChatColor.RED+"Il faut que le joueur soit en mode de jeu survie ou aventure !");
                return true;
            }
            if (u.getPlayer().getHealth() == u.getPlayer().getMaxHealth()){
                sender.sendMessage(ChatColor.RED+"Le joueur a toute sa vie !");
                return true;
            }
            u.getPlayer().setHealth(u.getPlayer().getMaxHealth());
            u.sendMessage(ChatColor.GRAY + "Vous avez été soigné !");
            sender.sendMessage(Things.message()+u.getName()+" a été soigné !");
            return true;
        }
        else {
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
                return true;
            }
            if (u.getPlayer().getGameMode().equals(GameMode.CREATIVE) || u.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                sender.sendMessage(ChatColor.RED+"Vous devez être en mode de jeu survie ou aventure !");
                return true;
            }
            if (u.getPlayer().getHealth() == u.getPlayer().getMaxHealth()){
                sender.sendMessage(ChatColor.RED+"Vous êtes déjà en pleine forme !");
                return true;
            }
            u.getPlayer().setHealth(u.getPlayer().getMaxHealth());
            u.sendMessage(Things.message() + "Vous avez été soigné !");
            return true;
        }
    }
}
