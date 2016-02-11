package fr.maxcraft.player.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.maxcraft.player.User;
import net.nathem.script.core.NSCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Jobs {
	
protected double xp;
protected UUID uuid;
protected String name;
	
	public int getLvl() {
		if((Math.sqrt(this.xp)/300)<=10)
			return (int) (Math.sqrt(this.xp/300));
		return 10;
	}

    public void initializeSave(){
        new Task(this).runTaskTimer(Main.getPlugin(),120,120);

    }
	
	public static Jobs buildJob(UUID uuid,String metier,double xp){
		try {
            Jobs j = (Jobs) Class.forName("fr.maxcraft.player.jobs.jobs." + metier).getConstructor(UUID.class, double.class).newInstance(uuid, xp);
            j.initializeSave();
            return j;
	} catch (Exception e) {
		e.printStackTrace();
		Main.logError("Erreur au chargement des metier!");
	}
	return null;
		
	}

	public static Jobs load(UUID uuid) {
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `jobs` WHERE `jobs`.`uuid` = '"+uuid.toString()+"'",true);
		try {
			if (!r.isFirst())
				return null;
		return buildJob(uuid,r.getString("metier"),r.getDouble("xp"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

    public List<String> info(){
        String progress = "";
        double xplvl =(this.xp - this.getLvl()*this.getLvl()*300);
        double xpnec = ((this.getLvl()+1)*(this.getLvl()+1)*300)-this.getLvl()*this.getLvl()*300;
        int progressper30 = (int) ((xplvl/xpnec)*30);
        for (int i = 1 ; i<=30;i++)
            if (i<progressper30) {
                progress += ChatColor.GREEN + "|";
            }else{
                progress += ChatColor.WHITE + "|";
            }

        return Arrays.asList(ChatColor.GRAY+"*** <"+ChatColor.GOLD+ this.name +ChatColor.GRAY +"> ***",ChatColor.WHITE+"Niveau : "+ChatColor.GREEN+this.getLvl()
        ,progress);
    }

	public void insert() {
        MySQLSaver.mysql_update("DELETE FROM `jobs` WHERE `uuid`= '"+this.uuid.toString()+"'");
        MySQLSaver.mysql_update("INSERT INTO `jobs` (`uuid`, `metier`, `xp`) VALUES ('"+this.uuid.toString()+"', '"+this.name+"', '"+this.xp+"');");
		
	}

    public void save() {
        MySQLSaver.mysql_update("UPDATE `jobs` SET `metier` ='"+this.name+"',`xp` ="+this.xp+" WHERE `uuid` = '"+this.uuid+"'");

    }

    public class Task extends BukkitRunnable {
        private Jobs job;
        private double lxp;

        public Task(Jobs job) {
            this.job = job;
            this.lxp = job.xp;
        }

        @Override
        public void run() {
            if (this.lxp+20<job.xp)
                job.save();
        }
    }

}