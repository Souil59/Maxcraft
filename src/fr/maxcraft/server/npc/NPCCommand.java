package fr.maxcraft.server.npc;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import fr.maxcraft.server.quester.Quester;
import fr.maxcraft.server.warzone.NPCFarmer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Crevebedaine on 11/02/2016.
 */
public class NPCCommand extends Command{

    public static HashMap<Player,Entity> selected = new HashMap<Player,Entity>();

    public NPCCommand(String npc) {
        super(npc);
        this.setPerms("maxcraft.modo").register();
        this.tabComplete(npc, Arrays.asList("new"));
    }

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] args) {
        if (args[0]==null)
            return false;
        if (args[0].equals("new")){
            EntityNPC e = EntityNPC.spawn(((Player) arg0).getLocation());
            selected.put((Player) arg0,e.getBukkitEntity());
            if (args[1]!=null)
                e.setCustomName(args[1]);
        }
        if (args[0].equals("tp")){
            if (selected.containsKey(arg0)){
                selected.get(arg0).teleport((Entity) arg0);
                return true;
            }
            arg0.sendMessage("Aucun NPC selectionné.");
        }
        if (args[0].equals("quest")){
            if (args[1]==null)
                return false;
            try {
                new Quester((Entity) selected.get(arg0),"fr.maxcraft.server.quester.quest."+args[1],new ArrayList<User>(),new ArrayList<String>()).insert();
            } catch (Exception e) {
                arg0.sendMessage(e.getCause().getMessage());
                arg0.sendMessage("Quete non trouvée.");
                return false;
            }
        }
        if (args[0].equals("warzone")){
            if (args[1]==null)
                return false;
            if (((Player)arg0).getItemInHand()==null)
                return false;
            if (!selected.containsKey(arg0))
                return false;
            new NPCFarmer(selected.get(arg0).getUniqueId(),((Player)arg0).getItemInHand().clone(),Integer.valueOf(args[1]),0).insert();
        }
        return true;
    }
}
