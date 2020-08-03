package de.koppy.cases;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.koppy.project.Main;

public class EVENT_Case implements Listener{
	
	public Main main;
	public EVENT_Case(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onInteractWith(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) return;
		if(Case.inCaseInv.contains(e.getWhoClicked())) {
			e.setCancelled(true);
			
			ItemStack useKey = new ItemStack(Material.END_CRYSTAL);
			ItemMeta useKeym = useKey.getItemMeta();
			useKeym.setDisplayName("§3Kiste mit Key öffnen. §7§o(Rechtsklick)");
			useKey.setItemMeta(useKeym);
			if(e.getCurrentItem().isSimilar(useKey)) {
				Player p = (Player) e.getWhoClicked();
				//Case öffnen
				Cases cases = Cases.valueOf(Case.inCase.get(p).toUpperCase());
				if(Case.isKeyInInv(p, cases)) {
					
					p.setItemInHand(null);
					Case.removeKeyInInv(p, cases);
					p.closeInventory();
					Inventory inv = Case.openCase(p, cases);
					p.openInventory(inv);
					
				}else {
					p.closeInventory();
					p.sendMessage(COMMAND_Case.prefix + "§cDu hast kein Key dafür!");
				}
			}
		}
	}
	
	@EventHandler
	public void onInteractWith(InventoryCloseEvent e) {
		if(Case.inCaseInv.contains(e.getPlayer())) Case.inCaseInv.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onInteractWith(PlayerInteractEvent e) {
		
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			
			if(p.getItemInHand() != null) {
				if(p.getItemInHand().getType() == Material.WHITE_SHULKER_BOX) {
					if(Case.isCase(p.getItemInHand())) {
						e.setCancelled(true);
						//* Case Preview
							
							Inventory inv = Case.openCaseInv(p, Case.getCase(p.getItemInHand()));
							p.openInventory(inv);
							
						//* 		//
					}
				}else if(p.getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
					if(Case.isCase(p.getItemInHand())) {
						e.setCancelled(true);
						p.sendMessage(COMMAND_Case.prefix + "§7Du kannst Case-Keys nicht platzieren!");
					}
				}
				
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(Case.isCase(e.getItemDrop().getItemStack())){
			Player p = e.getPlayer();
			e.setCancelled(true);
			p.sendMessage(COMMAND_Case.prefix + "§7Du darfst Cases und Keys nocht droppen!");
		}
	}
	
}
