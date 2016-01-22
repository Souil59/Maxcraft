package fr.maxcraft.player.faction;

import fr.maxcraft.player.User;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.maxcraft.Main;
import org.bukkit.scoreboard.Scoreboard;

public class FactionListener implements Listener {
	
	public FactionListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onco(PlayerJoinEvent e){
		User u = User.get(e.getPlayer());
		if (u.getFaction()==null)
			return;
		Scoreboard s = u.getPlayer().getScoreboard();
		for (Faction f : u.getFaction().getAllies()){
			s.getTeam(f.getTAG()).setPrefix(ChatColor.GREEN+f.getTAG()+" ");
		}
		for (Faction f : u.getFaction().getEnnemies()){
			s.getTeam(f.getTAG()).setPrefix(ChatColor.RED+f.getTAG()+" ");
		}
		s.getTeam(u.getFaction().getTAG()).setPrefix(ChatColor.GREEN+u.getFaction().getTAG()+" ");
		u.getPlayer().setScoreboard(s);
	}
}
