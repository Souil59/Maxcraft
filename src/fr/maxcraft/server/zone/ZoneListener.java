package fr.maxcraft.server.zone;

import java.awt.Polygon;
import java.util.HashMap;

import fr.maxcraft.server.economy.shop.Shop;
import fr.maxcraft.server.zone.sale.Sale;
import fr.maxcraft.server.zone.sale.SaleType;
import fr.maxcraft.utils.Serialize;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.*;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.world.StructureGrowEvent;

import fr.maxcraft.Main;
import fr.maxcraft.player.User;

public class ZoneListener implements Listener{
	
	public static HashMap<User,Polygon> selections =  new HashMap<User,Polygon>();

    private Main plugin;

		//CONSTRUCTOR
		public ZoneListener(Main plugin){
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
			this.plugin = plugin;
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onZoneSelection(PlayerInteractEvent e){
			Player p = e.getPlayer();
			if(p.getItemInHand().getType().equals(Material.FLINT) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) )
			{
				Location l = e.getClickedBlock().getLocation();
				User m = User.get(e.getPlayer());
				
				if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				{
					selections.remove(m);
					p.sendMessage(message("Sélection réinitialisée !"));
				}
				else if(e.getAction().equals(Action.LEFT_CLICK_BLOCK))
				{
					if(!selections.containsKey(m)||selections.get(m).npoints==0)
						selections.put(m, new Polygon());
					else {
					if (Zone.getZone(l)!=null)
						if (Zone.getZone(new Location(l.getWorld(),selections.get(m).xpoints[0],0,selections.get(m).ypoints[0]))!=null)
							if(!Zone.getZone(l).equals(Zone.getZone(new Location(l.getWorld(),selections.get(m).xpoints[0],0,selections.get(m).ypoints[0])))){
								e.setCancelled(true);
								p.sendMessage(message("Selection impossible, ce point n'est pas dans la même zone que le premier."));
								return;
					}
					if (Zone.getZone(l)==null&&Zone.getZone(new Location(l.getWorld(),selections.get(m).xpoints[0],0,selections.get(m).ypoints[0]))!=null){
						e.setCancelled(true);
						p.sendMessage(message("Selection impossible, ce point n'est pas dans la même zone que le premier."));
						return;
					}
					}
					selections.get(m).addPoint(l.getBlockX(), l.getBlockZ());
					p.sendMessage(message("Position "+selections.get(m).npoints+" enregistrée !"));
				}
					
				e.setCancelled(true);
				
				}
			
		}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignPlace(SignChangeEvent e) throws Exception{
            User u = User.get(e.getPlayer());
            Zone zone = Zone.getZone(e.getBlock().getLocation());
            if (zone==null)
                return;
            if (zone.canCubo(e.getPlayer()))
                if (e.getLine(0).toUpperCase().contains("VENDRE")) {
                    if (!isInt(e.getLine(1))) {
                        u.sendNotifMessage("Le prix de votre terrain doit être entier !");
                        return;
                    }
                    int price = Integer.parseInt(e.getLine(1));
                    if (price <= 0) {
                        u.sendNotifMessage("Le prix de votre terrain doit être strictement positif !");
                        return;
                    }
                    if (zone.isToSell()) {
                        u.sendNotifMessage("La zone est déjà en vente !");
                        return;
                    }
                    if (!zone.canCubo(u.getPlayer())) {
                        u.sendNotifMessage("Vous n'avez pas le droit de vendre ce terrain !");
                        return;
                    }
                    new Sale(zone,price, SaleType.SELL, Serialize.locationToString(e.getBlock().getLocation())).insert();
                }
         }

    @EventHandler
    public void onSignClic(PlayerInteractEvent e){
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&!e.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;
        if (e.getClickedBlock().getType().equals(Material.SIGN_POST)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                for (Sale s : Sale.getSaleliste())
                    if (s.getSign().getBlock().equals(e.getClickedBlock())) {
                        s.sell(User.get(e.getPlayer()));
                        return;
                    }
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK))
                for (Sale s : Sale.getSaleliste())
                    if (s.getSign().getBlock().equals(e.getClickedBlock())) {
                        e.setCancelled(true);
                        return;
                    }

        }
    }

		@EventHandler(priority = EventPriority.NORMAL)
		public void onBreak(BlockBreakEvent e)
		{
			User m = User.get(e.getPlayer());
			if(m == null){
				e.setCancelled(true);
				return;
			}
			Zone z = Zone.getZone(e.getBlock().getLocation());
			if(z != null)
			{
				if(!z.canBuild(e.getPlayer()))
				{
					String tag = "allow-break-"+e.getBlock().getType().name();
					if(!z.hasTag(tag) && ! z.hasTag("allow-break"))
					{
					e.setCancelled(true);
					e.getPlayer().sendMessage(message("Vous n'êtes pas builder sur cette zone !"));
					}
				}
			}
			
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onSoil(PlayerInteractEvent e)
		{
			if(e.getAction() != Action.PHYSICAL)
			{
				return;
			}
			
			if(! e.getClickedBlock().getType().equals(Material.SOIL))
			{
				return;
			}
			
			User m = User.get(e.getPlayer());
			
			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			Zone z = Zone.getZone(e.getClickedBlock().getLocation());
			if(z != null)
			{
				if(! z.canBuild(e.getPlayer()))
				{
					
					if(z.hasTag("allow-soil") && !z.hasTag("allow-break"))
					{
					e.setCancelled(true);
					e.getPlayer().sendMessage(message("Vous n'�tes pas builder sur cette zone !"));
					}
				}
			}
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onBreakFrameOrPainting(HangingBreakByEntityEvent e)
		{
			if(!(e.getEntity() instanceof ItemFrame || e.getEntity() instanceof Painting))
			{
				return;
			}
			

			if(! (e.getRemover() instanceof Player))
			{
				e.setCancelled(true);
			}
			
			Player p = (Player) e.getRemover();
			User m = User.get(p);
			
			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			
			Zone z = Zone.getZone(e.getEntity().getLocation());
			if(z != null)
			{
				if(!z.canBuild(p) && !z.hasTag("allow-break") && !z.hasTag("allow-break-hangings"))
				{
					e.setCancelled(true);
					p.sendMessage(message("Vous n'�tes pas builder sur cette zone !"));
				}
			}
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlaceFrameOrPainting(HangingPlaceEvent e)
		{
			if(!(e.getEntity() instanceof ItemFrame || e.getEntity() instanceof Painting))
			{
				return;
			}
			
		
			
			Player p = e.getPlayer();
			User m = User.get(e.getPlayer());
			
			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			
			Zone z = Zone.getZone(e.getEntity().getLocation());
			if(z != null)
			{
				if(! z.canBuild(p) && !z.hasTag("allow-place") && !z.hasTag("allow-place-hangings"))
				{
					e.setCancelled(true);
					p.sendMessage(message("Vous n'�tes pas builder sur cette zone !"));
				}
			}
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onFrameItemRotation(PlayerInteractEntityEvent e)
		{
		
			if(!(e.getRightClicked().getType().equals(Material.ITEM_FRAME)))
			{
				return;
			}
			
			
			Player p = e.getPlayer();
			User m = User.get(e.getPlayer());
			
			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			
			Zone z = Zone.getZone(e.getRightClicked().getLocation());
			if(z != null)
			{
				if(! z.canBuild(p) && !z.hasTag("allow-break") && !z.hasTag("allow-break-hangings"))
				{
					e.setCancelled(true);
					p.sendMessage(message("Vous n'�tes pas builder sur cette zone !"));
				}
			}
			
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlace(BlockPlaceEvent e)
		{
			User m = User.get(e.getPlayer());

			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			Zone z = Zone.getZone(e.getBlock().getLocation());
			if(z != null)
			{
				String tag = "allow-place-"+e.getBlock().getType().name();
				if(!z.hasTag(tag) && ! z.hasTag("allow-place"))
				{
				if(! z.canBuild(e.getPlayer()))
				{
					e.setCancelled(true);
					e.getPlayer().sendMessage(message("Vous n'�tes pas builder sur cette zone !"));
				}
				}
			}
			
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onHunger(FoodLevelChangeEvent e)
		{
			if(!(e.getEntity() instanceof Player)) return;
			Player p = (Player) e.getEntity();
			User m = User.get(p);
			
			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			
			Zone z = Zone.getZone(p.getLocation());
			if(z != null)
			{
				
				if(z.hasTag("no-hunger")&&e.getFoodLevel()!=20)
				{
					e.setCancelled(true);
				}
			}
			
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onBucket(PlayerBucketEmptyEvent e)
		{
			
			User m = User.get(e.getPlayer());
			
			if(m == null)
			{
				// Pas maxcraftien
				e.setCancelled(true);
				return;
			}
			
			
			Zone z = Zone.getZone(e.getBlockClicked().getLocation());
			if(z != null)
			{
				if(! z.canBuild(e.getPlayer()) && !z.hasTag("allow-place"))
				{
					e.setCancelled(true);
					e.getPlayer().sendMessage(message("Vous n'�tes pas builder sur cette zone !"));
				}
			}
			
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL)
		public void onIgnite(BlockIgniteEvent e)
		{
			
		if(e.getPlayer() == null)
		{
			return;
		}
		
		User m = User.get(e.getPlayer());
		
		if(m == null)
		{
			// Pas maxcraftien
			e.setCancelled(true);
			return;
		}
		Zone z = Zone.getZone(e.getBlock().getLocation());
		if(z != null)
		{
			if(!z.canBuild(e.getPlayer()) && ! z.hasTag("allow-fire"))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(message("Vous n'�tes pas builder, ni pyromane sur cette zone !"));
			}
		}
		
		}

        @EventHandler(priority = EventPriority.NORMAL)
         public void onMobSpawn(CreatureSpawnEvent e)
       {

	
	Zone z = Zone.getZone(e.getLocation());
	if (!e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG))
	{
	if(z != null)
	{
		if(z.hasNegativeTag("no-spawn-"+e.getEntityType().name()))
		{
			return;
		}
		
		if((e.getEntity() instanceof Monster || e.getEntity().getType().equals(EntityType.SLIME)) && (z.hasTag("no-spawn-monsters")))
		{
			e.setCancelled(true);
			return;
		}
		
		if(e.getEntity() instanceof Animals && z.hasTag("no-spawn-animals"))
		{
			e.setCancelled(true);
			return;
		}
		
		if(z.hasTag("no-spawn-"+e.getEntityType().name()))
		{
			
			e.setCancelled(true);
			return;
		}
	}
	}
}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent e)
    {

if(!(e.getEntity() instanceof Player))
{
	return;
}

Zone z = Zone.getZone(e.getEntity().getLocation());

if(z != null)
{
	if(z.hasTag("no-damage"))
	{
		e.setCancelled(true);
		return;
	}
	
	if((e.getCause() == DamageCause.PROJECTILE || e.getCause() == DamageCause.POISON || e.getCause() == DamageCause.MAGIC) && z.hasTag("no-pvp"))
	{
		e.setCancelled(true);
		return;
	}
}


}


    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent e)
    {
	
if(!(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
{
return;	
}

Zone z = Zone.getZone(e.getClickedBlock().getLocation());

if(z != null)
{
	if((z.hasTag("no-interact") || e.getItem() != null && (e.getItem().getType().equals(Material.STEP) || e.getItem().getType().equals(Material.WOOD_STEP))) && !z.canBuild(e.getPlayer()))
	{
	
		e.setCancelled(true);
		return;
	}
}


}


    @EventHandler(priority = EventPriority.NORMAL)
    public void onKillCheval(EntityDamageEvent e)
    {
	
if(!(e.getCause().equals(DamageCause.PROJECTILE) || e.getCause().equals(DamageCause.ENTITY_ATTACK) || e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.POISON) || e.getCause().equals(DamageCause.MAGIC)))
{
	return;
}

if(!(e.getEntity().getType().equals(EntityType.HORSE)))
{
	return;
}

Zone z = Zone.getZone(e.getEntity().getLocation());

if(z != null)
{
	if(z.hasTag("no-kill-horses"))
	{
		e.setCancelled(true);
		return;
	}
}
}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderpearl(PlayerTeleportEvent e)
    {
	Zone z = Zone.getZone(e.getTo());
	if (z != null)
		if (z.hasTag("no-hunger")){
			e.getPlayer().setHealth(20);
			e.getPlayer().setFoodLevel(20);
			
		}
	if(! e.getCause().equals(TeleportCause.ENDER_PEARL))
	{
		return;
	}
	
	User m = User.get(e.getPlayer());
	
	if(m == null)
	{
		// Pas maxcraftien
		e.setCancelled(true);
		return;
	}
	if(z != null)
	{
		if(! z.canBuild(e.getPlayer()))
		{
			
			if(z.hasTag("no-enderpearl-tp"))
			{
			e.setCancelled(true);
			e.getPlayer().sendMessage(message("Vous ne pouvez pas utiliser d'ender pearl dans cette zone !"));
			}
		}
	}
	
}


    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemFrameRightClick(PlayerInteractEntityEvent e){
	
	if(!( e.getRightClicked() instanceof ItemFrame))
	{
		return;
	}
	
	Zone z = Zone.getZone(e.getRightClicked().getLocation());
	
	if(z == null) return;
	
	if(!z.canBuild(e.getPlayer()) && !z.hasTag("allow-break") && !z.hasTag("allow-break-hangings"))
	{
		e.setCancelled(true);
		return;
	}
	
}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {

	//Item Frame
	if(e.getEntity() instanceof ItemFrame)
	{
		
		Zone z = Zone.getZone(e.getEntity().getLocation());
		
		if(z != null)
		{
			if(!(e.getDamager() instanceof Player))
			{
				e.setCancelled(true);
				return;
			}
			
			Player p = (Player) e.getDamager();
			if(!z.canBuild(p) && !z.hasTag("allow-break") && !z.hasTag("allow-break-hangings"))
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	
	//PVP
	if(e.getDamager().isOp())
	{
		return;
	}
	
	if(!(e.getDamager() instanceof Player))
	{
		return;
	}
	
	if(!(e.getEntity() instanceof Player))
	{
		return;
	}
	
	
	Zone z = Zone.getZone(e.getEntity().getLocation());
	
	if(z != null)
	{
		if(!(z.hasTag("no-pvp")))
		{
			return;
		}
		e.setCancelled(true);
		
	}
	
	}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent e)
    {
	
	Zone z = Zone.getZone(e.getEntity().getLocation());
	
	if(z != null)
	{
		if(z.hasTag("no-kill-animals") && e.getEntity() instanceof Animals)
		{
			e.setCancelled(true);
			return;
		}
		
		if(z.hasTag("no-kill-monsters") && e.getEntity() instanceof Monster)
		{
			e.setCancelled(true);
			return;
		}
	}
}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFarmProtect(PlayerInteractEvent e)
    {
	
	if(!(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL))
	{
		return;
	}
        
	Zone z = Zone.getZone(e.getClickedBlock().getLocation());
	
	if(z != null)
	{
		if(!(z.hasTag("farm")))
		{
			return;
		}
		

		
		User m = User.get(e.getPlayer());
		
		
		if(m != null && z.canBuild(e.getPlayer()))
		{
			return;
		}
		
		
		e.setCancelled(true);
		
		}
	
}

    @EventHandler
    public void BlockPhysicsEvent(BlockPhysicsEvent e)
    {
	if(e.getBlock().getType().equals(Material.SOIL))
		if (Zone.getZone(e.getBlock().getLocation())!=null)
			if (Zone.getZone(e.getBlock().getLocation()).hasTag("farm"))
					e.setCancelled(true);;
}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnterLeave(PlayerMoveEvent e)
    {
	Zone z1 = Zone.getZone(e.getFrom());
	Zone z2 = Zone.getZone(e.getTo());
	
	if(z1 != z2)
	{
		Player p = e.getPlayer();
		User u = User.get(p);
	
		//Messages
		if (z1!=null)
			if (z1.getParent()!=null)
				if (z1.getParent().equals(z2))
					return;
		if(z2 != null && z2.getTags().contains("enter-message"))
		{
			if(z2.getOwner()!=null){
				u.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "* "+ ChatColor.GOLD + z2.getName() + ChatColor.WHITE + " de "+ z2.getOwner().getName(),z2.description());
			}
			else{
				u.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + "*  "+ ChatColor.GOLD + z2.getName(),z2.description());	
			}
		}
		
		if(z1 != null && z1.hasTag("fly")){
			p.setFlying(false);
			p.sendMessage(message("Vous �tes sorti de la zone de fly."));
		}
		
		if(z2 != null && z2.hasTag("fly") && z2.canBuild(p)){
			p.setFlying(true);
			p.sendMessage(message("Vous �tes entr� dans une zone de fly."));
		}
		
		
	}
}
	
	/*
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerRespawnEvent e)
	{
		Zone z = Zone.getZone(e.getPlayer().getLocation());
		if(z != null)
		{
			for(String tag : z.getTags())
			{
				if(tag.startsWith("respawn-at-"))
				{
					String[] parts = tag.split("-");
					if(this.plugin.MM.isInt(parts[2]))
					{
						Lieu l = this.plugin.MM.getLieu(Integer.parseInt(parts[2]));
						if(l != null)
						{
							e.setRespawnLocation(l.getLocation());
							return;
						}
					}
				}
			}
		}
		
		
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeco(PlayerQuitEvent e)
	{
		Zone z = Zone.getZone(e.getPlayer().getLocation());
		if(z != null)
		{
			for(String tag : z.getTags())
			{
				if(tag.startsWith("respawn-at-"))
				{
					String[] parts = tag.split("-");
					if(this.plugin.MM.isInt(parts[2]))
					{
						Lieu l = this.plugin.MM.getLieu(Integer.parseInt(parts[2]));
						if(l != null)
						{
							e.getPlayer().teleport(l.getLocation());
							return;
						}
					}
				}
			}
		}
		
	}
	*/
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onTreeGrow(StructureGrowEvent e)
	{
		Zone z = Zone.getZone(e.getLocation());
		if(z != null)
		{
			if(z.hasNegativeTag("no-trees-"+e.getSpecies().name())) return;
			
			if(z.hasTag("no-trees") || z.hasTag("no-trees-"+e.getSpecies().name()))
			{
				e.setCancelled(true);
			}
		}
	
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockGrow(BlockGrowEvent e)
	{
		Zone z = Zone.getZone(e.getBlock().getLocation());
		if(z != null)
		{
		
			if(z.hasNegativeTag("no-growth-"+e.getNewState().getType().name())) return;
			if(z.hasTag("no-growth") || z.hasTag("no-growth-"+e.getNewState().getType().name()))
			{
				e.setCancelled(true);
			}
		}
	
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBonemeal(PlayerInteractEvent e)
	{
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}
		
		if(e.getItem() == null || !e.getItem().getType().equals(Material.INK_SACK))
		{
			return;
		}
		
		User m = User.get(e.getPlayer());
		
		if(m == null)
		{
			// Pas maxcraftien
			e.setCancelled(true);
			return;
		}
		Zone z = Zone.getZone(e.getClickedBlock().getLocation());
		if(z != null)
		{
			
			if(z.hasNegativeTag("no-growth-"+e.getClickedBlock().getType().name())) return;
			if(z.hasNegativeTag("no-growth-LONG_GRASS") && e.getClickedBlock().getType().equals(Material.GRASS)) return;
			if(z.hasTag("no-growth") || z.hasTag("no-growth-"+e.getClickedBlock().getType().name()))
			{
				e.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBoatBreak(VehicleDestroyEvent e)
	{
		Zone z = Zone.getZone(e.getVehicle().getLocation());
		
		if(z == null) return;
		
		if(! z.hasTag("no-break-vehicles")) return;
	
	
		if(e.getVehicle().getPassenger() instanceof Player)
		{
			e.setCancelled(true);
		}
		
	}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPistonExtend(BlockPistonExtendEvent e){

        Zone zPiston = Zone.getZone(e.getBlock().getLocation());
        Zone z;

        for (Block b : e.getBlocks()){

            Block bl = b.getRelative(e.getDirection());

            z = Zone.getZone(bl.getLocation());

            if (zPiston.equals(z)){

                for (Shop s : this.plugin.getShopManager().getShops()){

                    if (b.getLocation().distance(s.getItemFrame().getLocation()) == 1){
                        e.setCancelled(true);
                        return;
                    }
                }

                continue;
            }

            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPistonRetract(BlockPistonRetractEvent e){
        if (!e.isSticky()) return;

        Zone zPiston = Zone.getZone(e.getBlock().getLocation());
        Zone z = null;

        for (Block b : e.getBlocks()){

            z = Zone.getZone(b.getLocation());

            if (zPiston.equals(z)){

                for (Shop s : this.plugin.getShopManager().getShops()){

                    if (b.getLocation().distance(s.getItemFrame().getLocation()) == 1){
                        e.setCancelled(true);
                        return;
                    }
                }

                continue;
            }

            e.setCancelled(true);
            return;
        }

    }


	private String message(String message)
	{
		return ChatColor.AQUA + "[Zones] " + ChatColor.GRAY + message;
	}
    public boolean isInt(String chaine) {
        try {
            Integer.parseInt(chaine);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


}