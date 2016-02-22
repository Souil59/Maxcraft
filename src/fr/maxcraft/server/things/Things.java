package fr.maxcraft.server.things;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by admin on 14/02/16.
 */
public class Things {

    public static String message() {
        return ChatColor.BLUE+"["+ChatColor.GRAY+"Things"+ChatColor.BLUE+"]"+ChatColor.GRAY+" ";
    }

    public static String socialspyMessage(Player sender, Player p, String message){
        return ChatColor.GRAY+"["+ChatColor.GOLD+"Socialspy"+ChatColor.GRAY+"]"+ " "+sender.getName()+" --> "+p.getName()+" : "+message;
    }

}
