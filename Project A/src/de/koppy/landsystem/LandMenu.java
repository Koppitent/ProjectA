package de.koppy.landsystem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.koppy.UUIDFetcher.API_UUID;

public class LandMenu {
	
	public static HashMap<Player, Integer> seitenzahl = new HashMap<>();
	public static HashMap<Player, String> lastseite = new HashMap<>();
	public static HashMap<Player, Chunk> editingchunk = new HashMap<>();
	public static ArrayList<Player> openChunklist = new ArrayList<>();
	public static ArrayList<Player> memberlist = new ArrayList<>();
	public static ArrayList<Player> bannedlist = new ArrayList<>();
	public static HashMap<Player, Integer> seitenmemberzahl = new HashMap<>();
	
	public static void addChunksforSeite(Player p, Integer seite, Inventory inv) {
		List<String> chunkIDs = API_Land.getChunkList(p.getUniqueId().toString());
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
				
				String skullDataModell = "http://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622";
				Chunk c = API_Land.getChunk(chunkIDs.get(i-1), "world");
				if(API_Land.getMember(c) != null) skullDataModell = "http://textures.minecraft.net/texture/5f23f115cb9520dd4d4cb29124dabac5e6844f96cce241a3ec9ca6f7a296247";
				
				ItemStack istack = getSkull(skullDataModell);
				SkullMeta istackm = (SkullMeta) istack.getItemMeta();
				istackm.setDisplayName("§3GS: §2" + chunkIDs.get(i-1));
				List<String> istacklore = new ArrayList<>();
				istacklore.add("§7Nummer §e" + i);
				istackm.setLore(istacklore);
				istack.setItemMeta(istackm);
				
				inv.addItem(istack);
				
			}
		}
	}
	
	public static void addMemberforSeite(Player p, Integer seite, Inventory inv, Chunk c) {
		if(API_Land.getMember(c) != null) {
		List<String> members = API_Land.getMember(c);
		Integer chunksfrom = (28*seite)-28;
		Integer chunksto = seite*28;
		chunksfrom++;
		chunksto++;
		if(seite == seitenmemberzahl.get(p)) chunksto = members.size()+1;
		for(int i=0; i<7; i++) {
			inv.setItem(10+i, null);
			inv.setItem(19+i, null);
			inv.setItem(28+i, null);
			inv.setItem(37+i, null);
		}
		for(int i=chunksfrom; i<chunksto; i++) {
			if(API_Land.getMember(c) != null) {
				
				String member = members.get(i-1);
				if(member.length() == 36) member = API_UUID.getUUIDorNAME(member);
				
				ItemStack istack = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta istackm = (SkullMeta) istack.getItemMeta();
				istackm.setOwner(member);
				istackm.setDisplayName("§3"+member);
				List<String> istacklore = new ArrayList<>();
				istacklore.add("§7» §cKlicken zum entfernen.");
				istackm.setLore(istacklore);
				istack.setItemMeta(istackm);
				
				inv.addItem(istack);
			}
		}
		}
	}
	
	public static void addBannedforSeite(Player p, Integer seite, Inventory inv, Chunk c) {
		if(API_Land.getBanned(c) != null) {
		List<String> members = API_Land.getBanned(c);
		Integer chunksfrom = (28*seite)-28;
		Integer chunksto = seite*28;
		chunksfrom++;
		chunksto++;
		if(seite == seitenmemberzahl.get(p)) chunksto = members.size()+1;
		for(int i=0; i<7; i++) {
			inv.setItem(10+i, null);
			inv.setItem(19+i, null);
			inv.setItem(28+i, null);
			inv.setItem(37+i, null);
		}
		for(int i=chunksfrom; i<chunksto; i++) {
			if(API_Land.getBanned(c) != null) {
				
				String member = members.get(i-1);
				if(member.length() == 36) member = API_UUID.getUUIDorNAME(member);
				
				ItemStack istack = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta istackm = (SkullMeta) istack.getItemMeta();
				istackm.setOwner(member);
				istackm.setDisplayName("§c"+member);
				List<String> istacklore = new ArrayList<>();
				istacklore.add("§7» §cKlicken zum entfernen.");
				istackm.setLore(istacklore);
				istack.setItemMeta(istackm);
				
				inv.addItem(istack);
			}
		}
		}
	}
	
	public static void setChunkSeite(Player p, Integer seite, Inventory inv) {
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
					
					addChunksforSeite(p, seite, inv);
					
				inv.setItem(inv.getSize()-2, pfeilvorn);
				inv.setItem(inv.getSize()-8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				}else {
					//keine pfeile
					addChunksforSeite(p, seite, inv);
					
					inv.setItem(inv.getSize()-8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
					inv.setItem(inv.getSize()-2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				}
			}else if(seitenzahl.get(p) > seite) {
				//größer und kleiner Pfeil
				addChunksforSeite(p, seite, inv);
				
				inv.setItem(inv.getSize()-2, pfeilvorn);
				inv.setItem(inv.getSize()-8, pfeilhinten);
			}else if(seitenzahl.get(p) == seite) {
				//nur kleiner Pfeil
				addChunksforSeite(p, seite, inv);
				
				inv.setItem(inv.getSize()-2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				inv.setItem(inv.getSize()-8, pfeilhinten);
			}
		}
	}
	
	
	public static void setMemberSeite(Player p, Integer seite, Inventory inv, String memberORbanned, Chunk c) {
		if(seitenmemberzahl.containsKey(p)) {
			
			Integer seitevorn = seite+1;
			ItemStack pfeilvorn = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta pfeilskull = (SkullMeta) pfeilvorn.getItemMeta();
			pfeilskull.setOwner("MHF_ArrowRight");
			pfeilskull.setDisplayName("§6Seite §7- " + seitevorn);
			pfeilvorn.setItemMeta(pfeilskull);
			
			Integer seitehinten = seite-1;
			ItemStack pfeilhinten = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta pfeilhintenskull = (SkullMeta) pfeilhinten.getItemMeta();
			pfeilhintenskull.setOwner("MHF_ArrowLeft");
			pfeilhintenskull.setDisplayName("§6Seite §7- " + seitehinten);
			pfeilhinten.setItemMeta(pfeilhintenskull);
			
			if(seite == 1) {
				if(seitenmemberzahl.get(p) > 1) {
				//nur größer Pfeil
					
					if(memberORbanned.equals("member")) {
						addMemberforSeite(p, seite, inv, c);
					}else {
						addBannedforSeite(p, seite, inv, c);
					}
					
					inv.setItem(inv.getSize()-2, pfeilvorn);
					inv.setItem(inv.getSize()-8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
					
				}else {
					//keine pfeile
					if(memberORbanned.equals("member")) {
						addMemberforSeite(p, seite, inv, c);
					}else {
						addBannedforSeite(p, seite, inv, c);
					}
					
					inv.setItem(inv.getSize()-8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
					inv.setItem(inv.getSize()-2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				}
			}else if(seitenmemberzahl.get(p) > seite) {
				//größer und kleiner Pfeil
				if(memberORbanned.equals("member")) {
					addMemberforSeite(p, seite, inv, c);
				}else {
					addBannedforSeite(p, seite, inv, c);
				}
				
				inv.setItem(inv.getSize()-2, pfeilvorn);
				inv.setItem(inv.getSize()-8, pfeilhinten);
			}else if(seitenmemberzahl.get(p) == seite) {
				//nur kleiner Pfeil
				if(memberORbanned.equals("member")) {
					addMemberforSeite(p, seite, inv, c);
				}else {
					addBannedforSeite(p, seite, inv, c);
				}
				
				inv.setItem(inv.getSize()-2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
				inv.setItem(inv.getSize()-8, pfeilhinten);
			}
		}
	}
	
	
	public static Inventory getChunkListe(Player p, String name) {
		String pUUID = p.getUniqueId().toString();
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		fillRandWithGlass(inv);
		
		Integer seitenanzahl = 0;
		List<String> chunkIDlist = API_Land.getChunkList(pUUID);
		
		if(chunkIDlist.size()%28 == 0) seitenanzahl = chunkIDlist.size()/28;
		else seitenanzahl = (chunkIDlist.size()/28)+1;
		seitenzahl.put(p, seitenanzahl);
		
		setChunkSeite(p, 1, inv);
		openChunklist.add(p);
		
		return inv;
	}
	
	public static Inventory getMemberListe(Player p, String name, Chunk c, String memberORbanned) {
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		fillRandWithGlass(inv);
		
		Integer seitenanzahl = 0;
		if(memberORbanned.equals("member")) {
			if(API_Land.getMember(c) != null) {
			List<String> chunkmember = API_Land.getMember(c);
			if(chunkmember.size()%28 == 0) seitenanzahl = chunkmember.size()/28;
			else seitenanzahl = (chunkmember.size()/28)+1;
			}else {
				seitenanzahl = 1;
			}
		}else {
			if(API_Land.getBanned(c) != null) {
			List<String> chunkbanned = API_Land.getBanned(c);
			if(chunkbanned.size()%28 == 0) seitenanzahl = chunkbanned.size()/28;
			else seitenanzahl = (chunkbanned.size()/28)+1;
			}else {
				seitenanzahl = 1;
			}
		}
		seitenmemberzahl.put(p, seitenanzahl);
		
		setMemberSeite(p, 1, inv, memberORbanned, c);
		openChunklist.add(p);
		
		return inv;
	}
	
	public static void fillRandWithGlass(Inventory inv) {
		for(int i=0; i<inv.getSize(); i=i+9) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=8; i<inv.getSize(); i=i+9) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=0; i<9; i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
		for(int i=inv.getSize()-9; i<inv.getSize(); i++) inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
	}
	
	public static void fillUpWithGlass(Inventory inv) {
		ItemStack istack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta istackM = istack.getItemMeta();
		istackM.setDisplayName(" ");
		istack.setItemMeta(istackM);
		for(int i=0; i<inv.getSize(); i++) if(inv.getItem(i) == null) inv.setItem(i, istack);
	}
	
	public static Inventory Info(Player p, String name) {
		Inventory inv = Bukkit.getServer().createInventory(null, 3*9, name);
		openChunklist.add(p);
		
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
		
		//bis 7 pro ding
		inv.setItem(10, help);
		inv.setItem(11, help2);
		inv.setItem(12, help3);
		inv.setItem(13, help4);
		inv.setItem(14, help5);
		inv.setItem(15, help6);
		inv.setItem(16, help7);
		
		return inv;
		
	}
	
	public static Inventory Menu(Player p, String name) {
		Inventory inv = Bukkit.getServer().createInventory(null, 3*9, name);
		openChunklist.add(p);
		
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
		
		ItemStack liste = new ItemStack(Material.PAPER);
		ItemMeta listem = liste.getItemMeta();
		listem.setDisplayName("§2L§aand§7-Liste");
		List<String> listelore = new ArrayList<>();
		listelore.add("§7» Öffnet eine Liste all deiner GS");
		listem.setLore(listelore);
		liste.setItemMeta(listem);
		
		inv.setItem(10, liste);
		inv.setItem(12, autoGS);
		inv.setItem(14, info);
		inv.setItem(16, schliessen);
		
		fillUpWithGlass(inv);
		return inv;
	}
	
	public static Inventory openMember(Player p, Chunk c, String name) {
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		openChunklist.add(p);
		fillRandWithGlass(inv);
		
		
		
		return inv;
	}
	
	public static Inventory ModifyLand(Player p, Chunk c, String name) {
		Inventory inv = Bukkit.getServer().createInventory(null, 3*9, name);
		openChunklist.add(p);
		editingchunk.put(p, c);
		
		ItemStack info = new ItemStack(Material.SPRUCE_SIGN);
		ItemMeta infom = info.getItemMeta();
		infom.setDisplayName("§3Info:");
		List<String> infomlore = new ArrayList<>();
		if(API_Land.canBuildonChunk(p.getUniqueId().toString(), c)) infomlore.add("§7» Baurechte: §a✔");
		else infomlore.add("§7» Baurechte: §4✖");
		if(API_Land.getFlag("pvp", c) == true) infomlore.add("§7» PvP: §a✔");
		else infomlore.add("§7» PvP: §4✖");
		if(API_Land.getFlag("pve", c) == true) infomlore.add("§7» PvE: §a✔");
		else infomlore.add("§7» PvE: §4✖");
		if(API_Land.getFlag("tnt", c) == true) infomlore.add("§7» TNT: §a✔");
		else infomlore.add("§7» TNT: §4✖");
		infomlore.add("§7» Biome: §3" + API_Land.getFlagBiome(c));
		infom.setLore(infomlore);
		info.setItemMeta(infom);
		
		ItemStack tpto = new ItemStack(Material.ENDER_PEARL);
		ItemMeta tptom = tpto.getItemMeta();
		if(p.getLocation().getChunk() != c) tptom.setDisplayName("§3Teleport");
		else tptom.setDisplayName("§c§mTeleport");
		List<String> tptomlore = new ArrayList<>();
		if(p.getLocation().getChunk() != c) tptomlore.add("§7» Teleportiert dich zu deinem GS.");
		else tptomlore.add("§7» §cDu stehst bereits auf dem GS.");
		tptom.setLore(tptomlore);
		tpto.setItemMeta(tptom);
		
		String skullDataModellBanned = "http://textures.minecraft.net/texture/5f23f115cb9520dd4d4cb29124dabac5e6844f96cce241a3ec9ca6f7a296247";
		String skullDataModellMember = "http://textures.minecraft.net/texture/478abdf982f909eab5de9bf969cf14f664db4c447738459ea40162b37d124";
		
		ItemStack skullMember = getSkull(skullDataModellMember);
		SkullMeta skullMm = (SkullMeta) skullMember.getItemMeta();
		if(API_Land.isOwner(p.getUniqueId().toString(), c)) skullMm.setDisplayName("§3Member§7-Liste");
		else skullMm.setDisplayName("§c§mMember-Liste");
		List<String> skullMmlore = new ArrayList<>();
		if(API_Land.isOwner(p.getUniqueId().toString(), c)) skullMmlore.add("§7» §7Öffnet eine Liste mit allen Mitgliedern.");
		else skullMmlore.add("§7» §cDu musst dafür der Besitzer sein.");
		skullMm.setLore(skullMmlore);
		skullMember.setItemMeta(skullMm);
		
		ItemStack skullBanned = getSkull(skullDataModellBanned);
		SkullMeta skullBm = (SkullMeta) skullBanned.getItemMeta();
		if(API_Land.isOwner(p.getUniqueId().toString(), c)) skullBm.setDisplayName("§cBan§7-Liste");
		else skullBm.setDisplayName("§c§mBan-Liste");
		List<String> skullBmlore = new ArrayList<>();
		if(API_Land.isOwner(p.getUniqueId().toString(), c)) skullBmlore.add("§7» §7Öffnet eine Liste aller gebannten Spieler.");
		else skullBmlore.add("§7» §cDu musst dafür der Besitzer sein.");
		skullBm.setLore(skullBmlore);
		skullBanned.setItemMeta(skullBm);
		
		inv.setItem(8, info);
		inv.setItem(10, skullMember);
		inv.setItem(11, skullBanned);
		inv.setItem(16, tpto);
		
		fillUpWithGlass(inv);
		return inv;
	}
	
	public static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        if(url.isEmpty())return head;
        
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
	
}
