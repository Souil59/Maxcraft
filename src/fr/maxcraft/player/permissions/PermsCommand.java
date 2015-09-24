package fr.maxcraft.player.permissions;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import fr.maxcraft.server.command.Command;

public class PermsCommand extends Command {

	public PermsCommand(String name) {
		super(name);
		this.setAliases(Arrays.asList("group")).setPerms("maxcraft.modo").register();
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		arg0.sendMessage(arg1+arg2);
		return true;
	}

}
