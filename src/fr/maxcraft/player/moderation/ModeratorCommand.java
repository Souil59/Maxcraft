package fr.maxcraft.player.moderation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.command.HelpManager;
import fr.maxcraft.utils.DurationParser;

public class ModeratorCommand implements CommandExecutor {

	public ModeratorCommand() {
		new HelpManager("Moderation").setClickText("/help moderation").setDesc("Liste des commandes de moderation.")
		.setUsage("Cliquez ici pour acceder aux commandes de moderations.").setPerm("maxcraft.guide");
		Main m = Main.getPlugin();
		m.getCommand("mute").setExecutor(this);
		new HelpManager("mute").setDesc("Empeche un joueur de parler.").setUsage("/mute <pseudo> [durée] [raison]\n/mute Crevebedaine 30s insultes").setPack("moderation");
		m.getCommand("ban").setExecutor(this);
		new HelpManager("ban").setDesc("Banni un joueur du serveur.").setUsage("/ban <pseudo> [durée] [raison]\n/ban Crevebedaine 2m x-ray").setPack("moderation");
		m.getCommand("jail").setExecutor(this);
		new HelpManager("jail").setDesc("Emprisone un joueur.").setUsage("/jail <pseudo> [durée] [raison]\n/jail Crevebedaine 1d vandalisme").setPack("moderation");
		m.getCommand("kick").setExecutor(this);
		new HelpManager("kick").setDesc("Kick un joueur.").setUsage("/kick <pseudo> [raison]\n/kick Crevebedaine spam").setPack("moderation");
		m.getCommand("invsee").setExecutor(this);
		new HelpManager("invsee").setDesc("Affiche l'inventaire d'un joueur").setUsage("/invsee <pseudo>\n/invsee Crevebedaine").setPack("moderation");
		m.getCommand("enderchest").setExecutor(this);
		new HelpManager("enderchest").setDesc("Affiche l'inventaire d'un joueur").setUsage("/invsee <pseudo>\n/invsee Crevebedaine").setPack("moderation");
		m.getCommand("journal").setExecutor(this);
		new HelpManager("journal").setDesc("Affiche les dernieres actions de moderations.").setUsage("/journal [pseudo]\n/journal Crevebedaine").setPack("moderation");
		m.getCommand("fine").setExecutor(this);
		new HelpManager("fine").setDesc("Donne un amende à un joueur.").setUsage("/fine <pseudo> <montant> [raison]\n/fine Crevebedaine 50 arnaque").setPack("moderation");
		
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
		// TODO Auto-generated method stub
		
	}

	private boolean ec(CommandSender sender, String[] args) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean invsee(CommandSender sender, String[] args) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean jail(CommandSender sender, String[] args) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean fine(CommandSender sender, String[] args) {
		return false;
		// TODO Auto-generated method stub
		
	}

	private boolean kick(CommandSender sender, String[] args) {
		return false;
		// TODO Auto-generated method stub
		
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
