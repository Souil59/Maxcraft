package fr.maxcraft.player.moderation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.maxcraft.player.User;
import fr.maxcraft.utils.DurationParser;

public class ModeratorCommand implements CommandExecutor {

	public ModeratorCommand() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		switch(cmd.getName())
		{
			case "mute":
				return this.mute(sender,args);
			case "ban":
				return this.ban(sender,args);
			case "kick":
				return this.kick(sender,args);
			case "fine":
				return this.fine(sender,args);
			case "jail":
				return this.jail(sender,args);
		    case "invsee":
		    	return this.invsee(sender,args);
		    case "ec":
		    	return this.ec(sender,args);
		    case "journal":
		    	return this.journal(sender,args);
		}
		return true;
	}

	private boolean journal(CommandSender sender, String[] args) {
		return false;
		
	}

	private boolean ec(CommandSender sender, String[] args) {
		return false;
		
	}

	private boolean invsee(CommandSender sender, String[] args) {
		return false;
		
	}

	private boolean jail(CommandSender sender, String[] args) {
		return false;
		
	}

	private boolean fine(CommandSender sender, String[] args) {
		return false;
		
	}

	private boolean kick(CommandSender sender, String[] args) {
		return false;
		
	}

	private boolean ban(CommandSender sender, String[] args) {
		args = args.toString().split(" ", 3);
		sender.sendMessage(args[0]);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage("Ce joueur n'existe pas ou est inactif");
			return true;
		}
		if (args.length>2){
			long d = DurationParser.translateTimeStringToDate(args[1]);
			sender.sendMessage(args[0]+" est desormais mué");
			j.sendNotifMessage("Vous avez été ban "+DurationParser.translateToString(args[1])+" pour :"+args[2]);
			j.getModeration().setBan(true, d);
			Journal.add(sender.getName(),"ban",j.getUuid(),DurationParser.translateToString(args[1]),args[2]);
			return true;
		}
		if (!j.getModeration().isBan()){
			j.getModeration().setBan(true, -1);
			sender.sendMessage(args[0]+" est desormais mué");
			j.sendNotifMessage("Vous avez été ban.");
			Journal.add(sender.getName(),"ban",j.getUuid(),"","");
			return true;
		}
		if (j.getModeration().isBan()){
			j.getModeration().setBan(false, -1);
			sender.sendMessage(args[0]+" n'est plus mué");
			j.sendNotifMessage("Vous n'est plus ban.");
			Journal.add(sender.getName(),"deban",j.getUuid(),"","");
			return true;
		}
		return false;
		
	}

	private boolean mute(CommandSender sender, String[] args) {
		args = args.toString().split(" ", 3);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage("Ce joueur n'existe pas ou est inactif");
			return true;
		}
		if (args.length>2){
			long d = DurationParser.translateTimeStringToDate(args[1]);
			sender.sendMessage(args[0]+" est desormais mué");
			j.sendNotifMessage("Vous avez été mute "+DurationParser.translateToString(args[1])+" pour :"+args[2]);
			j.getModeration().setMute(true, d);
			Journal.add(sender.getName(),"mute",j.getUuid(),DurationParser.translateToString(args[1]),args[2]);
			return true;
		}
		if (!j.getModeration().isMute()){
			j.getModeration().setMute(true, -1);
			sender.sendMessage(args[0]+" est desormais mué");
			j.sendNotifMessage("Vous avez été mute.");
			Journal.add(sender.getName(),"mute",j.getUuid(),"","");
			return true;
		}
		if (j.getModeration().isMute()){
			j.getModeration().setMute(false, -1);
			sender.sendMessage(args[0]+" n'est plus mué");
			j.sendNotifMessage("Vous n'est plus mute.");
			Journal.add(sender.getName(),"demute",j.getUuid(),"","");
			return true;
		}
		return false;
	}

}
