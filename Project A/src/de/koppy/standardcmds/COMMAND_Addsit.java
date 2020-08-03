package de.koppy.standardcmds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.handler.InfoHandler;
import de.koppy.project.Main;

public class COMMAND_Addsit implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Addsit(Main main) {
		this.main = main;
		main.getCommand("addsit").setExecutor(this);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Server/Sits", "sitlist.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(p.hasPermission("server.addsit")) {
			
			if(args.length == 0) {
				if(p.getItemInHand() != null) {
					Material m = p.getItemInHand().getType();
					if(m.isBlock() && m != Material.AIR) {
						if(cfg.getKeys(false).contains(m.toString()) == false) {
							cfg.set(m.toString(), "BLOCK");
							InfoHandler.saveFile(file, cfg);
							p.sendMessage(Main.prefix + "§7Der Block §e" + m.toString() + " §7wurde hinzugefügt.");
						}else {
							p.sendMessage(Main.prefix + "§cDer Block ist bereits in der Liste!");
						}
					}else {
						p.sendMessage(Main.prefix + "§cEs muss ein Block sein!");
					}
				}else {
					p.sendMessage(Main.prefix + "§cDu hast nix in der Hand!");
				}
			}else if(args.length == 1) {
				String material = args[0];
				Material m = Material.valueOf(material);
				if(m != null) {
					if(cfg.getKeys(false).contains(m.toString())) {
						
						cfg.set(m.toString(), null);
						InfoHandler.saveFile(file, cfg);
						p.sendMessage(Main.prefix + "§7Der Block §e" + m.toString() + " §7wurde §centfernt§7.");
						
					}else {
						p.sendMessage(Main.prefix + "§cDer Block ist nicht in der Liste!");
					}
				}else {
					p.sendMessage(Main.prefix + "§cDas Material " + material + " existiert nicht!");
				}
				
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/addsit [Material]");
			}
			
		}else{
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
