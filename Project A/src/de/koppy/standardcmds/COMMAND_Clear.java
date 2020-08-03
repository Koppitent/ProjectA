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

public class COMMAND_Clear implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Clear(Main main) {
		this.main = main;
		main.getCommand("clear").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.clear")) {
			
			if(args.length == 0) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.sendMessage(Main.prefix + "§7Du hast dein §3Inventar §7gecleart!");
			}else if(args.length == 1) {
				String target = args[0];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
					
					t.getInventory().clear();
					t.getInventory().setArmorContents(null);
					t.sendMessage(Main.prefix + "§7Dein §3Inventar §7wurde gecleart!");
					p.sendMessage(Main.prefix + "§7Du hast das §3Inventar §7von " + t.getName() + " §7gecleart!");
					
				}else {
					p.sendMessage(Main.nichtonline.replace("%player%", target));
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
