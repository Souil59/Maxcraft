package fr.maxcraft.server.things.Tasks;

import fr.maxcraft.Main;
import fr.maxcraft.server.things.Things;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by admin on 20/02/16.
 */
public class AutoKillTask extends BukkitRunnable{

    private Player p;

    public AutoKillTask(Main plugin, Player p){
        this.p = p;
    }

    @Override
    public void run() {
        this.p.setHealth((double)0);
        p.sendMessage(Things.message()+"Vous vous êtes suicidé...");
    }
}
