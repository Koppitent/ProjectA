package de.koppy.landsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.cases.Case;
import de.koppy.cases.Cases;
import de.koppy.handler.InfoHandler;
import de.koppy.handler.ProfileHandler;
import de.koppy.project.ChunkEditor;
import de.koppy.project.ChunkHelper;
import de.koppy.project.EVENT_NBG;
import de.koppy.project.Main;

public class COMMAND_Land implements CommandExecutor, TabCompleter{
	
	private Main main;
	public static String prefix = "§8[§2L§aandChunk§8] §r";
	public static String err000 = prefix + "§cDu musst der Besitzer des Grundstücks sein. §4(0)";
	public static String err001 = prefix + "§cDieses §aGrundstück §cgehört niemanden. §4(1)";
	public static String err002 = prefix + "§cDieses §aGrundstück §cgehört niemanden. §4(2)";
	public static String err003 = prefix +  "§cDu musst dafür in der Grundstückswelt sein. §4(3)";
	public static String err004 = prefix + "§cDieses §aGrundstück §cwurde bereits geclaimt. §4(4)";
	public static String err005 = prefix + "§cDu hast nicht genug §eClaims §cübrig! §4(5)";
	public static String err006 = prefix + "§cDieses Biom existiert nicht! §4(6)";
	public static String err007 = prefix + "§cDieser Spieler war noch nie auf dem Server! §4(7)";
	public static String err008 = prefix + "§cDieser Spieler ist bereits Mitglied auf deinem GS! §4(8)";
	public static String err009 = prefix + "§cDieser Spieler ist gar kein Mitglied auf deinem GS! §4(9)";
	public static String err010 = prefix + "§cDieser Spieler ist bereits von deinem GS entbannt! §4(10)";
	public static String err011 = prefix + "§cDieser Spieler ist gar nicht von deinem GS gebannt! §4(11)";
	public static String err012 = prefix + "§cDer Owner oder Mitglieder können nicht gebannt werden! §4(12)";
	public static String err013 = prefix + "§cDer Owner kann kein Mitglied sein! §4(13)";
	public static String err014 = prefix + "§cDu besitzt keine Grundstücke. §4(14)";
	public static String err015 = prefix + "§cDu besitzt nicht so viele Grundstücke. §4(15)";
	public static String err016 = prefix + "§cDu Spieler besitzt nicht so viele Grundstücke. §4(16)";
	public static boolean globalBuild = true;
	public static HashMap<Chunk, Player> claimrequest = new HashMap<>();
	public static HashMap<Chunk, Player> removerequest = new HashMap<>();
	public static HashMap<Player, Chunk> autorequest = new HashMap<>();
	public COMMAND_Land(Main main) {
		this.main = main;
		main.getCommand("land").setExecutor(this);
	}
	
	public static Location pos1 = new Location(Bukkit.getWorld("world"), 2216, 0, -9355);
	public static Location pos2 = new Location(Bukkit.getWorld("world"), 8704, 255, -712);
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length == 0) {
			// Liste Menu
			Inventory inv = LandMenu.Menu(p, "§2L§aandChunk §7- Menu");
			p.openInventory(inv);
			
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("edit")) {
					
				if(API_Land.existChunk(p.getLocation().getChunk())) {
					Inventory inv = LandMenu.ModifyLand(p, p.getLocation().getChunk(), "§2L§aand§7-Edit");
					p.openInventory(inv);
				}else {
					p.sendMessage(err001);
				}
				
			}else if(args[0].equalsIgnoreCase("menu")) {
				
				Inventory inv = LandMenu.Menu(p, "§2L§aandChunk §7- Menu");
				p.openInventory(inv);
				
			}else if(args[0].equalsIgnoreCase("nehmen")) {
				
				if(p.hasPermission("server.land.admin")) {
					
					Chunk c = p.getLocation().getChunk();
					for(int x=0; x<2; x++) {
						for(int z=0; z<2; z++) {
							
							Chunk e = p.getWorld().getChunkAt(c.getX()+x, c.getZ());
							Chunk f = p.getWorld().getChunkAt(c.getX()-x, c.getZ());
							Chunk g = p.getWorld().getChunkAt(c.getX(), c.getZ()-z);
							Chunk h = p.getWorld().getChunkAt(c.getX(), c.getZ()+z);
							Chunk i = p.getWorld().getChunkAt(c.getX()+x, c.getZ()+z);
							Chunk j = p.getWorld().getChunkAt(c.getX()+x, c.getZ()-z);
							Chunk k = p.getWorld().getChunkAt(c.getX()-x, c.getZ()+z);
							Chunk l = p.getWorld().getChunkAt(c.getX()-x, c.getZ()-z);
							
							API_Land.registerChunk(p.getUniqueId().toString(), c);
							API_Land.registerChunk(p.getUniqueId().toString(), e);
							API_Land.registerChunk(p.getUniqueId().toString(), f);
							API_Land.registerChunk(p.getUniqueId().toString(), g);
							API_Land.registerChunk(p.getUniqueId().toString(), h);
							API_Land.registerChunk(p.getUniqueId().toString(), i);
							API_Land.registerChunk(p.getUniqueId().toString(), j);
							API_Land.registerChunk(p.getUniqueId().toString(), k);
							API_Land.registerChunk(p.getUniqueId().toString(), l);
							
						}
					}
					
				}else {
					p.sendMessage(prefix + "§cKeine Rechte!");
				}
				
			}else if(args[0].equalsIgnoreCase("member") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("banplayer")) {
				
				p.sendMessage(prefix + "§cVerwende §e/land " + args[0] + " <add/remove/list> [Name]");
				
			}else if(args[0].equalsIgnoreCase("liste") || args[0].equalsIgnoreCase("list")) {
				
				Inventory inv = LandMenu.getChunkListe(p, "§2L§aandChunk §7- Liste");
				p.openInventory(inv);
				
			}else if(args[0].equalsIgnoreCase("help")) {
				
				p.sendMessage("§8§m-------------§r§8[§2L§aandChunk§7 / Seite 1§8]§m-------------");
				p.sendMessage("§e/land claim §7- Claim das GS auf dem du stehst.");
				p.sendMessage("§e/land auto §7- Erhalte ein zufälliges Grundstück.");
				p.sendMessage("§e/land info §7- Siehe Infos über das momentane GS.");
				p.sendMessage("§e/land flag set pve <true/false> §7- Ändert das PVP-Verhalten auf deinem GS.");
				p.sendMessage("§e/land flag set pvp <true/false> §7- Ändert das PVP-Verhalten auf deinem GS.");	
				p.sendMessage("§e/land flag set biome <biome> §7- Ändert dein Biome für 1 CT.");	
				p.sendMessage("§8§m------------------------------------");
				
			}else if(args[0].equalsIgnoreCase("auto")) {
				Integer price = 5;
				if(API_Land.hasEnoughClaims(p.getUniqueId().toString(), price)) {
				if(autorequest.get(p) != null) {
					Chunk c = autorequest.get(p);
					autorequest.put(p, c);
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
					ChunkEditor.setRandBlock(p, Material.TORCH);
					
					API_Land.removeLandClaims(p.getUniqueId().toString(), price);
					p.sendMessage("§c- §7" + price + " Claim-Tokens");
					p.sendMessage(prefix + "§7Du hast dieses §aGrundstück §7erfolgreich §egeclaimt§7.");
					
					//* Tutorial
					if(ProfileHandler.getTutorial(p.getUniqueId().toString()).equals("land")) {
						ProfileHandler.setTutorial(p.getUniqueId().toString(), "finished");
						
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(Main.TutorialBot + "§7Und? Wie gefällt dir dein neues §eGS§7? Wenn du möchtest, kannst du dir sogar noch das Grundstück nebendran nehmen! Da jedes Grundstück ein Chunk groß ist, kannst du mit §eF3 + G §7sehen, wo das nächste Grundstück beginnt. Sobal du darauf sethst musst du nurnoch §e/land claim §7eingeben!");
								EVENT_NBG.tutorial3.removePlayer(p);
							}
						}, 20*2);
						
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(Main.TutorialBot + "§7Weitere §eClaim-Tokens§7, die du für Grundstücke benötigst, kannst du durchs voten erhalten oder mit In-Game Geld kaufen. Alle anderen wichtigen Befehle siehst du mit §e/land help§7.");
							}
						}, 20*10);
						
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(Main.TutorialBot + "§7Das wären nun die wichtigsten Infos zum System gewesen! Natürlich gibt es noch viel mehr zu entdecken... Wenn du dich später einmal eingelebt hast interessierst du dich vllt auch für einen eigenen §3Warp§7. Infos dazu findest du dann unter §e/warp help§7.");
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
							}
						}, 20*15);
						
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								p.sendMessage(Main.TutorialBot + "§7Du bist nun fertig mit der Einführung! Als kleines §5Willkommens-Geschenk §7habe ich hier noch eine §5Vote-Kiste mit Key §7für dich! Bei weiteren fragen kannst du dich gerne an einen §eSupporter §7wenden. ich wünsche dir noch viel Spaß!");
								p.getInventory().addItem(Case.giveCase(Cases.VOTE));
								p.getInventory().addItem(Case.giveCaseKey(Cases.VOTE));
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
							}
						}, 20*20);
						
					}
					
				}else {
					Chunk c = API_Land.getRNDMfreeChunk();
					ChunkEditor.setParticles(ChunkHelper.getChunk(p), Particle.DRIP_WATER);
					autorequest.put(p, c);
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						@Override
						public void run() {
							autorequest.remove(p, c);
						}}, 20*30);
					
					p.sendMessage(prefix + "§7Wenn du §7wirklich ein RNDM §aGrundstück §7für §e" + price + " Claim-Tokens §7kaufen möchtest, gib den §eBefehl §7erneut ein.");
					
				}
				}else {
					p.sendMessage(prefix + "§cDu hast nicht genug Claim-Tokens dafür. Du benötigst " + (5-API_Land.getLandClaims(p.getUniqueId().toString())) + " weitere.");
				}
				
			}else if(args[0].equalsIgnoreCase("info")) {
				if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(API_Land.existChunk(ChunkHelper.getChunk(p))) {
					
					for(int i=0; i<2; i++) {
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							
							@Override
							public void run() {
								ChunkEditor.setParticles(ChunkHelper.getChunk(p), Particle.DRIP_WATER);
							}
						}, 80*i);
					}
					
					String member = "";
					if(API_Land.getMember(ChunkHelper.getChunk(p)) != null) {
						for(String uuids : API_Land.getMember(ChunkHelper.getChunk(p))) {
							uuids = API_UUID.getUUIDorNAME(uuids);
							member = member + uuids + ", ";
						}
					}else {
						member = "§ckeinee";
					}
					member = member.trim();
					member = member.substring(0, member.length() - 1);
					
					String banned = "";
					if(API_Land.getBanned(ChunkHelper.getChunk(p)) != null) {
						for(String uuids : API_Land.getBanned(ChunkHelper.getChunk(p))) {
							uuids = API_UUID.getUUIDorNAME(uuids);
							banned = banned + uuids + ", ";
						}
					}else {
						banned = "§ckeinee";
					}
					banned = banned.trim();
					banned = banned.substring(0, banned.length() - 1);
					
					p.sendMessage("§8§m-|--------§r§8[§2L§aandChunk§8]§m--------|-");
					p.sendMessage("§8-| §4Besitzer: §c" + API_UUID.getUUIDorNAME(API_Land.getOwner(ChunkHelper.getChunk(p))));
					p.sendMessage("§8-|");
					p.sendMessage("§8-| §3Mitglieder: §7" + member);
					p.sendMessage("§8-| §3Gebannt: §7" + banned);
					p.sendMessage("§8-| §3ID: §7x" + ChunkHelper.getChunk(p).getX() + " z" + ChunkHelper.getChunk(p).getZ());
					p.sendMessage("§8-| §3Baurechte: §7" + API_Land.canBuildonChunk(p.getUniqueId().toString(), ChunkHelper.getChunk(p)));
					p.sendMessage("§8-| §3Flags: §7" + API_Land.getChunkFlags(ChunkHelper.getChunk(p)));
					p.sendMessage("§8§m-|--------------------------|-");
					
				}else {
					p.sendMessage(err001);
				}
				}else {
					p.sendMessage(err003);
				}
			}else if(args[0].equalsIgnoreCase("claim")) {
					Integer price = 0;
					if(API_Land.ownNearChunk(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
						price = 3;
					}else {
						price = 5;
					}
				if(p.getWorld().getName().equals("world")  && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(API_Land.existChunk(ChunkHelper.getChunk(p)) == false) {
				if(API_Land.hasEnoughClaims(p.getUniqueId().toString(), price)) {
				if(claimrequest.get(ChunkHelper.getChunk(p)) != null && claimrequest.get(ChunkHelper.getChunk(p)).equals(p)) {
					
					claimrequest.remove(ChunkHelper.getChunk(p), p);
					for(int i=0; i<2; i++) {
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							
							@Override
							public void run() {
								ChunkEditor.setParticlesEcken(ChunkHelper.getChunk(p), Particle.VILLAGER_HAPPY);
							}
						}, 80*i);
					}
					API_Land.registerChunk(p.getUniqueId().toString(), ChunkHelper.getChunk(p));
					ChunkEditor.setRandBlock(p, Material.TORCH);
					
					API_Land.removeLandClaims(p.getUniqueId().toString(), price);
					p.sendMessage("§c- §7" + price + " Claim-Tokens");
					
					p.sendMessage(prefix + "§7Du hast dieses §aGrundstück §7erfolgreich §egeclaimt§7.");
					
				}else {
					ChunkEditor.setParticles(ChunkHelper.getChunk(p), Particle.DRIP_WATER);
					claimrequest.put(ChunkHelper.getChunk(p), p);
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						@Override
						public void run() {
							claimrequest.remove(ChunkHelper.getChunk(p), p);
						}}, 20*30);
					
					p.sendMessage(prefix + "§7Wenn du das §aGrundstück §7wirklich für §e" + price + " Claim-Tokens §7kaufen möchtest, gib den §eBefehl §7erneut ein.");
					
				}
				}else {
					p.sendMessage(err005);
				}
				}else {
					p.sendMessage(err004);
				}
				}else {
					p.sendMessage(err003);
				}
			}else if(args[0].equalsIgnoreCase("remove")) {
				if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(API_Land.existChunk(ChunkHelper.getChunk(p)) == true) {
					if(API_Land.isOwner(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
					if(removerequest.get(ChunkHelper.getChunk(p)) != null && removerequest.get(ChunkHelper.getChunk(p)).equals(p)) {
						removerequest.remove(ChunkHelper.getChunk(p), p);
						for(int i=0; i<2; i++) {
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									ChunkEditor.setParticlesEcken(ChunkHelper.getChunk(p), Particle.BARRIER);
								}
							}, 80*i);
						}
						ChunkEditor.setRandBlock(p, Material.REDSTONE_TORCH);
						API_Land.removeChunk(ChunkHelper.getChunk(p));
						p.sendMessage(prefix + "§7Du hast dieses §aGrundstück §7erfolgreich §everkauft§7.");
						
						//Implementing Eco-System
						API_Land.addLandClaims(p.getUniqueId().toString(), 1);
						p.sendMessage("§a+ §71 Claim-Token");
						
					}else {
						ChunkEditor.setParticles(ChunkHelper.getChunk(p), Particle.DRIP_WATER);
						removerequest.put(ChunkHelper.getChunk(p), p);
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								removerequest.remove(ChunkHelper.getChunk(p), p);
							}}, 20*30);
						
						p.sendMessage(prefix + "§7Wenn du dieses §aGrundstück §7wirklich verkaufen möchtest gib den §eBefehl §7erneut ein.");
						
					}
					}else {
						p.sendMessage(err000);
					}
				}else {
					p.sendMessage(err001);
				}
				}else {
					p.sendMessage(err003);
				}
			}
			
		}else if(args.length == 2) {
			
			if(args[0].equalsIgnoreCase("nehmen")) {
				
				if(p.hasPermission("server.land.admin")) {
					Integer zahl = Integer.valueOf(args[1]);
					
					Chunk c = p.getLocation().getChunk();
					for(int x=0; x<zahl+1; x++) {
						for(int z=0; z<zahl+1; z++) {
							
							Chunk e = p.getWorld().getChunkAt(c.getX()+x, c.getZ());
							Chunk f = p.getWorld().getChunkAt(c.getX()-x, c.getZ());
							Chunk g = p.getWorld().getChunkAt(c.getX(), c.getZ()-z);
							Chunk h = p.getWorld().getChunkAt(c.getX(), c.getZ()+z);
							Chunk i = p.getWorld().getChunkAt(c.getX()+x, c.getZ()+z);
							Chunk j = p.getWorld().getChunkAt(c.getX()+x, c.getZ()-z);
							Chunk k = p.getWorld().getChunkAt(c.getX()-x, c.getZ()+z);
							Chunk l = p.getWorld().getChunkAt(c.getX()-x, c.getZ()-z);
							
							API_Land.registerChunk(p.getUniqueId().toString(), c);
							API_Land.registerChunk(p.getUniqueId().toString(), e);
							API_Land.registerChunk(p.getUniqueId().toString(), f);
							API_Land.registerChunk(p.getUniqueId().toString(), g);
							API_Land.registerChunk(p.getUniqueId().toString(), h);
							API_Land.registerChunk(p.getUniqueId().toString(), i);
							API_Land.registerChunk(p.getUniqueId().toString(), j);
							API_Land.registerChunk(p.getUniqueId().toString(), k);
							API_Land.registerChunk(p.getUniqueId().toString(), l);
							
							ChunkEditor.setParticlesEcken(e, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(f, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(g, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(h, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(i, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(j, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(k, Particle.BARRIER);
							ChunkEditor.setParticlesEcken(l, Particle.BARRIER);
							
						}
					}
					
					p.sendMessage(prefix + "§7Du hast alle GS im Umkreis von §e" + zahl + " §7genommen! (wie peter dein Arschloch)");
					
				}else {
					p.sendMessage(prefix + "§cKeine Rechte!");
				}
				
			}else if(args[0].equalsIgnoreCase("member") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("banplayer")){
					if(args[1].equalsIgnoreCase("list")) {
						
						if(args[0].equalsIgnoreCase("member")) {
							
							String member = "";
							if(API_Land.getMember(ChunkHelper.getChunk(p)) != null) {
								for(String uuids : API_Land.getMember(ChunkHelper.getChunk(p))) {
									uuids = API_UUID.getUUIDorNAME(uuids);
									member = member + uuids + ", ";
								}
							}else {
								member = "§ckeinee";
							}
							member = member.trim();
							member = member.substring(0, member.length() - 1);
							
							p.sendMessage(prefix + "§3Mitlgieder: §7" + member);
							
						}else if(args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("banplayer")) {
							
							String banned = "";
							if(API_Land.getBanned(ChunkHelper.getChunk(p)) != null) {
								for(String uuids : API_Land.getBanned(ChunkHelper.getChunk(p))) {
									uuids = API_UUID.getUUIDorNAME(uuids);
									banned = banned + uuids + ", ";
								}
							}else {
								banned = "§ckeinee";
							}
							banned = banned.trim();
							banned = banned.substring(0, banned.length() - 1);
							
							p.sendMessage(prefix + "§3Gebannt: §7" + banned);
							
						}
					}else {
						p.sendMessage(prefix + "§cVerwende §e/land " + args[0] + " <add/remove/list> [Name]");
					}
			}else if(args[0].equalsIgnoreCase("tp")) {
				
				if(API_Land.getChunkList(p.getUniqueId().toString()).isEmpty()) {
					p.sendMessage(err014);
				}else {
					if(args[1].matches("[0-9]+")) {
						Integer gsnummer = Integer.valueOf(args[1]);
						if(gsnummer <= API_Land.getChunkList(p.getUniqueId().toString()).size()) {
							Chunk c = API_Land.getChunk(API_Land.getChunkList(p.getUniqueId().toString()).get(gsnummer-1), "world");
							Location loc = API_Land.getChunkMitte(c);
							p.teleport(loc);
							p.sendMessage(Main.prefix + "§7Du wurdest auf dein §e" + gsnummer + ".tes §7Grundstück teleportiert.");
						}else {
							p.sendMessage(err015);
						}
					}else {
						p.sendMessage(prefix + "§cGib eine Zahl ein!");
					}
				}
				
			}else if(args[0].equalsIgnoreCase("help")) {
				if(args[1].equalsIgnoreCase("1")) {
					p.sendMessage("§8§m-------------§r§7[§2L§aandChunk§7 / Seite 1§8]§m-------------");
					p.sendMessage("§e/land claim §7- Claim das GS auf dem du stehst.");
					p.sendMessage("§e/land auto §7- Erhalte ein zufälliges Grundstück.");
					p.sendMessage("§e/land info §7- Siehe Infos über das momentane GS.");
					p.sendMessage("§e/land flag set pve <true/false> §7- Ändert das PVP-Verhalten auf deinem GS.");
					p.sendMessage("§e/land flag set pvp <true/false> §7- Ändert das PVP-Verhalten auf deinem GS.");	
					p.sendMessage("§e/land flag set biome <biome> §7- Ändert dein Biome für 1 CT.");	
					p.sendMessage("§8§m------------------------------------");
				}else if(args[1].equalsIgnoreCase("2")) {
					p.sendMessage("§8§m-------------§r§7[§2L§aandChunk§7 / Seite 2§8]§m-------------");
					p.sendMessage("§e/land flag set tnt <true/false> §7- Ändert das TNT-Verhalten auf deinem GS.");
					p.sendMessage("§e/land member add [Spieler] §7- Fügt einen Spieler zum GS hinzu.");
					p.sendMessage("§e/land member remove [Spieler] §7- Entfernt einen Spieler vom GS.");
					p.sendMessage("§e/land member addall [Spieler]  §7- Fügt einen Spieler auf all deinen GS hinzu.");
					p.sendMessage("§e/land member removeall [Spieler] §7- Entfernt einen Spieler von all deinem GS.");	
					p.sendMessage("§e/land help §7- Sehe das Hilfe-Menu ein.");
					p.sendMessage("§8§m------------------------------------");
				}else if(args[1].equalsIgnoreCase("3")) {
					p.sendMessage("§8§m-------------§r§7[§2L§aandChunk§7 / Seite 3§8]§m-------------");
					p.sendMessage("§e/land banplayer add [Spieler] §7- Bannt einen Spieler von deinem GS.");
					p.sendMessage("§e/land banplayer remove [Spieler] §7- Entbannt einen Spieler von deinem GS.");	
					p.sendMessage("§e/land banplayer addall [Spieler] §7- Bannt einen Spieler von all deinen GS.");
					p.sendMessage("§e/land banplayer removeall [Spieler] §7- Entbannt einen Spieler von all deinen GS.");
					p.sendMessage("§e/land list §7- Eine Liste deiner GS.");
					p.sendMessage("§e/land menu §7- Ein Menu für alles.");
					p.sendMessage("§8§m------------------------------------");
				}else {
					p.sendMessage("§8§m-------------§r§7[§2L§aandChunk§7 / Seite 3§8]§m-------------");
					p.sendMessage("§e/land banplayer add [Spieler] §7- Bannt einen Spieler von deinem GS.");
					p.sendMessage("§e/land banplayer remove [Spieler] §7- Entbannt einen Spieler von deinem GS.");	
					p.sendMessage("§e/land banplayer addall [Spieler] §7- Bannt einen Spieler von all deinen GS.");
					p.sendMessage("§e/land banplayer removeall [Spieler] §7- Entbannt einen Spieler von all deinen GS.");
					p.sendMessage("§e/land list §7- Eine Liste deiner GS.");
					p.sendMessage("§e/land menu §7- Ein Menu für alles.");
					p.sendMessage("§8§m------------------------------------");
				}
			}else if(args[0].equalsIgnoreCase("flag") || args[0].equalsIgnoreCase("flags")) {
				if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(args[1].equalsIgnoreCase("info")) {
					
					p.sendMessage(prefix + "§3Flags: §7" + API_Land.getChunkFlags(ChunkHelper.getChunk(p)));
					
				}else {
					p.sendMessage(prefix + "§cVerwende: §e/flags info");
				}
				}else {
					p.sendMessage(err003); 
				}
			}else if(args[0].equalsIgnoreCase("member")){
				if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(args[1].equalsIgnoreCase("add")) {
					p.sendMessage(prefix + "§cVerwende §e/land member add [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("remove")) {
					p.sendMessage(prefix + "§cVerwende §e/land member remove [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("addall")) {
					p.sendMessage(prefix + "§cVerwende §e/land member addall [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("removeall")) {
					p.sendMessage(prefix + "§cVerwende §e/land member removeall [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("info")) {
					String member = "";
					if(API_Land.getMember(ChunkHelper.getChunk(p)) != null) {
						for(String uuids : API_Land.getMember(ChunkHelper.getChunk(p))) {
							uuids = API_UUID.getUUIDorNAME(uuids);
							member = member + uuids + ", ";
						}
					}else {
						member = "§ckeinee";
					}
					member = member.trim();
					member = member.substring(0, member.length() - 1);
					
					p.sendMessage(prefix + "§3Mitlgieder: §7" + member);
					
				}
				}else {
					p.sendMessage(err003);
				}
			}else if(args[0].equalsIgnoreCase("banplayer") || args[0].equalsIgnoreCase("ban")) {
				if(p.getWorld().getName().equals("world")) {
				if(args[1].equalsIgnoreCase("add")) {
					p.sendMessage(prefix + "§cVerwende §e/land banplayer add [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("remove")) {
					p.sendMessage(prefix + "§cVerwende §e/land banplayer remove [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("addall")) {
					p.sendMessage(prefix + "§cVerwende §e/land banplayer addall [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("removeall")) {
					p.sendMessage(prefix + "§cVerwende §e/land banplayer removeall [Spieler]§c.");
				}else if(args[1].equalsIgnoreCase("info")) {
					
					String banned = "";
					if(API_Land.getBanned(ChunkHelper.getChunk(p)) != null) {
						for(String uuids : API_Land.getBanned(ChunkHelper.getChunk(p))) {
							uuids = API_UUID.getUUIDorNAME(uuids);
							banned = banned + uuids + ", ";
						}
					}else {
						banned = "§ckeinee";
					}
					banned = banned.trim();
					banned = banned.substring(0, banned.length() - 1);
					
					p.sendMessage(prefix + "§3Gebannt: §7" + banned);
					
				}
				}else {
					p.sendMessage(err003);
				}
			}
		
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("tp")) {
				String target = args[2];
				if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
				if(API_UUID.existUUIDorNAME(target)) {
				if(API_Land.getChunkList(target).isEmpty()) {
					p.sendMessage(err014);
				}else {
					if(args[1].matches("[0-9]+")) {
						Integer gsnummer = Integer.valueOf(args[1]);
							if(gsnummer <= API_Land.getChunkList(target).size()) {
								Chunk c = API_Land.getChunk(API_Land.getChunkList(target).get(gsnummer-1), "world");
								Location loc = API_Land.getChunkMitte(c);
								p.teleport(loc);
								p.sendMessage(Main.prefix + "§7Du wurdest auf das §e" + gsnummer + ".te §7Grundstück von dem Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7teleportiert.");
							}else {
								p.sendMessage(err015);
							}
					}else {
						p.sendMessage(prefix + "§cGib eine Zahl ein!");
					}
				}
				}else {
					p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Netzwerk.");
				}
				
			}else if(args[0].equalsIgnoreCase("member")){
				if(args[1].equalsIgnoreCase("add")) {
					if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
					if(API_Land.isOwner(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							if(API_Land.isMember(target, ChunkHelper.getChunk(p)) == false) {
								if(API_Land.isOwner(target, ChunkHelper.getChunk(p)) == false) {
									
									API_Land.addMember(target, ChunkHelper.getChunk(p));
									p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7ist nun §aMitglied §7auf deinem Grundtück.");
									
								}else {
									p.sendMessage(err013);
								}
							}else {
								p.sendMessage(err008);
							}
						}else {
							p.sendMessage(err007);
						}
					}else {
						p.sendMessage(err000);
					}
					}else {
						p.sendMessage(err003);
					}
				}else if(args[1].equalsIgnoreCase("remove")) {
					if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
					if(API_Land.isOwner(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							if(API_Land.isMember(target, ChunkHelper.getChunk(p)) == true) {
								
								API_Land.removeMember(target, ChunkHelper.getChunk(p));
								p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7ist nun §cnicht §7mehr §aMitglied §7auf deinem Grundtück.");
								
							}else {
								p.sendMessage(err009);
							}
						}else {
							p.sendMessage(err007);
						}
					}else {
						p.sendMessage(err000);
					}
					}else {
						p.sendMessage(err003);
					}
				}else if(args[1].equalsIgnoreCase("addall")) {
					
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							if(!p.getName().equals(API_UUID.getUUIDorNAME(target))) {
								API_Land.addAllMember(target, p.getUniqueId().toString());
								p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7ist nun §aMitglied §7auf all deinen Grundtücken.");
							}else {
								p.sendMessage(prefix + "§cDas bist du selbst...");
							}
						}else {
							p.sendMessage(err007);
						}
					
				}else if(args[1].equalsIgnoreCase("removeall")) {
					
					String target = args[2];
					if(API_UUID.existUUIDorNAME(target)) {
						if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
						if(!p.getName().equals(API_UUID.getUUIDorNAME(target))) {
							API_Land.removeAllMember(target, p.getUniqueId().toString());
							p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7wurde §7auf all deinen Grundtücken §centfernt§7.");
						}else {
							p.sendMessage(prefix + "§cDas bist du selbst...");
						}
					}else {
						p.sendMessage(err007);
					}
					
				}
			}else if(args[0].equalsIgnoreCase("banplayer") || args[0].equalsIgnoreCase("ban")) {
				if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(args[1].equalsIgnoreCase("add")) {
					if(API_Land.isOwner(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
								if(API_Land.isBanned(target, ChunkHelper.getChunk(p)) == false) {
								if(API_Land.isOwner(target, ChunkHelper.getChunk(p)) == false & API_Land.isMember(target, ChunkHelper.getChunk(p)) == false) {
									API_Land.addBanned(target, ChunkHelper.getChunk(p));
									p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7ist nun von deinem Grundtück §cgebannt§7.");
								}else {
									p.sendMessage(err012);
								}
							}else {
								p.sendMessage(err010);
							}
						}else {
							p.sendMessage(err007);
						}
					}else {
						p.sendMessage(err000);
					}
				}else {
					p.sendMessage(err003);
				}
				}else if(args[1].equalsIgnoreCase("remove")) {
					if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
					if(API_Land.isOwner(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							if(API_Land.isBanned(target, ChunkHelper.getChunk(p)) == true) {
								
								API_Land.removeBanned(target, ChunkHelper.getChunk(p));
								p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7ist nun §cnicht §7mehr von deinem Grundtück §cgebannt§7.");
								
							}else {
								p.sendMessage(err010);
							}
						}else {
							p.sendMessage(err007);
						}
					}else {
						p.sendMessage(err000);
					}
					}else {
						p.sendMessage(err003);
					}
				}else if(args[1].equalsIgnoreCase("addall")) {
					
					String target = args[2];
					if(API_UUID.existUUIDorNAME(target)) {
						if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							if(!p.getName().equals(API_UUID.getUUIDorNAME(target))) {
							API_Land.addAllBans(target, p.getUniqueId().toString());
							p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7wurde auf all deinen §aGrundstücken §cgebannt§7.");
							}else {
								p.sendMessage(prefix + "§cDas bist du selbst...");
							}
					}else {
						p.sendMessage(err007);
					}
				
			}else if(args[1].equalsIgnoreCase("removeall")) {
				
				String target = args[2];
				if(API_UUID.existUUIDorNAME(target)) {
					if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
					if(!p.getName().equals(API_UUID.getUUIDorNAME(target))) {
						API_Land.removeAllBans(target, p.getUniqueId().toString());
						p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7wurde auf all deinen §aGrundtücken §7entbannt§7.");
					}else {
						p.sendMessage(prefix + "§cDas bist du selbst...");
					}
				}else {
					p.sendMessage(err007);
				}
				
			}
			}
			
		}else if(args.length == 4) {
			if(API_Land.isOwner(p.getUniqueId().toString(), ChunkHelper.getChunk(p))) {
			if(args[0].equalsIgnoreCase("flag") || args[0].equalsIgnoreCase("flags")) {
				if(p.getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), pos1, pos2)) {
				if(args[1].equalsIgnoreCase("set")) {
					if(args[2].equalsIgnoreCase("pvp")) {
						if(args[3].equalsIgnoreCase("true")) {
							API_Land.setFlag("pvp", true, ChunkHelper.getChunk(p));
							p.sendMessage(prefix + "§7Du hast die Flag PvP zu §atrue §7geändert.");
						}else if(args[3].equalsIgnoreCase("false")) {
							API_Land.setFlag("pvp", false, ChunkHelper.getChunk(p));
							p.sendMessage(prefix + "§7Du hast die Flag PvP zu §afalse §7geändert.");
						}
					}else if(args[2].equalsIgnoreCase("pve")) {
						if(args[3].equalsIgnoreCase("true")) {
							API_Land.setFlag("pve", true, ChunkHelper.getChunk(p));
							p.sendMessage(prefix + "§7Du hast die Flag PvE zu §atrue §7geändert.");
						}else if(args[3].equalsIgnoreCase("false")) {
							API_Land.setFlag("pve", false, ChunkHelper.getChunk(p));
							p.sendMessage(prefix + "§7Du hast die Flag PvE zu §afalse §7geändert.");
						}
					}else if(args[2].equalsIgnoreCase("tnt")) {
						if(args[3].equalsIgnoreCase("true")) {
							API_Land.setFlag("tnt", true, ChunkHelper.getChunk(p));
							p.sendMessage(prefix + "§7Du hast die Flag TNT zu §atrue §7geändert.");
						}else if(args[3].equalsIgnoreCase("false")) {
							API_Land.setFlag("tnt", false, ChunkHelper.getChunk(p));
							p.sendMessage(prefix + "§7Du hast die Flag TNT zu §afalse §7geändert.");
						}
					}else if(args[2].equalsIgnoreCase("biome")) {
						if(API_Land.hasEnoughClaims(p.getUniqueId().toString(), 1)) {
							Biome biome = Biome.valueOf(args[3]);
							if(biome != null) {
								
								API_Land.removeLandClaims(p.getUniqueId().toString(), 1);
								API_Land.setFlag("biome", biome, ChunkHelper.getChunk(p), p);
								p.sendMessage(prefix + "§7Du hast das Biome zu §a" + biome.toString() + " §7geändert.");
								
							}else {
								p.sendMessage(err006);
							}
						}else {
							p.sendMessage(prefix + "§cDu hast nicht genug Claim-Tokens, um diesen Befehl auszuführen. Du benötigst noch 1.");
						}
					}
				}
				}else {
					p.sendMessage(err003);
				}
			}
			}else {
				p.sendMessage(err000);
			}
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("tp");
			check.add("help");
			check.add("edit");
			check.add("auto");
			check.add("claim");
			check.add("info");
			check.add("flags");
			check.add("liste");
			check.add("member");
			check.add("menu");
			check.add("remove");
			check.add("banplayer");
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("flags") || args[0].equalsIgnoreCase("flags")) {
				List<String> check = new ArrayList<>();
				check.add("set");
				for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
			}else if(args[0].equalsIgnoreCase("member") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("banplayer")) {
				List<String> check = new ArrayList<>();
				check.add("add");
				check.add("remove");
				check.add("addall");
				check.add("removeall");
				check.add("list");
				for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
			}else if(args[0].equalsIgnoreCase("help")){
				List<String> check = new ArrayList<>();
				check.add("1");
				check.add("2");
				check.add("3");
				for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
			}else if(args[0].equalsIgnoreCase("tp")){
				tcomplete.add("§4§l✘ §r§4Anzahl §4§l✘");
			}
			
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("flags") || args[0].equalsIgnoreCase("flags")) {
				if(args[1].equalsIgnoreCase("set")) {
				List<String> check = new ArrayList<>();
				check.add("pvp");
				check.add("tnt");
				check.add("pve");
				check.add("biome");
				for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
				}
			}else if(args[0].equalsIgnoreCase("member") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("banplayer")) {
				if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("addall") || args[1].equalsIgnoreCase("removeall")) {
					List<String> check = new ArrayList<>();
					check.add("§4§l✘ §r§4Spieler(auch offline) §4§l✘");
					for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
				}
			}else if(args[0].equalsIgnoreCase("tp")){
				tcomplete.add("§4§l✘ §r§4Spieler(auch offline) §4§l✘");
			}
			
		}else if(args.length == 4) {
			if(args[0].equalsIgnoreCase("flag") || args[0].equalsIgnoreCase("flags")) {
				if(args[1].equalsIgnoreCase("set")) {
					if(args[2].equalsIgnoreCase("pvp") || args[2].equalsIgnoreCase("pve") || args[2].equalsIgnoreCase("tnt")) {
						List<String> check = new ArrayList<>();
						check.add("true");
						check.add("false");
						for(String s : check) if(s.toLowerCase().startsWith(args[3].toLowerCase())) tcomplete.add(s);
					}else if(args[2].equalsIgnoreCase("biome")) {
						List<String> check = new ArrayList<>();
						for(Biome biom : Biome.values()) check.add(biom.toString());
						for(String s : check) if(s.toLowerCase().startsWith(args[3].toLowerCase())) tcomplete.add(s);
					}
				}
			}
		}
		
		return tcomplete;
	}
	
}
