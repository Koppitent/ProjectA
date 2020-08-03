package de.koppy.chestshop;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.koppy.eco.COMMAND_Eco;
import de.koppy.eco.Economy;
import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENT_Adminshop implements Listener{

	private Main main;
	public EVENT_Adminshop(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	
	public static HashMap<Player, Double> lastminus = new HashMap<>();
	
	DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent e) {
		if(e.getBlock().getState() instanceof Sign) {
			Sign sign = (Sign) e.getBlock().getState();
			if(sign.getLine(0).equals("§8[§4A§cdminshop§8]")) {
				if(e.getPlayer().hasPermission("server.adminshop")) {
					if(e.getPlayer().isSneaking()) {
						e.getPlayer().sendMessage(EVENT_ChestShop.prefix + "§7Das Adminshop-Schild wurde zerstört!");
					}else {
						e.setCancelled(true);
						e.getPlayer().sendMessage(EVENT_ChestShop.prefix + "§cDu musst sneaken um das Schild zerstören zu können.");
					}
				}else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(EVENT_ChestShop.prefix + "§cDu darfst dieses Schild nicht zerstören!");
				}
			}
		}
	}
	
	@EventHandler
	public void onSignIneract(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null) {
			if(e.getClickedBlock().getState() instanceof Sign) {
				Player p = e.getPlayer();
				Sign sign = (Sign) e.getClickedBlock().getState();
				
				if(sign.getLine(0).equals("§8[§4A§cdminshop§8]")) {
					
					Integer amount = Integer.valueOf(sign.getLine(1).split(" ")[0].replace("§7", ""));
					Double price = Double.valueOf(sign.getLine(2).split(" ")[1].replace(".", "").replace(",", ".").replace("§e", ""));
					String material = sign.getLine(3).toUpperCase();
					Material m = Material.valueOf(material);
					if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						
						if(Economy.hasEnoughMoney(p.getUniqueId().toString(), price)) {
							if(isInvFullAfter(p, amount) == false) {
							
							ItemStack istack = new ItemStack(m, amount);
							p.getInventory().addItem(istack);
							Economy.removeMoney(p.getUniqueId().toString(), price, "gezahlt an AdminShop für " + amount +"x " + m.toString());
							
							if(lastminus.containsKey(p)) {
								Double amounts = lastminus.get(p);
								price += amounts;
							}
							lastminus.put(p, price);
							
							final double preis = price;
							Bukkit.getScheduler().runTaskLater(main, new Runnable() {
								@Override
								public void run() {
									if(lastminus.get(p) == preis) {
										lastminus.remove(p);
									}
								}
							}, 20);
							
							String fprice = f.format(price);
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c-"+fprice+COMMAND_Eco.moneyformat));
							
							}else {
								p.sendMessage(EVENT_ChestShop.prefix + "§cDein Inventar ist zu voll um etwas kaufen zu können!");
							}
						}else {
							p.sendMessage(EVENT_ChestShop.prefix + "§cDu hast nicht genug Geld dafür!");
						}
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSignPlace(SignChangeEvent e) {
		if(e.getPlayer().hasPermission("server.adminshop")) {
			Player p = e.getPlayer();
			if(e.getLine(0).equalsIgnoreCase("[AdminShop]")) {
				if(e.getLine(1).matches("[0-9]+")) {
					if(e.getLine(2).matches("[0.0-9.0]+")) {
						if(getMaterials().contains(e.getLine(3).toUpperCase())) {
							
							Integer amount = Integer.valueOf(e.getLine(1));
							Double price = Double.valueOf(e.getLine(2));
							String fprice = f.format(price);
							String material = e.getLine(3);
							
							e.setLine(0, "§8[§4A§cdminshop§8]");
							e.setLine(1, "§7"+amount+" Stück");
							e.setLine(2, "§3Preis:§e "+fprice+COMMAND_Eco.moneyformat);
							e.setLine(3, material.toLowerCase());
							p.sendMessage(EVENT_ChestShop.prefix + "§7Das AdminShop-Schild wurde §aerfolgreich §7erstellt.");
							
						}else {
							e.getBlock().breakNaturally();
							p.sendMessage(EVENT_ChestShop.prefix + "§cDu einen Block angeben!"); 
						}
					}else {
						e.getBlock().breakNaturally();
						p.sendMessage(EVENT_ChestShop.prefix + "§cDu musst eine Double als Zahl eingeben!"); 
					}
				}else {
					e.getBlock().breakNaturally();
					p.sendMessage(EVENT_ChestShop.prefix + "§cDu musst einen Integer eingeben!"); 
				}
			}
		}
	}
	
	public static boolean isInvFullAfter(Player p, Integer amountthatiscoming) {
		Integer havetobeemtpy = amountthatiscoming/64;
		if(amountthatiscoming%64 != 0) havetobeemtpy++;
		Integer emptyslots=0;
		for(int i=0; i<p.getInventory().getSize()-5; i++) if(p.getInventory().getItem(i) == null) emptyslots++;
		if(emptyslots >= havetobeemtpy) {
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean isInvFullAfter(Inventory inv, Integer amountthatiscoming) {
		Integer havetobeemtpy = amountthatiscoming/64;
		if(amountthatiscoming%64 != 0) havetobeemtpy++;
		Integer emptyslots=0;
		for(int i=0; i<inv.getSize(); i++) if(inv.getItem(i) == null) emptyslots++;
		if(emptyslots >= havetobeemtpy) {
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean isInvFull(Inventory inv) {
		if(inv.firstEmpty() == -1) return true;
		else return false;
	}
	
	public static List<String> getMaterials(){
		List<String> materials = new ArrayList<String>();
		for(Material m : Material.values()) {
			materials.add(m.toString());
		}
		return materials;
	}
	
	
}
