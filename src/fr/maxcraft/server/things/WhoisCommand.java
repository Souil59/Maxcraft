package fr.maxcraft.server.things;

import fr.maxcraft.player.User;
import fr.maxcraft.server.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class WhoisCommand extends Command {

    public WhoisCommand(String name) {
        super(name);
        this.setPerms("maxcraft.modo").register();
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!User.get(sender.getName()).getPerms().hasPerms("maxcraft.modo")) return false;
        if (args.length!=1){
            sender.sendMessage(ChatColor.RED+"Le nombre de paramètres est incorrect !");
            return true;
        }
        User u = User.get(args[0]);
        if (u==null){
            sender.sendMessage(ChatColor.RED+"Joueur introuvable !");
            return true;
        }

        String uName = u.getName();
        double heal =u.getPlayer().getHealth();
        double maxHeal = u.getPlayer().getMaxHealth();
        int foodLevel = u.getPlayer().getFoodLevel();
        float xp = u.getPlayer().getTotalExperience();
        int xpLvl = u.getPlayer().getExpToLevel();
        String worldName = u.getPlayer().getWorld().getName();
        double x = u.getPlayer().getLocation().getBlockX();
        double y = u.getPlayer().getLocation().getBlockY();
        double z = u.getPlayer().getLocation().getBlockZ();
        double balance = u.getBalance();
        String ip = u.getPlayer().getAddress().getAddress().getHostAddress();
        String gamemode = u.getPlayer().getGameMode().toString();
        boolean god =u.isGod();
        boolean flyAllowed = u.getPlayer().getAllowFlight();
        boolean fly = u.getPlayer().isFlying();
        boolean afk = u.isAfk();
        boolean jail = u.getModeration().isJail();
        boolean mute = u.getModeration().isMute();

        String godMsg;
        String flyAllowedMsg;
        String flyMsg;
        String afkMsg;
        String jailMsg;
        String muteMsg;

        if (god) godMsg = ChatColor.GREEN+"vrai"; else godMsg = ChatColor.RED+"faux";
        if (flyAllowed) flyAllowedMsg = ChatColor.GREEN+"vrai"; else flyAllowedMsg = ChatColor.RED+"faux";
        if (fly) flyMsg = ChatColor.GRAY+" (en vol)"; else flyMsg = ChatColor.GRAY+" (ne volant pas)";
        if (afk) afkMsg = ChatColor.GREEN+"vrai"; else afkMsg = ChatColor.RED+"faux";
        if (jail) jailMsg = ChatColor.GREEN+"vrai"; else jailMsg = ChatColor.RED+"faux";
        if (mute) muteMsg = ChatColor.GREEN+"vrai"; else muteMsg = ChatColor.RED+"faux";


        this.whoisMessage(sender, uName, heal, maxHeal,foodLevel, xp, xpLvl, worldName, x, y, z, balance, ip, gamemode, godMsg, flyAllowedMsg, flyMsg, afkMsg, jailMsg, muteMsg);

        return true;
    }

    private boolean whoisMessage(CommandSender sender, String uName, double heal, double maxHeal,int foodLvl, float xp, int xpLvl,
                                 String worldName, double x, double y, double z, double balance, String ip, String gamemode,
                                 String godMsg, String flyAllowedMsg, String flyMsg, String afkMsg, String jailMsg, String muteMsg){


        sender.sendMessage(Things.message()+ChatColor.GOLD+"===== Whois "+uName+" =====");
        sender.sendMessage(ChatColor.GRAY+"- Santé: " +heal+"/"+maxHeal);
        sender.sendMessage(ChatColor.GRAY+"- Faim: "+foodLvl+"/20");
        sender.sendMessage(ChatColor.GRAY+"- Expérience: "+xp+" (Niveau "+xpLvl+")");
        sender.sendMessage(ChatColor.GRAY+"- Position: "+worldName+"; "+x+"; "+y+"; "+z);
        sender.sendMessage(ChatColor.GRAY+"- Somme d'argent: "+ChatColor.ITALIC+balance);
        sender.sendMessage(ChatColor.GRAY+"- Adresse Ip: "+ip);
        sender.sendMessage(ChatColor.GRAY+"- Mode de jeu: "+gamemode);
        sender.sendMessage(ChatColor.GRAY+"- Mode Dieu: "+godMsg);
        sender.sendMessage(ChatColor.GRAY+"- Fly: "+flyAllowedMsg+flyMsg);
        sender.sendMessage(ChatColor.GRAY+"- AFK/Absent: "+afkMsg);
        sender.sendMessage(ChatColor.GRAY+"- Jail: "+jailMsg);
        sender.sendMessage(ChatColor.GRAY+"- Mute: "+muteMsg);
        sender.sendMessage(Things.alertMessage()+"Pour des infos plus précises concernant la Modération allez voir le journal du joueur !");
        return true;
    }
}
