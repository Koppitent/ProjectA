package de.koppy.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class COMMAND_Config implements CommandExecutor, TabCompleter{

	public static String prefix = "§8[§2C§aonfig§8] §r";
	public static String err = prefix + "§cDu kannst derzeit nur §e/config reload §ceingeben!";
	
	public static List<String> arguments = new ArrayList<>();
	
	private Main main;
	public COMMAND_Config(Main main) {
		this.main = main;
		main.getCommand("config").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.isOp()) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					Main.setConfig();
					p.sendMessage(prefix + "§7Du hast die §aConfig.yml §7erfolgreich neu geladen!");
				}else {
					p.sendMessage(err);
				}
			}else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("set")) {
					if(args[1].equalsIgnoreCase("nbgs")) {
						if(args[2].equalsIgnoreCase("true")) {
							File file = new File("plugins/Server/NBG", "config.yml");
							FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
							cfg.set("NBGs aktivieren", true);
							try {
								cfg.save(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							p.sendMessage(prefix + "§7Du hast die §3NBGs §7eingeschaltet.");
						}else if(args[2].equalsIgnoreCase("false")) {
							File file = new File("plugins/Server/NBG", "config.yml");
							FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
							cfg.set("NBGs aktivieren", false);
							try {
								cfg.save(file);
							} catch (IOException e) {
								e.printStackTrace();
							}
							p.sendMessage(prefix + "§7Du hast die §3NBGs §7ausgeschaltet.");
						}else {
							p.sendMessage(prefix + "§cVerwende: §e/config set nbg true/false"); 
						}
					}else if(args[1].equalsIgnoreCase("nbglink")) {
						File file = new File("plugins/Server/NBG", "config.yml");
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
						cfg.set("Link", args[2]);
						try {
							cfg.save(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
						p.sendMessage(prefix + "§7Der derzeitige Link zu den §3NBGs §7ist nun §e" + args[2] + "§7.");
					}else {
						p.sendMessage(prefix + "§cZur Verfügung: nbgs true/false ,nbglink (link)"); 
					}
				}
			}else {
				p.sendMessage(err);
			}
		}
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player) sender;
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("reload");
			check.add("set");
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("set")) {
			List<String> check = new ArrayList<>();
			check.add("nbgs");
			check.add("nbglink");
			for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
			}
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("set")) {
				if(args[1].equalsIgnoreCase("nbgs")) {
				List<String> check = new ArrayList<>();
				check.add("true");
				check.add("false");
				for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
				}else if(args[1].equalsIgnoreCase("nbglink")) {
					tcomplete.add("§4§l✘ §r§4Your Link here §4§l✘");
				}
			}
		}
		
		return tcomplete;
	}

}
