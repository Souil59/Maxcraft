package fr.maxcraft.player.moderation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import fr.maxcraft.utils.MySQLSaver;

public class Moderation {
	private boolean mute;
	private boolean jail;
	private boolean ban;
	private long muteend;
	private long jailend;
	private long banend;
	private UUID uuid;

	public Moderation(UUID uuid,boolean ismute,long muteend,boolean isjail,long jailend,boolean isban,long banend,boolean insert){
		this.uuid = uuid;
		this.mute = ismute;
		this.muteend = muteend;
		this.jail = isjail;
		this.jailend = jailend;
		this.ban = isban;
		this.banend = banend;
		if (insert)
			this.sqlInsert();
	}
	public static Moderation load(UUID uuid){
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `moderation` WHERE `moderation`.`id` = '"+uuid.toString()+"';",true);
		try {
			if (r.next())
				return new Moderation(UUID.fromString(r.getString("id")),r.getBoolean("ismute"),r.getLong("muteend"),r.getBoolean("isjail"),r.getLong("jailend"),r.getBoolean("isban"),r.getLong("banend"),false);
			return new Moderation(uuid,false,-1,false,-1,false,-1,true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void sqlInsert() {
		MySQLSaver.mysql_update("INSERT INTO `moderation` (`id`, `ismute`, `isban`, `isjail`, `muteend`, `banend`, `jailend`) "
				+ "VALUES ('"+this.uuid.toString()+"', '0', '0', '0', '-1', '-1', '-1');");
		
	}
	public void save(){
		MySQLSaver.mysql_update("UPDATE `moderation` SET `ismute` = '"+this.mute+"' ,`isban` = '"+this.ban+"',`isjail` = '"+this.jail+"'"
				+ ",`muteend` = '"+this.muteend+"',`banend` = '"+this.banend+"',`jailend` = '"+this.jailend+"' WHERE `id` = '"+this.uuid.toString()+"';");
	}
	
	public boolean isMute() {
		if (muteend<new Date().getTime()&&muteend!=-1)
			mute = false;
		return mute;
	}

	public boolean isJail() {
		if (jailend<new Date().getTime()&&jailend!=-1)
			jail = false;
		return jail;
	}

	public boolean isBan() {
		if (banend<new Date().getTime()&&banend!=-1)
			ban = false;
		return ban;
	}

	public void setJail(boolean jail,long jailend) {
		this.jail = jail;
		this.jailend = jailend;
		this.save();
	}

	public void setMute(boolean mute,long muteend) {
		this.mute = mute;
		this.muteend = muteend;
		this.save();
	}

	public void setBan(boolean ban,long banend) {
		this.ban = ban;
		this.banend = banend;
		this.save();
	}
}