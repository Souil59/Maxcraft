package fr.maxcraft.player.moderation;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.world.marker.Marker;
import fr.maxcraft.utils.DurationParser;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ModeratorCommand extends fr.maxcraft.server.command.Command {

    Main plugin;

	public ModeratorCommand(String s, Main plugin) {
        super(s);
        this.plugin = plugin;
        if (s.equals("ban")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("bantemp")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("mute")){
            this.setPerms("maxcraft.guide").register();
        }
        if (s.equals("mutetemp")){
            this.setPerms("maxcraft.guide").register();
        }
        if (s.equals("jail")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("kick")){
            this.setPerms("maxcraft.modo").register();
        }
        if (s.equals("moderation")){
            this.setPerms("maxcraft.guide").setAliases(Arrays.asList("m", "modo")).register();
        }
	}

    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
        if (!User.get(arg0.getName()).getPerms().hasPerms("maxcraft.guide")) return true;
        switch(arg1)
        {
            case "mute":   //done                       //OK
                return this.mute(arg0, arg2);
            case "mutetemp":                            //OK
                return this.mutetemp(arg0, arg2);
            case "ban":    //done                       //OK
                return this.ban(arg0, arg2);
            case "bantemp": //done                      //OK
                return this.bantemp(arg0, arg2);
            case "kick":   //done                       //OK
                return this.kick(arg0, arg2);
            case "jail":   //done                       //OK
                return this.jail(arg0, arg2);

            case "m":
            case "modo":
            case "moderation":

                switch (arg2[0]){

                    case "fine":   // = amende //Done   //OK
                        return this.fine(arg0, arg2);
                    case "invsee": //done
                        return this.invsee(arg0, arg2);
                    case "ec":      //done
                        return this.ec(arg0, arg2);
                    case "pardon":  //done              //OK
                        return this.unban(arg0, arg2);
                    case "unjail": //done               //OK
                        return this.unjail(arg0, arg2);
                    /*case "journal": // Sur le site
                        return this.journal(arg0, arg2);*/
                }

            default:
                arg0.sendMessage(ChatColor.RED+"Erreur...");
                return false;
        }
    }

    private boolean unjail(CommandSender sender, String[] args){ //moderation unjail <joueur>
        if (args.length != 2){
            sender.sendMessage(ChatColor.GRAY+"Paramètres incorrects ! /moderation unjail <joueur>");
            return false;
        }
        User u = User.get(args[1]);
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcarft.modo")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous n'avez pas la permission de libérer ce joueur !");
            return true;
        }
        if(!u.getModeration().isJail()){
            sender.sendMessage(ChatColor.RED+"Le joueur que vous tentez de libérer est déjà libre... ou s'est enfui !");
            return true;
        }
        u.getModeration().setJail(false, -1);
        u.getPlayer().teleport(Marker.getMarker("spawn"));
        u.getPlayer().setGameMode(GameMode.SURVIVAL);
        Moderation.sendMessageToStaffs(Moderation.message() + u.getName() + " a été libéré ! (" + sender.getName() + ")");
        u.sendNotifMessage(ChatColor.GREEN + "Vous avez été libéré ! Tenez-vous mieux la prochaine fois...");
        Journal.add(sender.getName(), "unjail", u.getUuid(), DurationParser.getCurrentTimeStampInString(), "Pas de raison");
        return true;
    }


    private boolean unban(CommandSender sender, String[] args){ //arg[0] : "pardon", arg[1]: <joueur>
        if (args.length != 2){
            sender.sendMessage(ChatColor.GRAY+"Nombre de paramètres incorrect : /moderation pardon <joueur>");
            return false;
        }
        User u = User.get(args[1]);
        if (u == null){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+"Joueur introuvable !");
            return false;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED+"Erreur: "+ChatColor.RED+" Vous n'avez pas le droit de faire ça !");
            return false;
        }
        if (!u.getModeration().isBan()){
            sender.sendMessage(ChatColor.RED+"La commande ne peut s'exécuter dans le contexte actuel ! (joueur pas banni)");
            return false;
        }

        u.getModeration().setBan(false, -1, null);
        Journal.add(sender.getName(), "unban", u.getUuid(), "le : " + DurationParser.getCurrentTimeStampInString(), "");
        Moderation.sendMessageToStaffs(Moderation.message() + ChatColor.BOLD + u.getName() + " a été débanni. "+ChatColor.GRAY+ "(" + sender.getName() + ")");
        return true;
    }

	/*private boolean journal(CommandSender sender, String[] args) {
		return false;
	}*/

	private boolean ec(CommandSender sender, String[] args) { //moderation ec <joueur>
        if (args.length != 2){
            sender.sendMessage(ChatColor.GRAY+"Paramètres incorrects ! /moderation ec <joueur>");
        }
        User u = User.get(args[1]);
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + " Vous n'avez pas le droit de faire ça !");
            return true;
        }
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return true;
        }
        if (u.getPlayer().isOnline()){
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

	private boolean invsee(CommandSender sender, String[] args) { //moderation invsee <joueur>
        if (args.length != 2){
            sender.sendMessage(ChatColor.GRAY+"Paramètres incorrects : /moderation invsee <joueur>");
            return false;
        }
        User u = User.get(args[1]);
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.guide")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + " Vous n'avez pas le droit de faire ça !");
            return false;
        }
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return false;
        }
        if (u.getPlayer().isOnline()){

            Player senderP = (Player) sender;
            Inventory pInv = u.getPlayer().getInventory();
            ItemStack tst = new ItemStack(Material.EXP_BOTTLE);
            tst.getItemMeta().setDisplayName("Menu");

            senderP.closeInventory();
            senderP.openInventory(pInv);

            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Le joueur n'est pas en ligne!");
            return false;
        }
	}

	private boolean jail(CommandSender sender, String[] args) { //jail <joueur> <durée> [raison]
        if (args.length != 2 && args.length != 3){
            sender.sendMessage(ChatColor.GRAY+"Paramètres incorrects ! /jail <joueur> <durée> [raison]");
            return false;
        }
        args = argsToString(args).split(" ", 3);
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcarft.modo") || u.getPerms().hasPerms("maxcraft.admin")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous ne pouvez pas emprisonner ce joueur !");
            return true;
        }

        if (!u.getModeration().isJail()){
            long d = DurationParser.translateTimeStringToDate(args[1]);
            if (!this.verifyEndDate(d)){
                sender.sendMessage(ChatColor.RED+"La durée donnée est incorrecte !");
                return false;
            }
            u.getModeration().setJail(true, d);
            String end;
            try {
                end = this.getEndDate(d);
            } catch (ParseException e) {
                sender.sendMessage(ChatColor.RED+"Erreur dans la conversion de la date !");
                return false;
            }
            try{
                u.getPlayer().teleport(Marker.getMarker("jail"));

            }
            catch (NullPointerException e){
                sender.sendMessage(ChatColor.RED+"Le warp/marker \"jail\" n'a pas été trouvé !");
                return true;
            }
            u.getPlayer().setGameMode(GameMode.ADVENTURE);
            if (args.length == 2){
                u.sendNotifMessage(ChatColor.GOLD + "Vous avez été jail jusqu'au "+end);
                Journal.add(sender.getName(), "jail", u.getUuid(), end, "Pas de raison");
                Moderation.sendMessageToStaffs(Moderation.message() + args[0] + " a été jail jusqu'au "+end+ChatColor.GRAY+" ("+sender.getName()+")");
            }
            else{
                u.sendNotifMessage(ChatColor.GOLD + "Vous avez été jail jusqu'au " + end + " pour : " + args[2]);
                Journal.add(sender.getName(), "jail", u.getUuid(), end, args[2]);
                Moderation.sendMessageToStaffs(Moderation.message() + args[0] + " a été jail jusqu'au " + end + " pour : " + args[2]+ChatColor.GRAY+" ("+sender.getName()+")");
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Erreur dans l'exécution de la commande...");
        return false;
		
	}

	private boolean fine(CommandSender sender, String[] args) { //moderation fine <joueur> <somme> <raison>
        if (args.length != 4){
            sender.sendMessage(ChatColor.GRAY+"Parmètres incorrects ! /moderation fine <joueur> <somme> <raison>");
            return false;
        }
        args = argsToString(args).split(" ", 4);
        User u = User.get(args[1]);
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo") || u.getPerms().hasPerms("maxcraft.admin")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous ne pouvez pas donner d'amende à ce joueur !");
            return false;
        }
        Double somme, uBalance=u.getBalance();
        try{
            somme = Double.parseDouble(args[2]);
        }
        catch (ClassCastException e){
           sender.sendMessage(ChatColor.RED+"Erreur dans la saisie du montant à retirer !");
           return false;
        }
        uBalance-=somme;
        MySQLSaver.mysql_update("UPDATE `player` SET `balance` = " + uBalance + " WHERE `id` = '" + u.getUuid().toString() + "';");
        if (u.getPlayer().isOnline()) u.sendNotifMessage(ChatColor.RED + "Votre compte a été débité de " + somme + " POs suite à une amende pour " + ChatColor.ITALIC + args[3]);
        Moderation.sendMessageToStaffs(Moderation.message() + args[1] + "a été débité de " + somme + " POS suite à une amende par " + sender.getName() + " pour "+ChatColor.ITALIC+ args[3]);
        Journal.add(sender.getName(), "amende", u.getUuid(), "", somme + " POs" + ", " + args[3]);
        return true;
		
	}

	private boolean kick(CommandSender sender, String[] args) {
		args = argsToString(args).split(" ", 2);
		User u = User.get(args[0]);
        if (u ==  null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur :" + ChatColor.RED + " Joueur introuvable !");
            return true;
        }
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo") ||u.getPerms().hasPerms("maxcraft.modo")){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous ne pouvez pas kick ce joueur !");
            return true;
        }
        if (u.getPlayer().isOnline()){
            if (args[1].isEmpty()){
                u.getPlayer().kickPlayer(ChatColor.RED+ "Ejecté du Serveur");
                Moderation.sendMessageToStaffs(Moderation.message() + args[0] + " a été kick du serveur " + ChatColor.ITALIC + "sans raison !"+ " ("+sender.getName()+")");
                Journal.add(sender.getName(), "kick", u.getUuid(), "", "pas de raison");
                return true;
            }
            else{
                u.getPlayer().kickPlayer(ChatColor.RED+ args[1]);
                Moderation.sendMessageToStaffs(Moderation.message() + args[0] + " a été kick du serveur pour: " + ChatColor.ITALIC + args[1]+ " ("+sender.getName()+")");
                Journal.add(sender.getName(), "kick", u.getUuid(), "", args[1]);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "Erreur dans la soumission de la commande !");
		return false;
		
	}

	private boolean ban(CommandSender sender, String[] args) {
        if (args.length <= 1){
            sender.sendMessage(ChatColor.RED + "Vous devez indiquer une raison");
            return false;
        }
        args = argsToString(args).split(" ", 2);
		User j = User.get(args[0]);
		if (j==null){
			sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Ce joueur n'existe pas !");
			return true;
		}
		if (!j.getModeration().isBan() && args.length > 1){
			j.getModeration().setBan(true, -1, args[1]);
            Moderation.sendMessageToStaffs(Moderation.message() + ChatColor.RED + j.getName() + ChatColor.GOLD + " a été banni pour " + ChatColor.ITALIC + args[1]+ChatColor.RESET+ChatColor.GRAY+" ("+sender.getName()+")");
			j.sendNotifMessage(ChatColor.GOLD + "Vous avez été banni pour " + args[1]);
			Journal.add(sender.getName(), "ban", j.getUuid(), "definitif", args[1]);
            j.getPlayer().kickPlayer(ChatColor.RED+"Vous avez été banni : "+ChatColor.GOLD+args[1]);
			return true;
		}
        else{
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Le joueur est déjà banni ou une erreur s'est produite dans l'exécution de la commande !");
            return true;
        }
	}

	private boolean mute(CommandSender sender, String[] args) { //mute <joueur> [raison]

        if (args.length<1){
            sender.sendMessage(ChatColor.RED+"Il manque des paramètres !");
            return false;
        }

		User j = User.get(args[0]);

		if (j==null){

			sender.sendMessage(ChatColor.RED + "Joueur non trouvé !");

			return true;
		}

        if (j.getPerms().hasPerms("maxcraft.modo")){

            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Vous en pouvez pas rendre ce joueur muet !");

            return true;
        }

        if (!j.getModeration().isMute() && args.length == 1){

            j.getModeration().setMute(true, -1);
            j.sendNotifMessage(ChatColor.GOLD+"Vous avez été mute !");
            Journal.add(sender.getName(), "mute", j.getUuid(), "Pas de durée", "Pas de raison");
            Moderation.sendMessageToStaffs(Moderation.message() + j.getName() + " a été mute par "+sender.getName());

            return true;
        }

        if (!j.getModeration().isMute() && args.length > 1){

            args = this.argsToString(args).split(" ", 2);

            j.getModeration().setMute(true, -1);
            j.sendNotifMessage(ChatColor.GOLD+"Vous avez été mute pour : "+args[1]);
            Journal.add(sender.getName(), "mute", j.getUuid(), "Pas de durée", args[1]);
            Moderation.sendMessageToStaffs(Moderation.message() + j.getName() + " a été mute par "+sender.getName()+" pour : " + args[1]);

            return true;
        }

        if (j.getModeration().isMute()){

            j.getModeration().setMute(false, -1);
            Moderation.sendMessageToStaffs(Moderation.message() + j.getName() + " n'est plus muet !");
            j.sendNotifMessage(ChatColor.GREEN+"Vous n'êtes plus muet.");
            Journal.add(sender.getName(), "demute", j.getUuid(), "", "");

            return true;
        }

        sender.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");

        return false;
	}

    private boolean mutetemp(CommandSender sender, String[] args){

        if (args.length<2){

            sender.sendMessage(ChatColor.RED+"Il manque des paramètres !");

            return false;
        }

        User j = User.get(args[0]);

        if (j==null){

            sender.sendMessage(ChatColor.RED + "Joueur non trouvé !");

            return true;
        }

        if (j.getPerms().hasPerms("maxcraft.modo")){

            sender.sendMessage(ChatColor.DARK_RED + "Erreur: "+ChatColor.RED+"Vous en pouvez pas rendre ce joueur muet !");

            return true;
        }

        if (!j.getModeration().isMute()&& args.length== 2){

            long d = DurationParser.translateTimeStringToDate(args[1]);

            if (!this.verifyEndDate(d)){
                sender.sendMessage(ChatColor.RED+"La durée donnée est incorrecte !");
                return false;
            }

            j.getModeration().setMute(true, d);
            String end;
            try {
                 end = this.getEndDate(d);
            } catch (ParseException e) {
                sender.sendMessage(ChatColor.RED+"Erreur dans la conversion de la durée !");
                return false;
            }
            j.sendNotifMessage(ChatColor.GOLD + "Vous avez été mute jusqu'au "+end+ " (" + args[1] +")");
            Journal.add(sender.getName(), "mute", j.getUuid(), "Jusqu'au " + end + " (" + args[1] +")", "Pas de raison");
            Moderation.sendMessageToStaffs(Moderation.message() + j.getName() + " a été mute jusqu'au " + end + " (" + args[1] + ") par " +sender.getName());

            return true;
        }

        if (!j.getModeration().isMute()&& args.length > 2){

            args = this.argsToString(args).split(" ", 3);

            long d = DurationParser.translateTimeStringToDate(args[1]);

            if (!this.verifyEndDate(d)){
                sender.sendMessage(ChatColor.RED+"La durée donnée est incorrecte !");
                return false;
            }

            String end;

            try {
                 end = this.getEndDate(d);
            } catch (ParseException e) {
                sender.sendMessage(ChatColor.RED+"Erreur dans la conversion de la durée !");
                return false;
            }

            j.getModeration().setMute(true, d);
            j.sendNotifMessage(ChatColor.GOLD + "Vous avez été mute jusqu'au " + end + " (" + args[1] + ") pour : " + args[2]);
            Journal.add(sender.getName(), "mute", j.getUuid(), "Jusqu'au " + end + " (" + args[1] +")", args[2]);
            Moderation.sendMessageToStaffs(Moderation.message() + j.getName() + " a été mute jusqu'au " + end + " (" + args[1] + ") par " + sender.getName() + " pour : " + args[2]);

            return true;
        }

        if (j.getModeration().isMute()){

            j.getModeration().setMute(false, -1);
            Moderation.sendMessageToStaffs(Moderation.message() + j.getName() + " n'est plus muet !");
            j.sendNotifMessage(ChatColor.GREEN+"Vous n'êtes plus muet.");
            Journal.add(sender.getName(), "demute", j.getUuid(), "", "");

            return true;
        }

        sender.sendMessage(ChatColor.RED+"Erreur dans la soumission de la commande !");

        return false;
    }

    private boolean bantemp(CommandSender sender, String[] args){ //bantemp <joueur> <durée> [raison]
        if (args.length != 3){
            sender.sendMessage(ChatColor.GRAY + "Nombre de paramètres incorrect ! /bantemp <joueur> <durée> <raison>");
            return false;
        }
        args = argsToString(args).split(" ", 3);
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.DARK_RED + "Erreur: " + ChatColor.RED + "Joueur introuvable !");
            return  true;
        }

        if (!u.getModeration().isBan()){

            long d = DurationParser.translateTimeStringToDate(args[1]);

            if (!this.verifyEndDate(d)){
                sender.sendMessage(ChatColor.RED+"La durée donnée est incorrecte !");
                return false;
            }

            String end = null;
            try {
                end = this.getEndDate(d);
            } catch (ParseException e) {
                sender.sendMessage(ChatColor.RED+"Erreur dans la conversion de la date !");
            }

            u.getModeration().setBan(true, d, ChatColor.RED+"Vous êtes banni : "+ChatColor.GOLD+args[2]+ChatColor.ITALIC+" Fin: "+end);
            Moderation.sendMessageToStaffs(Moderation.message() + ChatColor.RED + u.getName() + ChatColor.GOLD + " a été banni " + args[1] + " pour " + ChatColor.ITALIC + args[2] +" jusqu'au "+end+ ChatColor.RESET + ChatColor.GRAY + " (" + sender.getName() + ")");
            u.sendNotifMessage(ChatColor.GOLD + "Vous avez été banni jusqu'au " + end + " pour " + args[2]);
            Journal.add(sender.getName(), "ban", u.getUuid(), "jusqu'au " + end, args[2]);
            u.getPlayer().kickPlayer(ChatColor.RED+"Vous avez été banni : "+ChatColor.GOLD + args[2]+ChatColor.ITALIC+"Fin : "+end);
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Erreur dans la soumission de la commande !");
            return true;
        }
    }

    private String argsToString(String[] args) {
                String s = "";
                for (String sc : args)
                    s+=sc+" ";
                return s;
    }

    private String getEndDate(long dateToAdd) throws ParseException {
        Date date = new Date(dateToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    private boolean verifyEndDate(long date){
        long now = new Date().getTime();
        return date > now;
    }

}