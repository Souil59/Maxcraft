package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class SpeedCommand extends Command {

    public SpeedCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage(ChatColor.RED + "nombre de paramètres incorrects !");
            return true;
        }
        if (args.length == 3) {
            User u = User.get(args[2]);
            if (u == null) {
                sender.sendMessage(ChatColor.RED + "Joueur introuvable !");
                return true;
            }
            this.next(sender, u, args);
        }
        else {
            User u = User.get(sender.getName());
            if (u == null) {
                sender.sendMessage(ChatColor.RED + "Joueur introuvable !");
                return true;
            }
            this.next(sender, u, args);
        }
        return false;
    }

    public boolean next(CommandSender sender, User u, String[] args){
        int vitesse;
        try{
            vitesse = Integer.parseInt(args[0]);
        }
        catch (ClassCastException e){
            sender.sendMessage(ChatColor.RED+"Erreur dans la conversion de la valeure de la vitesse !");
            return true;
        }
        if (args[1].equals("fly")){
            u.getPlayer().setFlySpeed((float) vitesse);
            u.sendMessage(Things.message()+"Vitesse de fly changée à "+vitesse);
            return true;
        }
        else if(args[1].equals("walk")){
            u.getPlayer().setWalkSpeed((float) vitesse);
            u.sendMessage(Things.message()+"Vitesse de marche changée à "+vitesse);
            return true;
        }
        u.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");
        return true;
    }
}
