package de.koppy.job;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.koppy.handler.ProfileHandler;
import de.koppy.project.EVENT_NBG;
import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class COMMAND_Job implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Job(Main main) {
		this.main = main;
		main.getCommand("job").setExecutor(this);
	}
	
	public static String prefix = "§8[§2J§aob§8] §r";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length == 0) {
			//* Menu //
			Inventory inv = JobMenu.createJobInv(p, "§2J§aob§7-Auswahl");
			p.openInventory(inv);
			//*     //
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("menu")) {
				Inventory inv = JobMenu.createJobInv(p, "§2J§aob§7-Auswahl");
				p.openInventory(inv);
			}else if(args[0].equalsIgnoreCase("join")) {
				p.sendMessage(prefix + "§cVerwende §e/job join <Jobname> §cum einem Job beizutreten.");
			}else if(args[0].equalsIgnoreCase("leave")) {
				if(Jobs.getJob(p.getUniqueId().toString()) != Job.ARBEITSLOS) {
					Jobs.setJob(p, p.getUniqueId().toString(), Job.ARBEITSLOS);
					p.sendMessage(prefix + "§7Du bist nun §3Arbeitslos§7...");
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2Job §cverlassen!"));
				}else {
					p.sendMessage(prefix + "§cDu bist in keinem Job!");
				}
			}else if(args[0].equalsIgnoreCase("list")) {
				String out = "";
				for(String jobs : Jobs.getJobList()) out = out + jobs + " | ";
				p.sendMessage(prefix + "§7Es existieren folgende Jobs: §e" + out.replace("ARBEITSLOS |", ""));
			}else if(args[0].equalsIgnoreCase("info")) {
				//Erweiterte Info
				if(Jobs.getJob(p.getUniqueId().toString()) != Job.ARBEITSLOS) {
					p.sendMessage(prefix + "§7Du bist im Job §e" + Jobs.getJob(p.getUniqueId().toString()) + "§7.");
					Integer currentLEVEL = Jobs.level.get(p);
					Integer xpneeded=0;
					if(currentLEVEL <= 14) {
						for(int i=1; i<16; i++) if(currentLEVEL == i) xpneeded=Jobs.mainXP*i;
					}
					if(currentLEVEL < 15) {
						p.sendMessage(prefix + "§7Level: §e"+Jobs.level.get(p));
						p.sendMessage(prefix + "§7XP: §3"+Jobs.xp.get(p)+"§7/"+xpneeded);
					}else {
						p.sendMessage(prefix + "§7Level: §e"+Jobs.level.get(p) + "§c(maximum)");
						p.sendMessage(prefix + "§7XP: §cmaximales Level erreicht.");
					}
					p.sendMessage("");
					if(Jobs.getJob(p.getUniqueId().toString()) == Job.MINER) {
						
						if(EVENT_Miner.coalandnetherore.get(p) != null) p.sendMessage(prefix + "§7Nether/Kohle-Erze: §3"+EVENT_Miner.coalandnetherore.get(p)+"§7/"+EVENT_Miner.coalandnetheroreBEN); 
						else p.sendMessage(prefix + "§7Nether/Kohle-Erze: §c0§7/"+EVENT_Miner.coalandnetheroreBEN);
						
						if(EVENT_Miner.ironlapisgoldore.get(p) != null) p.sendMessage(prefix + "§7Eisen/Lapis/Gold-Erze: §3"+EVENT_Miner.ironlapisgoldore.get(p)+"§7/"+EVENT_Miner.ironlapisgoldoreBEN); 
						else p.sendMessage(prefix + "§7Eisen/Lapis/Gold: §c0§7/"+EVENT_Miner.ironlapisgoldoreBEN);
						
						if(EVENT_Miner.redstoneore.get(p) != null) p.sendMessage(prefix + "§7Redstone-Erze: §3"+EVENT_Miner.redstoneore.get(p)+"§7/"+EVENT_Miner.redstoneoreBEN); 
						else p.sendMessage(prefix + "§7Redstone-Erze: §c0§7/"+EVENT_Miner.redstoneoreBEN);
						
						if(EVENT_Miner.diamondemeraldore.get(p) != null) p.sendMessage(prefix + "§7Diamant-Erze: §3"+EVENT_Miner.diamondemeraldore.get(p)+"§7/"+EVENT_Miner.diamondemeraldoreBEN); 
						else p.sendMessage(prefix + "§7Diamant-Erze: §c0§7/"+EVENT_Miner.diamondemeraldoreBEN);
						
					}else if(Jobs.getJob(p.getUniqueId().toString()) == Job.FARMER) {
						
						if(EVENT_Farmer.potatocarrotwheatbeetrootfarm.get(p) != null) p.sendMessage(prefix + "§7Kartoffeln/Karotten/Weizen: §3"+EVENT_Farmer.potatocarrotwheatbeetrootfarm.get(p)+"§7/"+EVENT_Farmer.potatocarrotwheatbeetrootfarmBEN); 
						else p.sendMessage(prefix + "§7Kartoffeln/Karotten/Weizen: §c0§7/"+EVENT_Farmer.potatocarrotwheatbeetrootfarmBEN);
						
						if(EVENT_Farmer.melonpumpkinfarm.get(p) != null) p.sendMessage(prefix + "§7Melonen/Kürbis: §3"+EVENT_Farmer.melonpumpkinfarm.get(p)+"§7/"+EVENT_Farmer.melonpumpkinfarmBEN); 
						else p.sendMessage(prefix + "§7Melonen/Kürbis: §c0§7/"+EVENT_Farmer.melonpumpkinfarmBEN);
						
					}else if(Jobs.getJob(p.getUniqueId().toString()) == Job.HOLZFAELLER) {
						
						if(EVENT_Holzfaeller.oakbirchlog.get(p) != null) p.sendMessage(prefix + "§7Eichen/Birken-Holz: §3"+EVENT_Holzfaeller.oakbirchlog.get(p)+"§7/"+EVENT_Holzfaeller.oakbirchlogBEN);
						else p.sendMessage(prefix + "§7Eichen/Birken-Holz: §c0§7/"+EVENT_Holzfaeller.oakbirchlogBEN);
						
						if(EVENT_Holzfaeller.spruceacacialog.get(p) != null) p.sendMessage(prefix + "§7Fichte/Akazien-Holz: §3"+EVENT_Holzfaeller.spruceacacialog.get(p)+"§7/"+EVENT_Holzfaeller.spruceacacialogBEN); 
						else p.sendMessage(prefix + "§7Fichte/Akazien-Holz: §c0§7/"+EVENT_Holzfaeller.spruceacacialogBEN);
						
						if(EVENT_Holzfaeller.jungledarklog.get(p) != null) p.sendMessage(prefix + "§7Dschungel/Dunkles Eichen-Holz: §3"+EVENT_Holzfaeller.jungledarklog.get(p)+"§7/"+EVENT_Holzfaeller.jungledarklogBEN); 
						else p.sendMessage(prefix + "§7Dschungel/Dunkles Eichen-Holz: §c0§7/"+EVENT_Holzfaeller.jungledarklogBEN);
						
					}else if(Jobs.getJob(p.getUniqueId().toString()) == Job.GRABER) {
						
						if(EVENT_Graber.dirtgrassgraben.get(p) != null) p.sendMessage(prefix + "§7Dirt-Blöcke: §3"+EVENT_Graber.dirtgrassgraben.get(p)+"§7/"+EVENT_Graber.dirtgrassgrabenBEN);
						else p.sendMessage(prefix + "§7Dirt-Blöcke: §c0§7/"+EVENT_Graber.dirtgrassgrabenBEN);
						
					}else if(Jobs.getJob(p.getUniqueId().toString()) == Job.ERBAUER) {
						
						if(EVENT_Erbauer.bauenbauen.get(p) != null) p.sendMessage(prefix + "§7Gebaute Blöcke: §3"+EVENT_Erbauer.bauenbauen.get(p)+"§7/"+EVENT_Erbauer.bauenbauenBEN);
						else p.sendMessage(prefix + "§7Gebaute Blöcke: §c0§7/"+EVENT_Erbauer.bauenbauenBEN);
						
					}else if(Jobs.getJob(p.getUniqueId().toString()) == Job.NETHERHOLZFAELLER) {
						
						if(EVENT_Netherholzfaeller.warpedcrimsonstem.get(p) != null) p.sendMessage(prefix + "§7Warped/Crimson-Stämme: §3"+EVENT_Netherholzfaeller.warpedcrimsonstem.get(p)+"§7/"+EVENT_Netherholzfaeller.warpedcrimsonstemBEN);
						else p.sendMessage(prefix + "§7Warped/Crimson-Stämme: §c0§7/"+EVENT_Netherholzfaeller.warpedcrimsonstemBEN);
						
					}else if(Jobs.getJob(p.getUniqueId().toString()) == Job.SCHMIED) {
						
						if(EVENT_Schmied.enchantitem.get(p) != null) p.sendMessage(prefix + "§7Enchanted Items: §3"+EVENT_Schmied.enchantitem.get(p)+"§7/"+EVENT_Schmied.enchantitemBEN);
						else p.sendMessage(prefix + "§7Enchanted Items: §c0§7/"+EVENT_Schmied.enchantitemBEN);
						
					}
					
				}else {
					p.sendMessage(prefix + "§7Du bist §cArbeitslos§7.");
				}
			}else if(args[0].equalsIgnoreCase("help")) {
				
				p.sendMessage("§8§m-------------§r§8[" + prefix.replace("[", "").replace("]", "") + "§7/ Seite 1§8]§m-------------");
				p.sendMessage("§e/job §7- Öffnet das Job-Menü.");
				p.sendMessage("§e/job help §7- Hilfemenü für Jobs.");
				p.sendMessage("§e/job info §7- Siehe infos über deinen Job.");
				p.sendMessage("§e/job join <Job> §7- Nimm einen Job an.");
				p.sendMessage("§e/job leave <Job> §7- Verlasse deinen derzeitigen Job.");
				p.sendMessage("§e/job list §7- Eine Liste an Jobs.");
				p.sendMessage("§8§m--------------------------------------");
				
			}
			
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("join")) {
				String job = args[1];
				if(Jobs.getJobList().contains(job.toUpperCase())){
					if(Jobs.getJob(p.getUniqueId().toString()) == Job.ARBEITSLOS) {
						job = job.toUpperCase();
						Job realjob = Job.valueOf(job);
						Jobs.setJob(p, p.getUniqueId().toString(), realjob);
						p.sendMessage(Main.prefix + "§7Du hast den Job §e"+realjob.toString()+"§7 angenommen.");
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§2Job §aangenommen!"));
						
						//* Tutorial
						if(ProfileHandler.getTutorial(p.getUniqueId().toString()).equals("job")) {
							ProfileHandler.setTutorial(p.getUniqueId().toString(), "bank");
							
							p.sendMessage(Main.TutorialBot + "§7Sehr gut! Nun kannst du mit deinem neuen Job Geld verdienen!");
							
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									p.sendMessage(Main.TutorialBot + "§7Alle Infos über deinen Job siehst du mit §e/job info§7.");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
								}
							}, 20*2);
							
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									p.sendMessage(Main.TutorialBot + "§7Du musst immer gewisse §5'Miniquests' §7erfüllen um XP für deinen Job zu erhalten um dann wiederrum aufzuleveln. Dadurch erhälst du für deinen Job mehr Geld!");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
								}
							}, 20*4);
							
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									p.sendMessage(Main.TutorialBot + "§7Alle weiteren Infos zu dem Job-System siehst du bei §e/job help§7.");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
								}
							}, 20*6);
							
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									p.sendMessage(Main.TutorialBot + "§7Nun solltest du dein hart erfarmtest Geld sichern gehen. Für schlechte Zeiten du weisst bescheid... (§e/bank create [Kontoname]§7)");
									p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
									p.sendTitle("§3Erstelle ein Bankkonto!", "§5New Objective!");
									EVENT_NBG.tutorial1.removePlayer(p);
									EVENT_NBG.tutorial2.addPlayer(p);
								}
							}, 20*15);
							
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									p.sendMessage(Main.TutorialBot + "§7Du musst dafür übrigens in die Bank (§e/warp§7)");
									p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
								}
							}, 20*18);
							
						}
						
					}else {
						p.sendMessage(prefix + "§cDu hast bereits einen Job!");
					}
				}else {
					String out = "";
					for(String jobs : Jobs.getJobList()) out = out + jobs + " | ";
					p.sendMessage(prefix + "§cEs existieren folgende Jobs: §e" + out.replace("ARBEITSLOS |", ""));
				}
				
			}
				
			
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("info");
			check.add("list");
			check.add("leave");
			check.add("help");
			check.add("menu");
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("join")) {
				for(Job job : Job.values()) check.add(job.toString().toLowerCase());
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
		}
		
		return tcomplete;
	}
}
