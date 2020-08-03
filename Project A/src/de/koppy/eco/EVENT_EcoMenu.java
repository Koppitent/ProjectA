package de.koppy.eco;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENT_EcoMenu implements Listener{
	
	private Main main;
	public EVENT_EcoMenu(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
	
	@EventHandler
	public void onInvClick(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(EVENT_Bank.isSchildbesetzt.containsKey(p)) {
			Location loc = EVENT_Bank.isSchildbesetzt.get(p);
			EVENT_Bank.isSchildbesetzt.remove(p, loc);
		}
		
		if(EconomyMenu.lastseite.containsKey(p)) {
			if(EconomyMenu.lastseite.get(p).equals("menu")) {
				String seite = EconomyMenu.lastseite.get(p);
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						Inventory inv = EconomyMenu.openBankMenu(p, "§3B§bank§7-Schalter");
						p.openInventory(inv);
					}
				}, 1);
				EconomyMenu.lastseite.remove(p, seite);
			}else {
				String bankacc = EconomyMenu.lastseite.get(p);
				if(EconomyMenu.saveBank.get(p).equals(bankacc)) {
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						
						@Override
						public void run() {
							Inventory inv = EconomyMenu.openBankMenu(p, "§3B§bank§7-Schalter");
							EconomyMenu.setInvofBankacc(p, inv, bankacc);
							p.openInventory(inv);
						}
					}, 1);
				}
				EconomyMenu.lastseite.remove(p, bankacc);
			}
			
		}
		
		if(EconomyMenu.isinMenu.contains(p)) {
			EconomyMenu.isinMenu.remove(p);
		}
		if(COMMAND_Bank.inbankautomat.contains(p)) {
			COMMAND_Bank.inbankautomat.remove(p);
		}
		if(EconomyMenu.saveBank.containsKey(p)) {
			String bank = EconomyMenu.saveBank.get(p);
			EconomyMenu.saveBank.remove(p, bank);
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClick() != null) {
			if(e.getCurrentItem() != null) {
				Player p = (Player) e.getWhoClicked();
				if(EconomyMenu.isinMenu.contains(p)) {
					if(COMMAND_Bank.inbankautomat.contains(p)) {
						
						if(e.getCurrentItem().getType() == Material.PAPER) {
							if(COMMAND_Bank.isInBank(p)) {
								if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7» §3Log #") == false) {
									String bank[] = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
									String bankacc = bank[1].replace("§3", "");
									EconomyMenu.setInvofBankacc(p, e.getInventory(), bankacc);
								}
							}else {
								p.sendMessage(COMMAND_Bank.prefix + "§cDu musst in der Bank sein.");
							}
						}else if(e.getCurrentItem().getType() == Material.COAL_BLOCK) {
							
							String amountsplit[] = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
							String amounts = amountsplit[0].replace("§6", "").replace(".", "");
							double amount = Double.valueOf(amounts);
							
							if(Economy.hasEnoughMoneyOnBank(EconomyMenu.saveBank.get(p), amount)) {
								String famount = f.format(amount);
								Economy.removeBankBalance(EconomyMenu.saveBank.get(p), amount, "auszahlung an "+p.getName());
								Economy.addMoney(p.getUniqueId().toString(), amount, "auszahlung von Konto " + EconomyMenu.saveBank.get(p));
								p.sendMessage(COMMAND_Bank.prefix + "§7Du hast §3" + famount + COMMAND_Eco.moneyformat + " von dem Konto §e" + EconomyMenu.saveBank.get(p) + " §7abgehoben.");
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+famount+COMMAND_Eco.moneyformat));
								for(String playerUUID : Economy.getMembers(EconomyMenu.saveBank.get(p))) {
									String playername = API_UUID.getUUIDorNAME(playerUUID);
									Player player = Bukkit.getPlayer(playername);
									if(player != null && player != p) {
										player.sendMessage(COMMAND_Bank.prefix + "§7Der Spieler §3" + p.getName() + " §7hat gerade §3" + famount + COMMAND_Eco.moneyformat + " §7von dem Konto §e" + EconomyMenu.saveBank.get(p) + " §7abgehoben.");
									}
								}
								EconomyMenu.setInvofBankacc(p, e.getInventory(), EconomyMenu.saveBank.get(p));
								
							}else {
								p.sendMessage(COMMAND_Bank.prefix + "§cDu hast nicht genug Geld auf der Bank.");
							}
							
						}else if(e.getCurrentItem().getType() == Material.GOLD_BLOCK) {
							
							String amountsplit[] = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
							String amounts = amountsplit[0].replace("§6", "").replace(".", "");
							double amount = Double.valueOf(amounts);
							
							if(Economy.hasEnoughMoney(p.getUniqueId().toString(), amount)) {
								
								String famount = f.format(amount);
								Economy.addBankBalance(EconomyMenu.saveBank.get(p), amount, "einzahlung von "+p.getName());
								Economy.removeMoney(p.getUniqueId().toString(), amount, "einzahlung auf Konto " + EconomyMenu.saveBank.get(p));
								p.sendMessage(COMMAND_Bank.prefix + "§7Du hast §3" + famount + COMMAND_Eco.moneyformat + " auf das Konto §e" + EconomyMenu.saveBank.get(p) + " §7eingezahlt.");
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c-"+famount+COMMAND_Eco.moneyformat));
								EconomyMenu.setInvofBankacc(p, e.getInventory(), EconomyMenu.saveBank.get(p));
								for(String playerUUID : Economy.getMembers(EconomyMenu.saveBank.get(p))) {
									String playername = API_UUID.getUUIDorNAME(playerUUID);
									Player player = Bukkit.getPlayer(playername);
									if(player != null && player != p) {
										player.sendMessage(COMMAND_Bank.prefix + "§7Der Spieler §3" + p.getName() + " §7hat gerade §3" + famount + COMMAND_Eco.moneyformat + " §7auf das Konto §e" + EconomyMenu.saveBank.get(p) + " §7eingezahlt.");
									}
								}
								
							}else {
								p.sendMessage(COMMAND_Bank.prefix + "§cDu hast nicht genug Geld dabei.");
							}
							
						}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §eMitglieder")) {
							
							EconomyMenu.setInvofMemberList(p, e.getInventory(), EconomyMenu.saveBank.get(p));
							
						}
					}
					
					e.setCancelled(true);
					
					ItemStack GeldVis = new ItemStack(Material.NAME_TAG);
					ItemMeta GeldVisM = GeldVis.getItemMeta();
					GeldVisM.setDisplayName("§7» §5Geld-Sichtbarkeit");
					List<String> GeldVisLore = new ArrayList<>();
					if(Economy.isPublic(p.getUniqueId().toString())) GeldVisLore.add("§aöffentlich");
					else GeldVisLore.add("§cprivat");
					GeldVisM.setLore(GeldVisLore);
					GeldVis.setItemMeta(GeldVisM);
					
					if(e.getCurrentItem().isSimilar(GeldVis)) {
						p.performCommand("moneytoggle");
						EconomyMenu.setEcoInv(p, e.getInventory());
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §eKontoauszüge")) {
						
						String bankacc = EconomyMenu.saveBank.get(p);
						EconomyMenu.setBankLogInv(p, e.getInventory(), bankacc);
						
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §5Enderchest §7§o(Rechtsklick)")) {
						
						p.openInventory(p.getEnderChest());
						p.sendMessage(Main.prefix + "§7Du hast die §5Enderchest §ageöffnet§7.");
						
					}
					
					if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §cZurück zu Economy-Menü")) {
						p.closeInventory();
						Inventory inv = EconomyMenu.openEcoMenu(p, COMMAND_Eco.prefix.replace("[", "").replace("]", "") + "§7Menu");
						p.openInventory(inv);
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §eBank-Info")) {
						
						p.closeInventory();
						p.performCommand("bank");
						
					}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Zur Bank")) {
						if(COMMAND_Bank.isInBank(p)) {
							p.closeInventory();
							Inventory inv = EconomyMenu.openBankMenu(p, "§3B§bank§7-Schalter");
							p.openInventory(inv);
						}else{
							p.sendMessage(COMMAND_Bank.prefix + "§cDu musst dafür in der Bank sein!");
						}
						
					}
					
				}
			}
		}
	}
	
	
}
