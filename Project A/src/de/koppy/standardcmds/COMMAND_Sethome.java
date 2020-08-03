package de.koppy.standardcmds;

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

import de.koppy.handler.InfoHandler;
import de.koppy.project.Main;

public class COMMAND_Sethome implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Sethome(Main main) {
		this.main = main;
		main.getCommand("sethome").setExecutor(this);
	}
	
	public static String prefix = "§8[§6Home§8] §r";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Homes", p.getUniqueId().toString()+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(args.length == 0) {
			String home = "home";
			if(!cfg.getKeys(false).contains(home)) {
				
				cfg.set(home + ".location", p.getLocation());
				InfoHandler.saveFile(file, cfg);
				p.sendMessage(prefix + "§7Du hast §aerfolgreich §7das Home §3" + home + " §7gesetzt.");
				
			}else {
				p.sendMessage(prefix + "§cDieses Home existiert bereits.");
			}
			
		}else if(args.length == 1) {
			Integer maxhomes = 0;
			if(p.hasPermission("server.maxhomes")) maxhomes = 8;
			if(cfg.getKeys(false).size() < maxhomes) {
			String home = args[0];
			if(!cfg.getKeys(false).contains(home.toLowerCase())) {
				
				cfg.set(home.toLowerCase() + ".location", p.getLocation());
				InfoHandler.saveFile(file, cfg);
				p.sendMessage("§7Du hast §aerfolgreich §7das Home §3" + home.toLowerCase() + " §7gesetzt.");
				
			}else {
				p.sendMessage(prefix + "§cDieses Home existiert bereits.");
			}
			
			}else {
				p.sendMessage(prefix + "§cDu hast bereits die Maximale Anzahl an Homes erreicht.");
			}
		}else {
			p.sendMessage(prefix + "§cVerwende §e/sethome [Homename]");
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
