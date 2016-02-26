package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.player.permissions.Group;
import fr.maxcraft.player.permissions.Perms;
import fr.maxcraft.player.permissions.groups.Citoyen;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class JoueurCommand extends Command{

    public JoueurCommand(String name) {
        super(name);
        this.setPerms("maxcraft.guide").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length != 1){
            sender.sendMessage(ChatColor.RED+"Vous devez donner un argument : \"on\" ou \"off\" ");
            return false;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du joueur !");
            return false;
        }
        switch (args[0]){
            case "on":
                this.setJoueurStateOn(u);
            case "off":
                this.setJoueurStateOff(u);
        }
        return false;
    }

    public boolean setJoueurStateOn(User u){
        if (u.isGod() || u.isSocialspy() || u.getPlayer().isFlying() || u.getPlayer().getWalkSpeed()!=1){
            u.sendMessage(Things.message()+ChatColor.RED+"Impossible de passer en joueur : le mode dieu ou socialspy est activé ou vous êtes en train de voler ou votre vitesse de marche n'est pas égales à 1 !");
            return false;
        }
        Group usualGroup = u.getPerms().getGroup();
        u.getPerms().setGroup(new Citoyen());
        Citoyen group  = (Citoyen) u.getPerms().getGroup();
        group.setIsStaff(true);
        group.setUsualGroup(usualGroup);
        u.sendMessage(Things.message()+"Vous êtes passé en 'mode joueur' !");
        return true;
    }

    public boolean setJoueurStateOff(User u){
        Citoyen group;
        try{
            group = (Citoyen) u.getPerms().getGroup();
        }
        catch (ClassCastException e){
            u.sendMessage(Things.message()+"Vous devez être en 'mode joueur' pour utiliser cette commande !");
            return false;
        }
        if (!group.isStaff()){
            u.sendMessage(Things.message()+"Vous devez être un membre du staff pour exécuter cette commande !");
            return false;
        }
        Group usualGroup = group.getUsualGroup();
        u.getPerms().setGroup(usualGroup);
        u.sendMessage(Things.message()+"Vous êtes repassé en 'mode staff' !");
        return true;
    }
}
