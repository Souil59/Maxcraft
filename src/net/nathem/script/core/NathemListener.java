package net.nathem.script.core;

import net.nathem.script.core.object.Captor;
import net.nathem.script.core.object.RedstoneSensor;
import net.nathem.script.core.object.Stuff;
import net.nathem.script.core.object.Tag;
import net.nathem.script.enums.ObjectType;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryHolder;

public class NathemListener implements Listener{

	public NSCore plugin;

	public NathemListener(NSCore plugin) {
		super();
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onRedstoneSensor(BlockPhysicsEvent e){
	
		NathemWorld nw = plugin.getNathemWorld(e.getBlock().getWorld());
		if(nw == null) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.REDSTONE_SENSOR))
		{
			if(no.getLocation().getBlock().equals(e.getBlock()))
			{
				RedstoneSensor ns = (RedstoneSensor) no;
				
				if(!ns.isActivated()) return;
				
				if(ns.getSwitchValue() == false && (e.getBlock().isBlockIndirectlyPowered() || e.getBlock().isBlockPowered()))
				{
					ns.powered(true);
				}
				else if(ns.getSwitchValue() == true && !(e.getBlock().isBlockIndirectlyPowered() || e.getBlock().isBlockPowered()))
				{
					ns.powered(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCaptor(PlayerMoveEvent e){
		
		NathemWorld nw = plugin.getNathemWorld(e.getPlayer().getWorld());
		if(nw == null) return;
		
		if(nw.getSpectators().contains(e.getPlayer())) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.CAPTOR))
		{
			Captor captor = (Captor) no;
			if(!captor.isActivated()) continue;
			
			if(captor.isInCaptorArea(e.getTo()) && !captor.isInCaptorArea(e.getFrom()))
			{
				captor.playerCrossLine(e.getPlayer(), true);
			}
			else if(captor.isInCaptorArea(e.getFrom()) && !captor.isInCaptorArea(e.getTo()))
			{
				captor.playerCrossLine(e.getPlayer(), false);
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onStuffAccess(PlayerInteractEvent e){
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) return;
		if(!(e.getClickedBlock().getState() instanceof InventoryHolder)) return;
		NathemWorld nw = plugin.getNathemWorld(e.getPlayer().getWorld());
		if(nw == null) return;
		for(NathemObject no : nw.getObjects(ObjectType.STUFF))
		{
			Stuff stuff = (Stuff) no;
			if(!(stuff.getChest().getBlock().equals(e.getClickedBlock()))) continue;

			if(!stuff.isOpenable())
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onHunger(FoodLevelChangeEvent e){
		
		NathemWorld nw = plugin.getNathemWorld(e.getEntity().getWorld());
		if(nw == null) return;
		
		if(nw.getSpectators().contains(e.getEntity())) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.TAG))
		{
			Tag tag = (Tag)no;
			if (tag.getType().equals("no-hunger"))
				if (tag.isInCaptorArea(e.getEntity().getLocation()))
					e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDamage(EntityDamageEvent e){
		
		NathemWorld nw = plugin.getNathemWorld(e.getEntity().getWorld());
		if(nw == null) return;
		
		if(e.getEntityType()!=EntityType.PLAYER) return;
		
		if(nw.getSpectators().contains(e.getEntity())) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.TAG))
		{
			Tag tag = (Tag)no;
			if((e.getCause() == DamageCause.PROJECTILE || e.getCause() == DamageCause.POISON || e.getCause() == DamageCause.MAGIC) && tag.getType().equals("no-pvp"))
				if (tag.isInCaptorArea(e.getEntity().getLocation()))
					e.setCancelled(true);
			if (tag.getType().equals("no-damage"))
				if (tag.isInCaptorArea(e.getEntity().getLocation()))
					e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		
		NathemWorld nw = plugin.getNathemWorld(e.getEntity().getWorld());
		if(nw == null) return;
		
		if(e.getEntityType()!=EntityType.PLAYER) return;
		if (!(e.getDamager() instanceof Player)) return;
		if(nw.getSpectators().contains(e.getEntity())) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.TAG))
		{
			Tag tag = (Tag)no;
			if (tag.getType().equals("no-pvp"))
				if (tag.isInCaptorArea(e.getEntity().getLocation()))
					e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		NathemWorld nw = plugin.getNathemWorld(e.getPlayer().getWorld());
		if(nw == null) return;
		if(nw.getSpectators().contains(e.getPlayer())) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.TAG))
		{
			Tag tag = (Tag)no;
			if (tag.getType().equals("no-break"))
				if (tag.isInCaptorArea(e.getPlayer().getLocation()))
					if (!e.getPlayer().hasPermission("maxcraft.modo"))
					e.setCancelled(true);
		}
	}
	@EventHandler
	public void onBreak(BlockPlaceEvent e)
	{
		NathemWorld nw = plugin.getNathemWorld(e.getPlayer().getWorld());
		if(nw == null) return;
		if(nw.getSpectators().contains(e.getPlayer())) return;
		
		for(NathemObject no : nw.getObjects(ObjectType.TAG))
		{
			Tag tag = (Tag)no;
			if (tag.getType().equals("no-place"))
				if (tag.isInCaptorArea(e.getPlayer().getLocation()))
					if (!e.getPlayer().hasPermission("maxcraft.modo"))
					e.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnderpearl(PlayerTeleportEvent e)
	{
		NathemWorld nw = plugin.getNathemWorld(e.getPlayer().getWorld());
		if(nw == null) return;
		if(nw.getSpectators().contains(e.getPlayer())) return;
		if(e.getCause().equals(TeleportCause.ENDER_PEARL))
		for(NathemObject no : nw.getObjects(ObjectType.TAG))
		{
			Tag tag = (Tag)no;
			if (tag.getType().equals("no-enderpearl"))
				if (tag.isInCaptorArea(e.getPlayer().getLocation()))
					e.setCancelled(true);
		}
	}
}
