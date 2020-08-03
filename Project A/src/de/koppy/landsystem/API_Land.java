package de.koppy.landsystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.handler.InfoHandler;
import de.koppy.project.ChunkEditor;

public class API_Land {
	
	//* Land-Anzahl
	public static File file = new File("plugins/Land/User", "Users.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static Integer getLandClaims(String uuid) {
		if(existPlayer(uuid) == true) return cfg.getInt(uuid + ".anzahl");
		else return 0;
	}
	
	public static void addLandClaims(String uuid, Integer anzahl) {
		if(existPlayer(uuid) == true) cfg.set(uuid + ".anzahl", getLandClaims(uuid)+anzahl);
		else cfg.set(uuid + ".anzahl", anzahl);
		try {	cfg.save(file);	} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void setLandClaims(String uuid, Integer anzahl) {
		cfg.set(uuid + ".anzahl", anzahl);
		try {	cfg.save(file);	} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void removeLandClaims(String uuid, Integer anzahl) {
		if(getLandClaims(uuid) > anzahl) {
			cfg.set(uuid + ".anzahl", getLandClaims(uuid)-anzahl);
			try {	cfg.save(file);	} catch (IOException e) {e.printStackTrace();}
		}else {
			cfg.set(uuid + ".anzahl", 0);
			try {	cfg.save(file);	} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public static boolean hasEnoughClaims(String uuid, Integer anzahl) {
		if(anzahl > getLandClaims(uuid)) return false;
		else return true;
	}
	
	public static boolean existPlayer(String uuid) {
		if(cfg.getString(uuid + ".anzahl") == null) return false;
		else return true;
	}
	
	public static boolean ownNearChunk(String uuid, Chunk c) {
		if(isOwner(uuid, c.getWorld().getChunkAt(c.getX()+1, c.getZ())) || isOwner(uuid, c.getWorld().getChunkAt(c.getX()-1, c.getZ())) || isOwner(uuid, c.getWorld().getChunkAt(c.getX()+1, c.getZ()+1)) || isOwner(uuid, c.getWorld().getChunkAt(c.getX()+1, c.getZ()-1)) || isOwner(uuid, c.getWorld().getChunkAt(c.getX()-1, c.getZ()+1)) || isOwner(uuid, c.getWorld().getChunkAt(c.getX(), c.getZ()+1)) || isOwner(uuid, c.getWorld().getChunkAt(c.getX(), c.getZ()-1))) {
			return true;
		}else {
			return false;
		}
	}
	
	//* Claim Lands
	public static File fileW = new File("plugins/Land/LandChunk", "world.yml");
	public static FileConfiguration cfgW = YamlConfiguration.loadConfiguration(fileW);
	
	public static boolean existChunk(Chunk c) {
		String id = c.getX()+";"+c.getZ();
		if(cfgW.getKeys(false).contains(id)) return true;
		else return false;
	}
	
	public static void removeChunk(Chunk c) {
		if(existChunk(c)) {
			ChunkEditor.resetChunkChestsAndStuff(c);
			String id = c.getX()+";"+c.getZ();
			if(c.getWorld().getName().equals("world")) {
				cfgW.set(id, null);
				try {
					cfgW.save(fileW);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
	}
	
	public static void deleteChunk(Chunk c) {
		ChunkEditor.resetChunk(c);
		removeChunk(c);
	}
	
	public static void registerChunk(String uuid, Chunk c) {
		if(c.getWorld().getName().equals("world")) {
			//Save in World
			List<String> member = new ArrayList<>();
			List<String> banned = new ArrayList<>();
			String id = c.getX()+";"+c.getZ();
			cfgW.set(id + ".owner", uuid);
			cfgW.set(id + ".member", member);
			cfgW.set(id + ".banned", banned);
			cfgW.set(id + ".biome", Bukkit.getWorld("world").getBlockAt(c.getX()*16, 100, c.getZ()*16).getBiome().toString());
			cfgW.set(id + ".pvp", false);
			cfgW.set(id + ".pve", false);
			cfgW.set(id + ".tnt", false);
			
			try {cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
			
		}
	}
	
	public static void setFlag(String flag, boolean bool, Chunk c) {
		String id = getChunkID(c);
		if(flag.equals("pvp")) {
			cfgW.set(id + ".pvp", bool);
		}else if(flag.equals("pve")) {
			cfgW.set(id + ".pve", bool);
		}else if(flag.equals("tnt")) {
			cfgW.set(id + ".tnt", bool);
		}
		
		try {cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static boolean getFlag(String flag, Chunk c) {
		String id = getChunkID(c);
		if(existChunk(c)) {
		if(flag.equals("pvp")) {
			return cfgW.getBoolean(id + ".pvp");
		}else if(flag.equals("pve")) {
			return cfgW.getBoolean(id + ".pve");
		}else if(flag.equals("tnt")) {
			return cfgW.getBoolean(id + ".tnt");
		}else {
			return false;
		}
		}else {
			return false;
		}
	}
	
	public static String getFlagBiome(Chunk c) {
		String id = getChunkID(c);
		return cfgW.getString(id + ".biome");
	}
	
	public static void setFlag(String flag, Biome biome, Chunk c, Player p) {
		String id = getChunkID(c);
		if(flag.equals("biome")) {
			cfgW.set(id + ".biome", biome.toString());
			ChunkEditor.setChunkBiome(p, biome);
		}
		
		try {cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static String getChunkFlags(Chunk c) {
		String id = getChunkID(c);
		if(existChunk(c)) {
			String flags="";
			flags = flags + "PvP: " + cfgW.getBoolean(id + ".pvp") + ", PvE: " + cfgW.getBoolean(id + ".pve") + ", TNT: " + cfgW.getBoolean(id + ".tnt") + ", Biome: " + cfgW.getString(id + ".biome");
			return flags;
		}else {
			return null;
		}
	}
	
	public static List<String> getChunkList(String pUUID) {
		List<String> chunklist = new ArrayList<>();
		for(String chunkID : cfgW.getKeys(false)) {
			if(getOwner(chunkID).equals(pUUID)) {
				chunklist.add(chunkID);
			}
		}
		return chunklist;
	}
	
	public static Chunk getRNDMfreeChunk() {
		Random rndm = new Random();
		int x = rndm.nextInt(4000) + ((int) COMMAND_Land.pos1.getX());
		int z = rndm.nextInt(8000) + ((int) COMMAND_Land.pos1.getZ());
		
		Location loc = new Location(Bukkit.getWorld("world"), x, Bukkit.getWorld("world").getHighestBlockYAt(x, z)+1, z);
		if(loc.subtract(0, 2, 0).getBlock().getType() != Material.WATER) {
		if(InfoHandler.isIn(loc, COMMAND_Land.pos1, COMMAND_Land.pos2)) {
			if(existChunk(loc.getChunk())) {
				return getRNDMfreeChunk();
			}else {
				return loc.getChunk();
			}
		}else {
			return getRNDMfreeChunk();
		}
		}else {
			return getRNDMfreeChunk();
		}
	}
	
	public static Location getChunkMitte(Chunk c) {
		int x = c.getX() * 16 + 8;
		int z = c.getZ() * 16 + 8;
		int y = c.getWorld().getHighestBlockYAt(x, z)+2;
		return new Location(c.getWorld(), x, y, z);
	}
	
	public static Chunk getChunk(String id, String world) {
		Integer x = Integer.valueOf(id.split(";")[0]);
		Integer z = Integer.valueOf(id.split(";")[1]);
		if(world.equals("world")) {
			return Bukkit.getWorld("world").getChunkAt(x, z);
		}else {
			return null;
		}
	}
	
	public static Chunk getChunk(Integer x, Integer z, String world) {
		if(world.equals("world")) {
			return Bukkit.getWorld("world").getChunkAt(x, z);
		}else {
			return null;
		}
	}
	
	public static String getChunkID(Chunk c) {
		return c.getX()+";"+c.getZ();
	}
	
	//* Information about Chunk
	
	public static String getOwner(Chunk c) {
		if(existChunk(c)) {
			return cfgW.getString(getChunkID(c) + ".owner");
		}else {
			return null;
		}
	}
	
	public static String getOwner(String chunkID) {
		return cfgW.getString(chunkID + ".owner");
	}
	
	public static List<String> getMember(Chunk c) {
		if(existChunk(c)) {
			if(cfgW.getStringList(getChunkID(c) + ".member").isEmpty()) {
				return null;
			}else {
				return cfgW.getStringList(getChunkID(c) + ".member");
			}
		}else {
			return null;
		}
	}
	
	public static List<String> getBanned(Chunk c) {
		if(existChunk(c)) {
			if(cfgW.getStringList(getChunkID(c) + ".banned").isEmpty()) {
				return null;
			}else {
				return cfgW.getStringList(getChunkID(c) + ".banned");
			}
		}else {
			return null;
		}
	}
	
	
	public static boolean isOwner(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(cfgW.getString(getChunkID(c) + ".owner").equals(uuid)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public static boolean isMember(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(cfgW.getStringList(getChunkID(c) + ".member").contains(uuid)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public static void addMember(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(isMember(uuid, c) == false) {
				List<String> member = new ArrayList<>();
				if(cfgW.getStringList(getChunkID(c) + ".member") != null) {
					member = cfgW.getStringList(getChunkID(c) + ".member");
				}
				member.add(uuid);
				cfgW.set(getChunkID(c) + ".member", member);
				try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public static void addAllMember(String memberuuid, String owneruuid) {
		for(String ids : cfgW.getKeys(false)) {
			if(cfgW.getString(ids + ".owner").equals(owneruuid)){
				if(cfgW.getStringList(ids + ".member") != null && cfgW.getStringList(ids + ".member").contains(memberuuid)) {
					
				}else {
					List<String> member = cfgW.getStringList(ids + ".member");
					member.add(memberuuid);
					cfgW.set(ids + ".member", member);
					try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
				}
			}
		}
	}
	
	public static void removeAllMember(String memberuuid, String owneruuid) {
		for(String ids : cfgW.getKeys(false)) {
			if(cfgW.getString(ids + ".owner").equals(owneruuid)){
				if(cfgW.getStringList(ids + ".member") != null && cfgW.getStringList(ids + ".member").contains(memberuuid)) {
					List<String> member = cfgW.getStringList(ids + ".member");
					member.remove(memberuuid);
					cfgW.set(ids + ".member", member);
					try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
				}
			}
		}
	}
	
	public static void addAllBans(String memberuuid, String owneruuid) {
		for(String ids : cfgW.getKeys(false)) {
			if(cfgW.getString(ids + ".owner").equals(owneruuid)){
				if(cfgW.getStringList(ids + ".banned") != null && cfgW.getStringList(ids + ".banned").contains(memberuuid)) {
					
				}else {
					List<String> banned = cfgW.getStringList(ids + ".banned");
					banned.add(memberuuid);
					cfgW.set(ids + ".banned", banned);
					try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
				}
			}
		}
	}
	
	public static void removeAllBans(String memberuuid, String owneruuid) {
		for(String ids : cfgW.getKeys(false)) {
			if(cfgW.getString(ids + ".owner").equals(owneruuid)){
				if(cfgW.getStringList(ids + ".banned") != null && cfgW.getStringList(ids + ".banned").contains(memberuuid)) {
					List<String> banned = cfgW.getStringList(ids + ".banned");
					banned.remove(memberuuid);
					cfgW.set(ids + ".banned", banned);
					try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
				}
			}
		}
	}
	
	public static void removeMember(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(isMember(uuid, c) == true) {
				cfgW.set(getChunkID(c) + ".member", cfgW.getStringList(getChunkID(c) + ".member").remove(uuid));
				try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public static boolean isBanned(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(cfgW.getStringList(getChunkID(c) + ".banned").contains(uuid)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public static void addBanned(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(isBanned(uuid, c) == false) {
				List<String> banned = new ArrayList<>();
				if(cfgW.getStringList(getChunkID(c) + ".banned") != null) {
					banned = cfgW.getStringList(getChunkID(c) + ".banned");
				}
				banned.add(uuid);
				cfgW.set(getChunkID(c) + ".banned", banned);
				try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public static void removeBanned(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(isBanned(uuid, c) == true) {
				cfgW.set(getChunkID(c) + ".banned", cfgW.getStringList(getChunkID(c) + ".banned").remove(uuid));
				try {	cfgW.save(fileW);} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public static boolean canBuildonChunk(String uuid, Chunk c) {
		if(existChunk(c)) {
			if(isMember(uuid, c) || isOwner(uuid, c)) {
				return true;
			}else {
				return false;
			}
		}else {
			return COMMAND_Land.globalBuild;
		}
	}
	
}
