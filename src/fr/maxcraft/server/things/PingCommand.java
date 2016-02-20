package fr.maxcraft.server.things;

import fr.maxcraft.server.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by admin on 20/02/16.
 */
public class PingCommand extends Command {

    public PingCommand(String name) {
        super(name);
        this.register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        sender.sendMessage(Things.message()+"pong !");
        return true;
    }
}
