package de.koppy.missions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Setregion implements CommandExecutor, TabCompleter {

	private Main main;
	public COMMAND_Setregion(Main main) {
		this.main = main;
		main.getCommand("setregion").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Mission", "locations.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(p.hasPermission("server.mission.admin")) {
		if(args.length == 2) {
			
			if(args[0].equalsIgnoreCase("1")) {
				
				String regionname = args[1];
				if(Explorer.getAllRegions().contains(regionname)) {
				cfg.set(regionname + ".location1", p.getLocation());
				RegionHandler.saveFile(file, cfg);
				p.sendMessage(Main.prefix + "§7Du hast die §e1. Position §7von der Region §3" + regionname + " §7gespeichert!");
				}else {
					p.sendMessage(Main.prefix + "§cDiese Region wurde noch nicht implementiert.");
				}
				
			}else if(args[0].equalsIgnoreCase("2")) {
				
				String regionname = args[1];
				if(Explorer.getAllRegions().contains(regionname)) {
				cfg.set(regionname + ".location2", p.getLocation());
				RegionHandler.saveFile(file, cfg);
				p.sendMessage(Main.prefix + "§7Du hast die §e2. Position §7von der Region §3" + regionname + " §7gespeichert!");
				}else {
					p.sendMessage(Main.prefix + "§cDiese Region wurde noch nicht implementiert.");
				}
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/setregion 1/2 [RegionName]");
			}
			
		}else {
			p.sendMessage(Main.prefix + "§cVerwende §e/setregion 1/2 [RegionName]");
		}
		}else {
			p.sendMessage(Main.prefix + "§cDazu hast du keine Rechte!");
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
