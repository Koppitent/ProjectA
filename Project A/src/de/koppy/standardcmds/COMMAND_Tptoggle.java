package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.handler.ProfileHandler;
import de.koppy.project.Main;

public class COMMAND_Tptoggle implements CommandExecutor, TabCompleter{
	
	private Main main;
	public static ArrayList<Player> paypause = new ArrayList<>();
	public COMMAND_Tptoggle(Main main) {
		this.main = main;
		main.getCommand("tptoggle").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(ProfileHandler.isTeleportAllowed(p.getUniqueId().toString())) {
			//zu false
			ProfileHandler.setTeleportAllowed(p.getUniqueId().toString(), false);
			p.sendMessage(Main.prefix + "§7Du hast Teleport-Anfragen §cdeaktiviert§7.");
			
		}else {
			//zu true
			ProfileHandler.setTeleportAllowed(p.getUniqueId().toString(), true);
			p.sendMessage(Main.prefix + "§7Du hast Teleport-Anfragen §aaktiviert§7.");
			
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		return tcomplete;
	}
}
