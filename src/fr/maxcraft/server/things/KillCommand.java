package fr.maxcraft.server.things;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import fr.maxcraft.server.things.Tasks.AutoKillTask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


public class KillCommand extends Command {

    public KillCommand(String name) {
        super(name);
        this.setPerms("maxcraft.citoyen").setAliases(Arrays.asList("mourir", "aidezmoisvpjesuiscoincedansuntrou", "coince")).register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.citoyen")) return false;
        switch (cmd){
            case "kill":
                if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")) return true;
                this.killCommand(sender, args);
            case "mourir":
            case "coince":
            case "aidezmoisvpjesuiscoincedansuntrou":
                this.mourirCommand(sender, args);
        }
        return false;
    }

    public boolean killCommand(CommandSender sender, String[] args){
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")) return true;
        if (args.length != 1){
            sender.sendMessage(ChatColor.RED+"Il manque des paramètres : /kill <joueur>");
            return true;
        }
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        if (u.getPerms().hasPerms("maxcraft.admin") || u.isGod()){
            sender.sendMessage(ChatColor.RED+"Vous ne pouvez pas tuer ce joueur !");
            return true;
        }
        u.getPlayer().setHealth((double)0);
        u.sendMessage(ChatColor.GRAY + "Vous avez été tué...");
        sender.sendMessage(Things.message()+"Vous avez tué "+u.getName());
        return true;
    }

    public boolean mourirCommand(CommandSender sender, String[] args){
        User u = User.get(sender.getName());
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
            return true;
        }
        if (u.isGod() || u.getPlayer().getGameMode().equals(GameMode.SPECTATOR) || u.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            sender.sendMessage(ChatColor.RED+"Vous ne pouvez pas vous suicider !");
            return true;
        }
        sender.sendMessage(Things.message()+"Vous allez mourir dans 10 secondes, patience...");
        new AutoKillTask(Main.getPlugin(), u.getPlayer()).runTaskLater(Main.getPlugin(), 10*20);
        return true;
    }
}
