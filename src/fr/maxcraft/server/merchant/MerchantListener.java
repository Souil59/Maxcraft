package fr.maxcraft.server.merchant;
import org.bukkit.command.CommandSender;

import fr.maxcraft.server.command.Command;

public class MerchantListener extends Command{

	public MerchantListener(String name) {
		super(name);
		//this.setAliases("alias");
		this.setPerms("maxcraft.guide");
		this.register();
	}
	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return true;
	}
}