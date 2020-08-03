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
import org.bukkit.inventory.meta.SkullMeta;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.handler.InfoHandler;
import de.koppy.landsystem.LandMenu;
import de.koppy.project.Main;

public class COMMAND_Warp implements CommandExecutor, TabCompleter{
	
	private Main main;
	public COMMAND_Warp(Main main) {
		this.main = main;
		main.getCommand("warp").setExecutor(this);
		main.getCommand("warps").setExecutor(this);
	}
	
	public static ArrayList<Player> removesure = new ArrayList<>();
	public static ArrayList<Player> notclicking = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
		Player p = (Player)sender;
		
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(args.length == 0) {
			//Casual warps-GUI (created)
			Inventory inv = createWarpInv(p, "§3Server-Teleporter");
			p.openInventory(inv);
			
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("create")) {
				p.sendMessage(Main.prefix + "§cVerwende §e/warp create [Name] [Beschreibung] §cum einen Warp zu registrieren.");
			}else if(args[0].equalsIgnoreCase("help")) {
				p.sendMessage(Main.prefix + "§7Verwende §e/warp create [Name] [Beschreibung] §7um einen Warp zu registrieren.");
				p.sendMessage(Main.prefix + "§7Denke daran, dass Warps zuerst von einem Admin geprüft werden müssen. Außerdem besteht nicht die möglichkeit die Location zu wechseln!");
			}else if(args[0].equalsIgnoreCase("list")) {
				
				List<String> warps = getWarps();
				Integer seiten = warps.size()/10;
				if(warps.size() % 10 != 0) {
					seiten++;
				}
				p.sendMessage("§7Warp-Liste (Seite 1/§3"+seiten+"§7):");
				String out = "";
				for(int i=0; i<10; i++) {
					if(warps.size()-1 >= i) {
						out = out + "§7- §3" + warps.get(i) + " §7\n";
					}
				}
				
				p.sendMessage(out);
				
			}else if(args[0].equalsIgnoreCase("adminlist")) {
				if(p.hasPermission("server.warps.admin")) {
					
					Inventory inv = createAdminListInv(p, "§3W§barp§7-Liste");
					p.openInventory(inv);
					
				}
			}else if(args[0].equalsIgnoreCase("status")) {
				//Status anfrage warps
				String out = "";
				for(String warps : getWarpListOfPlayer(p.getUniqueId().toString())) {
					String status = getWarpStatus(p.getUniqueId().toString(), warps);
					out = out + "§7- §3" + warps + " §7§o(" + status + ") \n";
				}
				p.sendMessage("§7Status deiner Warps: §e(" + getWarpAmount(p.getUniqueId().toString()) + " Warp-Tokens übrig)");
				p.sendMessage(out);
			}else {
			//Warp liste mit allen warps
				String warp = args[0];
				if(existWarp(warp) == true) {
					if(canTeleport(p, warp)) {
						Location warploc = getWarpLocation(warp);
						p.teleport(warploc);
						p.sendMessage("§7"+getWarpDescription(warp).replace("&", "§"));
						if(isPublic(warp) == false) p.sendMessage(Main.prefix + "§7Der Warp muss noch §ebestätigt §7werden!");
					}else {
						p.sendMessage(Main.prefix + "§cDu darfst dich nicht zu diesem Warp teleportieren!");
					}
				}else {
					p.sendMessage(Main.prefix + "§cDieser Warp existiert nicht!");
				}
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("list")) {
				if(args[1].matches("[0-9]+")) {
				Integer seite = Integer.valueOf(args[1]);
				List<String> warps = getWarps();
				Integer seiten = warps.size()/10;
				if(warps.size() % 10 != 0) {
					seiten++;
				}
				if(seite > seiten) seite = seiten;
				
				int mineintraege = seite * 10 - 10;
				int maxeintraege = seite * 10;
				
				p.sendMessage("§7Warp-Liste (Seite "+seite+"/§3"+seiten+"§7):");
				String out = "";
				for(int i=mineintraege; i<maxeintraege; i++) {
					if(warps.size()-1 >= i) {
					out = out + "§7- §3" + warps.get(i) + " §7\n";
					}
				}
				
				p.sendMessage(out);
				}else {
					p.sendMessage(Main.prefix + "§cBitte gib eine Zahl an!");
				}
			}else if(args[0].equalsIgnoreCase("create")) {
				p.sendMessage(Main.prefix + "§cVerwende §e/warp create [Name] [Beschreibung] §cum einen Warp zu registrieren.");
			}else if(args[0].equalsIgnoreCase("accept")) {
				if(p.hasPermission("server.warp.admin")) {
					String warp = args[1];
					if(existWarp(warp)) {
						acceptWarp(warp);
						p.sendMessage(Main.prefix+"§7Du hast den Warp §e" + warp + " §7nun §aakzeptiert§7.");
					}else {
						p.sendMessage(Main.prefix + "§cDieser Warp existiert nicht!");
					}
				}
			}else if(args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("reject")) {
				if(p.hasPermission("server.warp.admin")) {
					String warp = args[1];
					if(existWarp(warp)) {
						rejectWarp(warp);
						p.sendMessage(Main.prefix+"§7Du hast den Warp §e" + warp + " §7nun §cabgelehnt§7.");
					}else {
						p.sendMessage(Main.prefix + "§cDieser Warp existiert nicht!");
					}
				}
			}else if(args[0].equalsIgnoreCase("remove")) {
				String warp = args[1];
				if(existWarp(warp)) {
					if(getWarpCreator(warp).equals(p.getUniqueId().toString())) {
						if(removesure.contains(p)) {
							
							removesure.remove(p);
							removeWarp(p.getUniqueId().toString(), warp);
							p.sendMessage(Main.prefix + "§7Du hast erfolgreich dein Warp §e" + warp + " §7gelöscht.");
							
						}else {
							p.sendMessage(Main.prefix + "§4ACHTUNG!!! §cDu bist gerade dabei deinen Warp §4endgültig §czu löschen. Du kannst ihn nicht wieder herstellen. Wenn du dir sichher bist gib den Befehl erneut ein.");
							removesure.add(p);
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									removesure.remove(p);
								}
							}, 20*30);
						}
					}else {
						p.sendMessage(Main.prefix + "§cDu musst der Besitzer des Warps sein!");
					}
				}else {
					p.sendMessage(Main.prefix + "§cDieser Warp existiert nicht!");
				}
			}
		}else if(args.length >= 3) {
			if(args[0].equalsIgnoreCase("add")) {
				if(p.hasPermission("server.warps.admin")) {
				String playername = args[1];
				if(API_UUID.existUUIDorNAME(playername)) {
					if(args[2].matches("[0-9]+")) {
						Integer amount = Integer.valueOf(args[2]);
						if(playername.length() != 36) playername = API_UUID.getUUIDorNAME(playername);
						addWarpAmount(playername, amount);
						p.sendMessage(Main.prefix + "§7Du hast dem Spieler §3" + amount + " §7Warp-Tokens hinzugefügt.");
					}else {
						p.sendMessage("§cBitte gib eine Zahl an.");
					}
				}else {
					p.sendMessage("§cDer Spieler war noch nie auf dem Netzwerk.");
				}
				}else {
					p.sendMessage(Main.noperms);
				}
			}else if(args[0].equalsIgnoreCase("set")) {
				if(p.hasPermission("server.warps.admin")) {
				String playername = args[1];
				if(API_UUID.existUUIDorNAME(playername)) {
					if(args[2].matches("[0-9]+")) {
						Integer amount = Integer.valueOf(args[2]);
						if(playername.length() != 36) playername = API_UUID.getUUIDorNAME(playername);
						setWarpAmount(playername, amount);
						p.sendMessage(Main.prefix + "§7Du hast dem Spieler §3" + amount + " §7Warp-Tokens gesetzt.");
					}else {
						p.sendMessage("§cBitte gib eine Zahl an.");
					}
				}else {
					p.sendMessage("§cDer Spieler war noch nie auf dem Netzwerk.");
				}
				}else {
					p.sendMessage(Main.noperms);
				}
			}else if(args[0].equalsIgnoreCase("create")) {
				String name = args[1];
				if(getWarpAmount(p.getUniqueId().toString()) >= 1 || p.hasPermission("server.warp.admin")) {
				if(existWarp(name) == false){
					String description = "";
					for(int i=2; i<args.length; i++) {
						description = description + " " + args[i];
					}
					
					createWarp(p.getUniqueId().toString(), name, description, p.getLocation());
					p.sendMessage(Main.prefix + "§7Du hast erfolgreich den Warp §e" + name + " §7erstellt. §7Dieser muss vorerst von einem Admin geprüft werden!");
					
				}else {
					p.sendMessage(Main.prefix + "§cDer Warp mit diesem Namen existiert bereits!");
				}
				}else {
					p.sendMessage(Main.prefix + "§cDu hast nicht genug Warp-Tokens. Du kannst welche im §eShop §cerhalten: §eshop.playattack.de");
				}
			}
			
		}
		}else {
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("set")) {
					String playername = args[1];
					if(API_UUID.existUUIDorNAME(playername)) {
					Integer amount = Integer.valueOf(args[2]);
					if(playername.length() != 36) API_UUID.getUUIDorNAME(playername);
					setWarpAmount(playername, amount);
					}
				}else if(args[0].equalsIgnoreCase("add")) {
					String playername = args[1];
					if(API_UUID.existUUIDorNAME(playername)) {
					Integer amount = Integer.valueOf(args[2]);
					if(playername.length() != 36) API_UUID.getUUIDorNAME(playername);
					addWarpAmount(playername, amount);
					}
				}
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
			check.add("create");
			check.add("help");
			check.add("list");
			check.add("remove");
			if(p.hasPermission("server.warps.admin")) {
			check.add("add");
			check.add("set");
			check.add("deny");
			check.add("accept");
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("create")){
				check.add("§cwarpname");
			}else if(args[0].equalsIgnoreCase("remove")){
				for(String w : getWarpListOfPlayer(p.getUniqueId().toString())) check.add(w);
			}else if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("reject")){
				for(String w : getWarpsNotBestaetigt()) check.add(w);
			}else if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add")){
				for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
		}
		
		return tcomplete;
	}
	
	public static Integer getWarpAmount(String pUUID) {
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		if(cfgU.getString(pUUID + ".amount") != null) return cfgU.getInt(pUUID + ".amount");
		else return 0;
	}
	
	public static void setWarpAmount(String pUUID, Integer amount) {
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		cfgU.set(pUUID + ".amount", amount);
		InfoHandler.saveFile(fileU, cfgU);
	}
	
	public static void addWarpAmount(String pUUID, Integer amount) {
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		Integer zahl = getWarpAmount(pUUID);
		cfgU.set(pUUID + ".amount", zahl+amount);
		InfoHandler.saveFile(fileU, cfgU);
	}
	
	public static List<String> getWarpListOfPlayer(String pUUID){
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		List<String> warplist = new ArrayList<>();
		if(cfgU.getStringList(pUUID + ".warpsowned") != null) warplist.addAll(cfgU.getStringList(pUUID + ".warpsowned"));
		return warplist;
	}
	
	public static List<String> getWarpsNotBestaetigt(){
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> warplist = new ArrayList<>();
		for(String warps : cfg.getKeys(false)) {
			if(cfg.getBoolean(warps + ".bestaetigung") == false) {
				warplist.add(warps);
			}
		}
		return warplist;
	}
	
	public static List<String> getWarps(){
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> warplist = new ArrayList<>();
		for(String warps : cfg.getKeys(false)) {
			warplist.add(warps);
		}
		return warplist;
	}
	
	public static String getWarpCreator(String warp) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getString(warp.toLowerCase()+".creator");
	}
	
	public static boolean canTeleport(Player p, String warp) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(p.hasPermission("server.userwarps")) return true;
		else return cfg.getBoolean(warp.toLowerCase()+".bestaetigung");
	}
	
	public static boolean isPublic(String warp) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getBoolean(warp.toLowerCase()+".bestaetigung");
	}
	
	public static boolean existWarp(String warp) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getKeys(false).contains(warp.toLowerCase())) return true;
		else return false;
	}
	
	public static Location getWarpLocation(String name) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getLocation(name.toLowerCase()+".location");
	}
	
	public static String getWarpDescription(String name) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getString(name.toLowerCase()+".description");
	}
	
	public static String getWarpStatus(String pUUID, String name) {
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		return cfgU.getString(pUUID + "."+name.toLowerCase());
	}
	
	public static void acceptWarp(String name) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String pUUID = cfg.getString(name.toLowerCase()+".creator");
		cfg.set(name.toLowerCase()+".bestaetigung", true);
		InfoHandler.saveFile(file, cfg);
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		cfgU.set(pUUID + "."+name.toLowerCase(), "accepted");
		InfoHandler.saveFile(fileU, cfgU);
	}
	
	public static void rejectWarp(String name) {
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String pUUID = cfg.getString(name.toLowerCase()+".creator");
		cfg.set(name.toLowerCase(), null);
		InfoHandler.saveFile(file, cfg);
		addWarpAmount(pUUID, 1);
		
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		cfgU.set(pUUID + "."+name.toLowerCase(), "rejected");
		InfoHandler.saveFile(fileU, cfgU);
	}
	
	public static void createWarp(String pUUID, String name, String description, Location loc) {
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set(name.toLowerCase()+".description", description);
		cfg.set(name.toLowerCase()+".creator", pUUID);
		cfg.set(name.toLowerCase()+".location", loc);
		cfg.set(name.toLowerCase()+".bestaetigung", false);
		InfoHandler.saveFile(file, cfg);
		
		List<String> warps = new ArrayList<>();
		if(cfgU.getStringList(pUUID + ".warpsowned") != null) warps = cfgU.getStringList(pUUID + ".warpsowned");
		if(!warps.contains(name.toLowerCase())) warps.add(name.toLowerCase());
		Integer zahl = getWarpAmount(pUUID);
		cfgU.set(pUUID + ".amount", zahl-1);
		cfgU.set(pUUID + "."+name.toLowerCase(), "bearbeitung");
		cfgU.set(pUUID + ".warpsowned", warps);
		InfoHandler.saveFile(fileU, cfgU);
		
	}
	
	public static void removeWarp(String pUUID, String name) {
		File fileU = new File("plugins/Warps/User", "warpuser.yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		
		File file = new File("plugins/Warps", "warps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set(name.toLowerCase(), null);
		InfoHandler.saveFile(file, cfg);
		
		List<String> warps = cfgU.getStringList(pUUID + ".warpsowned");
		if(warps.contains(name.toLowerCase())) warps.remove(name.toLowerCase());
		cfgU.set(pUUID + "."+name.toLowerCase(), null);
		cfgU.set(pUUID + ".warpsowned", warps);
		InfoHandler.saveFile(fileU, cfgU);
		
	}
	
	public static Inventory createAdminListInv(Player p, String name) {
		Inventory inv = Bukkit.createInventory(null, 6*9, name);
		LandMenu.fillRandWithGlass(inv);
		notclicking.add(p);
		
		for(String warps : getWarpsNotBestaetigt()) {
			
			ItemStack istack = new ItemStack(Material.RED_CONCRETE);
			ItemMeta istackm = istack.getItemMeta();
			istackm.setDisplayName("§7> §3" + warps);
			List<String> istackmlore = new ArrayList<>();
			istackmlore.add("§7§oKlicken zum teleportieren.");
			istackm.setLore(istackmlore);
			istack.setItemMeta(istackm);
			
			inv.addItem(istack);
		}
		return inv;
	}
	
	public static Inventory createWarpInv(Player p, String name) {
		Inventory inv = Bukkit.createInventory(null, 6*9, name);
		LandMenu.fillUpWithGlass(inv);
		notclicking.add(p);
		
		ItemStack blueglass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemMeta blueglassM = blueglass.getItemMeta();
		blueglassM.setDisplayName("");
		blueglass.setItemMeta(blueglassM);
		
		ItemStack lightblueglass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		ItemMeta lightblueglassM = lightblueglass.getItemMeta();
		lightblueglassM.setDisplayName("");
		lightblueglass.setItemMeta(lightblueglassM);
		
		ItemStack gs = InfoHandler.getSkull("http://textures.minecraft.net/texture/4528ed45802400f465b5c4e3a6b7a9f2b6a5b3d478b6fd84925cc5d988391c7d");
		SkullMeta gsM = (SkullMeta) gs.getItemMeta();
		gsM.setDisplayName("§7» §3Grundstücke §7§o(Rechtsklick)");
		List<String> gsMlore = new ArrayList<>();
		gsMlore.add("");
		gsMlore.add("§c> Gelange zu den Grundstücken.");
		gsM.setLore(gsMlore);
		gs.setItemMeta(gsM);
		
		ItemStack hauptstadt = InfoHandler.getSkull("http://textures.minecraft.net/texture/11b3188fd44902f72602bd7c2141f5a70673a411adb3d81862c69e536166b");
		SkullMeta hauptstadtM = (SkullMeta) hauptstadt.getItemMeta();
		hauptstadtM.setDisplayName("§7» §3Hauptstadt §7§o(Rechtsklick)");
		List<String> hauptstadtMlore = new ArrayList<>();
		hauptstadtMlore.add("");
		hauptstadtMlore.add("§c> Gelange zur Hauptstadt.");
		hauptstadtM.setLore(hauptstadtMlore);
		hauptstadt.setItemMeta(hauptstadtM);
		
		ItemStack bank = InfoHandler.getSkull("http://textures.minecraft.net/texture/198df42f477f213ff5e9d7fa5a4cc4a69f20d9cef2b90c4ae4f29bd17287b5");
		SkullMeta bankM = (SkullMeta) bank.getItemMeta();
		bankM.setDisplayName("§7» §3Bank §7§o(Rechtsklick)");
		List<String> bankMlore = new ArrayList<>();
		bankMlore.add("");
		bankMlore.add("§c> Gelange zur Bank.");
		bankM.setLore(bankMlore);
		bank.setItemMeta(bankM);
		
		ItemStack job = InfoHandler.getSkull("http://textures.minecraft.net/texture/ca8d3d3b2e6babece55264136057a26821c3704132966ee9ecca93acc9887a0");
		SkullMeta jobM = (SkullMeta) job.getItemMeta();
		jobM.setDisplayName("§7» §3Job §7§o(Rechtsklick)");
		List<String> jobMlore = new ArrayList<>();
		jobMlore.add("");
		jobMlore.add("§c> Gelange zum Job.");
		jobM.setLore(jobMlore);
		job.setItemMeta(jobM);
		
		ItemStack farmwelt = InfoHandler.getSkull("http://textures.minecraft.net/texture/768ffd4c3d73f32d39eade310556150ba490e0d934ec4a6923c9ebbabbb9c246");
		SkullMeta farmweltM = (SkullMeta) farmwelt.getItemMeta();
		farmweltM.setDisplayName("§7» §3Farmwelt §7§o(Rechtsklick)");
		List<String> farmweltMlore = new ArrayList<>();
		farmweltMlore.add("");
		farmweltMlore.add("§c> Gelange zur Farmwelt.");
		farmweltM.setLore(farmweltMlore);
		farmwelt.setItemMeta(farmweltM);
		
		ItemStack endfarmwelt = InfoHandler.getSkull("http://textures.minecraft.net/texture/f4684e3e7890caf7d13762ea19eb14c5940b88fd7f077d81e6effb4f6df16c26");
		SkullMeta endfarmweltM = (SkullMeta) endfarmwelt.getItemMeta();
		endfarmweltM.setDisplayName("§7» §3End-Farmwelt §7§o(Rechtsklick)");
		List<String> endfarmweltMlore = new ArrayList<>();
		endfarmweltMlore.add("");
		endfarmweltMlore.add("§c> Gelange zur End-Farmwelt.");
		endfarmweltM.setLore(endfarmweltMlore);
		endfarmwelt.setItemMeta(endfarmweltM);
		
		ItemStack netherfarmwelt = InfoHandler.getSkull("http://textures.minecraft.net/texture/2c915db3fc40a79b63c2c453f0c490981e5227c5027501283272138533dea519");
		SkullMeta netherfarmweltM = (SkullMeta) netherfarmwelt.getItemMeta();
		netherfarmweltM.setDisplayName("§7» §3Nether-Farmwelt §7§o(Rechtsklick)");
		List<String> netherfarmweltMlore = new ArrayList<>();
		netherfarmweltMlore.add("");
		netherfarmweltMlore.add("§c> Gelange zur Nether-Farmwelt.");
		netherfarmweltM.setLore(netherfarmweltMlore);
		netherfarmwelt.setItemMeta(netherfarmweltM);
		
		ItemStack barrier = InfoHandler.getSkull("http://textures.minecraft.net/texture/3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025");
		SkullMeta barrierM = (SkullMeta) barrier.getItemMeta();
		barrierM.setDisplayName("§7» §3Not Available §7§o(Rechtsklick)");
		List<String> barrierMlore = new ArrayList<>();
		barrierMlore.add("");
		barrierMlore.add("§c> §ocoming soon.");
		barrierM.setLore(barrierMlore);
		barrier.setItemMeta(barrierM);
		
		//* fist Kapitel
		inv.setItem(10, blueglass);
		inv.setItem(11, gs);
		inv.setItem(12, blueglass);
		inv.setItem(19, bank);
		inv.setItem(20, hauptstadt);
		inv.setItem(21, job);
		
		//* secon Kapitel
		inv.setItem(32, lightblueglass);
		inv.setItem(33, farmwelt);
		inv.setItem(34, lightblueglass);
		inv.setItem(41, endfarmwelt);
		inv.setItem(42, netherfarmwelt);
		inv.setItem(43, barrier);
		
		return inv;
	}
	
	
}
