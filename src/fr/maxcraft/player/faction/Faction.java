package fr.maxcraft.player.faction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import fr.maxcraft.player.User;
import fr.maxcraft.server.economy.Account;
import fr.maxcraft.server.zone.Owner;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.Serialize;
import fr.maxcraft.Main;

public class Faction implements Owner{

	private static ArrayList<Faction> factionlist = new ArrayList<Faction>();
	private String name;
	private UUID id;
	private String TAG;
	private User owner;
	private ArrayList<User> members = new ArrayList<User>();
	private Location spawn;
	private Location jail;
	private double balance;
	private Team team;
	private ItemStack banner;

	@SuppressWarnings("deprecation")
	public Faction(String id,String name,String TAG,double balance,Location spawn,Location jail,User owner,ArrayList<User> heads,
			ArrayList<User> members,ArrayList<User>recruits,ItemStack banner){
		this.setName(name);
		this.id = UUID.fromString(id);
		this.TAG = TAG;
		this.owner = owner;
		this.setBalance(balance);
		this.spawn = spawn;
		this.jail = jail;
		this.banner = banner;
		if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(TAG)!=null)
			this.team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(TAG);
		else{
			this.team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(TAG);
			this.team.setAllowFriendlyFire(false);
			this.team.setCanSeeFriendlyInvisibles(true);
			this.team.setNameTagVisibility(NameTagVisibility.ALWAYS);
		}
		owner.setFaction(this);
		for (User u : members){
			u.setFaction(this);
			this.team.addPlayer(u.getPlayer());
		}
		factionlist.add(this);
	}
	public static void load(){
		ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `faction`", false);
		try {
		while (r.next()){
		Location spawn = Serialize.locationFromString(r.getString("spawn"));
		Location jail = Serialize.locationFromString(r.getString("jail"));
		new Faction(r.getString("id"),r.getString("name"),r.getString("tag"),r.getDouble("balance"),spawn,jail, User.get(r.getString("owner")), Serialize.usersFromString(r.getString("heads")), Serialize.usersFromString(r.getString("members")), Serialize.usersFromString(r.getString("recruits")),Serialize.stringToItemStack(r.getString("banner")));

		}
		} catch (SQLException e) {
			e.printStackTrace();
			Main.logError("Erreur au chargement des factions");
		}
		Main.log("Faction chargees. ("+factionlist.size()+" factions)");
	}
	public String getTAG() {
		return TAG;
	}
	public User getOwner() {
		return owner;
	}
	public ArrayList<User> getMembers() {
		return members;
	}
	public Location getSpawn() {
		return spawn;
	}
	public Location getJail() {
		return jail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public static Faction get(UUID uuid) {
		for (Faction f : factionlist)
			if (f.id.equals(uuid))
				return f;
		return null;
	}
	@Override
	public boolean take(double d) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void give(double d) {
		this.balance += d;
		MySQLSaver.mysql_update("UPDATE `faction` SET `balance` = '"+this.balance+"' WHERE `id` = '"+this.id.toString()+"';");
	}
	@Override
	public boolean pay(double d, Account to) {
		if (this.take(d))
			return false;
		to.give(d);
		return true;
	}
	@Override
	public UUID getUuid() {
		return id;
	}
	@Override
	public boolean isActive() {
		return true;
	}
	public ItemStack getBanner() {
		return this.banner;
	}
	
	public void setBanner(ItemStack s) {
		this.banner = s;
		MySQLSaver.mysql_update("UPDATE `faction` SET `banner` = '"+Serialize.itemstackToString(s)+"' WHERE `id` = '"+this.id.toString()+"';");

	}
	public void setJail(Location l ){
		this.jail = l;
		MySQLSaver.mysql_update("UPDATE `faction` SET `jail` = '"+Serialize.locationToString(l)+"' WHERE `id` = '"+this.id.toString()+"';");
	}
	public void setSpawn(Location l ){
		this.spawn = l;
		MySQLSaver.mysql_update("UPDATE `faction` SET `spawn` = '"+Serialize.locationToString(l)+"' WHERE `id` = '"+this.id.toString()+"';");
	}
}
