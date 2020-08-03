package de.koppy.handler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ProfileHandler {
	
	public static void setTeleportAllowed(String playerUUID, boolean bool) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(playerUUID + ".tpaallow", bool);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static Integer getSpielzeit(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + ".spielzeit") != null) return cfg.getInt(playerUUID + ".spielzeit");
		else return 0;
	}
	
	public static void setTutorial(String playerUUID, String status) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(playerUUID + ".tutorial", status);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static String getTutorial(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + ".tutorial") != null) return cfg.getString(playerUUID + ".tutorial");
		else return null;
	}
	
	public static void saveSpielzeit(String playerUUID, Integer spielzeit) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(playerUUID + ".spielzeit", spielzeit);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static boolean getScoreboardAllow(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + ".scoreboard") != null) return cfg.getBoolean(playerUUID + ".scoreboard");
		else return true;
	}
	
	public static void setScoreboard(String playerUUID, boolean bool) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(playerUUID + ".scoreboard", bool);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void setClanTag(String playerUUID, String ctag) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(playerUUID + ".clan", ctag);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static Integer getOpenedCases(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + ".openedCases") != null) return cfg.getInt(playerUUID + ".openedCases");
		else return 0;
	}
	
	public static void addOpenedCases(String playerUUID, Integer amount) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		amount += getOpenedCases(playerUUID);
		cfg.set(playerUUID + ".openedCases", amount);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static String getClanTag(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + ".clan") != null) return cfg.getString(playerUUID + ".clan");
		else return null;
	}
	
	public static boolean isTeleportAllowed(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.getString(playerUUID + ".tpaallow") != null) return cfg.getBoolean(playerUUID + ".tpaallow");
		else return true;
		
	}
	
	public static String getLastOnlineDate(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.getString(playerUUID + ".lastOnline") != null) {
			SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			return cfg.getString(playerUUID + ".lastOnline");
		}else {
			return null;
		}
	}
	
	public static void saveLastOnlineDate(String playerUUID) {
		File file = new File("plugins/Server/UUIDs", "Profiles.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String original = date.format(new Date());
		cfg.set(playerUUID + ".lastOnline", original);
		try {cfg.save(file);} catch (IOException e) {e.printStackTrace();}
		
	}
	
}
