package de.koppy.landsystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.project.ChunkEditor;
import de.koppy.project.Main;

public class EVENT_LandMenu implements Listener{
	
	private Main main;
	public EVENT_LandMenu(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onInv(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(LandMenu.openChunklist.contains(e.getPlayer())) {
			LandMenu.openChunklist.remove(e.getPlayer());
			LandMenu.memberlist.remove(e.getPlayer());
			LandMenu.bannedlist.remove(e.getPlayer());
			LandMenu.seitenzahl.remove(e.getPlayer());
			LandMenu.seitenmemberzahl.remove(e.getPlayer());
		}
		
		if(LandMenu.lastseite.get(e.getPlayer()) != null && LandMenu.lastseite.get(e.getPlayer()).equals("menu")) {
			LandMenu.lastseite.remove(p);
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					Inventory inv = LandMenu.Menu(p, "§2L§aandChunk §7- Menu");
					p.openInventory(inv);
				}
			}, 1);
		}else if(LandMenu.lastseite.get(e.getPlayer()) != null && LandMenu.lastseite.get(e.getPlayer()).equals("edit")) {
			LandMenu.lastseite.remove(p);
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					Inventory inv = LandMenu.ModifyLand(p, LandMenu.editingchunk.get(p), "§2L§aand§7-Edit");
					p.openInventory(inv);
				}
			}, 1);
		}
	}
	
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(e.getClick() != null) {
		if(e.getCurrentItem() != null) {
			Player p = (Player) e.getWhoClicked();
			if(LandMenu.openChunklist.contains(p)) {
				e.setCancelled(true);
				
				if(LandMenu.bannedlist.contains(p)) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§c")) {
						p.closeInventory();
						API_Land.removeBanned(API_UUID.getUUIDorNAME(e.getCurrentItem().getItemMeta().getDisplayName().replace("§c", "")), LandMenu.editingchunk.get(p));
						p.sendMessage(COMMAND_Land.prefix + "§7Der Spieler §3" + e.getCurrentItem().getItemMeta().getDisplayName().replace("§c", "") + " §7ist nun §cnicht §7mehr von deinem Grundtück §cgebannt§7.");
					}
				}else if(LandMenu.memberlist.contains(p)) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3")) {
						p.closeInventory();
						API_Land.removeMember(API_UUID.getUUIDorNAME(e.getCurrentItem().getItemMeta().getDisplayName().replace("§3", "")), LandMenu.editingchunk.get(p));
						p.sendMessage(COMMAND_Land.prefix + "§7Der Spieler §3" + e.getCurrentItem().getItemMeta().getDisplayName().replace("§3", "") + " §7ist nun §ckein §7Mitglied mehr.");
					}
				}
				
				if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§6Seite")) {
						
						String[] pfeilsplit = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
						Integer seite = Integer.valueOf(pfeilsplit[2]);
						if(LandMenu.memberlist.contains(p)) {
							LandMenu.setMemberSeite(p, seite, e.getInventory(), "member", LandMenu.editingchunk.get(p));
						}else if(LandMenu.bannedlist.contains(p)) {
							LandMenu.setMemberSeite(p, seite, e.getInventory(), "banned", LandMenu.editingchunk.get(p));
						}
						
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3Seite")) {
						String[] pfeilsplit = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
						Integer seite = Integer.valueOf(pfeilsplit[2]);
						LandMenu.setChunkSeite(p, seite, e.getInventory());
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3GS: ")) {
						String[] gssplit = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
						String id = gssplit[1].replace("§2", "");
						Chunk c = API_Land.getChunk(id, "world");
						LandMenu.lastseite.remove(p);
						
						Inventory inv = LandMenu.ModifyLand(p, c, "§2L§aand§7-Edit");
						p.openInventory(inv);
						
					}
				}
				
				//Land Settings
				ItemStack tpto = new ItemStack(Material.ENDER_PEARL);
				ItemMeta tptom = tpto.getItemMeta();
				tptom.setDisplayName("§3Teleport");
				List<String> tptomlore = new ArrayList<>();
				tptomlore.add("§7» Teleportiert dich zu deinem GS.");
				tptom.setLore(tptomlore);
				tpto.setItemMeta(tptom);
				
				String skullDataModellBanned = "http://textures.minecraft.net/texture/5f23f115cb9520dd4d4cb29124dabac5e6844f96cce241a3ec9ca6f7a296247";
				String skullDataModellMember = "http://textures.minecraft.net/texture/478abdf982f909eab5de9bf969cf14f664db4c447738459ea40162b37d124";
				
				ItemStack skullMember = LandMenu.getSkull(skullDataModellMember);
				SkullMeta skullMm = (SkullMeta) skullMember.getItemMeta();
				skullMm.setDisplayName("§3Member§7-Liste");
				List<String> skullMmlore = new ArrayList<>();
				skullMmlore.add("§7» §7Öffnet eine Liste mit allen Mitgliedern.");
				skullMm.setLore(skullMmlore);
				skullMember.setItemMeta(skullMm);
				
				ItemStack skullBanned = LandMenu.getSkull(skullDataModellBanned);
				SkullMeta skullBm = (SkullMeta) skullBanned.getItemMeta();
				skullBm.setDisplayName("§cBan§7-Liste");
				List<String> skullBmlore = new ArrayList<>();
				skullBmlore.add("§7» §7Öffnet eine Liste aller gebannten Spieler.");
				skullBm.setLore(skullBmlore);
				skullBanned.setItemMeta(skullBm);
				
				if(e.getCurrentItem().isSimilar(tpto)) {
					p.closeInventory();
					Chunk c = LandMenu.editingchunk.get(p);
					p.teleport(API_Land.getChunkMitte(c));
					p.sendMessage(COMMAND_Land.prefix + "§7Du wurdest auf das ausgewählte §aGrundstück §7teleportiert.");
					LandMenu.editingchunk.remove(p);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§3Member§7-Liste")) {
					p.closeInventory();
					LandMenu.memberlist.add(p);
					Chunk c = LandMenu.editingchunk.get(p);
					Inventory inv = LandMenu.getMemberListe(p, "§2L§aandChunk §7- Member", c, "member");
					p.openInventory(inv);
					LandMenu.lastseite.put(p, "edit");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§cBan§7-Liste")) {
					p.closeInventory();
					LandMenu.bannedlist.add(p);
					Chunk c = LandMenu.editingchunk.get(p);
					Inventory inv = LandMenu.getMemberListe(p, "§2L§aandChunk §7- Banned", c, "banned");
					p.openInventory(inv);
					LandMenu.lastseite.put(p, "edit");
				}
				
				
				
				//Land hilfe
				ItemStack help7 = new ItemStack(Material.BOOK);
				ItemMeta help7m = help7.getItemMeta();
				help7m.setDisplayName("§e/land ban <add/remove> <Spieler>");
				List<String> help7lore = new ArrayList<>();
				help7lore.add("§7» Banne oder entbanne Spieler von deinem GS.");
				help7m.setLore(help7lore);
				help7.setItemMeta(help7m);
				
				ItemStack help6 = new ItemStack(Material.BOOK);
				ItemMeta help6m = help6.getItemMeta();
				help6m.setDisplayName("§e/land member <add/remove> <Spieler>");
				List<String> help6lore = new ArrayList<>();
				help6lore.add("§7» Füge oder entferne Spieler von deinem GS als Mitlgied.");
				help6m.setLore(help6lore);
				help6.setItemMeta(help6m);
				
				ItemStack help5 = new ItemStack(Material.BOOK);
				ItemMeta help5m = help5.getItemMeta();
				help5m.setDisplayName("§e/land flag <set/info> <flag> <arg>");
				List<String> help5lore = new ArrayList<>();
				help5lore.add("§7» Verändere das Verhalten deines GS.");
				help5m.setLore(help5lore);
				help5.setItemMeta(help5m);
				
				ItemStack help4 = new ItemStack(Material.BOOK);
				ItemMeta help4m = help4.getItemMeta();
				help4m.setDisplayName("§e/land info");
				List<String> help4lore = new ArrayList<>();
				help4lore.add("§7» Erhalte Infos über das GS auf dem du stehst.");
				help4m.setLore(help4lore);
				help4.setItemMeta(help4m);
				
				ItemStack help3 = new ItemStack(Material.BOOK);
				ItemMeta help3m = help3.getItemMeta();
				help3m.setDisplayName("§e/claims");
				List<String> help3lore = new ArrayList<>();
				help3lore.add("§7» Siehe deine Claim-Tokens ein.");
				help3m.setLore(help3lore);
				help3.setItemMeta(help3m);
				
				ItemStack help2 = new ItemStack(Material.BOOK);
				ItemMeta help2m = help2.getItemMeta();
				help2m.setDisplayName("§e/land claim");
				List<String> help2lore = new ArrayList<>();
				help2lore.add("§7» Erhalte das GS auf dem du stehst.");
				help2lore.add("§c» Kosten: 3-5 Claim-Tokens");
				help2m.setLore(help2lore);
				help2.setItemMeta(help2m);
				
				ItemStack help = new ItemStack(Material.BOOK);
				ItemMeta helpm = help.getItemMeta();
				helpm.setDisplayName("§e/land help <Seite>");
				List<String> helplore = new ArrayList<>();
				helplore.add("§7» Siehe alle Befehle ein.");
				helpm.setLore(helplore);
				help.setItemMeta(helpm);
				if(e.getCurrentItem().isSimilar(help)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("land help");
				}else if(e.getCurrentItem().isSimilar(help2)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("land claim");
				}else if(e.getCurrentItem().isSimilar(help3)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("claims");
				}else if(e.getCurrentItem().isSimilar(help4)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("land info");
				}else if(e.getCurrentItem().isSimilar(help5)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("land flag");
				}else if(e.getCurrentItem().isSimilar(help6)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("land member");
				}else if(e.getCurrentItem().isSimilar(help7)) {
					LandMenu.lastseite.remove(p);
					p.closeInventory();
					p.performCommand("land banplayer");
				}
				
				//Land menu
				ItemStack liste = new ItemStack(Material.PAPER);
				ItemMeta listem = liste.getItemMeta();
				listem.setDisplayName("§2L§aand§7-Liste");
				List<String> listelore = new ArrayList<>();
				listelore.add("§7» Öffnet eine Liste all deiner GS");
				listem.setLore(listelore);
				liste.setItemMeta(listem);
				
				ItemStack autoGS = new ItemStack(Material.JUNGLE_FENCE_GATE);
				ItemMeta autoGSm = autoGS.getItemMeta();
				autoGSm.setDisplayName("§2L§aand§7-Auto");
				List<String> autoGSlore = new ArrayList<>();
				autoGSlore.add("§7» Erhalte ein random GS.");
				autoGSlore.add("§c» Kosten: 5 Claim-Token");
				autoGSm.setLore(autoGSlore);
				autoGS.setItemMeta(autoGSm);
				
				ItemStack schliessen = new ItemStack(Material.BARRIER);
				ItemMeta schliessenm = schliessen.getItemMeta();
				schliessenm.setDisplayName("§4Schließen");
				List<String> schliessenlore = new ArrayList<>();
				schliessenlore.add("§7» Schließt dieses Inventar");
				schliessenm.setLore(schliessenlore);
				schliessen.setItemMeta(schliessenm);
				
				ItemStack info = new ItemStack(Material.REDSTONE_TORCH);
				ItemMeta infom = info.getItemMeta();
				infom.setDisplayName("§2L§aand§7-Hilfe");
				List<String> infolore = new ArrayList<>();
				infolore.add("§7» Öffnet eine Liste an nützlichen Befehlen");
				infom.setLore(infolore);
				info.setItemMeta(infom);
				
				if(e.getCurrentItem().isSimilar(liste)) {
					p.closeInventory();
					p.performCommand("land list");
					LandMenu.lastseite.put(p, "menu");
				}else if(e.getCurrentItem().isSimilar(schliessen)) {
					
					p.closeInventory();
					p.sendMessage(COMMAND_Land.prefix + "§7Das §2L§aand§7-Menu wurde §cgeschlossen§7.");
					
				}else if(e.getCurrentItem().isSimilar(info)) {
					p.closeInventory();
					LandMenu.lastseite.put(p, "menu");
					Inventory inv = LandMenu.Info(p, "§2L§aandChunk §7- Info");
					p.openInventory(inv);
				}else if(e.getCurrentItem().isSimilar(autoGS)) {
					p.closeInventory();
					Integer price = 5;
					if(API_Land.hasEnoughClaims(p.getUniqueId().toString(), price)) {
						Chunk c = API_Land.getRNDMfreeChunk();
						for(int i=0; i<2; i++) {
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								
								@Override
								public void run() {
									ChunkEditor.setParticlesEcken(c, Particle.VILLAGER_HAPPY);
								}
							}, 80*i);
						}
						p.teleport(API_Land.getChunkMitte(c));
						API_Land.registerChunk(p.getUniqueId().toString(), c);
						
						API_Land.removeLandClaims(p.getUniqueId().toString(), price);
						p.sendMessage("§c- §7" + price + " Claim-Tokens");
						p.sendMessage(COMMAND_Land.prefix + "§7Du hast dieses §aGrundstück §7erfolgreich §egeclaimt§7.");
					}else {
						p.sendMessage(COMMAND_Land.prefix + "§cDu hast nicht genug Claim-Tokens dafür. Du benötigst " + (5-API_Land.getLandClaims(p.getUniqueId().toString())) + " weitere.");
					}
				}
			}
			}
		}
	}
	
}
