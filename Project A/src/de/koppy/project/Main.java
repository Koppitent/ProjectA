package de.koppy.project;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.koppy.MySQL.MySQL;
import de.koppy.MySQL.MySQLload;
import de.koppy.UUIDFetcher.EVENT_UUID;
import de.koppy.cases.COMMAND_Case;
import de.koppy.cases.EVENT_Case;
import de.koppy.chestshop.EVENT_Adminshop;
import de.koppy.chestshop.EVENT_ChestShop;
import de.koppy.eco.COMMAND_Bank;
import de.koppy.eco.COMMAND_Eco;
import de.koppy.eco.COMMAND_Moneytoggle;
import de.koppy.eco.COMMAND_Pay;
import de.koppy.eco.EVENT_Bank;
import de.koppy.eco.EVENT_EcoMenu;
import de.koppy.job.COMMAND_Job;
import de.koppy.job.EVENT_Erbauer;
import de.koppy.job.EVENT_Farmer;
import de.koppy.job.EVENT_Graber;
import de.koppy.job.EVENT_Holzfaeller;
import de.koppy.job.EVENT_Job;
import de.koppy.job.EVENT_Miner;
import de.koppy.eco.EVENT_Eco;
import de.koppy.landsystem.COMMAND_Claims;
import de.koppy.landsystem.COMMAND_Land;
import de.koppy.landsystem.EVENT_Land;
import de.koppy.landsystem.EVENT_LandMenu;
import de.koppy.missions.COMMAND_FastTravel;
import de.koppy.missions.COMMAND_Region;
import de.koppy.missions.COMMAND_Setregion;
import de.koppy.missions.EVENT_Explore;
import de.koppy.standardcmds.COMMAND_Addsit;
import de.koppy.standardcmds.COMMAND_Clear;
import de.koppy.standardcmds.COMMAND_Difficulty;
import de.koppy.standardcmds.COMMAND_Fly;
import de.koppy.standardcmds.COMMAND_GameMode;
import de.koppy.standardcmds.COMMAND_Head;
import de.koppy.standardcmds.COMMAND_Help;
import de.koppy.standardcmds.COMMAND_Home;
import de.koppy.standardcmds.COMMAND_Invsee;
import de.koppy.standardcmds.COMMAND_Local;
import de.koppy.standardcmds.COMMAND_Msg;
import de.koppy.standardcmds.COMMAND_Profile;
import de.koppy.standardcmds.COMMAND_Removehome;
import de.koppy.standardcmds.COMMAND_Sethome;
import de.koppy.standardcmds.COMMAND_Setpos;
import de.koppy.standardcmds.COMMAND_Setspawn;
import de.koppy.standardcmds.COMMAND_Spawn;
import de.koppy.standardcmds.COMMAND_Teleport;
import de.koppy.standardcmds.COMMAND_Time;
import de.koppy.standardcmds.COMMAND_Togglescoreboard;
import de.koppy.standardcmds.COMMAND_Tpa;
import de.koppy.standardcmds.COMMAND_Tpaccept;
import de.koppy.standardcmds.COMMAND_Tpdeny;
import de.koppy.standardcmds.COMMAND_Tptoggle;
import de.koppy.standardcmds.COMMAND_Trade;
import de.koppy.standardcmds.COMMAND_Warp;
import de.koppy.standardcmds.COMMAND_Weather;
import de.koppy.standardcmds.COMMAND_give;
import de.koppy.standardcmds.COMMAND_givemecock;
import de.koppy.standardcmds.COMMAND_r;
import de.koppy.standardcmds.COMMAND_setClanTag;
import de.koppy.standardcmds.COMMAND_spielzeit;

public class Main extends JavaPlugin{
	
	//  /Top
	
	public static String prefix = "§8[§3S§bystem§8] §r";
	public static String TutorialBot = "§3HilfeMann §8| §7Peter187 §8» §r";
	public static String noperms = prefix + "§cdafür hast du nicht die benötigte Berechtigung";
	public static String nichtonline = prefix + "&cDer Spieler %player% muss online sein.";
	public static Main instance;
	public static boolean activateNBG = true;
	public static boolean activateChunk = true;
	public static boolean activateLand = true;
	public static boolean activateUUID = true;
	public static boolean activateTablistANDChat = true;
	public static boolean activatestandardCMDS = true;
	public static boolean activateEconomy = true;
	public static boolean activateJobs = true;
	public static boolean activateChestshop = true;
	public static boolean activateCase = true;
	public static boolean activateExplore = true;
	public static boolean activateMySQL = true;
	public static String servername = "";
	public static Location spawnloc;
	
	@Override
	public void onEnable() {
		
		setConfig();
		registerCMD();
		registerListener();
		spawnloc = COMMAND_Setspawn.loadLoc();
		instance = this;
		
		Tablistener.startUpdating();
		
		if(Bukkit.getWorld("worldCOPY") == null) WorldAPI.copyWorld(new File("world"), new File("worldCOPY"));
		WorldAPI.loadWorld("worldCOPY");
		
		if(Bukkit.getWorld("world_netherCOPY") == null) WorldAPI.copyWorld(new File("world_nether"), new File("world_netherCOPY"));
		WorldAPI.loadNetherWorld("world_netherCOPY");
		
		Bukkit.getServer().getConsoleSender().sendMessage("§8|--------------------- §r§3Project-A §8§m---------------------|");
		Bukkit.getServer().getConsoleSender().sendMessage("§8|  ");
		if(activateNBG == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3NBG-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3NBG-System: §cfailed");
		if(activateChunk == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Chunk-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Chunk-System: §cfailed");
		if(activateUUID == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3UUID-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3UUID-System: §cfailed");
		if(activateLand == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Land-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Land-System: §cfailed");
		if(activateEconomy == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Economy-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Economy-System: §cfailed");
		if(activateJobs == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Jobs-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Jobs-System: §cfailed");
		if(activateCase == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Case-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Case-System: §cfailed");
		if(activateExplore == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Explore-System: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Explore-System: §cfailed");
		if(activatestandardCMDS == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Standard-Commands: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Standard-Commands: §cfailed");
		if(activateChestshop == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3ChestShops: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3ChestShops: §cfailed");
		if(activateTablistANDChat == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Tablist&Chat: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3Tablist&Chat: §cfailed");
		if(activateMySQL == true) Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3MySQL: §aactive");
		else Bukkit.getServer().getConsoleSender().sendMessage("§8|  §3MySQL: §cfailed");
		Bukkit.getServer().getConsoleSender().sendMessage("§8|  ");
		Bukkit.getServer().getConsoleSender().sendMessage("§8|-----------------------------------------------------|");
		
		if(activateMySQL == true) {
			MySQLload file = new MySQLload();
			file.setStandard();
			file.readData();
			MySQL.connect();
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Economy (UUID VARCHAR(100),Amount DOUBLE(10,2))");
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onDisable() {
		instance = null;
		
		for(Location loc : EVENT_Explore.blocklocs) {
			loc.getBlock().setType(EVENT_Explore.resetBlocks.get(loc));
		}
		
	}
	
	public void registerCMD() {
		
		new COMMAND_Config(this);
		if(activateCase == true) new COMMAND_Case(this);
		if(activateChunk == true) new COMMAND_chunk(this);
		if(activateLand == true) new COMMAND_Land(this);
		if(activateLand == true) new COMMAND_Claims(this);
		if(activatestandardCMDS == true) new COMMAND_GameMode(this);
		if(activatestandardCMDS == true) new COMMAND_Fly(this);
		if(activatestandardCMDS == true) new COMMAND_give(this);
		if(activatestandardCMDS == true) new COMMAND_Weather(this);
		if(activatestandardCMDS == true) new COMMAND_Head(this);
		if(activatestandardCMDS == true) new COMMAND_Difficulty(this);
		if(activatestandardCMDS == true) new COMMAND_Teleport(this);
		if(activatestandardCMDS == true) new COMMAND_Time(this);
		if(activatestandardCMDS == true) new COMMAND_Addsit(this);
		if(activatestandardCMDS == true) new COMMAND_Tpa(this);
		if(activatestandardCMDS == true) new COMMAND_Tpaccept(this);
		if(activatestandardCMDS == true) new COMMAND_Tpdeny(this);
		if(activatestandardCMDS == true) new COMMAND_Tptoggle(this);
		if(activatestandardCMDS == true) new COMMAND_Warp(this);
		if(activatestandardCMDS == true) new COMMAND_Spawn(this);
		if(activatestandardCMDS == true) new COMMAND_Setspawn(this);
		if(activatestandardCMDS == true) new COMMAND_Clear(this);
		if(activatestandardCMDS == true) new COMMAND_Help(this);
		if(activatestandardCMDS == true) new COMMAND_setClanTag(this);
		if(activatestandardCMDS == true) new COMMAND_Setpos(this);
		if(activatestandardCMDS == true) new COMMAND_Profile(this);
		if(activatestandardCMDS == true) new COMMAND_spielzeit(this);
		if(activatestandardCMDS == true) new COMMAND_Local(this);
		if(activatestandardCMDS == true) new COMMAND_Trade(this);
		if(activatestandardCMDS == true) new COMMAND_Togglescoreboard(this);
		if(activatestandardCMDS == true) new COMMAND_givemecock(this);
		if(activatestandardCMDS == true) new COMMAND_Home(this);
		if(activatestandardCMDS == true) new COMMAND_Sethome(this);
		if(activatestandardCMDS == true) new COMMAND_Removehome(this);
		if(activatestandardCMDS == true) new COMMAND_Msg(this);
		if(activatestandardCMDS == true) new COMMAND_r(this);
		if(activatestandardCMDS == true) new COMMAND_Invsee(this);
		if(activateEconomy == true) new COMMAND_Eco(this);
		if(activateEconomy == true) new COMMAND_Pay(this);
		if(activateEconomy == true) new COMMAND_Bank(this);
		if(activateEconomy == true) new COMMAND_Moneytoggle(this);
		if(activateJobs == true) new COMMAND_Job(this);
		if(activateExplore == true) new COMMAND_Setregion(this);
		if(activateExplore == true) new COMMAND_FastTravel(this);
		if(activateExplore == true) new COMMAND_Region(this);
		
	}
	
	public void registerListener() {
		
		new EVENT_Spiel(this);
		new EVENT_Savepoints(this);
		if(activateUUID == true) new EVENT_UUID(this);
		if(activateTablistANDChat == true) new Tablistener(this);
		if(activateNBG == true) new EVENT_NBG(this);
		if(activateLand == true) new EVENT_LandMenu(this);
		if(activateLand == true) new EVENT_Land(this);
		if(activateEconomy == true) new EVENT_Eco(this);
		if(activateEconomy == true) new EVENT_Bank(this);
		if(activateEconomy == true) new EVENT_EcoMenu(this);
		if(activateJobs == true) new EVENT_Job(this);
		if(activateJobs == true) new EVENT_Erbauer(this);
		if(activateJobs == true) new EVENT_Graber(this);
		if(activateJobs == true) new EVENT_Miner(this);
		if(activateJobs == true) new EVENT_Farmer(this);
		if(activateJobs == true) new EVENT_Holzfaeller(this);
		if(activateChestshop == true) new EVENT_Adminshop(this);
		if(activateChestshop == true) new EVENT_ChestShop(this);
		if(activateCase == true) new EVENT_Case(this);
		if(activateExplore == true) new EVENT_Explore(this);
		
	}
	
	public static void setConfig(){
		//Config
		File file = new File("plugins/Server", "config.yml");
		if(!file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			//Set Standard
			cfg.set("Prefix", "&8[&3S&bystem&8] &r");
			cfg.set("Link", "http://playattack.de/nbg");
			cfg.set("NBG-System", true);
			cfg.set("UUID-System", true);
			cfg.set("Chunk-System", true);
			cfg.set("Land-System", true);
			cfg.set("Standard-Commands", true);
			cfg.set("Tablist&Chat", true);
			cfg.set("Economy-System", true);
			cfg.set("Job-System", true);
			cfg.set("MySQL-System", true);
			cfg.set("ChestShops", true);
			cfg.set("Case-System", true);
			cfg.set("Explore-System", true);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Messages
		File filemsg = new File("plugins/Server", "messages.yml");
		if(!filemsg.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(filemsg);
			//Set Standard
			cfg.set("TablistHeader", "&8» &3%servername% &8« %n% %n% &cAlpha &3release coming soon! %n% ");
			cfg.set("TablistFooter", "%n% &eTS: &7ts.playattack.de %n% &eMC: &7playattack.de %n% &eWebsite: &7playattack.de %n%");
			cfg.set("Keine Berechtigung", "&cdafür hast du nicht die benötigte Berechtigung.");
			cfg.set("Nicht Online", "&cDer Spieler %player% muss online sein.");
			cfg.set("Land Error-000", "&cDu musst der Besitzer des Grundstücks sein. &4(0)");
			cfg.set("Land Error-001", "&cDieses §aGrundstück §cgehört niemanden. &4(1)");
			cfg.set("Land Error-002", "&cDieses §aGrundstück §cgehört niemanden. &4(2)");
			cfg.set("Land Error-003", "&cDu musst dafür in der Grundstückswelt sein. &4(3)");
			cfg.set("Land Error-004", "&cDieses §aGrundstück §cwurde bereits geclaimt. &4(4)");
			cfg.set("Land Error-005", "&cDu hast nicht genug §eClaims §cübrig! &4(5)");
			cfg.set("Land Error-006", "&cDieses Biom existiert nicht! &4(6)");
			cfg.set("Land Error-007", "&cDieser Spieler war noch nie auf dem Server! &4(7)");
			cfg.set("Land Error-008", "&cDieser Spieler ist bereits Mitglied auf deinem GS! &4(8)");
			cfg.set("Land Error-009", "&cDieser Spieler ist gar kein Mitglied auf deinem GS! &4(9)");
			cfg.set("Land Error-010", "&cDieser Spieler ist bereits von deinem GS entbannt! &4(10)");
			cfg.set("Land Error-011", "&cDieser Spieler ist gar nicht von deinem GS gebannt! &4(11)");
			cfg.set("Land Error-012", "&cDer Owner oder Mitglieder können nicht gebannt werden! &4(12)");
			cfg.set("Land Error-013", "&cDer Owner kann kein Mitglied sein! &4(13)");
			cfg.set("Land Error-014", "&cDu besitzt keine Grundstücke. &4(14)");
			cfg.set("Land Error-015", "&cDu besitzt nicht so viele Grundstücke. &4(15)");
			cfg.set("Land Error-016", "&cDu Spieler besitzt nicht so viele Grundstücke. &4(16)");
			
			try {
				cfg.save(filemsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Config
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		prefix = cfg.getString("Prefix").replace("&", "§");
		EVENT_NBG.link = cfg.getString("Link");
		activateNBG = cfg.getBoolean("NBG-System");
		activateUUID = cfg.getBoolean("UUID-System");
		activateChunk = cfg.getBoolean("Chunk-System");
		activateLand = cfg.getBoolean("Land-System");
		activateEconomy = cfg.getBoolean("Economy-System");
		activateJobs = cfg.getBoolean("Job-System");
		activatestandardCMDS = cfg.getBoolean("Standard-Commands");
		activateTablistANDChat = cfg.getBoolean("Tablist&Chat");
		activateMySQL = cfg.getBoolean("MySQL-System");
		activateChestshop = cfg.getBoolean("ChestShops");
		activateCase = cfg.getBoolean("Case-System");
		activateExplore = cfg.getBoolean("Explore-System");
		
		//Messages
		FileConfiguration cfgmsg = YamlConfiguration.loadConfiguration(filemsg);
		Tablistener.tablistheader = cfgmsg.getString("TablistHeader").replace("&", "§").replace("%n%", "\n");
		Tablistener.tablistfooter = cfgmsg.getString("TablistFooter").replace("&", "§").replace("%n%", "\n");
		noperms = prefix + cfgmsg.getString("Keine Berechtigung").replace("&", "§");
		nichtonline = prefix + cfgmsg.getString("Nicht Online").replace("&", "§");
		
		COMMAND_Land.err000 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-000").replace("&", "§");
		COMMAND_Land.err001 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-001").replace("&", "§");
		COMMAND_Land.err002 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-002").replace("&", "§");
		COMMAND_Land.err003 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-003").replace("&", "§");
		COMMAND_Land.err004 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-004").replace("&", "§");
		COMMAND_Land.err005 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-005").replace("&", "§");
		COMMAND_Land.err006 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-006").replace("&", "§");
		COMMAND_Land.err007 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-007").replace("&", "§");
		COMMAND_Land.err008 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-008").replace("&", "§");
		COMMAND_Land.err009 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-009").replace("&", "§");
		COMMAND_Land.err010 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-010").replace("&", "§");
		COMMAND_Land.err011 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-011").replace("&", "§");
		COMMAND_Land.err012 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-012").replace("&", "§");
		COMMAND_Land.err013 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-013").replace("&", "§");
		COMMAND_Land.err014 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-014").replace("&", "§");
		COMMAND_Land.err015 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-015").replace("&", "§");
		COMMAND_Land.err016 = COMMAND_Land.prefix + cfgmsg.getString("Land Error-016").replace("&", "§");
		
	}
	
}
