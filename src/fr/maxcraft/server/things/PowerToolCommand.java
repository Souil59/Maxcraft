package fr.maxcraft.server.things;


import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class PowerToolCommand extends Command {

    public PowerToolCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").setAliases(Arrays.asList("powtool", "ptool", "ptz")).register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args[0].equals("rem")){
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du joueur !");
                return true;
            }
            if (u.getPowertool().containsKey(u.getPlayer().getItemInHand().getType())){
                u.getPowertool().remove(u.getPlayer().getItemInHand().getType());
                return true;
            }
            else {
                sender.sendMessage(ChatColor.GRAY+"Cet item n'a pas de powerTool actif !");
                return true;
            }
        }
        else if (args.length>0){
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du joueur !");
                return true;
            }
            String argus = argsToString(args);
            if (u.getPowertool().containsKey(u.getPlayer().getItemInHand().getType())) u.getPowertool().remove(u.getPlayer().getItemInHand().getType());
            u.getPowertool().put(u.getPlayer().getItemInHand().getType(), argus);
            u.sendMessage(Things.message()+"Powertool prêt !");
            return true;
        }
        else {
            User u = User.get(sender.getName());
            if (u==null){
                sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du joueur !");
                return true;
            }
            if (u.getPowertool().containsKey(u.getPlayer().getItemInHand().getType())){
                String cmmd = u.getPowertool().get(u.getPlayer().getItemInHand().getType());
                u.sendMessage(Things.message()+"Cet item possède des propriétés de powertool pour : "+cmmd);
                return true;
            }
            else{
                u.sendMessage(Things.message()+"Pas de propriétés de powertool sur cet item !");
                return true;
            }
        }

    }

    private String argsToString(String[] args) {
        String s = "";
        for (String sc : args)
            s+=sc+" ";
        return s;
    }
}
