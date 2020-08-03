package de.koppy.job;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENT_Job implements Listener{

	private Main main;
	public EVENT_Job(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	
	@EventHandler
	public void onJob(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Job job = Jobs.getJob(p.getUniqueId().toString());
		Jobs.setJobLevel(p, job);
		Jobs.setJobXP(p, job);
	}
	
	@EventHandler
	public void onJob(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		Job job = Jobs.getJob(p.getUniqueId().toString());
		Jobs.saveJobLevel(p, job);
		Jobs.saveJobXP(p, job);
	}
	
	@EventHandler
	public void onJob(BlockBreakEvent e) {
		if(Jobs.blockplaced.contains(e.getBlock().getLocation())) {
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					Jobs.blockplaced.remove(e.getBlock().getLocation());
					
				}
			}, 20);
		}
	}
	
	@EventHandler
	public void onJob(PlayerAddXPatJobEvent e) {
		Player p = e.getPlayer();
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a+"+e.getAmount()+ " XP"));
		Integer currentXP = Jobs.xp.get(p);
		Integer currentLEVEL = Jobs.level.get(p);
		
		if(currentLEVEL <= 14) {
			for(int i=1; i<16; i++) {
				if(currentLEVEL == i) {
					Integer xpneeded=Jobs.mainXP*i;
					if(currentXP+e.getAmount() >= xpneeded) {
						Integer restXP = currentXP+e.getAmount()-xpneeded;
						Jobs.xp.put(p, restXP);
						Jobs.addLevel(p, 1);
					}
					break;
				}
			}
		}
		
	}
	
	@EventHandler
	public void onJob(PlayerAddLevelatJobEvent e) {
		Player p = e.getPlayer();
		Integer currentLEVEL = Jobs.level.get(p)+e.getAmount();
		p.sendMessage(COMMAND_Job.prefix + "§7Du bist nun§3 " + Jobs.getJob(p.getUniqueId().toString()) + " §7auf §eLevel " + currentLEVEL + "§7.");
	}
	
	@EventHandler
	public void onJobInv(InventoryCloseEvent e) {
		if(JobMenu.jobinv.contains(e.getPlayer())) JobMenu.jobinv.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onJobInv(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(JobMenu.jobinv.contains(p)) {
			e.setCancelled(true);
			if(e.getCurrentItem() != null) {
				
				if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§cJob kündigen! §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("job leave");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3")) {
					String job = e.getCurrentItem().getItemMeta().getDisplayName().replace("§3", "");
					p.performCommand("job join "+job);
				}
				
			}
		}
	}
	
}
