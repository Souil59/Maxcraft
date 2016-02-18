package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by admin on 18/02/16.
 */
public class FeedCommand extends Command {

    public FeedCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length!=0 && args.length!=1 ){
            sender.sendMessage(Things.message()+"Il manque des paramètres !");
            return true;
        }
        if (args.length == 1){
            User u = User.get(args[0]);
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Erreur : Joueur introuvable !");
                return true;
            }
            if (u.getPlayer().getGameMode().equals(GameMode.SPECTATOR) || u.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                sender.sendMessage(ChatColor.RED+"Le joueur n'est pas en mode survie ou aventure !");
                return true;
            }
            u.getPlayer().setFoodLevel(20);
            u.getPlayer().setSaturation(10);
            u.sendMessage(ChatColor.GRAY + "Vous avez été nourri !");
            sender.sendMessage(Things.message()+u.getName()+" a été rassasié !");
            return true;
        }

        if ( !(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"Vous devez être un joueur pour exécuter cette commande avec ces paramètres !");
            return  true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Erreur : Joueur introuvable !");
            return true;
        }
        if (u.getPlayer().getGameMode().equals(GameMode.SPECTATOR) || u.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            sender.sendMessage(ChatColor.RED+"Vous devez être en mode survie ou aventure !");
            return true;
        }
        u.getPlayer().setFoodLevel(20);
        u.getPlayer().setSaturation(10);
        u.sendMessage(Things.message() + "Vous avez été nourri !");
        return true;
    }
}
