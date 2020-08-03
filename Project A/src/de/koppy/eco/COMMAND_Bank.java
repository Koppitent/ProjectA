package de.koppy.eco;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.cases.Case;
import de.koppy.cases.Cases;
import de.koppy.handler.InfoHandler;
import de.koppy.handler.ProfileHandler;
import de.koppy.project.EVENT_NBG;
import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class COMMAND_Bank implements CommandExecutor, TabCompleter{
	
	private Main main;
	public static String prefix = "§8[§3B§bank§8] §r";
	public static ArrayList<Player> deletebankacc = new ArrayList<>();
	public static ArrayList<Player> inbankautomat = new ArrayList<>();
	public COMMAND_Bank(Main main) {
		this.main = main;
		main.getCommand("bank").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
		
		if(args.length == 0) {
			
			if(Economy.getAccountNames(p.getUniqueId().toString()).size() != 0) {
				double moneyall = Economy.getMoney(p.getUniqueId().toString());
				p.sendMessage("§8§m-------------§r§8[" + prefix.replace("[", "").replace("]", "").replace(" ", "") + "§7-Konten§8]§m-------------");
				p.sendMessage("");
				for(String bankacc : Economy.getAccountNames(p.getUniqueId().toString())) {
					double bal = Economy.getBankBalance(bankacc);
					moneyall += bal;
					String fbal = f.format(bal);
					p.sendMessage("§7» §3"+bankacc + ": §e" + fbal + COMMAND_Eco.moneyformat);
				}
				p.sendMessage("");
				String fbal = f.format(moneyall);
				p.sendMessage("§7» §7Insgesamt verfügbares Geld: §e" + fbal + COMMAND_Eco.moneyformat);
				p.sendMessage("");
				p.sendMessage("§8§m------------------------------------");
			}else{
				p.sendMessage("§8§m-------------§r§8[" + prefix.replace("[", "").replace("]", "") + "§7-Konten §8]§m-------------");
				p.sendMessage("");
				p.sendMessage("§7» §cEs gibt keine Konten, auf die du Zugriff hast!");
				p.sendMessage("");
				p.sendMessage("§8§m------------------------------------");
			}
			
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("create")) {
				p.sendMessage(prefix + "§cVerwende §e/bank create [Kontoname] §cwenn du in der Bank bist!");
			}else if(args[0].equalsIgnoreCase("help")) {
				p.sendMessage("§8§m-------------§r§8[" + prefix.replace("[", "").replace("]", "") + "§7 / Seite 1§8]§m-------------");
				p.sendMessage("§e/bank §7- Zeigt alle deine Bankkonten-Zugriffe an.");
				p.sendMessage("§e/bank help §7- Zeigt alle möglichen Befehle an.");
				p.sendMessage("§e/bank create <Konto> §7- Erstellt ein Konto.");
				p.sendMessage("§e/bank leave <Konto> §7- Verlassen des Kontos.");
				p.sendMessage("§e/bank addmember <Spieler> <Konto>§7- Fügt einen Spieler zum Konto hinzu.");
				p.sendMessage("§e/bank removemember <Spieler> <Konto> §7- Entfernt einen Spieler vom Konto.");	
				p.sendMessage("§8§m------------------------------------");
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("ab67j")) {
				if(EVENT_Bank.bankschildamount.containsKey(p)) {
				String bankacc = args[1];
				Double amount = EVENT_Bank.bankschildamount.get(p);
				
				if(Economy.hasEnoughMoneyOnBank(bankacc, amount)) {
					
					Economy.removeBankBalance(bankacc, amount, "auszahlung von " + p.getName());
					Economy.addMoney(p.getUniqueId().toString(), amount, "auszahlung von Konto " + bankacc);
					String famount = f.format(amount);
					p.sendMessage(COMMAND_Bank.prefix + "§7Du hast gerade §e" + famount + COMMAND_Eco.moneyformat + " §7von dem Bankkonto §3" + bankacc + " §7ausgezahlt.");
					EVENT_Bank.bankschildamount.remove(p, amount);
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+famount+COMMAND_Eco.moneyformat));
					for(String playerUUID : Economy.getMembers(bankacc)) {
						String playername = API_UUID.getUUIDorNAME(playerUUID);
						Player player = Bukkit.getPlayer(playername);
						if(player != null && player != p) {
							player.sendMessage(COMMAND_Bank.prefix + "§7Der Spieler §3" + p.getName() + " §7hat gerade §3" + famount + COMMAND_Eco.moneyformat + " §7von dem Konto §e" + EconomyMenu.saveBank.get(p) + " §7abgehoben.");
						}
					}
					
				}else {
					p.sendMessage(COMMAND_Bank.prefix + "§cDas Bankkonto hat nicht genug Geld!");
				}
				}
			}else if(args[0].equalsIgnoreCase("ab67c")) {
				if(EVENT_Bank.bankschildamount.containsKey(p)) {
				String bankacc = args[1];
				Double amount = EVENT_Bank.bankschildamount.get(p);
				
				if(Economy.hasEnoughMoney(p.getUniqueId().toString(), amount)) {
					
					Economy.addBankBalance(bankacc, amount, "einzahlung von " + p.getName());
					Economy.removeMoney(p.getUniqueId().toString(), amount, "einzahlung auf Konto " + bankacc);
					String famount = f.format(amount);
					p.sendMessage(COMMAND_Bank.prefix + "§7Du hast gerade §e" + famount + COMMAND_Eco.moneyformat + " §7auf das Bankkonto §3" + bankacc + " §7eingezahlt.");
					EVENT_Bank.bankschildamount.remove(p, amount);
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c-"+famount+COMMAND_Eco.moneyformat));
					for(String playerUUID : Economy.getMembers(bankacc)) {
						String playername = API_UUID.getUUIDorNAME(playerUUID);
						Player player = Bukkit.getPlayer(playername);
						if(player != null && player != p) {
							player.sendMessage(COMMAND_Bank.prefix + "§7Der Spieler §3" + p.getName() + " §7hat gerade §3" + famount + COMMAND_Eco.moneyformat + " §7auf das Konto §e" + EconomyMenu.saveBank.get(p) + " §7eingezahlt.");
						}
					}
					
				}else {
					p.sendMessage(COMMAND_Bank.prefix + "§cDu hast nicht genug Geld dabei!");
				}
				}
			}else if(args[0].equalsIgnoreCase("info")) {
				String bankacc = args[1];
				
				if(Economy.existAcc(bankacc)) {
					
					if(Economy.isMember(p.getUniqueId().toString(), bankacc) || p.hasPermission("server.bank.admin")) {
						
						double bal = Economy.getBankBalance(bankacc);
						String fbal = f.format(bal);
						List<String> members = Economy.getMembers(bankacc);
						String out = "";
						for(String member : members) {
							out = out + API_UUID.getUUIDorNAME(member) + ", ";
						}
						
						p.sendMessage("§8§m-------------§r§8[" + prefix.replace("[", "").replace("]", "").replace(" ", "") + "§7-Info §8]§m-------------");
						p.sendMessage("");
						p.sendMessage("§7» Account: §e" + bankacc);
						p.sendMessage("§7» §3Guthaben: §e" + fbal + COMMAND_Eco.moneyformat);
						p.sendMessage("");
						p.sendMessage("§7» §3Mitglieder: §e" + out);
						p.sendMessage("");
						p.sendMessage("§8§m------------------------------------");
						
					}else{
						p.sendMessage(prefix + "§cDu musst Mitglied des Bankkontos sein um mehr Infos darüber zu erhalten.");
					}
				}else {
					p.sendMessage(prefix + "§cDas Bankkonto " + bankacc + " existiert nicht.");
				}
			}
			if(isInBank(p)) {
			if(args[0].equalsIgnoreCase("create")) {
				String name = args[1];
				if(isAlpha(name)) {
					
					if(Economy.existAcc(name) == false) {
						if(Economy.getAccounts(p.getUniqueId().toString()) < 3) {
							Economy.createBank(p.getUniqueId().toString(), name);
							p.sendMessage(prefix + "§7Du hast erfolgreich ein neues Bankkonto mit dem Namen §e" + name + " §aerfolgreich §7erstellt.");
							
							//* Tutorial
								if(ProfileHandler.getTutorial(p.getUniqueId().toString()).equals("bank")) {
									ProfileHandler.setTutorial(p.getUniqueId().toString(), "land");
									
									
									Bukkit.getScheduler().runTaskLater(main, new Runnable() {
										@Override
										public void run() {
											p.sendMessage(Main.TutorialBot + "§7Gut gemacht! Nun ist dein Geld gesichert, denn in manchen fällen kann es passieren, dass du dein Geld, dass du dabei hast nach einem Tod verschwindet. Aber keine Angst... Du wirst zuvor benachrichtigt für den Fall, dass das passieren könnte. Außerdem wird auch nicht dein ganzes Geld verloren gehen.");
											p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
										}
									}, 20*2);
									
									Bukkit.getScheduler().runTaskLater(main, new Runnable() {
										@Override
										public void run() {
											p.sendMessage(Main.TutorialBot + "§7Jetzt nochmal zu dem wichtigen! Du kannst alle Befehle über dein Konto mit §e/bank help §7einsehen. Infos zu deinem Bankkonto findest du am Bankschalter!");
											p.playSound(p.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 2);
										}
									}, 20*10);
									
									Bukkit.getScheduler().runTaskLater(main, new Runnable() {
										@Override
										public void run() {
											p.sendMessage(Main.TutorialBot + "§7Nun gehts zu deinem ersten Grundstück! Das erhälst du mit §e/land auto§7!");
											p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
											p.sendTitle("§3Nimm dir ein GS!", "§5New Objective!");
											EVENT_NBG.tutorial2.removePlayer(p);
											EVENT_NBG.tutorial3.addPlayer(p);
										}
									}, 20*15);
									
								}
							
						}else {
							p.sendMessage(prefix + "§cDu hast bereits die maximale Anzahl an Bankkonten erreicht!");
						}
					}else {
						p.sendMessage(prefix + "§cDas Bankkonto existiert bereits!");
					}
					
				}else {
					p.sendMessage(prefix + "§cDu darfst nur normale Zeichen eingeben!");
				}
				
			}else if(args[0].equalsIgnoreCase("leave")) {
				String bankacc = args[1];
				
				if(Economy.existAcc(bankacc)) {
					if(Economy.getMembers(bankacc).size() > 1) {
						
						Economy.removeMemberBank(p.getUniqueId().toString(), bankacc);
						p.sendMessage(prefix + "§7Du hast das Bankkonto §e" + bankacc + "§cverlassen§7.");
						for(String playerkonto : Economy.getMembers(bankacc)) {
							Player bankmember = Bukkit.getPlayer(API_UUID.getUUIDorNAME(playerkonto));
							if(bankmember != null && bankmember != p) {
								bankmember.sendMessage(prefix + "§7Der Spieler §5" + p.getName() + " §7hat das Bankkonto §e" + bankacc + " §7verlassen!");
							}
						}
						
					}else {
						if(deletebankacc.contains(p)) {
							deletebankacc.remove(p);
						//ACHTUNG acc löschung message
						double auszahlen = Economy.getBankBalance(bankacc);
						String famount = f.format(auszahlen);
						Economy.addMoney(p.getUniqueId().toString(), auszahlen, "auszahlung durch schliessung von Konto §e" + bankacc);
						Economy.deleteBank(bankacc);
						p.sendMessage(prefix + "§7Das Bankkonto §e" + bankacc + " §7wurde §cgelöscht§7.");
						if(auszahlen != 0) p.sendMessage(prefix + "§7Dir wurden die restlichen §3" + famount + COMMAND_Eco.moneyformat + " §7von dem Konto ausgeszahlt!");
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+famount + COMMAND_Eco.moneyformat));
						}else {
							p.sendMessage(prefix + "§4ACHTUNG!!! §cWenn du dieses Konto verlässt, wird es automatisch gelöscht! Wenn du dir sicher bist  gib den Befehl erneut ein.");
							deletebankacc.add(p);
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									deletebankacc.remove(p);
								}
							}, 20*30);
						}
					}
				}else {
					p.sendMessage(prefix + "§cDas Bankkonto existiert nicht!");
				}
				
			}
			}else {
				p.sendMessage(prefix + "§cDu musst in der Bank sein!");
			}
		}else if(args.length ==3) {
			if(isInBank(p)) {
			if(args[0].equalsIgnoreCase("addmember")) {
				String bankacc = args[2];
				String target = args[1];
				if(Economy.existAcc(bankacc) == true) {
					if(Economy.getMembers(bankacc).size() < 28) {
					if(API_UUID.existUUIDorNAME(target)) {
						if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
						
						if(Economy.isMember(target, bankacc) == false) {
							
							if(Economy.getAccounts(target) < 3) {
								
								for(String playerkonto : Economy.getMembers(bankacc)) {
									Player bankmember = Bukkit.getPlayer(API_UUID.getUUIDorNAME(playerkonto));
									if(bankmember != null && bankmember != p) {
										bankmember.sendMessage(prefix + "§7Der Spieler §5" + p.getName() + " §7hat den Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7zu dem Bankkonto §e" + bankacc + " §7hinzugefügt!");
									}
								}
								Economy.addMemberBank(target, bankacc);
								p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7wurde zu dem Konto §e" + bankacc + " §ahinzugefügt§7.");
								Player t = Bukkit.getPlayer(API_UUID.getUUIDorNAME(target));
								if(t != null) t.sendMessage(prefix + "§7Du wurdest zu dem Bankkonto §e" + bankacc + " §ahinzugefügt§7.");
								
							}else{
								p.sendMessage(prefix + "§cDer Spieler hat bereits die maximale Anzahl an Bankkonten erreicht!");
							}
							
						}else{
							p.sendMessage(prefix + "§cDer Spieler ist bereits Mitglied auf dem Bankkonto."); 
						}
						
					}else {
						p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Netzwerk."); 
					}
					}else {
						p.sendMessage(prefix + "§cEs wurde bereits die maximale Anzahl an Mitgliedern auf dem Konto erreicht!"); 
					}
				}else {
					p.sendMessage(prefix + "§cDas Bankkonto existiert nicht!");
				}
				
			}else if(args[0].equalsIgnoreCase("removemember")) {
				String bankacc = args[1];
				String target = args[2];
				if(Economy.existAcc(bankacc) == true) {
					if(API_UUID.existUUIDorNAME(target)) {
						if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
						
						if(Economy.isMember(target, bankacc) == true) {
							
							if(!API_UUID.getUUIDorNAME(target).equals(p.getName())) {
								
								Economy.removeMemberBank(target, bankacc);
								p.sendMessage(prefix + "§7Der Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7wurde vom Konto §e" + bankacc + " §centfernt§7.");
								Player t = Bukkit.getPlayer(API_UUID.getUUIDorNAME(target));
								if(t != null) t.sendMessage(prefix + "§7Du wurdest von dem Bankkonto §e" + bankacc + " §centfernt§7.");
								for(String playerkonto : Economy.getMembers(bankacc)) {
									Player bankmember = Bukkit.getPlayer(API_UUID.getUUIDorNAME(playerkonto));
									if(bankmember != null && bankmember != p) {
										bankmember.sendMessage(prefix + "§7Der Spieler §5" + p.getName() + " §7hat den Spieler §3" + API_UUID.getUUIDorNAME(target) + " §7von dem Bankkonto §e" + bankacc + " §7entfernt!");
									}
								}
								
							}else {
								p.sendMessage(prefix + "§cDas bist du selbst!");
							}
							
						}else{
							p.sendMessage(prefix + "§cDer Spieler ist kein Mitglied auf dem Bankkonto."); 
						}
						
					}else {
						p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Netzwerk."); 
					}
				}else {
					p.sendMessage(prefix + "§cDer Bankaccount existiert nicht!");
				}
			}
			}else {
				p.sendMessage(prefix + "§cDu musst in der Bank sein!");
			}
		}
		return true;	
	}
	
	public static boolean isInBank(Player p) {
		Location pos1 = new Location(Bukkit.getWorld("world"), 5509, 0, -6339);
		Location pos2 = new Location(Bukkit.getWorld("world"), 5462, 60, -6366);
		if(InfoHandler.isIn(p.getLocation(), pos1, pos2) || inbankautomat.contains(p)) return true;
		else return false;
	}
	
	public static boolean isAlpha(String text) {
		  return text.matches("[a-zA-ZäÄöÖüÜß]+");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player)sender;
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("help");
			check.add("info");
			check.add("create");
			check.add("leave");
			if(Economy.getAccountNames(p.getUniqueId().toString()).size() != 0) {
				check.add("addmember");
				check.add("removemember");
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("removemember") || args[0].equalsIgnoreCase("addmember")) {
				if(Economy.getAccountNames(p.getUniqueId().toString()).size() != 0) {
					for(String konten : Economy.getAccountNames(p.getUniqueId().toString())) check.add(konten);
				}else {
					check.add("§4§l✘ §r§cDu hast kein Konto §4§l✘");
				}
			}else if(args[0].equalsIgnoreCase("addmember") || args[0].equalsIgnoreCase("removemember")) {
				for(Player all : Bukkit.getOnlinePlayers()) if(all != p) check.add(all.getName());
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 3) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("addmember") || args[0].equalsIgnoreCase("removemember")) {
				String bankacc = args[1];
				if(Economy.existAcc(bankacc)) {
					for(String member : Economy.getMembers(bankacc)) check.add(API_UUID.getUUIDorNAME(member));
				}else {
					check.add("§4§l✘ §r§cDas Konto existiert nicht §4§l✘");
				}
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
		}
		
		return tcomplete;
	}
}
