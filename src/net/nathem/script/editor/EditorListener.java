package net.nathem.script.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import net.nathem.script.core.Map;
import net.nathem.script.core.Utils;
import net.nathem.script.core.sign.OutputSignal;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.editor.event.SignBreakEvent;
import net.nathem.script.editor.event.SignPlaceEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class EditorListener implements Listener{

	private Editor editor;

	public EditorListener(Editor editor) {


		this.editor = editor;
	}
	
	// PRIMARY EVENTS
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent e)
	{
		if(this.editor.getWorlds().keySet().contains(e.getBlock().getWorld()) && e.getLine(0).startsWith("="))
		{
			Bukkit.getPluginManager().callEvent(new SignPlaceEvent(e, this.editor));
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent e)
	{
		Map map = this.editor.getMap(e.getBlock().getWorld());
		if(map == null) return;
		
		Sign sign = map.getSign(e.getBlock().getLocation());
		if(sign == null) return;
		
		Bukkit.getPluginManager().callEvent(new SignBreakEvent(e, this.editor, map, sign));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignSelection(PlayerInteractEvent e)
	{
		Map map = this.editor.getMap(e.getPlayer().getWorld());
		if(map == null) return;
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if(!(e.getClickedBlock().getType().equals(Material.SIGN_POST) || e.getClickedBlock().getType().equals(Material.WALL_SIGN))) return;
		Sign sign = map.getSign(e.getClickedBlock().getLocation());
		if(sign == null) return;
		
		// Save selection
		this.editor.getSelections().put(e.getPlayer(), sign);
		EditorCommand infos = new EditorCommand(this.editor, e.getPlayer(), null);
		infos.signInfo(sign, map);
		
	}
	
	// SECONDARY EVENTS
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onScriptSignPlaced(SignPlaceEvent e)
	{
		if(!e.getHandler().getPlayer().hasPermission("nathem.script.editor")) 
		{
			EditorCommand.message(e.getHandler().getPlayer(), "You need permission nathem.script.editor.* or nathem.script.editor."+ e.getHandler().getBlock().getWorld().getName() + " to script this map.");
			e.setCancelled(true);
			return;
		}
		
		org.bukkit.material.Sign signBlock = (org.bukkit.material.Sign) e.getHandler().getBlock().getState().getData();
		Map map = e.getEditor().getMap(e.getHandler().getBlock().getWorld());
		Sign sign = new Sign(UUID.randomUUID(), e.getHandler().getBlock().getLocation(), e.getHandler().getLine(0), signBlock.getFacing(), signBlock.isWallSign());
		
		if(sign.getObjectType() == null)
		{
			EditorCommand.message(e.getHandler().getPlayer(), "Type "+ sign.getTypeName() + " doesn't exist !");
			e.setCancelled(true);
			return;
		}
		
		// Inputs, Outputs
		sign.setInputsSignals(Utils.readSignIO(e.getHandler().getLine(1), map));
		ArrayList<Integer> outputsOn = Utils.readSignIO(e.getHandler().getLine(2), map);
		ArrayList<Integer> outputsOff = Utils.readSignIO(e.getHandler().getLine(3), map);
		
		for(int output : outputsOff)
		{
			sign.getOutputSignals().add(new OutputSignal(output, false));
		}
		
		for(int output : outputsOn)
		{
			sign.getOutputSignals().add(new OutputSignal(output, true));
		}
		
		
		map.getSigns().add(sign);
		
		try {
			map.writeSigns();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		this.editor.buildSign(sign, e.getHandler().getBlock().getWorld());
		
		String[] lines = this.editor.buildLines(sign);
		e.getHandler().setLine(0, lines[0]);
		e.getHandler().setLine(1, lines[1]);
		e.getHandler().setLine(2, lines[2]);
		e.getHandler().setLine(3, lines[3]);
		
		this.editor.getSelections().put(e.getHandler().getPlayer(), sign);
		EditorCommand infos = new EditorCommand(this.editor, e.getHandler().getPlayer(), null);
		infos.signInfo(sign, map);
		
		EditorCommand.message(e.getHandler().getPlayer(), "Sign "+ sign.getTypeName()+ " added");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onScriptSignBroke(SignBreakEvent e)
	{
		if(!e.getHandler().getPlayer().hasPermission("nathem.script.editor")) 
		{
			EditorCommand.message(e.getHandler().getPlayer(), "You need permission nathem.script.editor.* or nathem.script.editor."+ e.getHandler().getBlock().getWorld().getName() + " to script this map.");
			e.setCancelled(true);
			return;
		}
		
		e.getMap().getSigns().remove(e.getSign());
		
		try {
			e.getMap().writeSigns();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		EditorCommand.message(e.getHandler().getPlayer(), "Sign "+ e.getSign().getTypeName()+ " removed");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		
		// JOIN INSTANCE
		Instance i = this.editor.getInstance(e.getTo().getWorld());
		if(i != null && !e.getTo().getWorld().equals(e.getFrom().getWorld()))
		{
			i.getBackLocations().put(e.getPlayer(), e.getPlayer().getLocation());
		}
	}

}
