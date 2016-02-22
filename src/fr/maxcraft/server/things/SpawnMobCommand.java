package fr.maxcraft.server.things;


import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.Set;

public class SpawnMobCommand extends Command{

    public SpawnMobCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length != 1 && args.length!= 2){
            sender.sendMessage(ChatColor.RED+"Le nombre de paramètres est incorrect !");
            return true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        Location l = u.getPlayer().getTargetBlock((Set<Material>) null, 100).getLocation();
        int nb=1;
        if (args.length>1){
            try {
                nb = Integer.parseInt(args[1]);
            }
            catch (ClassCastException e){
                sender.sendMessage(ChatColor.RED+"Erreur dans la conversion du nombre d'entités à créer !");
                return true;
            }
        }
        if (nb > 10) nb=10;
        u.sendMessage(Things.message()+"Nombre d'entités réduit à 10");
        EntityType et = null;
        try {
            et = EntityType.fromName(args[0]);
        }
        catch (ClassCastException e){
            u.sendMessage(ChatColor.RED+"Entité introuvable !");
            return false;
        }
        for (int t =0; t<=nb; t++){
            u.getPlayer().getWorld().spawnEntity(l, et);
        }
        return true;
    }
}
