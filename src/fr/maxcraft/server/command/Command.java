package fr.maxcraft.server.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.maxcraft.Main;


public abstract class Command extends org.bukkit.command.Command{

	protected Command(String name) {
		super(name);
		super.setName(name);
	}

	public void register() {
		Main.getCmap().register(Main.getPlugin().getDescription().getName(), this);
		Main.log("Command "+this.getName()+" registered");
	}
	public Command setAliases(List<String> s) {
		super.setAliases(s);
		return this;
	}
	public Command setPerms(String s) {
		super.setPermission(s);
		return this;
	}
	@Override
	abstract public boolean execute(CommandSender arg0, String arg1, String[] arg2);
}
