package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Time implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Time(Main main) {
		this.main = main;
		main.getCommand("time").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		if(p.hasPermission("server.time")) {
			
			if(args.length == 0) {
				p.sendMessage(Main.prefix + "§7Die Welt steht derzeit bei §e" + p.getWorld().getTime() + " Ticks§7.");
			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("add")) {
					p.sendMessage(Main.prefix + "§cVerwende §e/time add [ticks]");
				}else if(args[0].equalsIgnoreCase("set")) {
					p.sendMessage(Main.prefix + "§cVerwende §e/time set [ticks]");
				}else if(args[0].equalsIgnoreCase("remove")) {
					p.sendMessage(Main.prefix + "§cVerwende §e/time remove [ticks]");
				}else if(args[0].equalsIgnoreCase("info")) {
					p.performCommand("time");
				}else {
					p.sendMessage(Main.prefix + "§cVerwende §e/time <remove/add/set> [ticks]");
				}
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("add")) {
					if(args[1].matches("[0-9]+")) {
					Long zahl = Long.valueOf(args[1]);
					zahl += p.getWorld().getTime();
					p.getWorld().setTime(zahl);
					p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + zahl + " Ticks §7gesetzt.");
					}else {
						p.sendMessage(Main.prefix + "§cVerwende §e/time add [ticks]");
					}
				}else if(args[0].equalsIgnoreCase("set")) {
					if(args[1].matches("[0-9]+")) {
						Long zahl = Long.valueOf(args[1]);
						p.getWorld().setTime(zahl);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + zahl + " Ticks §7gesetzt.");
					}else if(args[1].equalsIgnoreCase("day")) {
						p.getWorld().setTime(1000);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + "TAG" + " §7gesetzt.");
					}else if(args[1].equalsIgnoreCase("night")) {
						p.getWorld().setTime(13000);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + "NACHT" + " §7gesetzt.");
					}else if(args[1].equalsIgnoreCase("midnight")) {
						p.getWorld().setTime(18000);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + "MITTERNACHT" + " §7gesetzt.");
					}else if(args[1].equalsIgnoreCase("morning")) {
						p.getWorld().setTime(0);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + "MORGEN" + " §7gesetzt.");
					}else if(args[1].equalsIgnoreCase("noon")) {
						p.getWorld().setTime(6000);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + "SONNENUNTERGANG" + " §7gesetzt.");
					}else {
						p.sendMessage(Main.prefix + "§cVerwende §e/time set [ticks]");
					}
				}else if(args[0].equalsIgnoreCase("remove")) {
					if(args[1].matches("[0-9]+")) {
						Long zahl = Long.valueOf(args[1]);
						zahl = p.getWorld().getTime() - zahl;
						p.getWorld().setTime(zahl);
						p.sendMessage(Main.prefix + "§7Die §3Zeit §7wurde auf §e" + zahl + " Ticks §7gesetzt.");
						}else {
							p.sendMessage(Main.prefix + "§cVerwende §e/time remove [ticks]");
						}
				}else {
					p.sendMessage(Main.prefix + "§cVerwende §e/time <remove/add/set> [ticks]");
				}
				
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/time <remove/add/set> [ticks]");
			}
			
		}else {
			p.sendMessage(Main.noperms);
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player)sender;
		if(p.hasPermission("server.time")) {
			if(args.length == 1) {
				List<String> check = new ArrayList<>();
				check.add("set");
				check.add("add");
				check.add("remove");
				check.add("info");
				for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("add")) {
					List<String> check = new ArrayList<>();
					check.add("§4§l✘ §r§cTicks §4§l✘");
					for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
				}else if(args[0].equalsIgnoreCase("set")) {
					List<String> check = new ArrayList<>();
					check.add("§4§l✘ §r§cTicks §4§l✘");
					check.add("morning");
					check.add("day");
					check.add("noon");
					check.add("night");
					check.add("midnight");
					for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
				}else if(args[0].equalsIgnoreCase("remove")) {
					List<String> check = new ArrayList<>();
					check.add("§4§l✘ §r§cTicks §4§l✘");
					for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
				}
			}
		}
		return tcomplete;
	}
}
