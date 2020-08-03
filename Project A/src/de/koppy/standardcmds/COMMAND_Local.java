package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Local implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Local(Main main) {
		this.main = main;
		main.getCommand("local").setExecutor(this);
		main.getCommand("l").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		
		if(args.length > 0) {
				String msg = "";
				for(int i=0; i < args.length; i++) {
					msg = msg + args[i] + " ";
				}
				
				if(p.hasPermission("server.admin")) {
					p.sendMessage("§e[l] §4ADMIN §8| §7" + p.getName() + " §8» §e" + msg);
				}else if(p.hasPermission("server.leipung")) {
					p.sendMessage("§e[l] §cLEITUNG §8| §7" + p.getName() + " §8» §e" + msg);
				}else if(p.hasPermission("server.dev")) {
					p.sendMessage("§e[l] §3DEVELOPER §8| §7" + p.getName() + " §8» §e" + msg);
				}else if(p.hasPermission("server.spaff")) {
					p.sendMessage("§e[l] §cSTAFF §8| §7" + p.getName() + " §8» §e" + msg);
				}else if(p.hasPermission("server.yp")) {
					p.sendMessage("§e[l] §5YOUTUBER §8| §7" + p.getName() + " §8» §e" + msg);
				}else if(p.hasPermission("server.freund")) {
					p.sendMessage("§e[l] §3FREUND §8| §7" + p.getName() + " §8» §e" + msg);
				}else {
					p.sendMessage("§e[l] §7RANDOM §8| §7" + p.getName() + " §8» §e" + msg);
				}
				
				boolean issomeonenear = false;
				for(Entity entitys : p.getNearbyEntities(100, 100, 100)) {
				if(entitys instanceof Player) {
					issomeonenear = true;
					Player t = (Player) entitys;
					if(p.hasPermission("server.admin")) {
						t.sendMessage("§e[l] §4ADMIN §8| §7" + p.getName() + " §8» §e" + msg);
					}else if(p.hasPermission("server.leitung")) {
						t.sendMessage("§e[l] §cLEITUNG §8| §7" + p.getName() + " §8» §e" + msg);
					}else if(p.hasPermission("server.dev")) {
						t.sendMessage("§e[l] §3DEVELOPER §8| §7" + p.getName() + " §8» §e" + msg);
					}else if(p.hasPermission("server.staff")) {
						t.sendMessage("§e[l] §cSTAFF §8| §7" + p.getName() + " §8» §e" + msg);
					}else if(p.hasPermission("server.yt")) {
						t.sendMessage("§e[l] §5YOUTUBER §8| §7" + p.getName() + " §8» §e" + msg);
					}else if(p.hasPermission("server.freund")) {
						t.sendMessage("§e[l] §3FREUND §8| §7" + p.getName() + " §8» §e" + msg);
					}else {
						t.sendMessage("§e[l] §7RANDOM §8| §7" + p.getName() + " §8» §e" + msg);
					}
				}
			}
				
				if(issomeonenear == false) p.sendMessage(Main.prefix + "§cEs ist niemand in deiner nähe, der dich hört.");
				
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
