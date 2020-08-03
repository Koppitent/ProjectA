package de.koppy.eco;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.koppy.handler.InfoHandler;

public class Economy {
	
	//* Standard Eco-Functions
	public static double getMoney(String userUUIDstring) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getDouble(userUUIDstring + ".money");
	}
	
	public static List<String> getLog(String userUUIDstring) {
		File file = new File("plugins/Economy/User", userUUIDstring+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> log = new ArrayList<>();
		
		for(String s : cfg.getKeys(false)) {
			String reason = cfg.getString(s);
			log.add(reason);
		}
		
		return log;
	}
	
	public static void addMoneyWithoutReason(String userUUIDstring, double amount) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double money = getMoney(userUUIDstring);
		money += amount;
		cfg.set(userUUIDstring + ".money", money);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void addMoney(String userUUIDstring, double amount, String reason) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double money = getMoney(userUUIDstring);
		money += amount;
		cfg.set(userUUIDstring + ".money", money);
		InfoHandler.saveFile(file, cfg);
		setReason(userUUIDstring, reason, "+"+amount);
	}
	
	public static void removeMoneyWithoutReason(String userUUIDstring, double amount) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double money = getMoney(userUUIDstring);
		money -= amount;
		cfg.set(userUUIDstring + ".money", money);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void removeMoney(String userUUIDstring, double amount, String reason) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double money = getMoney(userUUIDstring);
		money -= amount;
		cfg.set(userUUIDstring + ".money", money);
		InfoHandler.saveFile(file, cfg);
		setReason(userUUIDstring, reason, "-"+amount);
	}
	
	public static void setMoneyWithoutReason(String userUUIDstring, double amount) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(userUUIDstring + ".money", amount);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void setMoney(String userUUIDstring, double amount, String reason) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(userUUIDstring + ".money", amount);
		InfoHandler.saveFile(file, cfg);
		setReason(userUUIDstring, reason, ""+amount);
	}
	
	public static boolean hasEnoughMoney(String userUUIDstring, double topay) {
		if(getMoney(userUUIDstring) >= topay) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isPublic(String userUUIDstring) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(cfg.getString(userUUIDstring + ".public") != null) return cfg.getBoolean(userUUIDstring + ".public");
		else return true;
	}
	
	public static void setPublic(String userUUIDstring, boolean bool) {
		File file = new File("plugins/Economy", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(userUUIDstring + ".public", bool);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static Integer getEintraege(String userUUIDstring) {
		File fileU = new File("plugins/Economy/User", userUUIDstring+".yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		return cfgU.getKeys(false).size();
	}
	
	
	public static void setReason(String userUUIDstring, String reason, String amount) {
		File fileU = new File("plugins/Economy/User", userUUIDstring+".yml");
		FileConfiguration cfgU = YamlConfiguration.loadConfiguration(fileU);
		Integer save = getEintraege(userUUIDstring)+1;
		cfgU.set(save.toString(), amount + ", " + reason);
		InfoHandler.saveFile(fileU, cfgU);
	}
	
	//* Standard Bank-System
	public static void createBank(String pcreatorUUID, String accname) {
		File file = new File("plugins/Bank/Accounts", accname+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		File file2 = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(file2);
		
		List<String> member = new ArrayList<String>();
		member.add(pcreatorUUID);
		cfg2.set(accname + ".member", member);
		cfg2.set(accname + ".money", 0);
		InfoHandler.saveFile(file2, cfg2);
		cfg.set("Konto eröffnet", 0);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void deleteBank(String accname) {
		File file = new File("plugins/Bank/Accounts", accname+".yml");
		file.delete();
		File file2 = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(file2);
		cfg2.set(accname, null);
		InfoHandler.saveFile(file2, cfg2);
	}
	
	public static boolean existAcc(String accname) {
		File file = new File("plugins/Bank/Accounts", accname+".yml");
		if(file.exists()) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isMember(String tmemberUUID, String accname) {
		if(existAcc(accname)) {
			File file = new File("plugins/Bank", "accounts.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			List<String> member = cfg.getStringList(accname + ".member");
			if(member.contains(tmemberUUID)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public static Integer getAccounts(String tmemberUUID) {
		Integer output = 0;
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(String acc : cfg.getKeys(false)) if(cfg.getStringList(acc + ".member").contains(tmemberUUID)) output++;
		return output;
	}
	
	public static boolean isMemberFromBankaccount(String tmemberUUID, String accname) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(existAcc(accname)) {
			if(cfg.getStringList(accname + ".member").contains(tmemberUUID)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public static List<String> getAccountNames(String tmemberUUID) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> accnames = new ArrayList<>();
		for(String acc : cfg.getKeys(false)) if(cfg.getStringList(acc + ".member").contains(tmemberUUID)) accnames.add(acc);
		return accnames;
	}
	
	public static List<String> getMembers(String accname) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getStringList(accname + ".member");
	}
	
	public static void addMemberBank(String tmemberUUID, String accname) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(existAcc(accname)) {
			if(isMember(tmemberUUID, accname) == false) {
				if(getAccounts(tmemberUUID) < 3) {
				List<String> member = cfg.getStringList(accname + ".member");
				member.add(tmemberUUID);
				cfg.set(accname + ".member", member);
				InfoHandler.saveFile(file, cfg);
				}
			}
		}
	}
	
	public static void removeMemberBank(String tmemberUUID, String accname) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(existAcc(accname)) {
			if(isMember(tmemberUUID, accname) == true) {
				List<String> member = cfg.getStringList(accname + ".member");
				member.remove(tmemberUUID);
				cfg.set(accname + ".member", member);
				InfoHandler.saveFile(file, cfg);
			}
		}
	}
	
	//* Bank-Bal-System
	
	public static double getBankBalance(String accname) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getDouble(accname + ".money");
	}
	
	public static void addBankBalance(String accname, double amount, String reason) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double bal = cfg.getDouble(accname + ".money");
		bal = bal + amount;
		cfg.set(accname + ".money", bal);
		InfoHandler.saveFile(file, cfg);
		setBankReason(accname, reason, "+"+amount);
	}
	
	public static void removeBankBalance(String accname, double amount, String reason) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double bal = cfg.getDouble(accname + ".money");
		bal = bal - amount;
		cfg.set(accname + ".money", bal);
		InfoHandler.saveFile(file, cfg);
		setBankReason(accname, reason, "-"+amount);
	}
	
	public static void setBankBalance(String accname, double amount) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(accname + ".money", amount);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void addBankBalanceWithoutReason(String accname, double amount) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double bal = cfg.getDouble(accname + ".money");
		bal = bal + amount;
		cfg.set(accname + ".money", bal);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void removeBankBalanceWithoutReason(String accname, double amount) {
		File file = new File("plugins/Bank", "accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		double bal = cfg.getDouble(accname + ".money");
		bal = bal - amount;
		cfg.set(accname + ".money", bal);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static void setBankReason(String accname, String reason, String amount) {
		File file = new File("plugins/Bank/Accounts", accname+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Integer save = cfg.getKeys(false).size()+1;
		cfg.set(save.toString(), amount + ", " + reason);
		InfoHandler.saveFile(file, cfg);
	}
	
	public static List<String> getBankLog(String accname) {
		File file = new File("plugins/Bank/Accounts", accname+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		List<String> log = new ArrayList<>();
		
		for(String s : cfg.getKeys(false)) {
			String reason = cfg.getString(s);
			log.add(reason);
		}
		
		return log;
	}
	
	public static boolean hasEnoughMoneyOnBank(String accname, double topay) {
		if(getBankBalance(accname) >= topay) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
}
