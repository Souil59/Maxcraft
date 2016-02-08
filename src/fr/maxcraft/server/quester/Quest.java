package fr.maxcraft.server.quester;

import fr.maxcraft.player.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

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

    protected void Chat(User u, String s){
        u.sendMessage(ChatColor.GREEN+this.quester.getNpc().getName()+" "+ ChatColor.WHITE+s);
    }

    protected void Ask(User u, String s, int i, ChatColor color) {
        TextComponent message = new TextComponent("["+s+"]");
        message.setColor(color);
        message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/quest "+quester.getNpc().getUniqueID()+" "+Integer.toString(i)));
    }
}