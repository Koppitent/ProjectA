package de.koppy.missions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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

public class COMMAND_FastTravel implements CommandExecutor, TabCompleter {
	
	private Main main;
	public COMMAND_FastTravel(Main main) {
		this.main = main;
		main.getCommand("fasttravel").setExecutor(this);
		main.getCommand("travel").setExecutor(this);
		main.getCommand("ft").setExecutor(this);
	}
	
	public static ArrayList<Player> inInv = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		File file = new File("plugins/Mission", "locations.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(args.length == 0) {
			
			//* Region - GUI
			
			Inventory inv = Bukkit.createInventory(null, 5*9, "§5Fast-Travel Points");
			inInv.add(p);
			
			for(Regions region : Regions.values()) {
				ItemStack notavailable = new ItemStack(Material.GRAY_DYE);
				ItemMeta notavailableM = notavailable.getItemMeta();
				notavailableM.setDisplayName("§7» §c???");
				List<String> notavailableMlore = new ArrayList<>();
				notavailableMlore.add("");
				notavailableMlore.add("§c| Noch nicht entdeckt!");
				notavailable.setItemMeta(notavailableM);
				
				ItemStack available = new ItemStack(Material.LIME_DYE);
				ItemMeta availableM = available.getItemMeta();
				availableM.setDisplayName("§7» §3"+region.toString());
				List<String> availableMlore = new ArrayList<>();
				availableMlore.add("");
				availableMlore.add("§8| §7Klicken zum teleportieren!");
				available.setItemMeta(availableM);
				
				if(region != Regions.Farmwelten && region != Regions.Wilderness) {
				if(Explorer.wasInRegion(p, region)) inv.addItem(available);
				else inv.addItem(notavailable);
				}
			}
			
			p.openInventory(inv);
			
		}else if(args.length == 1) {
			String regionname = args[0];
			if(Explorer.getAllRegions().contains(regionname)) {
				if(Explorer.wasInRegion(p, Regions.valueOf(regionname))) {
					p.teleport(cfg.getLocation(regionname + ".spawnpoint", p.getLocation()));
					p.sendMessage(Main.prefix + "§7Du wurdest zu dem §eSpawn §7von §3" + regionname + " §7teleportiert.");
				}else {
					p.sendMessage(Main.prefix + "§cDu musst diese Region zuerst erkunden bevor du dich zu ihr teleportieren kannst!");
				}
			}else {
				String out = "| ";
				for(String reg : Explorer.getAllRegions()) out = out + reg + " | ";
				p.sendMessage(Main.prefix + "§cMögliche Regionen: §e");
				p.sendMessage("§e"+out);
			}
		}else if(args.length == 2) {
			if(p.hasPermission("server.mission.admin")) {
				if(args[0].equalsIgnoreCase("setspawnpoint")) {
					String regionname = args[1];
					if(Explorer.getAllRegions().contains(regionname)) {
						cfg.set(regionname + ".spawnpoint", p.getLocation());
						RegionHandler.saveFile(file, cfg);
						p.sendMessage(Main.prefix + "§7Du hast den §eSpawnpoint §7von der Region §3" + regionname + " §7gespeichert!");
					}else {
						p.sendMessage(Main.prefix + "§cDiese Region wurde noch nicht implementiert.");
					}
				}else {
					p.sendMessage(Main.prefix + "§cVerwende §e/setregion 1/2 [RegionName]");
				}
			}else {
				p.sendMessage(Main.prefix + "§cDazu hast du keine Rechte!");
			}
		}else {
			p.sendMessage(Main.prefix + "§cVerwende §e/travel setspawnpoint [RegionName]");
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
