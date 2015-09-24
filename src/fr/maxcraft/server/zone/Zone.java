package fr.maxcraft.server.zone;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.player.faction.Faction;
import fr.maxcraft.utils.MySQLSaver;

public class Zone {

	static ArrayList<Zone> zonelist = new ArrayList<Zone>();
	private int id;
	private int parentid;
	private String name;
	private Polygon polygon;
	private Owner owner;
	private ArrayList<String> tag = new ArrayList<String>();
	private ArrayList<User> builder = new ArrayList<User>();
	private ArrayList<User> cubo = new ArrayList<User>();
	private String world;

	public Zone (int id,String name,Owner owner,Polygon p,String worldname,int parent,ArrayList<String> tag,ArrayList<User> builder,ArrayList<User> cubo, Owner tenant, double rent , boolean payed){
		this.id = id;
		this.name = name;
		this.polygon = p;
		this.parentid = parent;
		this.owner = owner;
		if (tag!=null)
			this.tag = tag;
		if (builder!=null)
			this.builder = builder;
		if (cubo != null)
			this.cubo = cubo;
		this.world=worldname;
		zonelist.add(this);
	}
	
	public static void load() throws SQLException{
			ResultSet r = MySQLSaver.mysql_query("SELECT * FROM `zone`",false);
			while (r.next()){
			try {
			Owner o = null;
			if (r.getString("Owner")!=null){
				if (User.get(r.getString("Owner")) != null)
					o = User.get(r.getString("Owner"));
				if (Faction.get(UUID.fromString(r.getString("Owner"))) != null)
					o = Faction.get(UUID.fromString(r.getString("Owner")));
			}
			ArrayList<User> builder = new ArrayList<User>();
			ArrayList<User> cubo = new ArrayList<User>();
			ArrayList<String> tag = null;
			Polygon p = new Polygon();
			String[] s = r.getString("points").split(";");
			
			for (String s1 : s)
				p.addPoint(Integer.parseInt(s1.substring(0, s1.lastIndexOf(":"))),Integer.parseInt(s1.substring(s1.lastIndexOf(":")+1,s1.length())));
			if (r.getString("tags")!=null)
				tag =new ArrayList<String>(Arrays.asList(r.getString("tags").split(";")));
			
			new Zone(r.getInt("id"), r.getString("name"),o, p,r.getString("world"), r.getInt("parent")
					, tag, builder, cubo,User.get(r.getString("tenant")),r.getDouble("rent"),true);

			} catch (Exception e) {
			e.printStackTrace();
			Main.logError("Erreur au chargement de la zone : "+r.getInt("id"));
			}
		}
			Main.log("Zones chargees. ("+Zone.zonelist.size()+" zones.)");
	}

	public static Zone getZone(Location l) {
		Zone smalest = null;
		for (Zone z : zonelist){
			if(!l.getWorld().getName().equals(z.world))
				continue;
			if (z.isInside(l.getBlockX(), l.getBlockZ())){
				if (smalest == null)
					smalest = z;
				else if (z.getArea()<smalest.getArea())
					smalest = z;
			}
		}
		return smalest;
	}

	private boolean isInside(int x, int y) {
		if (this.polygon.contains(x, y))
			return true;
		int j = polygon.npoints-1;
		for (int i = 0;i<polygon.npoints;i++){
			if (Line2D.Double.ptSegDist(polygon.xpoints[i],polygon.ypoints[i],polygon.xpoints[j],polygon.ypoints[j], x, y)==0)
				return true;
			j=i;
		}
		return false;
	}

	public int getArea() {
		double area = 0;
		int j = this.polygon.npoints-1;
		for (int i = 0;i<this.polygon.npoints;i++){
			area += (this.polygon.xpoints[j]+this.polygon.xpoints[i]) * (this.polygon.ypoints[j]-this.polygon.ypoints[i]);
			j=i;
		}
		area = Math.abs(area/2);
		for (int i = 0;i<this.polygon.npoints;i++){
			area +=(Point.distance(this.polygon.xpoints[i],this.polygon.ypoints[i],this.polygon.xpoints[j],this.polygon.ypoints[j])/2);
			j=i;
		}
		return (int) area+1;
	}

	public static Zone getZone(int id) {
		for (Zone z : zonelist)
			if(z.id==id)
				return z;
		return null;
	}
	public Point getCenter(){
		int x = 0;
		int z = 0;
		for (int i : this.polygon.xpoints)
			x=x+i;
		for (int i : this.polygon.ypoints)
			z=z+i;
		return new Point(x/this.polygon.npoints,z/this.polygon.npoints);
	}

	public World getWorld() {
		return Bukkit.getServer().getWorld(world);
	}

	public String getName() {
		return name;
	}

	public boolean hasTag(String tag) {
		if (this.tag.contains(tag))
			return true;
		if (this.getParent()!=null)
			if (this.getParent().hasTag(tag))
				return true;
		return false;
	}

	public boolean canBuild(Player player) {
		User u = User.get(player);
		if (this.cubo.contains(u)||this.builder.contains(u)||player.hasPermission("maxcraft.modo"))
			return true;
		if (this.owner!=null)
			if (this.owner.equals(u))
				return true;
		return false;
	}

	public boolean hasNegativeTag(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	public Zone getParent() {
		if (this.parentid==0)
			return null;
		if(getZone(this.parentid)!=null)
			return getZone(this.parentid);
		return null;
	}

	public ArrayList<String> getTags() {
		return tag;
	}

	public Owner getOwner() {
		return this.owner;
	}

	public static String create(Polygon p, User u, String name) {
		World w = u.getPlayer().getWorld();
		Owner o = null;
		if (p.npoints<2)
			return "Vous devez faire une s�lection d'au moins 2 points !";
		if (p.npoints==2){
			int[] x = p.xpoints.clone();
			int[] y = p.ypoints.clone();
			p.reset();
			p.addPoint(x[0], y[0]);
			p.addPoint(x[0], y[1]);
			p.addPoint(x[1], y[1]);
			p.addPoint(x[1], y[0]);
		}
		Zone parent = Zone.getZone(new Location(w,p.xpoints[0],0,p.ypoints[0]));
		if (parent==null && !u.getPlayer().hasPermission("maxcraft.modo"))
			return "Cette s�lection n'est pas dans une zone, vous devez �tre admin.";
		if (parent!=null && !parent.canCubo(u.getPlayer()))
			return "Cette s�lection n'est pas dans une zone o� vous pouvez cuboider !";
		int pid;
		
		if (parent==null)
			pid = 0;
		else {
			pid = parent.id;
			if (parent.owner!=null)
				o = parent.owner;
		}
		Zone z = new Zone(0, name, o, p, w.getName(), pid, null, null, null,null,0,true);
		z.insert();
		return "La zone <"+ ChatColor.GOLD+ name + ChatColor.GRAY +"> A �t� cr�e !";
		
	}

	public boolean canCubo(Player player) {
		if (this.owner!=null)
			if (this.owner.equals(User.get(player)))
				return true;
		if (this.cubo.contains(User.get(player)))
			return true;
		if (player.hasPermission("maxcraft.modo"))
			return true;
		return false;
	}

	private void insert() {
		String points="";
		Object owner = "NULL";
		if (this.owner!=null)
			owner = this.owner.getUuid().toString();
		for (int i = 0;i<this.polygon.npoints;i++)
			points = points+this.polygon.xpoints[i]+":"+this.polygon.ypoints[i]+";";
		MySQLSaver.mysql_update("INSERT INTO `zone` (`id`, `name`,`parent`, `points`, `owner`,  `world`) VALUES"
				+ " (NULL, '"+this.name+"', "+this.parentid+", '"+points+"', "+owner+", '"+this.world+"');");
		
	}
	public String description(){
		String d = "";
		d+=ChatColor.WHITE+"*** <"+ChatColor.GOLD+ this.getName() +ChatColor.GRAY +"> ***";
		d+="\n" + ChatColor.WHITE+"Num�ro : "+ ChatColor.GREEN + this.id;
		d+="\n" + ChatColor.WHITE+"Surface : "+ ChatColor.GREEN +this.getArea()+ " m�.";
		if(this.getOwner() != null)
			d+="\n" + ChatColor.WHITE+"Propri�taire : "+ ChatColor.GREEN + this.owner.getName();
		else
			d+="\n" + ChatColor.WHITE+"Propri�taire : "+ ChatColor.RED + "Aucun";
		if(this.getParent() != null)
			d+="\n" + ChatColor.WHITE+"Localisation : "+ ChatColor.GREEN + this.getParent().getName();
		if (this.cubo.size()>0)
			d+="\n" + ChatColor.WHITE+"Cuboiders : "+ ChatColor.GREEN + this.cubo.toString();
		else
			d+="\n" + ChatColor.WHITE+"Cuboiders : "+ ChatColor.GREEN + "Pas de Cuboiders.";
		if (this.builder.size()>0)
			d+="\n" + ChatColor.WHITE+"Builders : "+ ChatColor.GREEN + this.builder.toString();
		else
			d+="\n" + ChatColor.WHITE+"Builders : "+ ChatColor.GREEN + "Pas de Builders.";
		if(this.getOwner() != null && this.getOwner().isActive())
			d+="\n" + ChatColor.WHITE+"Actif : "+ ChatColor.GREEN + "Oui";
		else if(this.getOwner() != null && ! this.getOwner().isActive())
			d+="\n" + ChatColor.WHITE+"Actif : "+ ChatColor.RED + "Non";
		if (this.tag.size()>0)
			d+="\n" + ChatColor.WHITE+"Tags : "+ ChatColor.GREEN + this.tag.toString().substring(1, this.tag.toString().length()-1);
		else
			d+="\n" + ChatColor.WHITE+"Tags : "+ ChatColor.GREEN + "Pas de Tags.";
		return d;
	}

	public int getId() {
		return this.id;
	} 

	public void setPolygon(Polygon p) {
		this.polygon = p;
		this.save();
	}

	public void reset() {
		this.owner = null;
		this.builder = new ArrayList<User>();
		this.cubo = new ArrayList<User>(); 
		this.save();
	}

	public void save() {
		String points="";	
		String owner = "NULL";
		if (this.owner!=null)
			owner = this.owner.getUuid().toString();
		for (int i = 0;i<this.polygon.npoints;i++)
			points = points+this.polygon.xpoints[i]+":"+this.polygon.ypoints[i]+";";
		MySQLSaver.mysql_update("UPDATE `zone` SET `owner` ='"+owner+"',`points` ='"+points+"',`parent` ="+this.parentid+""
				+ ",`name` ='"+this.name+"' WHERE 'id' = "+this.id);
		
	}

	public void setOwner(User user) {
		this.owner = user;
		this.save();
	}

	public boolean hasDirectlyTag(String string) {
		if (this.getTags().contains(string))
			return true;
		return false;
	}
}
