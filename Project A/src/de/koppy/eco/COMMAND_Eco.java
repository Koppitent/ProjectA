package de.koppy.eco;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.project.Main;

public class COMMAND_Eco implements CommandExecutor, TabCompleter{

	public static String prefix = "§8[§5E§dconomy§8] §r";
	public static String moneyformat = " €";
	
	private Main main;
	public COMMAND_Eco(Main main) {
		this.main = main;
		main.getCommand("eco").setExecutor(this);
		main.getCommand("economy").setExecutor(this);
		main.getCommand("money").setExecutor(this);
		main.getCommand("bal").setExecutor(this);
		main.getCommand("balance").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
		
		if(args.length == 0) {
			//Balance
			
			Double money = Economy.getMoney(p.getUniqueId().toString());
			String formatmoney = f.format(money);
			p.sendMessage(prefix + "§7Du hast §3" + formatmoney + moneyformat + " §7dabei.");
			
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("menu")) {
				
				Inventory inv = EconomyMenu.openEcoMenu(p, prefix.replace("[", "").replace("]", "") + "§7Menu");
				p.openInventory(inv);
				
			}else if(args[0].equalsIgnoreCase("help")) {
				p.sendMessage("§8§m-------------§r§8[" + prefix.replace("[", "").replace("]", "") + "§7/ Seite 1§8]§m-------------");
				p.sendMessage("§e/money §7- Zeigt an wie viel Geld du hast.");
				p.sendMessage("§e/money <Spieler> §7- Zeigt das Geld des Spielers.");
				p.sendMessage("§e/eco help §7- Siehe alle Befehle ein.");
				p.sendMessage("§e/pay <Spieler> <Anzahl> §7- Bezahlt dem Spieler einen Geldbetrag.");
				p.sendMessage("§e/moneytoggle §7- Setze deinen Geldstand auf privat/public.");
				p.sendMessage("§e/eco menu §7- Öffnet ein Menü mit deinem Eco-Stand.");
				p.sendMessage("§8§m------------------------------------");
			}else {
				if(API_UUID.existUUIDorNAME(args[0])) {
					String target = args[0];
					if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
					
					if(Economy.isPublic(target)) {
						Double money = Economy.getMoney(target);
						String formatmoney = f.format(money);
						p.sendMessage(prefix + "§7Der Spieler §e" + API_UUID.getUUIDorNAME(target) + " §7hat §3" + formatmoney + moneyformat + " §7dabei.");
						
					}else{
						Double money = Economy.getMoney(target);
						String formatmoney = f.format(money);
						if(p.hasPermission("server.eco.admin")) p.sendMessage(prefix + "§7Der Spieler §e" + API_UUID.getUUIDorNAME(target) + " §7hat §3" + formatmoney + moneyformat + " §7dabei. (§cprivat§7)");
						else p.sendMessage(prefix + "§cDer Spieler hat sein Geld auf Privat!");
					}
				}else {
					p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Netzwerk!");
				}
			}
			
		}else if(args.length == 2) {
			if(p.hasPermission("server.eco.admin")) {
				if(args[0].equalsIgnoreCase("add")) {
					if(args[1].matches("[0.0-9.0]+")) {
						
						double amount = Double.valueOf(args[1]);
						Economy.addMoney(p.getUniqueId().toString(), amount, "Von einem Admin hinzugefügt!");
						String famount = f.format(amount);
						p.sendMessage(prefix + "§7Du hast dir §3" + famount + moneyformat + " §7hinzugefügt.");
						
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
				}else if(args[0].equalsIgnoreCase("set")) {
					if(args[1].matches("[0.0-9.0]+")) {
						
						double amount = Double.valueOf(args[1]);
						Economy.setMoney(p.getUniqueId().toString(), amount, "Von einem Admin gesetzt!");
						String famount = f.format(amount);
						p.sendMessage(prefix + "§7Du hast dir §3" + famount + moneyformat + " §7gesetzt.");
						
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
				}else if(args[0].equalsIgnoreCase("remove")) {
					if(args[1].matches("[0.0-9.0]+")) {
						
						double amount = Double.valueOf(args[1]);
						Economy.setMoney(p.getUniqueId().toString(), amount, "Von einem Admin entfernt!");
						String famount = f.format(amount);
						p.sendMessage(prefix + "§7Du hast dir §3" + famount + moneyformat + " §7entfernt.");
						
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
				}
			}
			
		}else if(args.length == 3) {
			if(p.hasPermission("server.eco.admin")) {
				if(args[0].equalsIgnoreCase("add")) {
					if(args[1].matches("[0.0-9.0]+")) {
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							double amount = Double.valueOf(args[1]);
							Economy.addMoney(target, amount, "Von einem Admin hinzugefügt!");
							String famount = f.format(amount);
							p.sendMessage(prefix + "§7Du hast dem Spieler §e" + API_UUID.getUUIDorNAME(target) + " §3" + famount + moneyformat + " §7hinzugefügt.");
							if(Bukkit.getPlayer(API_UUID.getUUIDorNAME(target)) != null) Bukkit.getPlayer(API_UUID.getUUIDorNAME(target)).sendMessage(prefix + "§7Dir wurden §3" + famount + moneyformat + " §7hinzugefügt.");
							
						}else {
							p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Server");
						}
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
				}else if(args[0].equalsIgnoreCase("set")) {
					if(args[1].matches("[0.0-9.0]+")) {
						
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							double amount = Double.valueOf(args[1]);
							Economy.setMoney(target, amount, "Von einem Admin gesetzt!");
							String famount = f.format(amount);
							p.sendMessage(prefix + "§7Du hast dem Spieler §e" + API_UUID.getUUIDorNAME(target) + " §3" + famount + moneyformat + " §7gesetzt.");
							if(Bukkit.getPlayer(API_UUID.getUUIDorNAME(target)) != null) Bukkit.getPlayer(API_UUID.getUUIDorNAME(target)).sendMessage(prefix + "§7Dir wurden §3" + famount + moneyformat + " §7gesetzt.");
							
						}else {
							p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Server");
						}
						
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
				}else if(args[0].equalsIgnoreCase("remove")) {
					if(args[1].matches("[0.0-9.0]+")) {
						
						String target = args[2];
						if(API_UUID.existUUIDorNAME(target)) {
							
							if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
							double amount = Double.valueOf(args[1]);
							Economy.addMoney(target, amount, "Von einem Admin entfernt!");
							String famount = f.format(amount);
							p.sendMessage(prefix + "§7Du hast dem Spieler §e" + API_UUID.getUUIDorNAME(target) + " §3" + famount + moneyformat + " §7entfernt.");
							if(Bukkit.getPlayer(API_UUID.getUUIDorNAME(target)) != null) Bukkit.getPlayer(API_UUID.getUUIDorNAME(target)).sendMessage(prefix + "§7Dir wurden §3" + famount + moneyformat + " §7entfernt.");
							
						}else {
							p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Server");
						}
						
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
				}
			}
			
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player) sender;
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("help");
			check.add("menu");
			check.add("§4§l✘ §r§cSpielername §4§l✘");
			if(p.hasPermission("server.eco.admin")) {
				check.add("add");
				check.add("set");
				check.add("remove");
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
				if(p.hasPermission("server.eco.admin")) {
					check.add("§4§l✘ §r§cAmount §4§l✘");
				}
			}
			for(String s : check) if(s.startsWith(args[1])) tcomplete.add(s);
		}else if(args.length == 3) {
			List<String> check = new ArrayList<>();
			if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
				if(p.hasPermission("server.eco.admin")) {
					check.add("§4§l✘ §r§cSpielername §4§l✘");
				}
			}
			for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
		}
		
		return tcomplete;
	}
}
