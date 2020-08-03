package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Trade implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Trade(Main main) {
		this.main = main;
		main.getCommand("trade").setExecutor(this);
		main.getCommand("t").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		
		if(args.length > 0) {
				String msg = "";
				for(int i=0; i < args.length; i++) {
					msg = msg + args[i] + " ";
				}
				
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(p.hasPermission("server.admin")) {
						all.sendMessage("§3[Trading] §4ADMIN §8| §7" + p.getName() + " §8» §3" + msg);
					}else if(p.hasPermission("server.leipung")) {
						all.sendMessage("§3[Trading] §cLEITUNG §8| §7" + p.getName() + " §8» §3" + msg);
					}else if(p.hasPermission("server.dev")) {
						all.sendMessage("§3[Trading] §3DEVELOPER §8| §7" + p.getName() + " §8» §3" + msg);
					}else if(p.hasPermission("server.spaff")) {
						all.sendMessage("§3[Trading] §cSTAFF §8| §7" + p.getName() + " §8» §3" + msg);
					}else if(p.hasPermission("server.yp")) {
						all.sendMessage("§3[Trading] §5YOUTUBER §8| §7" + p.getName() + " §8» §3" + msg);
					}else if(p.hasPermission("server.freund")) {
						all.sendMessage("§3[Trading] §3FREUND §8| §7" + p.getName() + " §8» §3" + msg);
					}else {
						all.sendMessage("§3[Trading] §7RANDOM §8| §7" + p.getName() + " §8» §3" + msg);
					}
				}
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
