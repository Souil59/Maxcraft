package fr.maxcraft.player.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.nathem.script.core.NSCore;
import org.bukkit.Material;

import fr.maxcraft.Main;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Jobs {
	
protected double xp;
protected UUID uuid;
protected String name;
	
	public int getLvl() {
		if((Math.sqrt(this.xp)/30)<=10)
			return (int) (Math.sqrt(this.xp)/30);
		return 10;
	}

    public void initializeSave(){
        new Task(this).runTaskTimer(Main.getPlugin(),120,120);

    }
	
	public static Jobs buildJob(UUID uuid,String metier,double xp){
		try {
            Jobs j = (Jobs) Class.forName("fr.maxcraft.jobs.jobs." + metier).getConstructor(UUID.class, double.class).newInstance(uuid, xp);
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

	protected void insert() {
		MySQLSaver.mysql_update("INSERT INTO `jobs` (`uuid`, `metier`, `xp`) VALUES ('"+this.uuid.toString()+"', '"+this.name+"', '"+this.xp+"');");
		
	}

    public void save() {
        MySQLSaver.mysql_update("UPDATE `jobs` SET `metier` ='"+this.name+"',`xp` ='"+this.xp+"' WHERE 'uuid' = "+this.uuid);

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