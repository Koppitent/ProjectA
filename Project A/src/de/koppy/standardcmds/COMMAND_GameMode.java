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

public class COMMAND_GameMode implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_GameMode(Main main) {
		this.main = main;
		main.getCommand("gamemode").setExecutor(this);
		main.getCommand("gm").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.gamemode")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")|| args[0].equalsIgnoreCase("0")) {
					p.setGameMode(GameMode.SURVIVAL);
				}else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
					p.setGameMode(GameMode.CREATIVE);
				}else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
					p.setGameMode(GameMode.ADVENTURE);
				}else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec") || args[0].equalsIgnoreCase("3")) {
					p.setGameMode(GameMode.SPECTATOR);
				}else {
					p.sendMessage(Main.prefix + "§cEs existieren folgende SpielModi: §ecreative | adventure | survival | spectator");
				}
			}else if(args.length == 2) {
				String target = args[1];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
					if(t != p) {
						if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")|| args[0].equalsIgnoreCase("0")) {
							t.setGameMode(GameMode.SURVIVAL);
							p.sendMessage(Main.prefix + "§7Der SpielModus von §3" + t.getName() + " §7wurde zu §aSURVIVAL §7geändert.");
						}else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
							t.setGameMode(GameMode.CREATIVE);
							p.sendMessage(Main.prefix + "§7Der SpielModus von §3" + t.getName() + " §7wurde zu §aCREATIVE §7geändert.");
						}else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
							t.setGameMode(GameMode.ADVENTURE);
							p.sendMessage(Main.prefix + "§7Der SpielModus von §3" + t.getName() + " §7wurde zu §aADVENTURE §7geändert.");
						}else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec") || args[0].equalsIgnoreCase("3")) {
							t.setGameMode(GameMode.SPECTATOR);
							p.sendMessage(Main.prefix + "§7Der SpielModus von §3" + t.getName() + " §7wurde zu §aSPECTATOR §7geändert.");
						}else {
							p.sendMessage(Main.prefix + "§cEs existieren folgende SpielModi: §ecreative | adventure | survival | spectator");
						}
					}else {
						if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")|| args[0].equalsIgnoreCase("0")) {
							p.setGameMode(GameMode.SURVIVAL);
						}else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
							p.setGameMode(GameMode.CREATIVE);
						}else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("2")) {
							p.setGameMode(GameMode.ADVENTURE);
						}else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec") || args[0].equalsIgnoreCase("3")) {
							p.setGameMode(GameMode.SPECTATOR);
						}else {
							p.sendMessage(Main.prefix + "§cEs existieren folgende SpielModi: §ecreative | adventure | survival | spectator");
						}
					}
				}else {
					p.sendMessage(Main.nichtonline.replace("%player%", target));
				}
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/gamemode creative|survival|spectator|adventure [Name]");
			}
		}else {
			p.sendMessage(Main.noperms);
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("survival");
			check.add("creative");
			check.add("adventure");
			check.add("spectator");
			for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
			for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
		}
		
		return tcomplete;
	}

}
