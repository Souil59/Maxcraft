package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Created by admin on 19/02/16.
 */
public class GamemodeCommand extends Command {

    public GamemodeCommand(String name) {
        super(name);
        this.setPerms("maxcraft.builder").setAliases(Arrays.asList("gms", "gmc", "gmsp", "gma", "gm")).register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.builder")) return false;
        switch (cmd){
            case "gmc":
                this.creativeMode(sender, args, false);
                break;
            case "gms":
                this.survieMode(sender, args, false);
                break;
            case "gmsp":
                this.spectatorMode(sender, args, false);
                break;
            case "gma":
                this.adventureMode(sender, args, false);
                break;
            case "gm":
            case "gamemode":
                this.undefinedMode(sender, args);
                break;

        }
        return false;
    }

    private boolean undefinedMode(CommandSender sender, String[] args){
        if (args.length <= 0){
            sender.sendMessage(ChatColor.GRAY+"Il manque des paramètres !");
            return true;
        }
        switch (args[0]){
            case "0":
                this.survieMode(sender, args, true);
                break;
            case "1":
                this.creativeMode(sender, args, true);
                break;
            case "2":
                this.adventureMode(sender, args, true);
                break;
            case "3":
                this.spectatorMode(sender, args, true);
                break;
        }
        return false;
    }

    private boolean survieMode(CommandSender sender, String[] args, Boolean numbersMode){
        if (numbersMode){
            if (args.length>1){
                User u = User.get(args[1]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SURVIVAL);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode survie !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode survie !");
                return true;
            }
            else if (args.length ==1){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SURVIVAL);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode survie !");
                return true;
            }
        }
        else {
            if (args.length == 0){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SURVIVAL);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode survie !");
                return true;
            }
            else if (args.length>0){
                User u = User.get(args[0]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SURVIVAL);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode survie !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode survie !");
                return true;
            }
        }
        return false;
    }

    private boolean creativeMode(CommandSender sender, String[] args, Boolean numbersMode){
        if (numbersMode){
            if (args.length>1){
                User u = User.get(args[1]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.CREATIVE);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode créatif !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode créatif !");
                return true;
            }
            else if (args.length ==1){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.CREATIVE);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode créatif !");
                return true;
            }
        }
        else {
            if (args.length == 0){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.CREATIVE);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode créatif !");
                return true;
            }
            else if (args.length>0){
                User u = User.get(args[0]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.CREATIVE);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode créatif !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode créatif !");
                return true;
            }
        }
        return false;
    }

    private boolean spectatorMode(CommandSender sender, String[] args, Boolean numbersMode){
        if (numbersMode){
            if (args.length>1){
                User u = User.get(args[1]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SPECTATOR);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode spectateur !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode spectateur !");
                return true;
            }
            else if (args.length ==1){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SPECTATOR);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode spectateur !");
                return true;
            }
        }
        else {
            if (args.length == 0){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SPECTATOR);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode spectateur !");
                return true;
            }
            else if (args.length>0){
                User u = User.get(args[0]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.SPECTATOR);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode spectateur !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode spectateur !");
                return true;
            }
        }
        return false;
    }

    private boolean adventureMode(CommandSender sender, String[] args, Boolean numbersMode){
        if (numbersMode){
            if (args.length>1){
                User u = User.get(args[1]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.ADVENTURE);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode aventure !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode aventure !");
                return true;
            }
            else if (args.length ==1){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.ADVENTURE);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode aventure !");
                return true;
            }
        }
        else {
            if (args.length == 0){
                User u = User.get(sender.getName());
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.ADVENTURE);
                u.sendMessage(Things.message() + "Vous êtes désormais en mode aventure !");
                return true;
            }
            else if (args.length>0){
                User u = User.get(args[0]);
                if (u==null){
                    sender.sendMessage(org.bukkit.ChatColor.RED+"Joueur introuvable !");
                    return true;
                }
                u.getPlayer().setGameMode(GameMode.ADVENTURE);
                u.sendMessage(ChatColor.GRAY + "Vous êtes désormais en mode aventure !");
                sender.sendMessage(Things.message()+u.getName()+" est désormais en mode aventure !");
                return true;
            }
        }
        return false;
    }
}
