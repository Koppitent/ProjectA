package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.eco.COMMAND_Bank;
import de.koppy.handler.ProfileHandler;
import de.koppy.project.Main;

public class COMMAND_setClanTag implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_setClanTag(Main main) {
		this.main = main;
		main.getCommand("setClanTag").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.clantag")) {
		if(args.length == 1) {
			String tag = args[0].toUpperCase(); 
			if(COMMAND_Bank.isAlpha(tag)) {
				if(tag.equalsIgnoreCase("reset") || tag.equalsIgnoreCase("null")) {
					ProfileHandler.setClanTag(p.getUniqueId().toString(), null);
					p.sendMessage(Main.prefix + "§7Dein Clantag ist nun §eresettet§7.");
				}else if(tag.length() >= 2 && tag.length() <= 6) {
					ProfileHandler.setClanTag(p.getUniqueId().toString(), tag);
					p.sendMessage(Main.prefix + "§7Dein Clantag ist nun §e"+tag+"§7.");
				}else {
					p.sendMessage(Main.prefix + "§cDer Clan-Tag muss zwischen 2 bis 4 Buchstaben besitzen.");
				}
			}else {
				p.sendMessage(Main.prefix + "§cDu darfst nur Buchstaben von A-Z verwenden.");
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
		
		return tcomplete;
	}
}
