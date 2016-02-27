package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


public class ExpCommand extends Command {

    public ExpCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").setAliases(Arrays.asList("xp", "exp")).register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length !=2 && args.length != 3){
            sender.sendMessage(Things.message()+"Il manque des paramètres !");
            return true;
        }
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur non trouvé !");
            return true;
        }

        if (args.length < 3) this.onFalseLvl(sender, args, u);
        else if (args.length == 3){
            switch (args[2]){
                case "true":
                    this.onTrueLvl(sender, args, u);
                    break;
                case "false":
                    this.onFalseLvl(sender, args, u);
                    break;
            }
        }
        else{
            sender.sendMessage(ChatColor.RED + "Erreur dans la soumission de la commande !");
            return false;
        }
        return true;
    }

    public boolean onTrueLvl(CommandSender sender, String[] args, User u){
        int lvl;

        try {
            lvl = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Erreur dans la somme d'xp/niveaux soumise");
            return false;
        }

        int xpLvl = u.getPlayer().getLevel() + lvl;
        u.getPlayer().setLevel(xpLvl);
        u.sendMessage(ChatColor.GRAY + "Vous avez maintenant " + xpLvl + " niveaux d'expérience !");
        sender.sendMessage(Things.message()+u.getName()+" a désormais "+ xpLvl +" niveaux d'expérience !");
        return true;
    }

    public boolean onFalseLvl(CommandSender sender, String[] args, User u){
        int xp = 1;
        if (args.length==2) {
            try {
                xp = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + " Erreur dans la somme d'xp/niveaux soumise !");
                return false;
            }
        }
        u.getPlayer().giveExp(xp);
        u.sendMessage(ChatColor.GRAY + "Vous avez reçu " + xp + " points d'expérience supplémentaires !");
        sender.sendMessage(Things.message()+u.getName()+" a reçu " + xp + " points d'expérience supplémentaires !");
        return true;
    }
}
