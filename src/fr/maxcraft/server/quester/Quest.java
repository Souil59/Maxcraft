package fr.maxcraft.server.quester;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public abstract class Quest {

    private final Quester quester;
    public int asktimer = 0;


    public Quest(Quester q){
        this.quester = q;
    }

    protected abstract int rightclic(User u, int i);
    protected abstract int leftclic(User u, int i);
    protected abstract int answer(User u, int i, int answer);

    protected void chat(User u, String s){
        new Task(u,s,this).runTaskLater(Main.getPlugin(),asktimer);
        this.asktimer +=60;
    }

    protected void ask(User u, String s, int i, ChatColor color) {
        TextComponent message = new TextComponent("["+s+"]");
        message.setColor(color);
        message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/quest "+quester.getNpc().getUniqueId().toString()+" "+Integer.toString(i)));
        u.getPlayer().spigot().sendMessage(message);
    }

    public class Task extends BukkitRunnable{

        private final String message;
        private final User user;
        private final Quest quest;

        public Task(User u, String s,Quest quest) {
            this.user = u;
            this.message = s;
            this.quest = quest;
        }

        @Override
        public void run() {
            user.sendMessage(ChatColor.GREEN+this.quest.quester.getNpc().getName()+ChatColor.WHITE+" : "+message);
            quest.asktimer -=60;
        }
    }
}
