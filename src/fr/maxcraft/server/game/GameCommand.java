package fr.maxcraft.server.game;

import fr.maxcraft.server.command.Command;
import fr.maxcraft.server.npc.EntityNPC;
import fr.maxcraft.server.npc.customentities.Franky;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class GameCommand extends Command {

    public GameCommand(String game) {
        super(game);
        this.setPerms("maxcraft.modo").register();
        this.tabComplete(game, Arrays.asList("test","new [nbrInstance] [nom] [nomMap] [nbrJoueurs] [nbrVie]"));
    }

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] args){
        if (args[0].equals("test"))
            Franky.spawn(((Player)arg0));
        if (args[0].equals("new")) {
            Entity nmsEntity = EntityNPC.spawn(((Player)arg0).getLocation());
            StartSign s = new StartSign(nmsEntity.getBukkitEntity().getUniqueId(), Integer.parseInt(args[1]),false, args[2], args[3], args[4], Integer.parseInt(args[5]), Integer.parseInt(args[6]));
            s.insert();
        }
        return true;
    }
}
