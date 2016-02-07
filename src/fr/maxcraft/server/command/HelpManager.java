package fr.maxcraft.server.command;

import java.io.File;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.maxcraft.Main;

public class HelpManager{
	
	public static ArrayList<HelpManager> helper = new ArrayList<HelpManager>();
	private String name;
	private String desc;
	private String usage;
	private String perm;
	private String pack;
	private String clickText;
	private Action clickevent;
	private YamlConfiguration config;
	private File file;

	public HelpManager(String name){
		this.name = name;
		this.desc = "non renseign�";
		this.usage = "non renseign�";
		this.perm = "maxcraft.base";
		this.pack = "root";
		this.clickText = "";
		this.clickevent = ClickEvent.Action.RUN_COMMAND;
		helper.add(this);
	}

	public HelpManager() {
		this.file = new File("plugins/Maxcraft/helpmanager.yml");
		this.config = YamlConfiguration.loadConfiguration(this.file);
		configReader();
	}

	private void configReader() {
		for (String t : this.config.getKeys(false)){
			HelpManager h = new HelpManager(t);
			if (this.config.contains(t+".desc"))
				h.setDesc(this.config.getString(t+".desc").replace(" \n ", "\n"));
			if (this.config.contains(t+".usage"))
				h.setUsage(this.config.getString(t+".usage").replace(" \n ", "\n"));
			if (this.config.contains(t+".perm"))
				h.setPerm(this.config.getString(t+".perm"));
			if (this.config.contains(t+".pack"))
				h.setPack(this.config.getString(t+".pack"));
			if (this.config.contains(t+".cmd"))
				h.setClickText(this.config.getString(t+".cmd"));
		}
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

	public static boolean onCommand(CommandSender sender, Command cmd, String lbl,
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
