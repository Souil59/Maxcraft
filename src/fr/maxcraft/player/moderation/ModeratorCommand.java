package fr.maxcraft.player.moderation;

import fr.maxcraft.server.chatmanager.AdminChat;
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
			case "mute":   //done
				return this.mute(sender, args);
			case "ban":    //done
				return this.ban(sender, args);
			case "kick":
				return this.kick(sender, args);
			case "fine":  //amende
				return this.fine(sender, args);
			case "jail":
				return this.jail(sender, args);
		    case "invsee":
		    	return this.invsee(sender, args);
		    case "ec":
		    	return this.ec(sender, args);
		    case "journal":
		    	return this.journal(sender, args);
            case "pardon":
                return this.unban(sender, args);
            case "bantemp":
                return this.bantemp(sender, args);
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
		args = args.toString().split(" ", 2);
		User u = User.get(args[0]);
		if (u ==  null){
			sender.sendMessage(ChatColor.DARK_RED+"Erreur :"+ChatColor.RED+ " Joueur introuvable !");
			return true;
		}
        if (u.getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED + "Vous ne pouvez pas kick ce joueur !");
            return true;
        }
        if (u.getPlayer().isOnline()){
            if (args[1].isEmpty()){
                u.getPlayer().kickPlayer(ChatColor.RED+ "Ejecté du Serveur");
                AdminChat.sendMessageToStaffs(Moderation.message() + args[1] + " a été kick du serveur " + ChatColor.ITALIC + "sans raison !");
                Journal.add(sender.getName(), "kick", u.getUuid(), "", "pas de raison");
            }
        }
		return false;
		
	}

	private boolean ban(CommandSender sender, String[] args) {
        args = args.toString().split(" ", 2);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage(ChatColor.RED + "Ce joueur n'existe pas !");
			return true;
		}
        if (args[1] == null){
            sender.sendMessage("Vous devez indiquer une raison");
            return true;
        }
		if (!j.getModeration().isBan()){
			j.getModeration().setBan(true, -1);
			AdminChat.sendMessageToStaffs(Moderation.message() + ChatColor.RED + args[0] + ChatColor.GOLD + " est desormais banni pour" + ChatColor.ITALIC + args[1]);
			j.sendNotifMessage(ChatColor.GOLD + "Vous avez été banni pour" + args[1]);
			Journal.add(sender.getName(), "ban", j.getUuid(), "definitif", args[1]);
            j.getPlayer().kickPlayer(args[1]);
			return true;
		}
		return false;
		
	}

	private boolean mute(CommandSender sender, String[] args) {
        args = args.toString().split(" ", 3);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage(ChatColor.RED + "Joueur non trouvé !");
			return true;
		}
        if (j.getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: "+ChatColor.RED+"Vous en pouvez pas rendre ce joueur muet !");
            return true;
        }
		if (!j.getModeration().isMute() && !args[1].isEmpty()){
			long d = DurationParser.translateTimeStringToDate(args[1]);
            j.getModeration().setMute(true, d);
            if (args[2].isEmpty()){
                j.sendNotifMessage(ChatColor.GOLD + "Vous avez été mute "+DurationParser.translateToString(args[1]));
                Journal.add(sender.getName(), "mute", j.getUuid(), DurationParser.translateToString(args[1]), "Pas de raison");
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais muet"+DurationParser.translateToString(args[1]));
            }
			else{
                j.sendNotifMessage(ChatColor.GOLD+"Vous avez été mute "+DurationParser.translateToString(args[1])+" pour :"+args[2]);
                Journal.add(sender.getName(), "mute", j.getUuid(), DurationParser.translateToString(args[1]), args[2]);
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais muet" + DurationParser.translateToString(args[1]+" pour :"+args[2]));
            }
			return true;
		}
		if (!j.getModeration().isMute() && args[1].isEmpty()){
			j.getModeration().setMute(true, -1);
			if (args[2].isEmpty()){
				j.sendNotifMessage(ChatColor.GOLD+"Vous avez été mute !");
				Journal.add(sender.getName(), "mute", j.getUuid(), "Pas de durée", "Pas de raison");
				AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais muet");
			}
			else{
				j.sendNotifMessage(ChatColor.GOLD+"Vous avez été mute pour :"+args[2]);
				Journal.add(sender.getName(), "mute", j.getUuid(), "Pas de durée", args[2]);
				AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais muet pour :"+args[2]);
			}
			return true;
		}
		if (j.getModeration().isMute()){
			j.getModeration().setMute(false, -1);
			AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " n'est plus muet !");
			j.sendNotifMessage(ChatColor.GREEN+"Vous n'êtes plus muet.");
			Journal.add(sender.getName(), "demute", j.getUuid(), "", "");
			return true;
		}
		return false;
	}

    private boolean bantemp(CommandSender sender, String[] args){
        return false;
    }



}
