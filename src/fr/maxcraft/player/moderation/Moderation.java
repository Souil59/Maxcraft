package fr.maxcraft.player.moderation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import fr.maxcraft.player.User;
import fr.maxcraft.utils.MySQLSaver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Moderation {
	private boolean mute;
	private boolean jail;
	private boolean ban;
	private long muteend;
	private long jailend;
	private long banend;
	private UUID uuid;
    private String banReason;

	public Moderation(UUID uuid,boolean ismute,long muteend,boolean isjail,long jailend,boolean isban,long banend, String banReason){
		this.uuid = uuid;
		this.mute = ismute;
		this.muteend = muteend;
		this.jail = isjail;
		this.jailend = jailend;
		this.ban = isban;
		this.banend = banend;
        this.banReason = banReason;
	}
	public static Moderation load(UUID uuid){
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `moderation` WHERE `id` = '"+uuid.toString()+"';",true);
		try {
			assert r != null;
			if (!r.isFirst())
                return new Moderation(uuid,false,-1,false,-1,false,-1, null).sqlInsert();
            return new Moderation(UUID.fromString(r.getString("id")),r.getBoolean("ismute"),r.getLong("muteend"),r.getBoolean("isjail"),r.getLong("jailend"),r.getBoolean("isban"),r.getLong("banend"), r.getString("banreason"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String message(){
		return ChatColor.DARK_GRAY + "[Modération]" + ChatColor.GRAY+" ";
	}

	public static void sendMessageToStaffs(String message){
		for(Player p: Bukkit.getOnlinePlayers()) {
			if (User.get(p).getPerms().hasPerms("maxcraft.guide") || p.isOp()){
                p.sendMessage(org.bukkit.ChatColor.RED+"["+ org.bukkit.ChatColor.GRAY+"!"+ org.bukkit.ChatColor.RED+"]"+" "+ org.bukkit.ChatColor.GRAY+message);
            }
		}
	}

	public Moderation sqlInsert() {
		MySQLSaver.mysql_update("INSERT INTO `moderation` (`id`, `ismute`, `isban`, `isjail`, `muteend`, `banend`, `jailend`) "
				+ "VALUES ('"+this.uuid.toString()+"', '0', '0', '0', '-1', '-1', '-1');");
		return this;
	}
	public void save(){
		MySQLSaver.mysql_update("UPDATE `moderation` SET `ismute` = "+this.mute+" ,`isban` = "+this.ban+",`isjail` = "+this.jail+""
				+ ",`muteend` = '"+this.muteend+"',`banend` = '"+this.banend+"',`jailend` = '"+this.jailend+"',`banreason` = '"+this.banReason+"' WHERE `id` = '"+this.uuid.toString()+"';");
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

	public boolean isJailBoolean(){
		return this.jail;
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

	public void setBan(boolean ban,long banend, String reason) {
		this.ban = ban;
		this.banend = banend;
		this.banReason = reason;
		this.save();
	}

	public long getMuteend() {
		return muteend;
	}

	public void setMuteend(long muteend) {
		this.muteend = muteend;
	}

	public long getJailend() {
		return jailend;
	}

	public void setJailend(long jailend) {
		this.jailend = jailend;
	}

	public long getBanend() {
		return banend;
	}

	public void setBanend(long banend) {
		this.banend = banend;
	}

	public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

}