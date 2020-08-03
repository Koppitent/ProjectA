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

public class COMMAND_Removehome implements CommandExecutor, TabCompleter{

	private Main main;
	public static ArrayList<Player> isinHomeMenu = new ArrayList<>();
	public COMMAND_Removehome(Main main) {
		this.main = main;
		main.getCommand("removehome").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Homes", p.getUniqueId().toString()+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(args.length == 0) {
			//Home - GUI
			
			String home = "home";
			if(cfg.getKeys(false).contains(home)) {
				
				cfg.set(home.toLowerCase(), null);
				InfoHandler.saveFile(file, cfg);
				p.sendMessage(COMMAND_Sethome.prefix + "§7Du hast dein Home §3" + home + " §centfernt§7.");
				
			}else {
				p.sendMessage(COMMAND_Sethome.prefix + "§cDas Home " + home + " existiert nicht!");
			}
			
		}else if(args.length == 1) {
			String home = args[0];
			if(cfg.getKeys(false).contains(home.toLowerCase())) {
				
				cfg.set(home.toLowerCase(), null);
				InfoHandler.saveFile(file, cfg);
				p.sendMessage(COMMAND_Sethome.prefix + "§7Du hast dein Home §3" + home.toLowerCase() + " §centfernt§7.");
				
			}else {
				p.sendMessage(COMMAND_Sethome.prefix + "§cDas Home " + home.toLowerCase() + " existiert nicht!");
			}
			
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player) sender;
		if(args.length == 1) {
			File file = new File("plugins/Homes", p.getUniqueId().toString()+".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			List<String> check = new ArrayList<>();
			for(String s : cfg.getKeys(false)) check.add(s);
			for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
		}
		
		return tcomplete;
	}
}
