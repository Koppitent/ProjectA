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

public class EVENT_Netherholzfaeller implements Listener{

	private Main main;
	public EVENT_Netherholzfaeller(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Material> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> warpedcrimsonstem = new HashMap<>();
	
	public static Integer warpedcrimsonstemBEN = 20;
	public static Integer warpedcrimsonstemXPget = 1;
	public static Double warpedcrimsonstemMoney = 0.1;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.NETHERHOLZFAELLER) {
		if(!p.getWorld().getName().equals("world") || p.hasPermission("server.admin.test")) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) {
				
				if(e.getBlock().getType() == Material.WARPED_STEM || e.getBlock().getType() == Material.CRIMSON_STEM) {
					Integer farmint=0;
					if(warpedcrimsonstem.get(p) != null) farmint = warpedcrimsonstem.get(p);
					farmint++;
					if(farmint == warpedcrimsonstemBEN) {
						farmint=0;
						Jobs.addXP(p, warpedcrimsonstemXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), warpedcrimsonstemMoney*Jobs.level.get(p));
					}
					warpedcrimsonstem.put(p, farmint);
					
				}
			}
		}		
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		blocklistofjob.add(Material.WARPED_STEM);
		blocklistofjob.add(Material.CRIMSON_STEM);
		if(blocklistofjob.contains(e.getBlock().getType())) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) Jobs.blockplaced.add(e.getBlock().getLocation());
		}
	}
	
}