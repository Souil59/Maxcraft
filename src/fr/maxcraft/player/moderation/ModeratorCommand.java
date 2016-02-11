package fr.maxcraft.player.moderation;

import fr.maxcraft.server.chatmanager.AdminChat;
import fr.maxcraft.server.marker.Marker;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.maxcraft.player.User;
import fr.maxcraft.utils.DurationParser;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;

public class ModeratorCommand extends fr.maxcraft.server.command.Command {

	public ModeratorCommand(String s) {
        super(s);
        if (s.equals("ban")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("bantemp")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("mute")){
            this.setPerms("maxcraft.guide").register();
        }
        if (s.equals("jail")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("kick")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("moderation")){
            this.setPerms("maxcraft.guide").setAliases(Arrays.asList("m")).register();
        }
	}

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
        if (!User.get(arg0.getName()).getPerms().hasPerms("maxcraft.guide")) return true;
        switch(this.getName())
        {
            case "mute":   //done
                return this.mute(arg0, arg2);
            case "ban":    //done
                return this.ban(arg0, arg2);
            case "kick":   //done
                return this.kick(arg0, arg2);
            case "fine":   // = amende //Done
                return this.fine(arg0, arg2);
            case "jail":   //done
                return this.jail(arg0, arg2);
            case "invsee": //done
                return this.invsee(arg0, arg2);
            case "ec":      //done
                return this.ec(arg0, arg2);
            case "journal":
                return this.journal(arg0, arg2);
            case "pardon":  //done
                return this.unban(arg0, arg2);
            case "bantemp": //done
                return this.bantemp(arg0, arg2);
            case "unjail": //done
                return this.unjail(arg0, arg2);
        }
        return true;
    }

    private boolean unjail(CommandSender sender, String[] args){
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcarft.modo")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous n'avez pas la permission de libérer ce joueur !");
            return true;
        }
        if(!u.getModeration().isJail()){
            sender.sendMessage(ChatColor.RED+"Le joueur que vous tentez de libérer est déjà libre... ou s'est déjà enfui !");
            return true;
        }
        u.getModeration().setJail(false, -1);
        u.getPlayer().teleport(Marker.getMarker("spawn"));
        AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " a été libéré !");
        u.sendNotifMessage(ChatColor.GREEN + "Vous avez été libéré ! Tenez-vous mieux la prochaine fois...");
        Journal.add(sender.getName(), "unjail", u.getUuid(), DurationParser.getCurrentTimeStampInString(), "Pas de raison");
        return true;
    }


    private boolean unban(CommandSender sender, String[] args){
        args = args.toString().split(" ", 1);
        User u = User.get(args[0]);
        if (u == null){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+" Vous n'avez pas le droit de faire ça !");
            return true;
        }
        if (!u.getModeration().isBan()){
            sender.sendMessage(ChatColor.RED+"La commande ne peut s'exécuter dans le contexte actuel !");
            return true;
        }
        else if (!args[0].isEmpty()){
            u.getModeration().setBan(false, -1);
            Journal.add(sender.getName(), "unban", u.getUuid(), "le : "+DurationParser.getCurrentTimeStampInString(), "");
            AdminChat.sendMessageToStaffs(Moderation.message() + ChatColor.BOLD + args[0] + " a été débanni.");
        }
        sender.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");
		return false;
    }

	private boolean journal(CommandSender sender, String[] args) {
		return false;
	}

	private boolean ec(CommandSender sender, String[] args) {
        User u = User.get(args[0]);
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+" Vous n'avez pas le droit de faire ça !");
            return true;
        }
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        if (args.length > 0 && u.getPlayer().isOnline()){
            Player senderP = sender.getServer().getPlayer(sender.getName());
            senderP.closeInventory();
            senderP.openInventory(u.getPlayer().getEnderChest());
            return true;
        }
        else if(!(args.length > 0)){
            Player senderP = sender.getServer().getPlayer(sender.getName());
            senderP.closeInventory();
            senderP.openInventory(senderP.getEnderChest());
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED+"Le joueur n'est pas en ligne!");
            return true;
        }
	}

	private boolean invsee(CommandSender sender, String[] args) {
        User u = User.get(args[0]);
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + " Vous n'avez pas le droit de faire ça !");
            return true;
        }
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return true;
        }
        if (args.length > 0 && u.getPlayer().isOnline()){
            Player senderP = (Player)sender;
            senderP.closeInventory();
            senderP.openInventory(u.getPlayer().getInventory());
            return true;
        }
        else {
            if (!(args.length>0)) sender.sendMessage(ChatColor.RED+"Vous n'avez pas spécifié le joueur cible !");
            if (!u.getPlayer().isOnline())sender.sendMessage(ChatColor.RED + "Le joueur n'est pas en ligne!");
            return true;
        }
	}

	private boolean jail(CommandSender sender, String[] args) {
        args = args.toString().split(" ", 3);
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcarft.modo") || u.getPerms().hasPerms("maxcraft.admin")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous ne pouvez pas emprisonner ce joueur !");
            return true;
        }

        if (!u.getModeration().isJail() && !args[1].isEmpty()){
            long d = DurationParser.translateTimeStringToDate(args[1]);
            u.getModeration().setJail(true, d);
            try{
                u.getPlayer().teleport(Marker.getMarker("jail"));

            }
            catch (NullPointerException e){
                sender.sendMessage(ChatColor.RED+"Le warp/marker \"jail\" n'a pas été trouvé !");
                return true;
            }
            u.getPlayer().setGameMode(GameMode.ADVENTURE);
            if (args[2].isEmpty()){
                u.sendNotifMessage(ChatColor.GOLD + "Vous avez été jail jusqu'au "+DurationParser.translateToString(args[1]));
                Journal.add(sender.getName(), "jail", u.getUuid(), DurationParser.translateToString(args[1]), "Pas de raison");
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais jail jusqu'au "+DurationParser.translateToString(args[1]));
            }
            else{
                u.sendNotifMessage(ChatColor.GOLD + "Vous avez été jail jusqu'au " + DurationParser.translateToString(args[1]) + " pour :" + args[2]);
                Journal.add(sender.getName(), "jail", u.getUuid(), DurationParser.translateToString(args[1]), args[2]);
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais jail jusqu'au " + DurationParser.translateToString(args[1] + " pour :" + args[2]));
            }
            return true;
        }
        if (!u.getModeration().isJail() && args[1].isEmpty()){
            u.getModeration().setJail(true, -1);
            try{
                u.getPlayer().teleport(Marker.getMarker("jail"));
            }
            catch (NullPointerException e) {
                sender.sendMessage(ChatColor.RED + "Le warp/marker \"jail\" n'a pas été trouvé !");
                return true;
            }
            u.getPlayer().setGameMode(GameMode.ADVENTURE);
            if (args[2].isEmpty()){
                u.sendNotifMessage(ChatColor.GOLD + "Vous avez été jail !");
                Journal.add(sender.getName(), "jail", u.getUuid(), "Pas de durée", "Pas de raison");
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais jail sans durée ni raison");
            }
            else{
                u.sendNotifMessage(ChatColor.GOLD + "Vous avez été jail pour :" + args[2]);
                Journal.add(sender.getName(), "jail", u.getUuid(), "Pas de durée", args[2]);
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " est desormais jail indéfiniement pour :"+args[2]);
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED+"Erreur dans l'exécution de la commande...");
        return false;
		
	}

	private boolean fine(CommandSender sender, String[] args) {
        args = args.toString().split(" ", 3);
        User u = User.get(args[0]);
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo") || u.getPerms().hasPerms("maxcraft.admin")){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED + "Vous ne pouvez pas donner d'amende à ce joueur !");
            return true;
        }
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur :"+ChatColor.RED+ " Joueur introuvable !");
            return true;
        }
        if (args[2].isEmpty()){
            sender.sendMessage(ChatColor.RED+"Vous devez spécifier la raison de l'amende !");
            return true;
        }
        Double somme, uBalance=u.getBalance();
        try{
            somme = Double.parseDouble(args[1]);
        }
        catch (ClassCastException e){
           sender.sendMessage(ChatColor.RED+"Erreur dans la saisie du montant à retirer !");
           return false;
        }
        uBalance-=somme;
        MySQLSaver.mysql_update("UPDATE `player` SET `balance` = " + uBalance + " WHERE `id` = '" + u.getUuid().toString() + "';");
        if (u.getPlayer().isOnline()) u.sendNotifMessage(ChatColor.RED + "Votre compte a été débité de " + somme + " suite à une amende pour " + ChatColor.ITALIC + args[2]);
        AdminChat.sendMessageToStaffs(Moderation.message()+args[0]+"a été débité de "+somme+" suite à une amende par "+sender.getName()+" pour "+ChatColor.ITALIC+args[2]);
        Journal.add(sender.getName(), "amende", u.getUuid(), "", args[2]);
        return true;
		
	}

	private boolean kick(CommandSender sender, String[] args) {
		args = args.toString().split(" ", 2);
		User u = User.get(args[0]);
        if (u ==  null){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur :"+ChatColor.RED+ " Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo") ||u.getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED + "Vous ne pouvez pas kick ce joueur !");
            return true;
        }
        if (u.getPlayer().isOnline()){
            if (args[1].isEmpty()){
                u.getPlayer().kickPlayer(ChatColor.RED+ "Ejecté du Serveur");
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " a été kick du serveur " + ChatColor.ITALIC + "sans raison !");
                Journal.add(sender.getName(), "kick", u.getUuid(), "", "pas de raison");
            }
            else{
                u.getPlayer().kickPlayer(ChatColor.RED+ args[1]);
                AdminChat.sendMessageToStaffs(Moderation.message() + args[0] + " a été kick du serveur pour: " + ChatColor.ITALIC + args[1]);
                Journal.add(sender.getName(), "kick", u.getUuid(), "", args[1]);
            }
        }
        sender.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");
		return false;
		
	}

	private boolean ban(CommandSender sender, String[] args) {
        args = args.toString().split(" ", 2);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED + "Ce joueur n'existe pas !");
			return true;
		}
        if (args[1] == null){
            sender.sendMessage(ChatColor.RED +"Vous devez indiquer une raison");
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
        else{
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+"Le joueur est déjà banni ou une erreur s'est produite dans l'exécution de la commande !");
            return true;
        }
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
        sender.sendMessage(ChatColor.RED+"Erreur dans l'exécution de la commande...");
		return false;
	}

    private boolean bantemp(CommandSender sender, String[] args){
		args = args.toString().split(" ", 3);
		User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+"Joueur introuvable !");
            return  true;
        }
        if (args[2] == null){
            sender.sendMessage(ChatColor.RED+"Vous devez indiquer une raison !");
            return true;
        }
        if (!u.getModeration().isBan() && !args[1].isEmpty()){
            long d = DurationParser.translateTimeStringToDate(args[1]);
            u.getModeration().setBan(true, d);
            AdminChat.sendMessageToStaffs(Moderation.message() + ChatColor.RED + args[0] + ChatColor.GOLD + " est desormais banni jusqu'au " + d + " pour" + ChatColor.ITALIC + args[2]);
            u.sendNotifMessage(ChatColor.GOLD + "Vous avez été banni jusqu'au " + d + " pour" + args[1]);
            Journal.add(sender.getName(), "ban", u.getUuid(), "jusqu'au"+d, args[2]);
            u.getPlayer().kickPlayer(args[2]);
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");
            return true;
        }
    }



}
