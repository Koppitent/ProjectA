package de.koppy.job;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.koppy.eco.Economy;
import de.koppy.project.Main;

public class EVENT_Graber implements Listener{

	private Main main;
	public EVENT_Graber(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Material> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> dirtgrassgraben = new HashMap<>();
	
	public static Integer dirtgrassgrabenBEN = 100;
	public static Integer dirtgrassgrabenXPget = 1;
	public static Double dirtgrassgrabenMoney = 0.1;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.GRABER) {
		if(!p.getWorld().getName().equals("world") || p.hasPermission("server.admin.test")) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) {
				
				if(e.getBlock().getType() == Material.GRASS_BLOCK || e.getBlock().getType() == Material.DIRT) {
					Integer farmint=0;
					if(dirtgrassgraben.get(p) != null) farmint = dirtgrassgraben.get(p);
					farmint++;
					if(farmint == dirtgrassgrabenBEN) {
						farmint=0;
						Jobs.addXP(p, dirtgrassgrabenXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), dirtgrassgrabenMoney*Jobs.level.get(p));
					}
					dirtgrassgraben.put(p, farmint);
					
				}
			}
		}		
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		blocklistofjob.add(Material.DIRT);
		blocklistofjob.add(Material.GRASS_BLOCK);
		if(blocklistofjob.contains(e.getBlock().getType())) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) Jobs.blockplaced.add(e.getBlock().getLocation());
		}
	}
	
}