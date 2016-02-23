package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * Created by admin on 21/02/16.
 */
public class RepairCommand extends Command {

    public RepairCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Erreur dans la recherche du joueur...");
            return true;
        }
        ItemStack item = u.getPlayer().getItemInHand();
        if (item == null || item.getType().isBlock() || item.getDurability()==0){
            u.sendMessage(Things.message()+"Impossible de réparer cela !");
            return true;
        }
        if (item.getType().getMaxDurability() == item.getDurability()) return true;
        item.setDurability(item.getType().getMaxDurability());
        u.sendMessage(Things.message()+"Item réparé !");
        return true;
    }
}
