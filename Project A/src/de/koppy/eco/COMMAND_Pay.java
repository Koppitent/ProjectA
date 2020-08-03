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

import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class COMMAND_Pay implements CommandExecutor, TabCompleter{

	private Main main;
	public static ArrayList<Player> paypause = new ArrayList<>();
	public COMMAND_Pay(Main main) {
		this.main = main;
		main.getCommand("pay").setExecutor(this);
	}
	DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length == 2) {
			if(!paypause.contains(p)) {
			String target = args[0];
			Player t = Bukkit.getPlayer(target);
			if(t != null) {
				if(t != p) {
				if(args[1].matches("[0.0-9.0]+")) {
					double amount  = Double.valueOf(args[1]);
					
					if(Economy.hasEnoughMoney(p.getUniqueId().toString(), amount)) {
						
						paypause.add(p);
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							@Override
							public void run() {
								paypause.remove(p);
							}
						}, 20*5);
						String famount = f.format(amount);
						Economy.removeMoney(p.getUniqueId().toString(), amount, "gezahlt an " + t.getName());
						Economy.addMoney(t.getUniqueId().toString(), amount, "erhalten von " + p.getName());
						p.sendMessage(COMMAND_Eco.prefix + "§7Du hast dem Spieler §e" + t.getName() + " §7gerade §3" + famount + COMMAND_Eco.moneyformat + " §7gegeben.");
						t.sendMessage(COMMAND_Eco.prefix + "§7Du hast von dem Spieler §e" + p.getName() + " §7gerade §3" + famount + COMMAND_Eco.moneyformat + " §7erhalten.");
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c-"+famount+COMMAND_Eco.moneyformat));
						t.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+famount+COMMAND_Eco.moneyformat));
						
					}else {
						p.sendMessage(COMMAND_Eco.prefix + "§cDu hast nicht genug Geld dabei.");
					}
				}else {
					p.sendMessage(COMMAND_Eco.prefix + "§cDu musst eine Zahl angeben.");
				}
				}else{
					p.sendMessage(COMMAND_Eco.prefix + "§cDu kannst nur anderen Spielern Geld bezahlen!");
				}
			}else {
				p.sendMessage(COMMAND_Eco.prefix + "§cDer Spieler " + target + " ist nicht online!");
			}
			}else {
				p.sendMessage(COMMAND_Eco.prefix + "§cKomm mal runter! Nicht so schnell!");
			}
		}else if(args.length == 3) {
			if(!paypause.contains(p)) {
				String target = args[0];
				Player t = Bukkit.getPlayer(target);
				if(t != null) {
					if(t != p) {
					if(args[1].matches("[0.0-9.0]+")) {
						double amount  = Double.valueOf(args[1]);
						
						if(Economy.hasEnoughMoney(p.getUniqueId().toString(), amount)) {
							String payreason = args[2];
							if(COMMAND_Bank.isAlpha(payreason)) {
							
							paypause.add(p);
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									paypause.remove(p);
								}
							}, 20*5);
							String famount = f.format(amount);
							Economy.removeMoney(p.getUniqueId().toString(), amount, "gezahlt an " + t.getName() + ": "+payreason);
							Economy.addMoney(t.getUniqueId().toString(), amount, "erhalten von " + p.getName() + ": "+payreason);
							p.sendMessage(COMMAND_Eco.prefix + "§7Du hast dem Spieler §e" + t.getName() + " §7gerade §3" + famount + COMMAND_Eco.moneyformat + " §7gegeben. §e(" + payreason + ")");
							t.sendMessage(COMMAND_Eco.prefix + "§7Du hast von dem Spieler §e" + p.getName() + " §7gerade §3" + famount + COMMAND_Eco.moneyformat + " §7erhalten. §e(" + payreason + ")");
							
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c-"+famount + COMMAND_Eco.moneyformat));
							t.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+famount + COMMAND_Eco.moneyformat));
							
							}else{
								p.sendMessage(COMMAND_Eco.prefix + "§cDu darfst nur normale Buchstaben verwenden.");
							}
						}else {
							p.sendMessage(COMMAND_Eco.prefix + "§cDu hast nicht genug Geld dabei.");
						}
					}else {
						p.sendMessage(COMMAND_Eco.prefix + "§cDu musst eine Zahl angeben.");
					}
					}else{
						p.sendMessage(COMMAND_Eco.prefix + "§cDu kannst nur anderen Spielern Geld bezahlen!");
					}
				}else {
					p.sendMessage(COMMAND_Eco.prefix + "§cDer Spieler " + target + " ist nicht online!");
				}
				}else {
					p.sendMessage(COMMAND_Eco.prefix + "§cKomm mal runter! Nicht so schnell!");
				}
		}else {
			p.sendMessage(COMMAND_Eco.prefix + "§cVerwende §e/pay [Spieler] [Amount]");
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		if(args.length == 1) {
			Player p = (Player) sender;
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) if(all != p) check.add(all.getName());
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
			if(tcomplete.isEmpty()) tcomplete.add("§4§l✘ §r§cniemand online §4§l✘");
		}else if(args.length == 2) {
			tcomplete.add("§4§l✘ §r§cBegründung(ein Wort) §4§l✘");
		}
		return tcomplete;
	}
}
