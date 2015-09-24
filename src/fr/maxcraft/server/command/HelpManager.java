package fr.maxcraft.server.command;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxcraft.Main;

public class HelpManager implements CommandExecutor{
	
	private static ArrayList<HelpManager> helper = new ArrayList<HelpManager>();
	private String name;
	private String desc;
	private String usage;
	private String perm;
	private String pack;
	private String clickText;
	private Action clickevent;
	
	public HelpManager(String name){
		this.name = name;
		this.desc = "non renseigné";
		this.usage = "non renseigné";
		this.perm = "maxcraft.base";
		this.pack = "root";
		this.clickText = "";
		this.clickevent = ClickEvent.Action.RUN_COMMAND;
		helper.add(this);
	}

	public HelpManager() {
		Main.getPlugin().getCommand("help").setExecutor(this);
	}

	public HelpManager setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public HelpManager setUsage(String usage) {
		this.usage = usage;
		return this;
	}

	public HelpManager setPerm(String perm) {
		this.perm = perm;
		return this;
	}

	public HelpManager setPack(String pack) {
		this.pack = pack;
		return this;
	}

	public HelpManager setClickText(String clickText) {
		this.clickText = clickText;
		return this;
	}

	public HelpManager setClickevent(Action clickevent) {
		this.clickevent = clickevent;
		return this;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl,
			String[] args) {
		Player p = (Player) sender;
		sender.sendMessage(ChatColor.YELLOW+"---------"+ChatColor.WHITE+"HELP"+ChatColor.YELLOW+"---------");
		if (args.length==0)
			for (HelpManager h : helper)
				if (sender.hasPermission(h.perm)&&h.pack.equals("root")){
					TextComponent m = new TextComponent(ChatColor.GOLD+h.name+" : "+ChatColor.WHITE+h.desc);
					m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(h.usage).color(ChatColor.WHITE).create() ));
					m.setClickEvent( new ClickEvent( h.clickevent, h.clickText));
					p.spigot().sendMessage(m);
				}
		
		if (args.length==1)
			for (HelpManager h : helper)
				if (sender.hasPermission(h.perm)&&h.pack.equals(args[0])){
					TextComponent m = new TextComponent(ChatColor.GOLD+h.name+" : "+ChatColor.WHITE+h.desc);
					m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(h.usage).color(ChatColor.WHITE).create() ));
					m.setClickEvent( new ClickEvent( h.clickevent, h.clickText));
					p.spigot().sendMessage(m);
				}
		return true;
	}

}
