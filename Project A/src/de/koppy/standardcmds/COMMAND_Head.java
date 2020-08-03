package de.koppy.standardcmds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.inventory.meta.SkullMeta;

import de.koppy.handler.InfoHandler;
import de.koppy.project.Main;

public class COMMAND_Head implements CommandExecutor, TabCompleter{

	public static HashMap<Player, Integer> seitenzahl = new HashMap<>();
	public static ArrayList<Player> isinHeadList = new ArrayList<>();
	
	private Main main;
	public COMMAND_Head(Main main) {
		this.main = main;
		main.getCommand("head").setExecutor(this);
		main.getCommand("kopf").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.head")) {
		if(args.length == 0) {
			
			//Menu-GUI
			Inventory inv = getHeadListe(p, "§3H§beah§7-Liste");
			p.openInventory(inv);
			
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) {
				if(p.hasPermission("server.head.database")) {
					
					File file = new File("plugins/Server/HeadDatabase", "heads.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					
					String headnames = "";
					for(String head : cfg.getKeys(false)) headnames = headnames + head + ", ";
					p.sendMessage(Main.prefix + "§3Alle Köpfe: §e" + headnames);
					
				}else {
					p.sendMessage(Main.noperms);
				}
			}else {
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta skullm = (SkullMeta) skull.getItemMeta();
			skullm.setOwner(args[0]);
			skullm.setDisplayName("§3§o" + args[0]);
			skull.setItemMeta(skullm);
			
			p.getInventory().addItem(skull);
			p.sendMessage(Main.prefix + "§7Du hast den §aSpielerkopf §7von §3" + args[0] + " §7erhalten.");
			}
		}else if(args.length == 2) {
			if(p.hasPermission("server.head.database")) {
			if(args[0].equalsIgnoreCase("getURLHead")) {
				String skullMetaDataUrl = args[1];
				
				ItemStack skull = InfoHandler.getSkull(skullMetaDataUrl);
				SkullMeta skullm = (SkullMeta) skull.getItemMeta();
				skullm.setDisplayName("§3Customhead");
				skull.setItemMeta(skullm);
				
				p.getInventory().addItem(skull);
				p.sendMessage(Main.prefix + "§7Du hast den §aKopf §7mit der angegebenen URl erhalten.");
				
			}else if(args[0].equalsIgnoreCase("getfromdatabank") || args[0].equalsIgnoreCase("get")) {
				String headname = args[1];
				
				File file = new File("plugins/Server/HeadDatabase", "heads.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				
				if(cfg.getKeys(false).contains(headname)) {
					String skullMetaDataUrl = cfg.getString(headname + ".URL");
					
					ItemStack skull = InfoHandler.getSkull(skullMetaDataUrl);
					SkullMeta skullm = (SkullMeta) skull.getItemMeta();
					skullm.setDisplayName("§3" + headname);
					skull.setItemMeta(skullm);
					
					p.getInventory().addItem(skull);
					p.sendMessage(Main.prefix + "§7Du hast den Kopf §3" + args[1] + " §7aus der §eDatenbank §7erhalten.");
					
				}else {
					p.sendMessage(Main.prefix + "§cDieser Kopf existiert nicht in der §eDatenbank§c!");
				}
				
			}else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
				String headname = args[1];
				
				File file = new File("plugins/Server/HeadDatabase", "heads.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				
				if(cfg.getKeys(false).contains(headname)) {
					
					cfg.set(headname, null);
					try {cfg.save(file);} catch (IOException e) {e.printStackTrace();}
					p.sendMessage(Main.prefix + "§7Du hast den Kopf §3" + args[1] + " §7aus der §eDatenbank §7erfolgreich §centfernt.");
					
				}else {
					p.sendMessage(Main.prefix + "§cDieser Kopf existiert nicht in der §eDatenbank§c!");
				}
				
			}
			}else {
				p.sendMessage(Main.noperms);
			}
		}else if(args.length == 3) {
			if(p.hasPermission("server.head.database")) {
			if(args[0].equalsIgnoreCase("save")) {
				String skullMetaDataUrl = args[2];
				String headname = args[1];
				
				File file = new File("plugins/Server/HeadDatabase", "heads.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				
				if(cfg.getKeys(false).contains(headname) == false) {
					
					cfg.set(headname + ".URL", skullMetaDataUrl);
					try {cfg.save(file);} catch (IOException e) {e.printStackTrace();}
					p.sendMessage(Main.prefix + "§7Der Kopf §3" + headname + " §7wurde in der §eDatenbank §7gespeichert!");
					
				}else {
					p.sendMessage(Main.prefix + "§cDieser Kopfname existiert bereits in der §eDatenbank§c!");
				}
			}
			}else {
				p.sendMessage(Main.noperms);
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
		Player p = (Player)sender;
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			if(p.hasPermission("server.head.database")) {
			check.add("save");
			check.add("delete");
			check.add("get");
			}
			check.add("getURLHead");
			check.add("§4§l✘ §r§cKopfname §4§l✘");
			for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("getURLHead")) {
				check.add("§4§l✘ §r§cHead-URL §4§l✘");
			}else if(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("delete")) {
				File file = new File("plugins/Server/HeadDatabase", "heads.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				for(String head : cfg.getKeys(false)) check.add(head);
			}else {
				check.add("§4§l✘ §r§cKopf-Name §4§l✘");
			}
			for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
		}else if(args.length == 3) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("save")) check.add("§4§l✘ §r§cHead-URL §4§l✘");
			for(String s : check) if(s.startsWith(args[2])) tcomplete.add(s);
		}
		return tcomplete;
	}
	
	public static void setHeadSeite(Player p, Integer seite, Inventory inv) {
		if(seitenzahl.containsKey(p)) {
			
			Integer seitevorn = seite+1;
			ItemStack pfeilvorn = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta pfeilskull = (SkullMeta) pfeilvorn.getItemMeta();
			pfeilskull.setOwner("MHF_ArrowRight");
			pfeilskull.setDisplayName("§3Seite §7- " + seitevorn);
			pfeilvorn.setItemMeta(pfeilskull);
			
			Integer seitehinten = seite-1;
			ItemStack pfeilhinten = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta pfeilhintenskull = (SkullMeta) pfeilhinten.getItemMeta();
			pfeilhintenskull.setOwner("MHF_ArrowLeft");
			pfeilhintenskull.setDisplayName("§3Seite §7- " + seitehinten);
			pfeilhinten.setItemMeta(pfeilhintenskull);
			
			if(seite == 1) {
				if(seitenzahl.get(p) > 1) {
				//nur größer Pfeil
					
					addHeadsforSeite(p, seite, inv);
					
				inv.setItem(inv.getSize()-2, pfeilvorn);
				inv.setItem(inv.getSize()-8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				}else {
					//keine pfeile
					addHeadsforSeite(p, seite, inv);
					
					inv.setItem(inv.getSize()-8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
					inv.setItem(inv.getSize()-2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				}
			}else if(seitenzahl.get(p) > seite) {
				//größer und kleiner Pfeil
				addHeadsforSeite(p, seite, inv);
				
				inv.setItem(inv.getSize()-2, pfeilvorn);
				inv.setItem(inv.getSize()-8, pfeilhinten);
			}else if(seitenzahl.get(p) == seite) {
				//nur kleiner Pfeil
				addHeadsforSeite(p, seite, inv);
				
				inv.setItem(inv.getSize()-2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				inv.setItem(inv.getSize()-8, pfeilhinten);
			}
		}
	}
	
	public static void addHeadsforSeite(Player p, Integer seite, Inventory inv) {
		File file = new File("plugins/Server/HeadDatabase", "heads.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> chunkIDs = getHeadList();
		Integer chunksfrom = (28*seite)-28;
		Integer chunksto = seite*28;
		chunksfrom++;
		chunksto++;
		if(seite == seitenzahl.get(p)) chunksto = chunkIDs.size()+1;
		for(int i=0; i<7; i++) {
			inv.setItem(10+i, null);
			inv.setItem(19+i, null);
			inv.setItem(28+i, null);
			inv.setItem(37+i, null);
		}
		for(int i=chunksfrom; i<chunksto; i++) {
			if(chunkIDs.get(i-1) != null) {
				
				// Normal Chest = http://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622
				// Green Chest = http://textures.minecraft.net/texture/5f23f115cb9520dd4d4cb29124dabac5e6844f96cce241a3ec9ca6f7a296247
				
				String skullDataModell = cfg.getString(chunkIDs.get(i-1) + ".URL");
				ItemStack istack = InfoHandler.getSkull(skullDataModell);
				SkullMeta istackm = (SkullMeta) istack.getItemMeta();
				istackm.setDisplayName("§3Kopf von §2§o" + chunkIDs.get(i-1));
				List<String> istacklore = new ArrayList<>();
				istacklore.add("§7Nummer §e" + i);
				istackm.setLore(istacklore);
				istack.setItemMeta(istackm);
				
				inv.addItem(istack);
				
			}
		}
	}
	
	public static Inventory getHeadListe(Player p, String name) {
		File file = new File("plugins/Server/HeadDatabase", "heads.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		isinHeadList.add(p);
		String pUUID = p.getUniqueId().toString();
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		fillRandWithGlass(inv);
		
		Integer seitenanzahl = 0;
		List<String> chunkIDlist = getHeadList();
		
		if(chunkIDlist.size()%28 == 0) seitenanzahl = chunkIDlist.size()/28;
		else seitenanzahl = (chunkIDlist.size()/28)+1;
		seitenzahl.put(p, seitenanzahl);
		
		setHeadSeite(p, 1, inv);
		
		return inv;
	}
	
	public static List<String> getHeadList() {
		File file = new File("plugins/Server/HeadDatabase", "heads.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> chunklist = new ArrayList<>();
		for(String chunkID : cfg.getKeys(false)) {
				chunklist.add(chunkID);
		}
		return chunklist;
	}
	
	public static void fillRandWithGlass(Inventory inv) {
		for(int i=0; i<inv.getSize(); i=i+9) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=8; i<inv.getSize(); i=i+9) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=0; i<9; i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=inv.getSize()-9; i<inv.getSize(); i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
	}
	
}
