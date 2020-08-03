package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Teleport implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Teleport(Main main) {
		this.main = main;
		main.getCommand("teleport").setExecutor(this);
		main.getCommand("tp").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.teleport")) {
			if(args.length == 1) {
				if(Bukkit.getPlayer(args[0]) != null) {
					
					p.teleport(Bukkit.getPlayer(args[0]));
					p.sendMessage(Main.prefix + "§7Du wurdest zu dem Spieler §3" + Bukkit.getPlayer(args[0]).getName() + " §7teleportiert.");
					
				}else {
					p.sendMessage(Main.nichtonline.replace("%player%", args[0]));
				}
			}else if(args.length == 2) {
				if(p.hasPermission("server.teleport.others") || p.hasPermission("server.teleport.other")) {
				String target1 = args[0];
				Player t1 = Bukkit.getPlayer(target1);
				
				String target2 = args[1];
				Player t2 = Bukkit.getPlayer(target2);
				
				if(t1 != null) {
					if(t2 != null) {
						if(t1 != t2) {
						
							t1.teleport(t2);
							if(t1 == p) p.sendMessage(Main.prefix + "§eDu §7wurdest zu dem Spieler §3" + t2.getName() + " §7teleportiert.");
							else if(t2 == p) p.sendMessage(Main.prefix + "§7Der Spieler §3" + t1.getName() + " §7wurde zu §edir §7teleportiert.");
							else p.sendMessage(Main.prefix + "§7Der Spieler §3" + t1.getName() + " §7wurde zu dem Spieler §e" + t2.getName() + " §7teleportiert.");
						
						}else {
							p.sendMessage(Main.prefix + "§cDas ist der gleiche Spieler!");
						}
					}else {
						p.sendMessage(Main.nichtonline.replace("%player%", args[1]));
					}
				}else {
					p.sendMessage(Main.nichtonline.replace("%player%", args[0]));
				}
			}else {
				p.sendMessage(Main.noperms);
			}
			}else if(args.length == 3) {
				if(args[0].matches("[0.0-9.0]+") && args[1].matches("[0.0-9.0]+") && args[2].matches("[0.0-9.0]+")) {
					
					Double x = Double.valueOf(args[0]);
					Double y = Double.valueOf(args[1]);
					Double z = Double.valueOf(args[2]);
					
					Location loc = new Location(p.getWorld(), x, y, z);
					p.teleport(loc);
					p.sendMessage(Main.prefix + "§7Du wurdest zu den §eKoordinaten x"+x+" y"+y+" z"+z+" §7teleportiert.");
					
				}else {
					p.sendMessage(Main.prefix + "§cDu musst zahlen angeben!");
				}
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
			if(p.hasPermission("server.teleport")) {
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
			}
		}else if(args.length == 2) {
			if(p.hasPermission("server.teleport.other") || p.hasPermission("server.teleport.others")) {
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
			for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
			}
		}
		return tcomplete;
	}
}
