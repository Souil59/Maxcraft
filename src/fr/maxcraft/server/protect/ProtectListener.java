package fr.maxcraft.server.protect;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.material.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.maxcraft.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProtectListener implements Listener {

	public ProtectListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
		}

	@EventHandler
	public void onclic(PlayerInteractEvent e) {
		if (e.getClickedBlock()!=null)
			if (Lock.isProtected(e.getClickedBlock().getType()))
				if (!Lock.canUse(e.getPlayer(),e.getClickedBlock(),e.getClickedBlock().getLocation())&&!Lock.canAdmin(e.getPlayer())){
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.BLUE + "Ceci ne vous appartient pas!");
				}
	}
	@EventHandler
	public void onbreak(BlockBreakEvent e) {
		if (e.getBlock()!=null){
			if (e.getBlock().getType().equals(Material.WALL_SIGN))
				if (Lock.isProtected(e.getBlock().getRelative(((Sign)e.getBlock().getState()).getAttachedFace()).getType()))
					if (!Lock.canUse(e.getPlayer(),e.getBlock().getRelative(((Sign)e.getBlock()).getAttachedFace()),e.getBlock().getRelative(((Sign)e.getBlock()).getAttachedFace()).getLocation())&&!Lock.canAdmin(e.getPlayer())){
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.BLUE + "Ceci ne vous appartient pas!");
					}
			if (Lock.isProtected(e.getBlock().getType()))
				if (!Lock.canUse(e.getPlayer(),e.getBlock(),e.getBlock().getLocation())&&!Lock.canAdmin(e.getPlayer())){
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.BLUE + "Ceci ne vous appartient pas!");
				}
		}
	}
	@EventHandler
	public void onsign(SignChangeEvent e){
		if (e.getLine(0).equalsIgnoreCase("[private]")){
			if (!e.getPlayer().hasPermission("maxcraft.modo"))
				e.setLine(1,e.getPlayer().getName());
			e.setLine(0, "[private]");
		}
	}
	@EventHandler
	public void onsignplace(BlockPlaceEvent e){
		if (e.getBlock().equals(Material.WALL_SIGN))
				if (Lock.isProtected(e.getBlock().getRelative(((Sign)e.getBlock()).getAttachedFace()).getType()))
					if (!Lock.canUse(e.getPlayer(),e.getBlock().getRelative(((Sign)e.getBlock()).getAttachedFace()),e.getBlock().getRelative(((Sign)e.getBlock()).getAttachedFace()).getLocation())&&!Lock.canAdmin(e.getPlayer())){
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.BLUE + "Ceci ne vous appartient pas!");
					}
	}
	@EventHandler
	public void onclic(PlayerInteractAtEntityEvent e) {
		if ((e.getRightClicked() instanceof ArmorStand)) {
			if (!Lock.canUse(e.getPlayer(),e.getRightClicked().getLocation().getBlock(),e.getRightClicked().getLocation())&&!Lock.canAdmin(e.getPlayer())){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.BLUE + "Ceci ne vous appartient pas!");
			}
		}
	}
	@EventHandler
	public void ondamage(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof ArmorStand)) {
			if (!Lock.canUse((Player) e.getDamager(),e.getEntity().getLocation().getBlock(),e.getEntity().getLocation())&&!Lock.canAdmin((Player) e.getDamager())){
				e.setCancelled(true);
				e.getDamager().sendMessage(ChatColor.BLUE + "Ceci ne vous appartient pas!");
			}
		}
	}

    @EventHandler
    public void onExplose(EntityExplodeEvent e){
        ArrayList<Block> lb = new ArrayList<Block>();
        for (Block b : e.blockList())
            if (!b.getType().equals(Material.TNT))
                lb.add(b);
        e.blockList().removeAll(lb);
    }
}
