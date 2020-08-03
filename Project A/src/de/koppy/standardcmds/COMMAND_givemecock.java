package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_givemecock implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_givemecock(Main main) {
		this.main = main;
		main.getCommand("givemecock").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("givemecock")) {
			
			Location playerloc = p.getLocation();
			p.getWorld().getBlockAt(playerloc.add(0, 3, 0)).setType(Material.WHITE_WOOL);
			p.getWorld().getBlockAt(playerloc.add(0, 0, 1)).setType(Material.WHITE_WOOL);
			p.getWorld().getBlockAt(playerloc.add(0, 0, 1)).setType(Material.WHITE_WOOL);
			p.getWorld().getBlockAt(playerloc.add(0, 1, -1)).setType(Material.WHITE_WOOL);
			p.getWorld().getBlockAt(playerloc.add(0, 1, 0)).setType(Material.WHITE_WOOL);
			p.getWorld().getBlockAt(playerloc.add(0, 1, 0)).setType(Material.PINK_WOOL);
			
			p.sendMessage("§7Cock vorbe(reitet)!");
			
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
