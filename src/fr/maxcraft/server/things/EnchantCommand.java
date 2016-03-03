package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by admin on 16/02/16.
 */
public class EnchantCommand extends Command {

    public EnchantCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.builder")) return false;
        if (args.length != 1 && args.length != 2){
            sender.sendMessage(Things.message() + "Il manque des paramètres");
            return true;
        }
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Vous devez être un joueur pour soumettre cette commande !");
            return true;
        }

        if (u.getPlayer().getItemInHand() == null || u.getPlayer().getItemInHand().getType().equals(Material.AIR)){
            sender.sendMessage(ChatColor.RED+"Vous devez avoir un item enchantable dans la main !");
        }

        int level =1;
        Enchantment enchant;
        try{
            enchant = Enchantment.getByName(args[0].toUpperCase());
        }
        catch (ClassCastException e){
            u.sendMessage(ChatColor.RED+"Erreur dans le nom de l'enchantement !");
            return false;
        }

        if (enchant == null){
            u.sendMessage(ChatColor.RED+"Erreur dans le nom de l'enchantement ! (null)");
            return false;
        }

        if (args.length>1){

            try{
                level = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e2){
                level = 1;
                sender.sendMessage(ChatColor.RED+"Erreur dans le niveau d'enchantement soumis !");
                return false;
            }
        }

        ItemStack item = u.getPlayer().getItemInHand();
        if (level<=0 && item.getEnchantments().containsKey(enchant)){
            u.getPlayer().getItemInHand().getItemMeta().removeEnchant(enchant);
            u.getPlayer().updateInventory();
            u.sendMessage(Things.message()+"Enchantement supprimé !");
            return true;
        }
        else if(level > 0){
            //item.getItemMeta().addEnchant(enchant, level, true);
            try{
                item.getItemMeta().addEnchant(enchant, level, true);
            }
            catch (IllegalArgumentException e){
                sender.sendMessage(ChatColor.RED+"Cet enchantement ne peut pas s'appliquer à cet item !");
                return false;
            }
            u.sendMessage(Things.message()+"Enchantement ajouté !");
            u.getPlayer().updateInventory();
            return true;
        }
        u.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");
        return false;
    }
}
