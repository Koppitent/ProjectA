package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Weather implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Weather(Main main) {
		this.main = main;
		main.getCommand("weather").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.weather")) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				p.getWorld().setStorm(false);
				p.getWorld().setThundering(false);
				p.sendMessage(Main.prefix + "§7Das Wetter in der Welt §a" + p.getWorld().getName() + " §7ist nun auf §eSONNIG §7gestellte.");
			}else if(args[0].equalsIgnoreCase("sun")) {
				p.getWorld().setStorm(false);
				p.getWorld().setThundering(false);
				p.sendMessage(Main.prefix + "§7Das Wetter in der Welt §a" + p.getWorld().getName() + " §7ist nun auf §eSONNIG §7gestellte.");
			}else if(args[0].equalsIgnoreCase("rain")) {
				p.getWorld().setStorm(true);
				p.getWorld().setThundering(false);
				p.sendMessage(Main.prefix + "§7Das Wetter in der Welt §a" + p.getWorld().getName() + " §7ist nun auf §eREGEN §7gestellte.");
			}else if(args[0].equalsIgnoreCase("thunder")) {
				p.getWorld().setStorm(true);
				p.getWorld().setThundering(true);
				p.sendMessage(Main.prefix + "§7Das Wetter in der Welt §a" + p.getWorld().getName() + " §7ist nun auf §eSTURM §7gestellte.");
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/weather clear [Spieler]");
			}
		}else if(args.length == 2) {
			String target = args[1];
			Player t = Bukkit.getPlayer(target);
			if(t != null) {
				if(args[0].equalsIgnoreCase("clear")) {
					t.setPlayerWeather(WeatherType.CLEAR);
					p.sendMessage(Main.prefix + "§7Das Wetter in der Welt für §a" + t.getWorld().getName() + " §7ist nun auf §eSONNIG §7gestellte.");
				}else if(args[0].equalsIgnoreCase("sun")) {
					t.setPlayerWeather(WeatherType.CLEAR);
					p.sendMessage(Main.prefix + "§7Das Wetter in der Welt für §a" + t.getWorld().getName() + " §7ist nun auf §eSONNIG §7gestellte.");
				}else if(args[0].equalsIgnoreCase("rain")) {
					t.setPlayerWeather(WeatherType.DOWNFALL);
					p.sendMessage(Main.prefix + "§7Das Wetter in der Welt für §a" + t.getWorld().getName() + " §7ist nun auf §eREGEN §7gestellte.");
				}else if(args[0].equalsIgnoreCase("thunder")) {
					t.setPlayerWeather(WeatherType.DOWNFALL);
					p.sendMessage(Main.prefix + "§7Das Wetter in der Welt für §a" + t.getWorld().getName() + " §7ist nun auf §eSTURM §7gestellte.");
				}else {
					p.sendMessage(Main.prefix + "§cVerwende §e/weather clear [Spieler]");
				}
			}else {
				p.sendMessage(Main.prefix + "§cDer Spieler muss online sein!");
			}
		}else {
			p.sendMessage(Main.prefix + "§cVerwende §e/weather clear [Spieler]");
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
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("clear");
			check.add("sun");
			check.add("rain");
			check.add("thundering");
			for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) if(all != p) check.add(all.getName());
			for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
			if(tcomplete.isEmpty()) tcomplete.add("§4§l✘ §r§cniemand online §4§l✘");
		}
		return tcomplete;
	}
}
