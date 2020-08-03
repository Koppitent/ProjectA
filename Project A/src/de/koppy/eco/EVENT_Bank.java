package de.koppy.eco;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENT_Bank implements Listener{
	
	private Main main;
	public EVENT_Bank(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
	public static HashMap<Player, Double> bankschildamount = new HashMap<>();
	public static HashMap<Player, Location> isSchildbesetzt = new HashMap<>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign sign = (Sign) e.getClickedBlock().getState();
					
					if(sign.getLine(1).equals("§3B§bank§eschalter")) {
						if(!isSchildbesetzt.containsValue(sign.getLocation())) {
							Player p = e.getPlayer();
							Inventory inv = EconomyMenu.openBankMenu(p, "§3B§bank§7-Schalter");
							p.openInventory(inv);
							isSchildbesetzt.put(p, sign.getLocation());
						}else {
							Player p = e.getPlayer();
							p.sendMessage(COMMAND_Bank.prefix + "§cDer BankSchalter wird bereits verwendet.");
						}
					}else if(sign.getLine(0).equals("§8[§3B§bank§8]")) {
						if(sign.getLine(1).equals("§3einzahlen")) {
							Double amount  = Double.valueOf(sign.getLine(2).replace("§e", ""));
							Player p = e.getPlayer();
							if(Economy.getAccountNames(p.getUniqueId().toString()).size() == 1) {
								String bankacc = Economy.getAccountNames(p.getUniqueId().toString()).get(0);
								
								if(Economy.hasEnoughMoney(p.getUniqueId().toString(), amount)) {
									
									Economy.addBankBalance(bankacc, amount, "einzahlung von " + p.getName());
									Economy.removeMoney(p.getUniqueId().toString(), amount, "einzahlung auf Konto " + bankacc);
									String famount = f.format(amount);
									p.sendMessage(COMMAND_Bank.prefix + "§7Du hast gerade §e" + famount + COMMAND_Eco.moneyformat + " §7auf das Bankkonto §3" + bankacc + " §7eingezahlt.");
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
								
							}else if(Economy.getAccountNames(p.getUniqueId().toString()).size() == 0) {
								p.sendMessage(COMMAND_Bank.prefix + "§cDu bist an keinem Konto beteiligt!");
							}else {
								
								bankschildamount.put(p, amount);
								p.sendMessage("");
								p.sendMessage("§7Wähle das Konto aus, auf das du einzahlen möchtest:");
								for(String bankacc : Economy.getAccountNames(p.getUniqueId().toString())) {
									TextComponent tc = new TextComponent();
									tc.setText("§7- §3"+bankacc);
									tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Klicken!").create()));
									tc.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/bank ab67c " + bankacc));
									p.spigot().sendMessage(tc);
								}
								
							}
							
						}else if(sign.getLine(1).equals("§3auszahlen")) {
							Double amount  = Double.valueOf(sign.getLine(2).replace("§e", ""));
							Player p = e.getPlayer();
							if(Economy.getAccountNames(p.getUniqueId().toString()).size() == 1) {
								String bankacc = Economy.getAccountNames(p.getUniqueId().toString()).get(0);
								
								if(Economy.hasEnoughMoneyOnBank(bankacc, amount)) {
									
									Economy.removeBankBalance(bankacc, amount, "auszahlung von " + p.getName());
									Economy.addMoney(p.getUniqueId().toString(), amount, "auszahlung von Konto " + bankacc);
									String famount = f.format(amount);
									p.sendMessage(COMMAND_Bank.prefix + "§7Du hast gerade §e" + famount + COMMAND_Eco.moneyformat + " §7von dem Bankkonto §3" + bankacc + " §7ausgezahlt.");
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+famount+COMMAND_Eco.moneyformat));
									for(String playerUUID : Economy.getMembers(bankacc)) {
										String playername = API_UUID.getUUIDorNAME(playerUUID);
										Player player = Bukkit.getPlayer(playername);
										if(player != null && player != p) {
											player.sendMessage(COMMAND_Bank.prefix + "§7Der Spieler §3" + p.getName() + " §7hat gerade §3" + amount + COMMAND_Eco.moneyformat + " §7von dem Konto §e" + EconomyMenu.saveBank.get(p) + " §7abgehoben.");
										}
									}
									
								}else {
									p.sendMessage(COMMAND_Bank.prefix + "§cDas Bankkonto hat nicht genug Geld!");
								}
								
							}else if(Economy.getAccountNames(p.getUniqueId().toString()).size() == 0) {
								p.sendMessage(COMMAND_Bank.prefix + "§cDu bist an keinem Konto beteiligt!");
							}else {
								
								bankschildamount.put(p, amount);
								p.sendMessage("");
								p.sendMessage("§7Wähle das Konto aus, von dem du abheben möchtest:");
								for(String bankacc : Economy.getAccountNames(p.getUniqueId().toString())) {
									TextComponent tc = new TextComponent();
									TextComponent tcc = new TextComponent("Hey wahatasd asd");
									tc.setText("§7- §3"+bankacc);
									tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Klicken!").create()));
									tc.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/bank ab67j " + bankacc));
									p.spigot().sendMessage(tc);
								}
								
							}
							
							
						}
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		if(e.getBlock().getState() instanceof Sign) {
			Sign sign = (Sign) e.getBlock().getState();
			if(sign.getLine(0).equals("§8[§3B§bank§8]")) {
				if(e.getPlayer().isSneaking() && e.getPlayer().hasPermission("server.bank.admin")) {
					e.getPlayer().sendMessage(COMMAND_Bank.prefix + "§7Du hast das Bank-Schild zerstört!");
				}else{
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("server.bank.admin")) {
			
			if(e.getLine(0).equalsIgnoreCase("Bankschalter")) {
				
				e.setLine(0, "-----*-----");
				e.setLine(1, "§3B§bank§eschalter");
				e.setLine(2, " §6-> Enter <- ");
				e.setLine(3, "-----*-----");
				
			}
			
			if(e.getLine(0).equalsIgnoreCase("[Bank]") || e.getLine(0).equalsIgnoreCase("Bank")) {
				if(e.getLine(1).equalsIgnoreCase("einzahlen")) {
					String amount = e.getLine(2);
					if(amount.matches("[0-9]+")) {
						
						e.setLine(0, "§8[§3B§bank§8]");
						e.setLine(1, "§3einzahlen");
						e.setLine(2, "§e"+amount);
						e.setLine(3, "§7---*---");
						
					}else {
						p.sendMessage(COMMAND_Bank.prefix + "§cBitte eine Zahl angeben.");
						e.getBlock().breakNaturally();
					}
					
				}else if(e.getLine(1).equalsIgnoreCase("auszahlen")) {
					String amount = e.getLine(2);
					if(amount.matches("[0-9]+")) {
						
						e.setLine(0, "§8[§3B§bank§8]");
						e.setLine(1, "§3auszahlen");
						e.setLine(2, "§e"+amount);
						e.setLine(3, "§7---*---");
						
					}else {
						p.sendMessage(COMMAND_Bank.prefix + "§cBitte eine Zahl angeben.");
						e.getBlock().breakNaturally();
					}
				}
			}
			
		}
		
	}
	
}
