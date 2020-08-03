package de.koppy.standardcmds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.handler.InfoHandler;
import de.koppy.project.Main;

public class COMMAND_Setpos implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Setpos(Main main) {
		this.main = main;
		main.getCommand("setpos").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Server/SaveLocations", "locations.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(p.hasPermission("server.admin.setpos")) {
		if(args.length == 2) {
			if(args[0].equals("1")) {
				if(args[1].equalsIgnoreCase("farmwelt")) {
					
					cfg.set(args[1].toUpperCase() + ".location1", p.getLocation());
					InfoHandler.saveFile(file, cfg);
					p.sendMessage(Main.prefix + "§7Du hast den §e1. Savepoint §7für §e" + args[1].toUpperCase() + " §7gesetzt!");
					
				}else if(args[1].equalsIgnoreCase("nether")) {
					
					cfg.set(args[1].toUpperCase() + ".location1", p.getLocation());
					InfoHandler.saveFile(file, cfg);
					p.sendMessage(Main.prefix + "§7Du hast den §e1. Savepoint §7für §e" + args[1].toUpperCase() + " §7gesetzt!");
					
				}else if(args[1].equalsIgnoreCase("spawn")) {
					
					cfg.set(args[1].toUpperCase() + ".location1", p.getLocation());
					InfoHandler.saveFile(file, cfg);
					p.sendMessage(Main.prefix + "§7Du hast den §e1. Savepoint §7für §e" + args[1].toUpperCase() + " §7gesetzt!");
					
				}else {
					p.sendMessage(Main.prefix + "§cNutze farmwelt / Nether oder Spawn");
				}
			}else if(args[0].equals("2")) {
				if(args[1].equalsIgnoreCase("farmwelt")) {
					
					cfg.set(args[1].toUpperCase() + ".location2", p.getLocation());
					InfoHandler.saveFile(file, cfg);
					p.sendMessage(Main.prefix + "§7Du hast den §e2. Savepoint §7für §e" + args[1].toUpperCase() + " §7gesetzt!");
					
				}else if(args[1].equalsIgnoreCase("nether")) {
					
					cfg.set(args[1].toUpperCase() + ".location2", p.getLocation());
					InfoHandler.saveFile(file, cfg);
					p.sendMessage(Main.prefix + "§7Du hast den §e2. Savepoint §7für §e" + args[1].toUpperCase() + " §7gesetzt!");
					
				}else if(args[1].equalsIgnoreCase("spawn")) {
					
					cfg.set(args[1].toUpperCase() + ".location2", p.getLocation());
					InfoHandler.saveFile(file, cfg);
					p.sendMessage(Main.prefix + "§7Du hast den §e2. Savepoint §7für §e" + args[1].toUpperCase() + " §7gesetzt!");
					
				}else {
					p.sendMessage(Main.prefix + "§cNutze farmwelt / Nether oder Spawn");
				}
			}else {
				p.sendMessage(Main.prefix + "§cDu musst eine Zahl zwischen 1 und 2 eigeben!");
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
