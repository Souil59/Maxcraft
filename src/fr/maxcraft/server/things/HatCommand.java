package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;


public class HatCommand extends Command {

    public HatCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (args.length !=0){
            sender.sendMessage(ChatColor.RED+"Il y a trop de paramètres !");
            return true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        ItemStack head = u.getPlayer().getInventory().getHelmet();
        ItemStack hat = u.getPlayer().getItemInHand();
        u.getPlayer().getInventory().setHelmet(hat);
        u.getPlayer().setItemInHand(head);u.sendNotifMessage(Things.message()+"Chapeau changé !");
        return true;
    }
}
