package fr.maxcraft.server.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.maxcraft.Main;
import org.bukkit.command.TabCompleter;


public abstract class Command extends org.bukkit.command.Command{

    private HashMap<String,List<String>> completer = new HashMap<String,List<String>>();

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

    public void tabComplete(String lastargs, List<String> als){
        this.completer.put(lastargs,als);
    }
	@Override
	public abstract boolean execute(CommandSender arg0, String arg1, String[] arg2);

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length==1)
            return this.completer.get(this.getName());
        if (this.completer.containsKey(args[args.length-1]))
            return this.completer.get(args[args.length-1]);
        return super.tabComplete(sender,alias,args);
    }
}
