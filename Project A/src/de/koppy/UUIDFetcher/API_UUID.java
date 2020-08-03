package de.koppy.UUIDFetcher;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class API_UUID {
	
	public static String prefix = "§8[§3U§bUID§8] §r";
	
	public static void updatePlayer(Player p) {
		File file = new File("plugins/Server/UUIDs", "UUIDFetcher.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(p.getName(), p.getUniqueId().toString());
		cfg.set(p.getName().toLowerCase(), p.getUniqueId().toString());
		cfg.set(p.getUniqueId().toString(), p.getName());
		try {cfg.save(file);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static String getUUIDorNAME(String name) {
		File file = new File("plugins/Server/UUIDs", "UUIDFetcher.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getString(name);
	}
	
	public static boolean existUUIDorNAME(String name) {
		File file = new File("plugins/Server/UUIDs", "UUIDFetcher.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getKeys(false).contains(name.toLowerCase())) return true;
		else return false;
	}
	
}
