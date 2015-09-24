package fr.maxcraft.server.world;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxcraft.Main;

public class Travel implements CommandExecutor {
	
	static ArrayList<Travel> travel = new ArrayList<Travel>();
	static ArrayList<Player> traveler = new ArrayList<Player>();
	private Marker destination1;
	private Marker destination2;
	private Marker attente;
	
	public Travel(String args, String args2, String args3){
		this.destination1=Marker.marker.get(args);
		this.destination2=Marker.marker.get(args2);
		this.attente=Marker.marker.get(args3);
		travel.add(this);
	}



	public Travel() {
	}



	public static void register(Main main) {
		main.getCommand("travel").setExecutor(new Travel());
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,
			String[] args) {
			if (args.length!=3){
				sender.sendMessage("/travel <marker 1> <marker2> <marker intermediaire>");
			}
			new Travel(args[0],args[1],args[2]);
		return true;
	}


	public static void playerMove(Location from, Location to,Player p) {
		for (Travel t : travel){
			if (t.destination1.getLocation().distance(from)<3 && t.destination1.getLocation().distance(to)>3 &&t.destination1.getLocation().getWorld().equals(to.getWorld())){
				new TravelTask(p,t.attente.getLocation(),"Profitez du voyage pour admirer la vue !").runTaskLater(Main.getPlugin(), 100);
				new TravelTask(p,t.destination2.getLocation(),"Vous pouvez descendre du bateau, allez y avant qu'il ne reparte !").runTaskLater(Main.getPlugin(), 700);
			}
			else if(t.destination2.getLocation().distance(from)<5 && t.destination2.getLocation().distance(to)>5 &&t.destination2.getLocation().getWorld().equals(to.getWorld())){
				new TravelTask(p,t.attente.getLocation(),"Profitez du voyage pour admirer la vue !").runTaskLater(Main.getPlugin(), 100);
				new TravelTask(p,t.destination1.getLocation(),"Vous pouvez descendre du bateau, allez y avant qu'il ne reparte !").runTaskLater(Main.getPlugin(), 700);
			}
		}	
		
	}
}
