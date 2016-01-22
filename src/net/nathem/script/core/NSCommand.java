package net.nathem.script.core;

import fr.maxcraft.server.command.Command;
import net.nathem.script.editor.EditorCommand;
import org.bukkit.command.CommandSender;

/**
 * Created by Louis on 23/01/2016.
 */
public class NSCommand extends Command {
    public NSCommand(String nse) {
        super(nse);
        this.register();
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        new EditorCommand(NSCore.Editor, sender, args);
        return true;
    }
}
