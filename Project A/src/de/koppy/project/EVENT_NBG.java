package de.koppy.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.koppy.eco.Economy;
import de.koppy.handler.ProfileHandler;
import de.koppy.landsystem.API_Land;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class EVENT_NBG implements Listener{

private Main main;
	
	public static String prefix = "§8[§5NBG§8] §r";
	public static String link = "http://playattack.de/nbg";
	
	public EVENT_NBG(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	public static BossBar tutorial1 = Bukkit.createBossBar("§5§nObjective:§r §3Suche dir einen Job!", BarColor.WHITE, BarStyle.SOLID);
	public static BossBar tutorial2 = Bukkit.createBossBar("§5§nObjective:§r §3Erstelle ein Bankkonto!", BarColor.WHITE, BarStyle.SOLID);
	public static BossBar tutorial3 = Bukkit.createBossBar("§5§nObjective:§r §3Hol dir dein erstes Grundstück!", BarColor.WHITE, BarStyle.SOLID);
	
	File file = new File("plugins/Server/NBG", "NBG.yml");
	FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	@EventHandler
	public void onBar(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(ProfileHandler.getTutorial(p.getUniqueId().toString()) != null) {
		if(ProfileHandler.getTutorial(p.getUniqueId().toString()).equals("bank")) {
			p.sendMessage(Main.TutorialBot + "§7Hey! Du hast das letzte mal die Einführung gar nicht abgeschlossen! Kein Problem... Ich erkläre es dir gerne erneut.");
			p.sendMessage(Main.TutorialBot + "§7Am besten du erstellst dir gleich ein Bankkonto. Dafür musst du jedoch zur Bank! §7(§e/warp§7)");
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
			tutorial2.addPlayer(p);
		}else if(ProfileHandler.getTutorial(p.getUniqueId().toString()).equals("job")) {
			p.sendMessage(Main.TutorialBot + "§7Hey! Du hast das letzte mal die Einführung gar nicht abgeschlossen! Kein Problem... Ich erkläre es dir gerne erneut.");
			p.sendMessage(Main.TutorialBot + "§7Am besten fängst du an dir einen Job zu suchen! §7(§e/job§7)");
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
			tutorial1.addPlayer(p);
		}else if(ProfileHandler.getTutorial(p.getUniqueId().toString()).equals("land")) {
			p.sendMessage(Main.TutorialBot + "§7Hey! Du hast das letzte mal die Einführung gar nicht abgeschlossen! Kein Problem... Ich erkläre es dir gerne erneut.");
			p.sendMessage(Main.TutorialBot + "§7Am besten du nimmst dir nun dein erstes Grundstück! §7(§e/land auto§7)");
			p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
			tutorial3.addPlayer(p);
		}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getCurrentItem() != null) {
		Player p = (Player) e.getWhoClicked();
			
			ItemStack disagree = new ItemStack(Material.RED_WOOL);
			ItemMeta disagreem = disagree.getItemMeta();
			disagreem.setDisplayName("§c§lAblehnen");
			disagree.setItemMeta(disagreem);
			
			ItemStack agree = new ItemStack(Material.GREEN_WOOL);
			ItemMeta agreem = agree.getItemMeta();
			agreem.setDisplayName("§a§lAnnehmen");
			agree.setItemMeta(agreem);
			
			ItemStack book = new ItemStack(Material.BOOK);
			ItemMeta bookM = book.getItemMeta();
			bookM.setDisplayName("§3Hier findest du unsere Nutzungsbedingungen!");
			List<String> lore = new ArrayList<>();
			lore.add("§7Klicken um auf die Website zu kommen.");
			lore.add("");
			lore.add("§3" + link);
			bookM.setLore(lore);
			book.setItemMeta(bookM);
			
			if(e.getClickedInventory().contains(disagree)) {
				e.setCancelled(true);
			}
			
			if(e.getCurrentItem().isSimilar(disagree)) {
				p.kickPlayer(prefix + "§cDu musst zustimmen um auf dem Server spielen zu können.");
			}else if(e.getCurrentItem().isSimilar(agree)) {
				cfg.set(p.getUniqueId().toString(), true);
				try {
					cfg.save(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				p.closeInventory();
				p.sendMessage(prefix + "§aDu hast erfolgreich den Nutzungsbestimmungen zugestimmt.");
				ProfileHandler.setTutorial(p.getUniqueId().toString(), "job");
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						API_Land.addLandClaims(p.getUniqueId().toString(), 10);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), 1000);
						p.sendMessage(Main.TutorialBot + "§7Hey was geht neuer Spieler!");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
					}
				}, 20*4);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						p.sendMessage(Main.TutorialBot + "§7Du kannst dich zu allen wichtigen Orten mit §e/warp §7teleportieren!");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
					}
				}, 20*5);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						p.sendMessage(Main.TutorialBot + "§7Außerdem schaltest du durch das Erkunden der Map §3Schnellreisepunkte §7frei. (§e/travel§7)!");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
					}
				}, 20*6);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						p.sendMessage(Main.TutorialBot + "§7Mit §e/eco menu §7kannst du deine Geldeinstellungen einsehen!");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
					}
				}, 20*11);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						p.sendMessage(Main.TutorialBot + "§7Mit §e/help §7kannst du dir immer alle wichtigen Befehle einsehen!");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
					}
				}, 20*15);
				
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						p.sendMessage(Main.TutorialBot + "§7Am besten fängst du an dir einen Job zu suchen! §7(§e/job§7)");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
						p.sendTitle("§3Suche dir einen Job!", "§5new Objective!");
						tutorial1.addPlayer(p);
					}
				}, 20*20);
				
				File fileU = new File("plugins/Server/UUIDs", "UUIDFetcher.yml");
				FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
				
				Bukkit.broadcastMessage("§8§m-----------------§r§8§m-----------------");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("   §8✖  §7Der Spieler §e" + p.getName() + " §7ist nun auch Mitglied auf dem Server! §8[§3#"+(cfgU.getKeys(false).size()/3)+"§8]");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§8§m-----------------§r§8§m-----------------");
				
				
			}else if(e.getCurrentItem().isSimilar(book)) {
				
				TextComponent tc = new TextComponent();
				tc.setText(prefix + "§7Bitte klicke §f§o§nhier§r §7um auf die Website zu gelangen.");
				tc.setBold(true);
				tc.setClickEvent(new ClickEvent(Action.OPEN_URL, link));
				tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aKlicken um zu den NBG's zu gelangen!").create()));
				
				p.spigot().sendMessage(tc);
				
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(cfg.getBoolean(p.getUniqueId().toString()) == false) {
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					openNBG(p);
				}
			}, 5);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(!p.hasPlayedBefore()) {
			cfg.set(p.getUniqueId().toString(), false);
			try {
				cfg.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if(cfg.getBoolean(p.getUniqueId().toString()) == false) {
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					openNBG(p);
				}
			}, 5);
		}
	}
	
	
	public static void openNBG(Player p) {
		Inventory inv = p.getServer().createInventory(null, 9*3, "§5§lNBG§e's");
		
		fillwithGlass(inv);
		
		ItemStack disagree = new ItemStack(Material.RED_WOOL);
		ItemMeta disagreem = disagree.getItemMeta();
		disagreem.setDisplayName("§c§lAblehnen");
		disagree.setItemMeta(disagreem);
		
		ItemStack agree = new ItemStack(Material.GREEN_WOOL);
		ItemMeta agreem = agree.getItemMeta();
		agreem.setDisplayName("§a§lAnnehmen");
		agree.setItemMeta(agreem);
		
		ItemStack book = new ItemStack(Material.BOOK);
		ItemMeta bookM = book.getItemMeta();
		bookM.setDisplayName("§3Hier findest du unsere Nutzungsbedingungen!");
		List<String> lore = new ArrayList<>();
		lore.add("§7Klicken um auf die Website zu kommen.");
		lore.add("");
		lore.add("§3" + link);
		bookM.setLore(lore);
		book.setItemMeta(bookM);
		
		inv.setItem(0+9, agree);
		inv.setItem(4+9, book);
		inv.setItem(8+9, disagree);
		
		p.openInventory(inv);
	}
	
	public static void fillwithGlass(Inventory inv) {
		for(int i=0; i<inv.getSize(); i++) {
			inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		}
	}
	
}
