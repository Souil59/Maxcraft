package fr.maxcraft.server.game;

import fr.maxcraft.Main;
import fr.maxcraft.server.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.UUID;

/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class GameCommand extends Command {

    public GameCommand(String game) {
        super(game);
        this.setPerms("maxcraft.modo").register();
    }

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] args){
        if (args[0].equals("new")) {
            Villager e = (Villager) ((Player)arg0).getWorld().spawnEntity(((Player)arg0).getLocation(), EntityType.VILLAGER);
            StartSign s = new StartSign(e.getUniqueId(), Integer.parseInt(args[1]), Boolean.getBoolean(args[2]), args[3], args[4], args[5], Integer.parseInt(args[6]), Integer.parseInt(args[7]));
            s.save();
        }
        return true;
    }
}
