package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Invsee implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Invsee(Main main) {
		this.main = main;
		main.getCommand("invsee").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.invsee")) {
		if(args.length == 1) {
			String targetname = args[0];
			Player t = Bukkit.getPlayer(targetname);
			if(t != null) {
				
				p.openInventory(t.getInventory());
				p.sendMessage(Main.prefix + "§7Du hast das Inventar von §3" + t.getName() + " §ageöffnet§7.");
				
			}else {
				p.sendMessage(Main.nichtonline.replace("%player%", targetname));
			}
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("ec") || args[0].equalsIgnoreCase("enderchest")) {
					String targetname = args[1];
					Player t = Bukkit.getPlayer(targetname);
					if(t != null) {
						
						p.openInventory(t.getEnderChest());
						p.sendMessage(Main.prefix + "§7Du hast die Enderchest von §3" + t.getName() + " §ageöffnet§7.");
						
					}else {
						p.sendMessage(Main.nichtonline.replace("%player%", targetname));
					}
				}else if(args[0].equalsIgnoreCase("inv") || args[0].equalsIgnoreCase("inventory")) {
					String targetname = args[1];
					Player t = Bukkit.getPlayer(targetname);
					if(t != null) {
						
						p.openInventory(t.getInventory());
						p.sendMessage(Main.prefix + "§7Du hast das Inventar von §3" + t.getName() + " §ageöffnet§7.");
						
					}else {
						p.sendMessage(Main.nichtonline.replace("%player%", targetname));
					}
				}
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/Invsee [Spieler]");
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
