package fr.maxcraft.player.faction;

import fr.maxcraft.player.User;
import fr.maxcraft.utils.Serialize;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Louis on 22/01/2016.
 */
public class FactionRelations extends BukkitRunnable{

    private String ennemies;
    private String allies;
    private Faction faction;

    public FactionRelations(Faction f, String ennemies, String allies){
        this.faction = f;
        this.allies = allies;
        this.ennemies = ennemies;

    }

    @Override
    public void run() {
        faction.setAllies(Serialize.factionsFromString(allies));
        faction.setEnnemies(Serialize.factionsFromString(ennemies));
    }
}
