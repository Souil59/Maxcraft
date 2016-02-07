package fr.maxcraft.player.moderation;

import org.bukkit.ChatColor;
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
			case "fine":  //amende
				return this.fine(sender,args);
			case "jail":
				return this.jail(sender,args);
		    case "invsee":
		    	return this.invsee(sender,args);
		    case "ec":
		    	return this.ec(sender,args);
		    case "journal":
		    	return this.journal(sender,args);
            case "pardon":
                return this.unban(sender, args);
		}
		return true;
	}

    private boolean unban(CommandSender sender, String[] args){
        return false;
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

		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage(ChatColor.RED+"Ce joueur n'existe pas !");
			return true;
		}
        if (args[1] == null){
            sender.sendMessage("Vous devez indiquer une raison");
            return true;}
		if (args.length>2){
			long d = DurationParser.translateTimeStringToDate(args[2]);
			sender.sendMessage(Moderation.message() + args[0] + " est desormais muet");
			j.sendNotifMessage(ChatColor.RED + "Vous avez été ban " + DurationParser.translateToString(args[2]) + " pour :" + args[1]);
			j.getModeration().setBan(true, d);
			Journal.add(sender.getName(), "ban", j.getUuid(), DurationParser.translateToString(args[2]), args[1]);
			j.getPlayer().kickPlayer(args[1]);
			return true;
		}
		if (!j.getModeration().isBan()){
			j.getModeration().setBan(true, -1);
			sender.sendMessage(Moderation.message() + args[0] + " est desormais banni pour" + args[1]);
			j.sendNotifMessage(ChatColor.GOLD + "Vous avez été banni pour" + args[1]);
			Journal.add(sender.getName(), "ban", j.getUuid(), "", args[1]);
            j.getPlayer().kickPlayer(args[1]);
			return true;
		}
		return false;
		
	}

	private boolean mute(CommandSender sender, String[] args) {
		args = args.toString().split(" ", 3);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage("Ce joueur n'existe pas !");
			return true;
		}
		if (args.length>2){
			long d = DurationParser.translateTimeStringToDate(args[1]);
			sender.sendMessage(args[0]+" est desormais mu�");
			j.sendNotifMessage("Vous avez �t� mute "+DurationParser.translateToString(args[1])+" pour :"+args[2]);
			j.getModeration().setMute(true, d);
			Journal.add(sender.getName(),"mute",j.getUuid(),DurationParser.translateToString(args[1]),args[2]);
			return true;
		}
		if (!j.getModeration().isMute()){
			j.getModeration().setMute(true, -1);
			sender.sendMessage(args[0]+" est desormais mu�");
			j.sendNotifMessage("Vous avez �t� mute.");
			Journal.add(sender.getName(),"mute",j.getUuid(),"","");
			return true;
		}
		if (j.getModeration().isMute()){
			j.getModeration().setMute(false, -1);
			sender.sendMessage(args[0]+" n'est plus mu�");
			j.sendNotifMessage("Vous n'est plus mute.");
			Journal.add(sender.getName(),"demute",j.getUuid(),"","");
			return true;
		}
		return false;
	}

}
