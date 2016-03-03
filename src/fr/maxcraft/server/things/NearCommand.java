package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class NearCommand extends Command {

    public NearCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.builder")) return false;
        if (args.length != 0 && args.length!=1){
            sender.sendMessage(ChatColor.RED+"Il y a trop de paramètres !");
            return true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Erreur dans la position de référence !");
            return true;
        }
        int maxDistance = 200;
        ArrayList<Player> pList = new ArrayList<>();
        if (args.length==1){
            if (args[0].equals("map")) maxDistance = -1;
            else {
                try {
                    maxDistance = Integer.parseInt(args[0]);
                } catch (ClassCastException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "Erreur dans la conversion de la distance maximale, /near <rayon/\"map\">");
                    return false;
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.equals(u.getPlayer())) continue;
            if (!p.getWorld().equals(u.getPlayer().getWorld())) continue;
            if (p.getLocation().distance(u.getPlayer().getLocation())>maxDistance && maxDistance!=-1) continue;
            pList.add(p);
        }
        this.sendPlayerList(u, pList);
        return true;
    }

    private void sendPlayerList(User u, ArrayList<Player> pList){
        if (pList.isEmpty()) u.sendMessage(ChatColor.GRAY+"Aucun joueur dans le rayon spécifié !");
        else {
            String list = "";
            for (Player p : pList){
                list = list +p.getName()+" ";
            }
            u.sendMessage(Things.message()+"Joueurs aux alentours:");
            u.sendMessage(Things.message()+list);
        }

    }

}
