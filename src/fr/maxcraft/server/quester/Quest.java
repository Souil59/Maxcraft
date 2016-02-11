package fr.maxcraft.server.quester;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public abstract class Quest {

    private final Quester quester;


    public Quest(Quester q){
        this.quester = q;
    }

    protected abstract int rightclic(User u, int i);
    protected abstract int leftclic(User u, int i);
    protected abstract int answer(User u, int i, int answer);

    protected void chat(User u, String s){
        u.sendMessage(ChatColor.GREEN+this.quester.getNpc().getName()+ChatColor.WHITE+" : "+s);
    }

    protected void ask(User u, String s, int i, ChatColor color) {
        TextComponent message = new TextComponent("["+s+"]");
        message.setColor(color);
        message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/quest "+quester.getNpc().getUniqueId().toString()+" "+Integer.toString(i)));
        u.getPlayer().spigot().sendMessage(message);
    }
}
