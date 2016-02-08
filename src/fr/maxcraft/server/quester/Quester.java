package fr.maxcraft.server.quester;

import fr.maxcraft.player.User;
import fr.maxcraft.server.customentities.EntityNPC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Crevebedaine on 08/02/2016.
 */
public class Quester {

    private final EntityNPC npc;
    private final Quest quest;
    private HashMap<User,Integer> playerStats = new HashMap<User,Integer>();
    public static ArrayList<Quester> questers =new ArrayList<Quester>();

    public Quester(EntityNPC npc, Quest q, ArrayList<User> p, ArrayList<Integer> pi){
        this.npc = npc;
        this.quest = q;
        for (int i=0;i<p.size();i++)
            playerStats.put(p.get(i),pi.get(i));
        questers.add(this);
    }

    public EntityNPC getNpc(){
        return this.npc;
    }
}
