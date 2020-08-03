package de.koppy.project;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.koppy.eco.COMMAND_Eco;
import de.koppy.eco.Economy;
import de.koppy.handler.ProfileHandler;
import de.koppy.job.Job;
import de.koppy.job.Jobs;
import de.koppy.landsystem.API_Land;
import de.koppy.missions.EVENT_Explore;

public class Tablistener implements Listener{
	
	public static String tablistheader = "";
	public static String tablistfooter = "";
	
	private static Main main;
	public Tablistener(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onJ(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if(p.hasPermission("server.admin")) {
			msg = msg.replace("&", "§");
		}
		
//			for(Entity entitys : p.getNearbyEntities(10, 10, 10)) {
//				if(entitys instanceof Player) {
//					Player t = (Player) entitys;
//					if(t != null) {
//					if(p.hasPermission("server.admin")) {
//						t.sendMessage("§e[l] §4ADMIN §8| §7" + p.getName() + " §8» §e" + msg);
//					}else if(p.hasPermission("server.leitung")) {
//						t.sendMessage("§e[l] §cLEITUNG §8| §7" + p.getName() + " §8» §e" + msg);
//					}else if(p.hasPermission("server.dev")) {
//						t.sendMessage("§e[l] §3DEVELOPER §8| §7" + p.getName() + " §8» §e" + msg);
//					}else if(p.hasPermission("server.staff")) {
//						t.sendMessage("§e[l] §cSTAFF §8| §7" + p.getName() + " §8» §e" + msg);
//					}else if(p.hasPermission("server.yt")) {
//						t.sendMessage("§e[l] §5YOUTUBER §8| §7" + p.getName() + " §8» §e" + msg);
//					}else if(p.hasPermission("server.freund")) {
//						t.sendMessage("§e[l] §3Freund §8| §7" + p.getName() + " §8» §e" + msg);
//					}else {
//						t.sendMessage("§e[l] §7Random §8| §7" + p.getName() + " §8» §e" + msg);
//					}
//					}
//				}
//			}
			
			if(p.hasPermission("server.admin")) {
				e.setFormat("§4ADMIN §8| §7" + p.getName() + " §8» §7" + msg);
			}else if(p.hasPermission("server.leitung")) {
				e.setFormat("§cLEITUNG §8| §7" + p.getName() + " §8» §7" + msg);
			}else if(p.hasPermission("server.dev")) {
				e.setFormat("§3DEVELOPER §8| §7" + p.getName() + " §8» §7" + msg);
			}else if(p.hasPermission("server.staff")) {
				e.setFormat("§cSTAFF §8| §7" + p.getName() + " §8» §7" + msg);
			}else if(p.hasPermission("server.yt")) {
				e.setFormat("§5YOUTUBER §8| §7" + p.getName() + " §8» §7" + msg);
			}else if(p.hasPermission("server.freund")) {
				e.setFormat("§3FREUND §8| §7" + p.getName() + " §8» §7" + msg);
			}else {
				e.setFormat("§7RANDOM §8| §7" + p.getName() + " §8» §7" + msg);
			}
		}
	
	@EventHandler
	public void onJ(PlayerJoinEvent e) {
		setScoreboard(e.getPlayer());
	}
	
	public static void setScoreboard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.getObjective("aaa");
		if(obj == null) obj = sb.registerNewObjective("aaa", "bbb");	
		
		Integer spielzeitticks = EVENT_Spiel.spielzeit.get(p);
		Integer minutengesamt = 0;
		Integer stundengesamt = 0;
		Integer sekundengesamt = spielzeitticks/20;
		if(sekundengesamt >= 60) {
			minutengesamt = sekundengesamt/60;
			sekundengesamt = sekundengesamt%60;
		}
		if(minutengesamt >= 60) {
			stundengesamt = minutengesamt/60;
			minutengesamt = minutengesamt % 60;
		}
		
		DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
		String fmoney = f.format(Economy.getMoney(p.getUniqueId().toString()));
		
		obj.setDisplayName("§b✦ §l§nProfile:§r           §3");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		obj.getScore("§d ").setScore(14);
		obj.getScore("§8✗ §3Region:").setScore(13);
		obj.getScore("§a ").setScore(11);
		obj.getScore("§8✗ §3Spielstunden:").setScore(10);
		obj.getScore("§0 ").setScore(8);
		obj.getScore("§8✗ §2Job:").setScore(7);
		obj.getScore("§5 ").setScore(5);
		obj.getScore("§8✗ §6Money:").setScore(4);
		obj.getScore("§6 ").setScore(2);
			obj.getScore(updateTeam(sb, "Coins5", "§b  "+EVENT_Explore.explorer.get(p), "§2", ChatColor.DARK_AQUA)).setScore(12);
		obj.getScore(updateTeam(sb, "Coins", "§1  §7"+stundengesamt + "§bh §7" + minutengesamt + "§bm ", "§4", ChatColor.RED)).setScore(9);
		if(Jobs.getJob(p.getUniqueId().toString()) != Job.ARBEITSLOS) obj.getScore(updateTeam(sb, "Coins2", "  §3"+Jobs.getJob(p.getUniqueId().toString()).toString().toLowerCase(), "§4", ChatColor.YELLOW)).setScore(6);
		else obj.getScore(updateTeam(sb, "Coins2", "§1  §cArbeitslos", "§4", ChatColor.YELLOW)).setScore(6);
		obj.getScore(updateTeam(sb, "Coins3", "§1  §3"+fmoney+COMMAND_Eco.moneyformat, "§4", ChatColor.BLACK)).setScore(3);
		
		for(Player on : Bukkit.getOnlinePlayers()) {
			if(on.hasPermission("server.admin")) {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("2"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("2"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("2"+on.getName());
					if(team == null) team = sb.registerNewTeam("2"+on.getName());
				}
				
				team.setPrefix("§4ADMIN §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				else team.setSuffix(" §8[§4Team§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
				
			}else if(on.hasPermission("server.leitung")) {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("3"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("3"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("3"+on.getName());
					if(team == null) team = sb.registerNewTeam("3"+on.getName());
				}
				
				team.setPrefix("§cLEITUNG §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				else team.setSuffix(" §8[§4Team§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
			}else if(on.hasPermission("server.dev")) {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("4"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("4"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("4"+on.getName());
					if(team == null) team = sb.registerNewTeam("4"+on.getName());
				}
				
				team.setPrefix("§3DEVELOPER §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				else team.setSuffix(" §8[§4Team§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
			}else if(on.hasPermission("server.staff")) {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("5"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("5"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("5"+on.getName());
					if(team == null) team = sb.registerNewTeam("5"+on.getName());
				}
				
				team.setPrefix("§cSTAFF §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				else team.setSuffix(" §8[§4Team§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
			}else if(on.hasPermission("server.yt")) {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("6"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("6"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("6"+on.getName());
					if(team == null) team = sb.registerNewTeam("6"+on.getName());
				}
				
				team.setPrefix("§5YOUTUBER §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
			}else if(on.hasPermission("server.freund")) {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("7"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("7"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("7"+on.getName());
					if(team == null) team = sb.registerNewTeam("7"+on.getName());
				}
				
				team.setPrefix("§3FREUND §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
			}else {
				Team team = null;
				if(on.getName().length() == 16) {
					team = sb.getTeam("8"+on.getName().substring(0, on.getName().length()-1));
					if(team == null) team = sb.registerNewTeam("8"+on.getName().substring(0, on.getName().length()-1));
				}else {
					team = sb.getTeam("8"+on.getName());
					if(team == null) team = sb.registerNewTeam("8"+on.getName());
				}
				
				team.setPrefix("§7RANDOM §8| ");
				if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
				team.setColor(ChatColor.GRAY);
				team.setAllowFriendlyFire(true);
				team.addPlayer(on);
			}
		}
		p.setScoreboard(sb);
	}
	
	public static void updateScoreboard(Player p) {
			if(p.getScoreboard() == null) setScoreboard(p);
			Scoreboard sb = p.getScoreboard();
			if(ProfileHandler.getScoreboardAllow(p.getUniqueId().toString())) {
			Objective obj = sb.getObjective("aaa");
			
			Integer spielzeitticks = EVENT_Spiel.spielzeit.get(p);
			Integer minutengesamt = 0;
			Integer stundengesamt = 0;
			Integer sekundengesamt = spielzeitticks/20;
			if(sekundengesamt >= 60) {
				minutengesamt = sekundengesamt/60;
				sekundengesamt = sekundengesamt%60;
			}
			if(minutengesamt >= 60) {
				stundengesamt = minutengesamt/60;
				minutengesamt = minutengesamt % 60;
			}
				obj.getScore(updateTeam(sb, "Coins5", "§b  "+EVENT_Explore.explorer.get(p), "§2", ChatColor.DARK_AQUA)).setScore(12);
			obj.getScore(updateTeam(sb, "Coins", "§1  §7"+stundengesamt + "§bh §7" + minutengesamt + "§bm ", "§4", ChatColor.RED)).setScore(9);
			if(Jobs.getJob(p.getUniqueId().toString()) != Job.ARBEITSLOS) {
				obj.getScore(updateTeam(sb, "Coins2", "  §3"+Jobs.getJob(p.getUniqueId().toString()).toString().toLowerCase(), "§4", ChatColor.YELLOW)).setScore(6);
			}else obj.getScore(updateTeam(sb, "Coins2", "§1   §cArbeitslos", "§4", ChatColor.YELLOW)).setScore(6);
			DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
			String fmoney = f.format(Economy.getMoney(p.getUniqueId().toString()));
			obj.getScore(updateTeam(sb, "Coins3", "§1  §3"+fmoney+COMMAND_Eco.moneyformat, "§4", ChatColor.BLACK)).setScore(3);
			}
			
			for(Player on : Bukkit.getOnlinePlayers()) {
				if(on.hasPermission("server.admin")) {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("2"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("2"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("2"+on.getName());
						if(team == null) team = sb.registerNewTeam("2"+on.getName());
					}
					
					team.setPrefix("§4ADMIN §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					else team.setSuffix(" §8[§4Team§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
					
				}else if(on.hasPermission("server.leitung")) {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("3"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("3"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("3"+on.getName());
						if(team == null) team = sb.registerNewTeam("3"+on.getName());
					}
					
					team.setPrefix("§cLEITUNG §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					else team.setSuffix(" §8[§4Team§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
				}else if(on.hasPermission("server.dev")) {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("4"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("4"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("4"+on.getName());
						if(team == null) team = sb.registerNewTeam("4"+on.getName());
					}
					
					team.setPrefix("§3DEVELOPER §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					else team.setSuffix(" §8[§4Team§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
				}else if(on.hasPermission("server.staff")) {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("5"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("5"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("5"+on.getName());
						if(team == null) team = sb.registerNewTeam("5"+on.getName());
					}
					
					team.setPrefix("§cSTAFF §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					else team.setSuffix(" §8[§4Team§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
				}else if(on.hasPermission("server.yt")) {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("6"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("6"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("6"+on.getName());
						if(team == null) team = sb.registerNewTeam("6"+on.getName());
					}
					
					team.setPrefix("§5YOUTUBER §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
				}else if(on.hasPermission("server.freund")) {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("7"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("7"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("7"+on.getName());
						if(team == null) team = sb.registerNewTeam("7"+on.getName());
					}
					
					team.setPrefix("§3FREUND §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
				}else {
					Team team = null;
					if(on.getName().length() == 16) {
						team = sb.getTeam("8"+on.getName().substring(0, on.getName().length()-1));
						if(team == null) team = sb.registerNewTeam("8"+on.getName().substring(0, on.getName().length()-1));
					}else {
						team = sb.getTeam("8"+on.getName());
						if(team == null) team = sb.registerNewTeam("8"+on.getName());
					}
					
					team.setPrefix("§7RANDOM §8| ");
					if(ProfileHandler.getClanTag(on.getUniqueId().toString()) != null) team.setSuffix(" §8[§a"+ProfileHandler.getClanTag(on.getUniqueId().toString())+"§8]");
					team.setColor(ChatColor.GRAY);
					team.setAllowFriendlyFire(true);
					team.addPlayer(on);
				}
			}
	}
	
	public static Team getTeam(Scoreboard sb, String Team, String prefix, String suffix) {
		Team team = sb.getTeam(Team);
		if(team == null) team = sb.registerNewTeam(Team);
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.setColor(ChatColor.GRAY);
		team.setAllowFriendlyFire(true);
		team.setDisplayName("§7");
		return team;
	}
	
	public static String updateTeam(Scoreboard sb, String Team, String prefix, String suffix, ChatColor entry) {
		Team team = sb.getTeam(Team);
		if(team == null) team = sb.registerNewTeam(Team);
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.setColor(ChatColor.GRAY);
		team.addEntry(entry.toString());
		return entry.toString();
	}
	
	
	public static void startUpdating() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player on : Bukkit.getOnlinePlayers()) {
					updateScoreboard(on);
					Integer spielzeit = EVENT_Spiel.spielzeit.get(on);
					spielzeit+=20;
					EVENT_Spiel.spielzeit.put(on, spielzeit);
				}
			}
		}.runTaskTimer(Main.instance, 20, 20);
	}
	
	@EventHandler
	public void onJoinVersion(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		PluginDescriptionFile pdf = main.getDescription();
		
		p.setPlayerListHeader(tablistheader.replace("%servername%", Main.servername));
		p.setPlayerListFooter(tablistfooter.replace("%servername%", Main.servername));
		
	}
	
	
}
