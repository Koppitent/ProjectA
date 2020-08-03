package de.koppy.job;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.eco.COMMAND_Bank;
import de.koppy.handler.InfoHandler;

public class Jobs {
	
	public static Integer mainXP = 500;
	public static HashMap<Player, Integer> level = new HashMap<>();
	public static HashMap<Player, Integer> xp = new HashMap<>();
	public static ArrayList<Location> blockplaced = new ArrayList<>();
	
	public static void setJob(Player p, String playerUUID, Job job) {
		if(getJob(playerUUID) != Job.ARBEITSLOS) saveJobLevel(p, getJob(playerUUID)); saveJobXP(p, getJob(playerUUID));
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(playerUUID + ".job", job.toString().toLowerCase());
		setJobLevel(p, job); setJobXP(p, job);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void setJobXP(Player p, Job job) {
		String playerUUID = p.getUniqueId().toString();
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Integer savedXP = 0;
		if(cfg.getString(playerUUID + "."+job.toString().toLowerCase() + ".xp") != null) savedXP = cfg.getInt(playerUUID + "."+job.toString().toLowerCase() + ".xp");
		xp.put(p, savedXP);
	}
	
	public static void setJobLevel(Player p, Job job) {
		String playerUUID = p.getUniqueId().toString();
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Integer savedLEVEL=1;
		if(cfg.getString(playerUUID + "."+job.toString().toLowerCase() + ".level") != null) savedLEVEL = cfg.getInt(playerUUID + "."+job.toString().toLowerCase() + ".level");
		if(savedLEVEL == 0) savedLEVEL = 1;
		level.put(p, savedLEVEL);
	}
	
	public static void addXP(Player p, Integer amount) {
		if(xp.get(p) != null) {
			
			Integer currentXP = xp.get(p);
			currentXP += amount;
			
			Bukkit.getPluginManager().callEvent(new PlayerAddXPatJobEvent(p, amount));
			
			xp.put(p, currentXP);
			
		}else {
			p.sendMessage(COMMAND_Bank.prefix + "§4ERROR! §cBitte kontaktiere einen Admin. §4Fehlercode: #102");
		}
	}
	
	public static void addLevel(Player p, Integer amount) {
		if(xp.get(p) != null) {
			
			Integer currentLEVEL = level.get(p);
			currentLEVEL += amount;
			
			Bukkit.getPluginManager().callEvent(new PlayerAddLevelatJobEvent(p, amount));
			
			level.put(p, currentLEVEL);
			
		}else {
			p.sendMessage(COMMAND_Bank.prefix + "§4ERROR! §cBitte kontaktiere einen Admin. §4Fehlercode: #102");
		}
	}
	
	public static void removeXP(Player p, Integer amount) {
		if(xp.get(p) != null) {
			
			Integer currentXP = xp.get(p);
			currentXP -= amount;
			
			Bukkit.getPluginManager().callEvent(new PlayerRemoveXPatJobEvent(p, amount));
			
			xp.put(p, currentXP);
			
		}else {
			p.sendMessage(COMMAND_Bank.prefix + "§4ERROR! §cBitte kontaktiere einen Admin. §4Fehlercode: #102");
		}
	}
	
	public static void removeLevel(Player p, Integer amount) {
		if(xp.get(p) != null) {
			
			Integer currentLEVEL = level.get(p);
			currentLEVEL -= amount;
			
			Bukkit.getPluginManager().callEvent(new PlayerRemoveLevelatJobEvent(p, amount));
			
			level.put(p, currentLEVEL);
			
		}else {
			p.sendMessage(COMMAND_Bank.prefix + "§4ERROR! §cBitte kontaktiere einen Admin. §4Fehlercode: #102");
		}
	}
	
	public static Integer getJobXP(String playerUUID, Job job) {
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + "."+job.toString().toLowerCase() + ".xp") == null) return 0;
		else return cfg.getInt(playerUUID + "."+job.toString().toLowerCase() + ".xp");
	}
	
	public static Integer getJobLevel(String playerUUID, Job job) {
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + "."+job.toString().toLowerCase() + ".level") == null) return 0;
		else return cfg.getInt(playerUUID + "."+job.toString().toLowerCase() + ".level");
	}
	
	public static void saveJobXP(Player p, Job job) {
		String playerUUID = p.getUniqueId().toString();
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(xp.get(p) != null) cfg.set(playerUUID + "."+job.toString().toLowerCase() + ".xp", xp.get(p));
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void saveJobLevel(Player p, Job job) {
		String playerUUID = p.getUniqueId().toString();
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(level.get(p) != null)cfg.set(playerUUID + "."+job.toString().toLowerCase() + ".level", level.get(p));
		InfoHandler.saveFile(file, cfg);
	}
	
	public static Job getJob(String playerUUID) {
		File file = new File("plugins/Jobs", "users.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(playerUUID + ".job") != null) {
			if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("miner")) {
				return Job.MINER;
			}else if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("holzfaeller")) {
				return Job.HOLZFAELLER;
			}else if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("erbauer")) {
				return Job.ERBAUER;
			}else if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("farmer")) {
				return Job.FARMER;
			}else if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("graber")) {
				return Job.GRABER;
			}else if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("netherholzfaeller")) {
				return Job.NETHERHOLZFAELLER;
			}else if(cfg.getString(playerUUID + ".job").equalsIgnoreCase("schmied")) {
				return Job.SCHMIED;
			}else {
				return Job.ARBEITSLOS;
			}
		}else {
			return Job.ARBEITSLOS;
		}
	}
	
	public static List<String> getJobList(){
		List<String> jobList = new ArrayList<String>();
		for(Job jobs : Job.values()) {
			jobList.add(jobs.toString());
		}
		return jobList;
	}
	
	
	
}
