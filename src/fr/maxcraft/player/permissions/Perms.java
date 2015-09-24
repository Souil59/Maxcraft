package fr.maxcraft.player.permissions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;

public class Perms {

	
	private Group group;
	public ArrayList<String> perms;
	private UUID uuid;

	public Perms(UUID uuid,String group, ArrayList<String> arrayList){
		try {
			this.uuid = uuid;
			this.perms = arrayList;
			this.group = (Group) Class.forName("fr.maxcraft.player.permissions.groups."+group).getConstructor().newInstance();
			for (String p : this.group.getPermissions())
				this.perms.add(p);
		
	} catch (Exception e) {
		e.printStackTrace();
		Main.logError("Erreur au chargement des perms");
	}
	}

	public static Perms load(UUID uuid) {
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `perms` WHERE `perms`.`id` = '"+uuid.toString()+"'",true);
		try {
			if (!r.isFirst())
				return new Perms(uuid,"Citoyen",Serialize.ArrayStringFromString(""));
		return new Perms(uuid,r.getString("group"),Serialize.ArrayStringFromString(r.getString("perms")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Perms(uuid,"Citoyen",Serialize.ArrayStringFromString(""));
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	public boolean hasPerms(String s){
		if( this.perms.contains(s))
			return true;
		return false;
	}
	
	public String dysplayName(){
		return this.getGroup().getColor()+this.getGroup().getPrefix()+User.get(this.uuid).getName();
	}
}
