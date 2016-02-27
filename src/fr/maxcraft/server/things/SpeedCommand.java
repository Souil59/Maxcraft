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
        if (vitesse>9 || vitesse < 0){
            u.sendMessage(ChatColor.RED+"La vitesse doit être un nombre entier compris entre 0 et 9");
            return true;
        }
        float v = this.getBukkitSpeed(vitesse);
        if (args[1].equals("fly")){
            u.getPlayer().setFlySpeed(v);
            u.sendMessage(Things.message() + "Vitesse de fly changée à " + vitesse);
            return true;
        }
        else if(args[1].equals("walk")){
            u.getPlayer().setWalkSpeed(v);
            u.sendMessage(Things.message()+"Vitesse de marche changée à "+vitesse);
            return true;
        }
        u.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");
        return true;
    }

    public float getBukkitSpeed(int speed){
        switch (speed){
            case 0:
                return (float)0;

            case 1:
                return (float)0.2;
            case 2:
                return (float)0.3;
            case 3:
                return (float)0.4;
            case 4:
                return (float)0.5;
            case 5:
                return (float)0.6;
            case 6:
                return (float)0.7;
            case 7:
                return (float)0.8;
            case 8:
                return (float)0.9;
            case 9:
                return (float)1.0;
            default:
                return (float)0.2;
        }
    }
}
