package de.koppy.landsystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.project.Main;

public class COMMAND_Claims implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Claims(Main main) {
		this.main = main;
		main.getCommand("claims").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
		Player p = (Player)sender;
			if(args.length == 0) {
				p.sendMessage(COMMAND_Land.prefix + "§7Du hast §e" + API_Land.getLandClaims(p.getUniqueId().toString()) + " Claim-Token §7übrig.");
			}else if(args.length == 1) {
				if(p.hasPermission("server.claims.other")) {
					String target = args[0];
					if(API_UUID.existUUIDorNAME(target)) {
						if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
						if(!API_UUID.getUUIDorNAME(target).equals(p.getName())) {
							p.sendMessage(COMMAND_Land.prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7hat §e" + API_Land.getLandClaims(target) + " §eClaim-Token §7übrig.");
						}else {
							p.performCommand("claims");
						}
					}
				}else {
					p.sendMessage(COMMAND_Land.prefix + "§cDazu hast du keine Berechtigung.");
				}
			}else if(args.length == 2) {
				if(p.hasPermission("server.claims.admin")) {
				if(args[1].matches("[0-9]+")) {
					if(args[0].equalsIgnoreCase("add")) {
						API_Land.addLandClaims(p.getUniqueId().toString(), Integer.valueOf(args[1]));
						p.sendMessage(COMMAND_Land.prefix + "§7Du hast dir erfolgreich §e" + args[1] + " §eClaim-Tokens §ahinzugefügt§7.");
					}else if(args[0].equalsIgnoreCase("remove")) {
						API_Land.removeLandClaims(p.getUniqueId().toString(), Integer.valueOf(args[1]));
						p.sendMessage(COMMAND_Land.prefix + "§7Du hast dir erfolgreich §e" + args[1] + " §eClaim-Tokens §cabgezogen§7.");
					}else if(args[0].equalsIgnoreCase("set")) {
						API_Land.setLandClaims(p.getUniqueId().toString(), Integer.valueOf(args[1]));
						p.sendMessage(COMMAND_Land.prefix + "§7Du hast dir erfolgreich §e" + args[1] + " §eClaim-Tokens §agesetzt§7.");
					}else {
						p.sendMessage(COMMAND_Land.prefix + "§cVerwende: §e/claims add [number] [player]");
					}
				}else {
					p.sendMessage(COMMAND_Land.prefix + "§cDu musst eine Zahl angeben!");
				}
				}
			}else if(args.length == 3) {
				if(p.hasPermission("server.claims.admin")) {
					String uuid = args[2];
					if(API_UUID.existUUIDorNAME(uuid)) {
						if(uuid.length() != 36) uuid = API_UUID.getUUIDorNAME(uuid);
					if(args[1].matches("[0-9]+")) {
						if(args[0].equalsIgnoreCase("add")) {
							API_Land.addLandClaims(uuid, Integer.valueOf(args[1]));
							p.sendMessage(COMMAND_Land.prefix + "§7Du hast dem Spieler §3" + API_UUID.getUUIDorNAME(uuid) + " §7erfolgreich §e" + args[1] + " §eClaim-Tokens §ahinzugefügt§7.");
						}else if(args[0].equalsIgnoreCase("remove")) {
							API_Land.removeLandClaims(uuid, Integer.valueOf(args[1]));
							p.sendMessage(COMMAND_Land.prefix + "§7Du hast dem Spieler §3" + API_UUID.getUUIDorNAME(uuid) + " §7erfolgreich §e" + args[1] + " §eClaim-Tokens §cabgezogen§7.");
						}else if(args[0].equalsIgnoreCase("set")) {
							API_Land.setLandClaims(uuid, Integer.valueOf(args[1]));
							p.sendMessage(COMMAND_Land.prefix + "§7Du hast dem Spieler §3" + API_UUID.getUUIDorNAME(uuid) + " §7erfolgreich §e" + args[1] + " §eClaim-Tokens §agesetzt§7.");
						}else {
							p.sendMessage(COMMAND_Land.prefix + "§cVerwende: §e/claims add [number] [player]");
						}
					}else {
						p.sendMessage(COMMAND_Land.prefix + "§cDu musst eine Zahl angeben!");
					}
					}else {
						p.sendMessage(COMMAND_Land.prefix + "§cDer Spieler war noch nie auf dem Netzwerk!");
					}
					
				}
			}else {
				p.sendMessage(COMMAND_Land.prefix + "§cVerwende §e/claims [Spieler]");
			}
		}else {
			if(args.length == 3) {
					String uuid = args[2];
					if(API_UUID.existUUIDorNAME(uuid)) {
						if(uuid.length() != 36) uuid = API_UUID.getUUIDorNAME(uuid);
					if(args[1].matches("[0-9]+")) {
						if(args[0].equalsIgnoreCase("add")) {
							API_Land.addLandClaims(uuid, Integer.valueOf(args[1]));
						}else if(args[0].equalsIgnoreCase("remove")) {
							API_Land.removeLandClaims(uuid, Integer.valueOf(args[1]));
						}else if(args[0].equalsIgnoreCase("set")) {
							API_Land.setLandClaims(uuid, Integer.valueOf(args[1]));
						}
					}
					}
			}
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player)sender;
		
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			if(p.hasPermission("server.claims.admin")) {
			check.add("add");
			check.add("set");
			check.add("remove");
			}
			for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
				if(p.hasPermission("server.claims.admin")) {
					check.add("§4§l✘ §r§4Anzahl §4§l✘");
					for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
				}
			}
		}else if(args.length == 3) {
			List<String> check = new ArrayList<>();
			if(p.hasPermission("server.claims.admin")) {
			for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
		}
		
		return tcomplete;
	}
}
