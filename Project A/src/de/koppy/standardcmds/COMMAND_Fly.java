package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Fly implements CommandExecutor, TabCompleter{

	private Main main;
	public static ArrayList<Player> flylist = new ArrayList<>();
	public COMMAND_Fly(Main main) {
		this.main = main;
		main.getCommand("fly").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.fly")) {
			
			if(args.length == 0) {
				if(flylist.contains(p)) {
					if(p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
						p.setAllowFlight(false);
						p.setFlying(false);
					}
					p.sendMessage(Main.prefix + "§7Du fliegst nun §cnicht §7mehr.");
					flylist.remove(p);
				}else {
					p.setAllowFlight(true);
					p.sendMessage(Main.prefix + "§7Du kannst nun §afliegen§7.");
					flylist.add(p);
				}
			}else if(args.length == 1) {
				if(p.hasPermission("server.fly.other")) {
				String target = args[0];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
					if(t == p) {
						p.performCommand("fly");
					}else {
					if(flylist.contains(t)) {
						if(t.getGameMode() == GameMode.SURVIVAL || t.getGameMode() == GameMode.ADVENTURE) {
							t.setAllowFlight(false);
							t.setFlying(false);
						}
						t.sendMessage(Main.prefix + "§7Du fliegst nun §cnicht §7mehr.");
						p.sendMessage(Main.prefix + "§7Der Spieler " + t.getName() + " fliegt nun §cnicht §7mehr.");
						flylist.remove(t);
					}else {
						t.setAllowFlight(true);
						t.sendMessage(Main.prefix + "§7Du kannst nun §afliegen§7.");
						p.sendMessage(Main.prefix + "§7Der Spieler " + t.getName() + " §afliegt §7nun.");
						flylist.add(t);
					}
					}
				}else {
					p.sendMessage(Main.nichtonline.replace("%player%", target));
				}
				}else {
					p.sendMessage(Main.noperms);
				}
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/fly [Name]");
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
			for(Player all : Bukkit.getOnlinePlayers()) if(all != p) check.add(all.getName());
			for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
			if(tcomplete.isEmpty()) tcomplete.add("§4§l✘ §r§cniemand online §4§l✘");
		}
		return tcomplete;
	}
}
