package fr.maxcraft.player.jobs;

import fr.maxcraft.server.zone.Zone;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;
import fr.maxcraft.player.jobs.jobstype.Farmer;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;


public class JobsListener implements Listener {


	public JobsListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onbreak(BlockBreakEvent e) {
        if (Zone.getZone(e.getBlock().getLocation())!=null)
            if (!Zone.getZone(e.getBlock().getLocation()).canBuild(e.getPlayer()))
                e.setCancelled(true);
		if (e.isCancelled())
			return;
		if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
			return;
		if (e.getBlock().hasMetadata("player"))
			return;
		User j = User.get(e.getPlayer().getUniqueId());
		if (j.getJob() instanceof Farmer)
			if (((Farmer)j.getJob()).blockList().contains(e.getBlock().getType())){
				Material b = e.getBlock().getType();
				byte d = e.getBlock().getData();
				double blockrate = ((Farmer)j.getJob()).blockRate();
				double rate = Math.random()*100;
				if (blockrate<100&&rate>blockrate)
					e.getBlock().setType(Material.AIR);
				if (blockrate>100&&rate<blockrate-100){
					e.getBlock().breakNaturally();
					e.getBlock().setType(b);
					e.getBlock().setData(d);
				}
				((Farmer)j.getJob()).xp();
				return;
			}
		double rate = Math.random()*100;
		if (rate>50)
			e.getBlock().setType(Material.AIR);
	}
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
            return;
        e.getBlock().setMetadata("player",new FixedMetadataValue(Main.getPlugin(),e.getPlayer().getName()));
    }
}
