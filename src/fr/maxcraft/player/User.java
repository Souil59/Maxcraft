package fr.maxcraft.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.maxcraft.Main;
import fr.maxcraft.player.faction.Faction;
import fr.maxcraft.player.jobs.Jobs;
import fr.maxcraft.player.magicraft.Mage;
import fr.maxcraft.player.moderation.Moderation;
import fr.maxcraft.player.permissions.Perms;
import fr.maxcraft.server.economy.Account;
import fr.maxcraft.server.zone.Owner;
import fr.maxcraft.utils.MySQLSaver;
import fr.maxcraft.utils.PacketsUtil;

public class User implements Owner {

	private static ArrayList<User> playerlist = new ArrayList<User>();
	private UUID uuid;
	private String name;
	private Moderation moderation;
	private Jobs jobs;
	private boolean active;
	private double balance;
	private Perms perms;
	private Mage mage;
	private Faction faction;

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public User(UUID uuid,String name,double balance, boolean active){
		this.uuid = uuid;
		this.name = name;
		this.balance=balance;
		this.active=active;
		this.moderation = Moderation.load(uuid);
		this.jobs = Jobs.load(uuid);
		this.mage = Mage.load(uuid);
		this.perms = Perms.load(uuid);
		playerlist.add(this);
	}
	// My SQL statements
	
		public Mage getMage() {
		return mage;
	}

		private static User load(UUID uuid) {
			ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `player` WHERE `player`.`id` = '"+uuid.toString()+"';",true);
			if (r==null){
				Main.log("Player not found, creating new instance");
				return null;
			}
			try {
				if (r.isFirst()){
				return new User(UUID.fromString(r.getString("id")),r.getString("name"),r.getInt("balance"),r.getBoolean("actif"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			User u = new User(uuid,Bukkit.getPlayer(uuid).getName(),1000,true);
			u.sqlInsert();
			return u;
		}

		public void sqlInsert() {
			MySQLSaver.mysql_update("INSERT INTO `player` (`id`, `name`, `balance`, `actif`) VALUES ('"+this.uuid+"', '"+this.name+"', '"+this.balance+"', "+this.active+");");
			
		}

		public static void loadActive() {
			ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `player` WHERE `player`.`actif` = '1';",false);
			try {
			while (r.next()){
				UUID uuid = UUID.fromString(r.getString("id"));
				new User(uuid,r.getString("name"),r.getInt("balance"),r.getBoolean("actif"));
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Main.log("Joueurs chargés ("+playerlist.size()+" joueurs).");
		}
		public void sendMessage(String string){
			if (this.getPlayer()!=null)
				this.getPlayer().sendMessage(string);
		}
		public void sendMessage(String message,String popup){
			if (this.getPlayer()!=null){TextComponent m = new TextComponent(message);
			m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(popup).create() ));
			this.getPlayer().spigot().sendMessage(m);
			}
		}
		public void sendNotifMessage(String string){
			if (this.getPlayer()!=null){
				PacketsUtil.sendActionBar(this.getPlayer(), string);
				this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.NOTE_PIANO, 20f , 1.5f);
				this.getPlayer().sendMessage(string);
			}
		}

		public void sendNotifMessage(TextComponent message) {
			if (this.getPlayer()!=null){
				PacketsUtil.sendActionBar(this.getPlayer(), message.getText());
				this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.NOTE_PIANO, 20f , 1.5f);
				this.getPlayer().spigot().sendMessage(message);
			}
		}

		public Player getPlayer() {
			for (Player p : Bukkit.getOnlinePlayers())
				if (p.getUniqueId().equals(this.getUuid()))
					return p;
			return null;
		}

		public static User get(String string) {
			if (string == null)
				return null;
			if (string.length()==36)
				return get(UUID.fromString(string));
			for (User p : playerlist)
				if (p.name.equals(string))
					return p;
			return null;
		}

		public static User get(UUID uuid) {
			if (uuid!=null)
				for (User p : playerlist)
					if (p.uuid.equals(uuid))
						return p;
			return null;
		}

		public static User get(Player pl) {
			for (User p : playerlist)
				if (p.getUuid().equals(pl.getUniqueId()))
					return p;
			return load(pl.getUniqueId());
		}
		
		public Jobs getJob() {
			return jobs;
		}

		public Perms getPerms() {
			return perms;
		}

		public void setJob(Jobs jobs) {
			this.jobs = jobs;
		}

		public UUID getUuid() {
			return uuid;
		}

		public String getName() {
			return name;
		}

		public Moderation getModeration() {
			return moderation;
		}

		public boolean isActive() {
			return active;
		}
		
		public boolean isConnect(){
			if (this.getPlayer()==null)
				return false;
			return true;
		}

		@Override
		public boolean take(double d) {
			if (this.balance<d){
				this.sendNotifMessage("Vous n'avez pas assez d'argent.");
				return false;
			}
			this.balance-=d;
			this.sendNotifMessage("Vous avez payer "+d+" POs.");
			return true;
		}

		@Override
		public double getBalance() {
			return this.balance;
		}

		@Override
		public void give(double d) {
			this.balance+=d;
			this.sendNotifMessage("Vous avez recu "+d+" POs.");
			
		}

		@Override
		public boolean pay(double d, Account to) {
			if (this.take(d))
				return false;
			to.give(d);
			return true;
		}
}
