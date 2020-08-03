package de.koppy.eco;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.handler.InfoHandler;

public class EconomyMenu {

	public static DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
	public static ArrayList<Player> isinMenu = new ArrayList<>();
	public static HashMap<Player, String> saveBank = new HashMap<>();
	public static HashMap<Player, Integer> seitenzahl = new HashMap<>();
	public static HashMap<Player, String> lastseite = new HashMap<>();
	
	public static Inventory openEcoMenu(Player p, String name) {
		if(!isinMenu.contains(p)) isinMenu.add(p);
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		fillUpWithGlass(inv);
		setEcoInv(p, inv);
		return inv;
	}
	
	public static Inventory openBankMenu(Player p, String name) {
		if(!isinMenu.contains(p)) isinMenu.add(p);
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		fillUpWithGlass(inv);
		setBankInv(p, inv);
		return inv;
	}
	
	public static Inventory openMenuofBankacc(Player p, String name, String bankacc) {
		if(!isinMenu.contains(p)) isinMenu.add(p);
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		fillUpWithGlass(inv);
		setInvofBankacc(p, inv, bankacc);
		return inv;
	}
	
	public static void setInvofMemberList(Player p, Inventory inv, String bankacc) {
		resetInvWithNull(inv);
		fillRandWithGlass(inv);
		lastseite.put(p, bankacc);
		
		for(String playerUUID : Economy.getMembers(EconomyMenu.saveBank.get(p))) {
			String member = API_UUID.getUUIDorNAME(playerUUID);
				
				ItemStack istack = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta istackm = (SkullMeta) istack.getItemMeta();
				istackm.setOwner(member);
				istackm.setDisplayName("§3"+member);
				istack.setItemMeta(istackm);
				
				inv.addItem(istack);
				
		}
	}
	
	public static void setInvofBankacc(Player p, Inventory inv, String bankacc) {
		resetInv(inv);
		lastseite.put(p, "menu");
		if(!isinMenu.contains(p)) isinMenu.add(p);
		saveBank.put(p, bankacc);
		if(Economy.existAcc(bankacc)) {
			
			ItemStack add100000 = new ItemStack(Material.GOLD_BLOCK);
			ItemMeta add100000m = add100000.getItemMeta();
			add100000m.setDisplayName("§6100.000 §3einzahlen");
			add100000.setItemMeta(add100000m);
			
			ItemStack add10000 = new ItemStack(Material.GOLD_BLOCK);
			ItemMeta add10000m = add10000.getItemMeta();
			add10000m.setDisplayName("§610.000 §3einzahlen");
			add10000.setItemMeta(add10000m);
			
			ItemStack add100 = new ItemStack(Material.GOLD_BLOCK);
			ItemMeta add100m = add100.getItemMeta();
			add100m.setDisplayName("§6100 §3einzahlen");
			add100.setItemMeta(add100m);
			
			ItemStack add1000 = new ItemStack(Material.GOLD_BLOCK);
			ItemMeta add1000m = add1000.getItemMeta();
			add1000m.setDisplayName("§61.000 §3einzahlen");
			add1000.setItemMeta(add1000m);
			
			ItemStack minus100000 = new ItemStack(Material.COAL_BLOCK);
			ItemMeta minus100000m = minus100000.getItemMeta();
			minus100000m.setDisplayName("§6100.000 §3auszahlen");
			minus100000.setItemMeta(minus100000m);
			
			ItemStack minus10000 = new ItemStack(Material.COAL_BLOCK);
			ItemMeta minus10000m = minus10000.getItemMeta();
			minus10000m.setDisplayName("§610.000 §3auszahlen");
			minus10000.setItemMeta(minus10000m);
			
			ItemStack minus100 = new ItemStack(Material.COAL_BLOCK);
			ItemMeta minus100m = minus100.getItemMeta();
			minus100m.setDisplayName("§6100 §3auszahlen");
			minus100.setItemMeta(minus100m);
			
			ItemStack minus1000 = new ItemStack(Material.COAL_BLOCK);
			ItemMeta minus1000m = minus1000.getItemMeta();
			minus1000m.setDisplayName("§61.000 §3auszahlen");
			minus1000.setItemMeta(minus1000m);
			
			inv.setItem(10, add100);
			inv.setItem(10+9, add1000);
			inv.setItem(10+9+9, add10000);
			inv.setItem(10+9+9+9, add100000);
			
			inv.setItem(10+3, minus100);
			inv.setItem(10+9+3, minus1000);
			inv.setItem(10+9+9+3, minus10000);
			inv.setItem(10+9+9+9+3, minus100000);
			
			ItemStack money = new ItemStack(Material.SPRUCE_SIGN);
			ItemMeta moneym = money.getItemMeta();
			moneym.setDisplayName("§7» §3Kontostand");
			List<String> moneylore = new ArrayList<>();
			double amount = Economy.getBankBalance(bankacc);
			String famount = f.format(amount);
			moneylore.add("§e"+famount+COMMAND_Eco.moneyformat);
			moneym.setLore(moneylore);
			money.setItemMeta(moneym);
			
			inv.setItem(16, money);
			
			ItemStack Mitglied = InfoHandler.getSkull("http://textures.minecraft.net/texture/478abdf982f909eab5de9bf969cf14f664db4c447738459ea40162b37d124");
			SkullMeta MitgliedM = (SkullMeta) Mitglied.getItemMeta();
			MitgliedM.setDisplayName("§7» §eMitglieder");
			Mitglied.setItemMeta(MitgliedM);
			inv.setItem(42, Mitglied);
			
			ItemStack Map = new ItemStack(Material.LEGACY_EMPTY_MAP);
			ItemMeta MapM = Map.getItemMeta();
			MapM.setDisplayName("§7» §eKontoauszüge");
			Map.setItemMeta(MapM);
			inv.setItem(43, Map);
			
			fillUpWithGlass(inv);
			
		}else{
			p.closeInventory();
			p.sendMessage(COMMAND_Bank.prefix + "§4ERROR! §cBitte kontaktiere einen Admin. §4Fehlercode: #101");
		}
	}
	
	public static void setBankInv(Player p, Inventory inv) {
		resetInv(inv);
		if(!isinMenu.contains(p)) isinMenu.add(p);
		COMMAND_Bank.inbankautomat.add(p);
		
		ItemStack GeldInfo = new ItemStack(Material.SPRUCE_SIGN);
		ItemMeta GeldIngoM = GeldInfo.getItemMeta();
		GeldIngoM.setDisplayName("§7» §3Prozentsatz");
		List<String> geldlore = new ArrayList<>();
		geldlore.add("§e0.5%");
		GeldIngoM.setLore(geldlore);
		GeldInfo.setItemMeta(GeldIngoM);
		
		inv.setItem(11, GeldInfo);
		
		inv.setItem(13, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		inv.setItem(14, null);
		inv.setItem(15, null);
		inv.setItem(16, null);
		
		inv.setItem(13+9, null);
		inv.setItem(14+9, null);
		inv.setItem(15+9, null);
		inv.setItem(16+9, null);
		
		inv.setItem(13+9+9, null);
		inv.setItem(14+9+9, null);
		inv.setItem(15+9+9, null);
		inv.setItem(16+9+9, null);
		
		ItemStack Banks = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta BanksM = (SkullMeta) Banks.getItemMeta();
		BanksM.setOwner("MHF_ArrowLeft");
		BanksM.setDisplayName("§7» §cZurück zu Economy-Menü");
		Banks.setItemMeta(BanksM);
		inv.setItem(37, Banks);
		
		ItemStack Mitglied = InfoHandler.getSkull("http://textures.minecraft.net/texture/b527f1b6f021b39a39bf92df540d7ea00765e4f3c6cb0b7e378c97347840906");
		SkullMeta MitgliedM = (SkullMeta) Mitglied.getItemMeta();
		MitgliedM.setDisplayName("§7» §eBank-Info");
		Mitglied.setItemMeta(MitgliedM);
		inv.setItem(38, Mitglied);
		
		for(String acc : Economy.getAccountNames(p.getUniqueId().toString())) {
			
			ItemStack accs = new ItemStack(Material.PAPER);
			ItemMeta accsM = accs.getItemMeta();
			accsM.setDisplayName("§7» §3"+acc);
			List<String> LogLore = new ArrayList<>();
			accsM.setLore(LogLore);
			accs.setItemMeta(accsM);
			
			inv.addItem(accs);
			
		}
		
		fillUpWithGlass(inv);
		
	}
	
	public static void setEcoInv(Player p, Inventory inv) {
		resetInv(inv);
		if(!isinMenu.contains(p)) isinMenu.add(p);
		
		ItemStack GeldInfo = new ItemStack(Material.SPRUCE_SIGN);
		ItemMeta GeldIngoM = GeldInfo.getItemMeta();
		GeldIngoM.setDisplayName("§7» §3Money");
		List<String> geldlore = new ArrayList<>();
		Double money = Economy.getMoney(p.getUniqueId().toString());
		String fmoney = f.format(money);
		geldlore.add("§e"+fmoney+COMMAND_Eco.moneyformat);
		GeldIngoM.setLore(geldlore);
		GeldInfo.setItemMeta(GeldIngoM);
		
		inv.setItem(11, GeldInfo);
		
		inv.setItem(13, null);
		inv.setItem(14, null);
		inv.setItem(15, null);
		inv.setItem(16, null);
		
		inv.setItem(13+9, null);
		inv.setItem(14+9, null);
		inv.setItem(15+9, null);
		inv.setItem(16+9, null);
		
		inv.setItem(13+9+9, null);
		inv.setItem(14+9+9, null);
		inv.setItem(15+9+9, null);
		inv.setItem(16+9+9, null);
		
		ItemStack GeldVis = new ItemStack(Material.NAME_TAG);
		ItemMeta GeldVisM = GeldVis.getItemMeta();
		GeldVisM.setDisplayName("§7» §5Geld-Sichtbarkeit");
		List<String> GeldVisLore = new ArrayList<>();
		if(Economy.isPublic(p.getUniqueId().toString())) GeldVisLore.add("§aöffentlich");
		else GeldVisLore.add("§cprivat");
		GeldVisM.setLore(GeldVisLore);
		GeldVis.setItemMeta(GeldVisM);
		
		inv.setItem(37, GeldVis);
		
		ItemStack Banks = InfoHandler.getSkull("http://textures.minecraft.net/texture/b527f1b6f021b39a39bf92df540d7ea00765e4f3c6cb0b7e378c97347840906");
		SkullMeta BanksM = (SkullMeta) Banks.getItemMeta();
		BanksM.setDisplayName("§7» §3Zur Bank");
		Banks.setItemMeta(BanksM);
		
		inv.setItem(38, Banks);
		
		for(int i=Economy.getEintraege(p.getUniqueId().toString()); i>0; i--) {
			
			File fileU = new File("plugins/Economy/User", p.getUniqueId().toString()+".yml");
			FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
			
			String logU[] = cfgU.getString(""+i).split(",");
			String amounts = logU[0];
			
			String log = "";
			String reason = logU[1];
			
			DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
			
			if(amounts.startsWith("+")) {
				Double amount = Double.valueOf(amounts.replace("+", ""));
				String famount = f.format(amount);
				log = "§e" + famount + COMMAND_Eco.moneyformat + ", §3" + reason;
			}else if(amounts.startsWith("-")) {
				Double amount = Double.valueOf(amounts.replace("-", ""));
				String famount = f.format(amount);
				log = "§e" + famount + COMMAND_Eco.moneyformat + ", §3" + reason;
			}
			
			
			ItemStack Log = new ItemStack(Material.PAPER);
			ItemMeta LogM = Log.getItemMeta();
			LogM.setDisplayName("§7» §3Log #"+i);
			List<String> LogLore = new ArrayList<>();
			LogLore.add(log);
			LogM.setLore(LogLore);
			Log.setItemMeta(LogM);
			
			inv.addItem(Log);
		}
		
	}
	
	public static void setBankLogInv(Player p, Inventory inv, String bankacc) {
		lastseite.put(p, bankacc);
		resetInvWithNull(inv);
		fillRandWithGlass(inv);
		if(!isinMenu.contains(p)) isinMenu.add(p);
		
		for(int i=Economy.getBankLog(bankacc).size(); i>0; i--) {
			
			File file = new File("plugins/Bank/Accounts", bankacc+".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			String logU[] = cfg.getString(""+i).split(",");
			String amounts = logU[0];
			String reason = logU[1];
			
			DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
			String log = "";
			
			if(amounts.startsWith("+")) {
				Double amount = Double.valueOf(amounts.replace("+", ""));
				String famount = f.format(amount);
				log = "§e" + famount + COMMAND_Eco.moneyformat + ", §3" + reason;
			}else if(amounts.startsWith("-")) {
				Double amount = Double.valueOf(amounts.replace("-", ""));
				String famount = f.format(amount);
				log = "§e" + famount + COMMAND_Eco.moneyformat + ", §3" + reason;
			}
			
			ItemStack Log = new ItemStack(Material.PAPER);
			ItemMeta LogM = Log.getItemMeta();
			LogM.setDisplayName("§7» §3Log #"+i);
			List<String> LogLore = new ArrayList<>();
			LogLore.add(log);
			LogM.setLore(LogLore);
			Log.setItemMeta(LogM);
			
			inv.addItem(Log);
		}
		
	}
	
	public static void fillRandWithGlass(Inventory inv) {
		for(int i=0; i<inv.getSize(); i=i+9) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=8; i<inv.getSize(); i=i+9) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=0; i<9; i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=inv.getSize()-9; i<inv.getSize(); i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
	}
	
	public static void fillUpWithGlass(Inventory inv) {
		for(int i=0; i<inv.getSize(); i++) if(inv.getItem(i) == null) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
	}
	
	public static void resetInv(Inventory inv) {
		for(int i=0; i<inv.getSize(); i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
	}
	
	public static void resetInvWithNull(Inventory inv) {
		for(int i=0; i<inv.getSize(); i++) inv.setItem(i, null);
	}
	
}
