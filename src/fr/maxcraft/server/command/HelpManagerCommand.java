package fr.maxcraft.server.command;

import org.bukkit.command.CommandSender;

/**
 * Created by Crevebedaine on 07/02/2016.
 */
public class HelpManagerCommand extends Command{
    public HelpManagerCommand(String help) {
        super(help);
        this.register();
    }

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
        HelpManager.onCommand(arg0,this,arg1,arg2);
        return true;
    }
}
