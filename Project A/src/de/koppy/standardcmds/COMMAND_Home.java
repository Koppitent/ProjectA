package de.koppy.standardcmds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.koppy.project.Main;

public class COMMAND_Home implements CommandExecutor, TabCompleter{

	private Main main;
	public static ArrayList<Player> isinHomeMenu = new ArrayList<>();
	public COMMAND_Home(Main main) {
		this.main = main;
		main.getCommand("home").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Homes", p.getUniqueId().toString()+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(args.length == 0) {
			//Home - GUI
			if(cfg.getKeys(false).size() > 1) {
			isinHomeMenu.add(p);
			Inventory inv = Bukkit.createInventory(null, 9, "§eHomelist");
			for(String homes : cfgTOList(cfg)) {
				ItemStack home = new ItemStack(Material.BOOK);
				ItemMeta homeM = home.getItemMeta();
				homeM.setDisplayName("§e"+homes);
				home.setItemMeta(homeM);
				
				inv.addItem(home);
			}
			p.openInventory(inv);
			}else {
				List<String> homelist = cfgTOList(cfg);
				if(cfg.getKeys(false).size() == 1) p.teleport(cfg.getLocation(homelist.get(0) + ".location"));
			}
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) {
				
				isinHomeMenu.add(p);
				Inventory inv = Bukkit.createInventory(null, 9, "§eHomelist");
				for(String homes : cfg.getKeys(false)) {
					ItemStack home = new ItemStack(Material.BOOK);
					ItemMeta homeM = home.getItemMeta();
					homeM.setDisplayName("§e"+homes);
					home.setItemMeta(homeM);
					
					inv.addItem(home);
				}
				p.openInventory(inv);
			}else {
				String home = args[0];
				if(cfg.getKeys(false).contains(home.toLowerCase())) {
					
					Location loc = cfg.getLocation(home.toLowerCase()+ ".location");
					p.teleport(loc);
					p.sendMessage(Main.prefix + "§7Du wurdest zu deinem Home §3" + home.toLowerCase() + " §7teleportiert.");
					
				}else {
					p.sendMessage(Main.prefix + "§cDas Home " + home.toLowerCase() + " existiert nicht!");
				}
			}
		}
		
		
		return true;	
	}
	
	public static List<String> cfgTOList(FileConfiguration cfg) {
		List<String> list = new ArrayList<>();
		if(cfg.getKeys(false) != null) for(String homes : cfg.getKeys(false)) list.add(homes);
		return list;
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
