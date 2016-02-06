package fr.maxcraft.player.permissions;

import java.util.Arrays;

import fr.maxcraft.player.User;
import fr.maxcraft.utils.Serialize;
import org.bukkit.command.CommandSender;

import fr.maxcraft.server.command.Command;

public class PermsCommand extends Command {

	public PermsCommand(String name) {
		super(name);
		this.setAliases(Arrays.asList("group")).setPerms("maxcraft.modo").register();
        this.tabComplete("group",Arrays.asList("[player] set [group]"));
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] args) {
        User u = User.get(args[0]);
        if (u!=null&&args[1].equals("set")) {
            u.setPerms(new Perms(u.getUuid(), args[2], u.getPerms().getPerms()));
            u.getPlayer().setPlayerListName(u.getPerms().dysplayName());
            u.getPerms().save();
        }
		return true;
	}

}
