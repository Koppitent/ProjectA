package de.koppy.cases;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Case implements CommandExecutor, TabCompleter{
	
	private Main main;
	public COMMAND_Case(Main main) {
		this.main = main;
		main.getCommand("case").setExecutor(this);
	}
	
	public static String prefix = "§8[§6C§ease§8] §r";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(sender instanceof Player) {
		if(p.hasPermission("server.case.admin")) {
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("get")) {
				String cases = args[1];
				if(Case.existCase(cases)) {
					
					p.getInventory().addItem(Case.giveCase(Cases.valueOf(cases.toUpperCase())));
					p.sendMessage(prefix + "§7Du hast die Case §e" + cases + " §7erhalten!");
					
				}else {
					p.sendMessage(prefix + "§cDie Case existiert nicht!");
				}
			}else if(args[0].equalsIgnoreCase("getKey")) {
				String cases = args[1];
				if(Case.existCase(cases)) {
					
					p.getInventory().addItem(Case.giveCaseKey(Cases.valueOf(cases.toUpperCase())));
					p.sendMessage(prefix + "§7Du hast den Case-Key §e" + cases + " §7erhalten!");
					
				}else {
					p.sendMessage(prefix + "§cDie Case existiert nicht!");
				}
			}
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("get")) {
				String target = args[2];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
					String cases = args[1];
					if(Case.existCase(cases)) {
						
						t.getInventory().addItem(Case.giveCase(Cases.valueOf(cases.toUpperCase())));
						t.sendMessage(prefix + "§7Du hast die Case §e" + cases + " §7erhalten!");
						p.sendMessage(prefix + "§7Du hast dem Spieler §3" + t.getName() + " §7die Case §e" + cases + " §7gegeben.");
						
					}else {
						p.sendMessage(prefix + "§cDie Case existiert nicht!");
					}
				}else {
					p.sendMessage(prefix + "§cDer Spieler ist nicht online!");
				}
			}else if(args[0].equalsIgnoreCase("getKey")) {
				String target = args[2];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
				String cases = args[1];
				if(Case.existCase(cases)) {
					
					t.getInventory().addItem(Case.giveCaseKey(Cases.valueOf(cases.toUpperCase())));
					t.sendMessage(prefix + "§7Du hast den Case-Key §e" + cases + " §7erhalten!");
					p.sendMessage(prefix + "§7Du hast dem Spieler §3" + t.getName() + " §7die Case-Key §e" + cases + " §7gegeben.");
					
				}else {
					p.sendMessage(prefix + "§cDie Case existiert nicht!");
				}
				}else {
					p.sendMessage(prefix + "§cDer Spieler ist nicht online!");
				}
			}
		}else if(args.length == 4) {
			if(args[0].equalsIgnoreCase("get")) {
				String target = args[2];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
					String cases = args[1];
					if(Case.existCase(cases)) {
						if(args[3].matches("[0-9]+")) {
							Integer zahl = Integer.valueOf(args[3]);
							for(int i=0; i<zahl; i++) t.getInventory().addItem(Case.giveCase(Cases.valueOf(cases.toUpperCase())));
							t.sendMessage(prefix + "§7Du hast die Case §e" + cases + " §7erhalten!");
							p.sendMessage(prefix + "§7Du hast dem Spieler §3" + t.getName() + " §7die Case §e" + cases + " " + zahl + "x §7gegeben.");
							
						}else {
							p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
						}
					}else {
						p.sendMessage(prefix + "§cDie Case existiert nicht!");
					}
				}else {
					p.sendMessage(prefix + "§cDer Spieler ist nicht online!");
				}
			}else if(args[0].equalsIgnoreCase("getKey")) {
				String target = args[2];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
				String cases = args[1];
				if(Case.existCase(cases)) {
					
					if(args[3].matches("[0-9]+")) {
						Integer zahl = Integer.valueOf(args[3]);
						for(int i=0; i<zahl; i++) t.getInventory().addItem(Case.giveCaseKey(Cases.valueOf(cases.toUpperCase())));
						t.sendMessage(prefix + "§7Du hast die Case §e" + cases + " " + zahl +"x §7erhalten!");
						p.sendMessage(prefix + "§7Du hast dem Spieler §3" + t.getName() + " §7den Case-Key §e" + cases  + " " + zahl + "x §7gegeben.");
						
					}else {
						p.sendMessage(prefix + "§cDu musst eine Zahl angeben.");
					}
					
				}else {
					p.sendMessage(prefix + "§cDie Case existiert nicht!");
				}
				}else {
					p.sendMessage(prefix + "§cDer Spieler ist nicht online!");
				}
			}
		}
		}else {
			p.sendMessage(prefix + "§cPay rein wenn du hebeln möchtest!");
		}
		}else {
			
			if(args.length == 4) {
				if(args[0].equalsIgnoreCase("get")) {
					String target = args[2];
					Player t = Bukkit.getPlayer(target);
					if(t != null) {
						String cases = args[1];
						if(Case.existCase(cases)) {
							if(args[3].matches("[0-9]+")) {
								Integer zahl = Integer.valueOf(args[3]);
								for(int i=0; i<zahl; i++) t.getInventory().addItem(Case.giveCase(Cases.valueOf(cases.toUpperCase())));
								t.sendMessage(prefix + "§7Du hast die Case §e" + cases + " §7erhalten!");
							}
						}
					}
					
				}else if(args[0].equalsIgnoreCase("getKey")) {
					String target = args[2];
					Player t = Bukkit.getPlayer(target);
					if(t != null) {
					String cases = args[1];
					if(Case.existCase(cases)) {
						if(args[3].matches("[0-9]+")) {
							Integer zahl = Integer.valueOf(args[3]);
							for(int i=0; i<zahl; i++) t.getInventory().addItem(Case.giveCaseKey(Cases.valueOf(cases.toUpperCase())));
							t.sendMessage(prefix + "§7Du hast die Case §e" + cases + " " + zahl +"x §7erhalten!");
						}
					}
					}
				}
				
			}
			
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		if(args.length == 1) {
			
		}
		
		return tcomplete;
	}
}
