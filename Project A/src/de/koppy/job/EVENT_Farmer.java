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

public class EVENT_Farmer implements Listener{

	private Main main;
	public EVENT_Farmer(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Material> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> potatocarrotwheatbeetrootfarm = new HashMap<>();
	public static HashMap<Player, Integer> melonpumpkinfarm = new HashMap<>();
	
	public static Integer potatocarrotwheatbeetrootfarmBEN = 25;
	public static Integer melonpumpkinfarmBEN = 15;
	
	public static Integer potatocarrotwheatbeetrootfarmXPget = 2;
	public static Integer melonpumpkinfarmXPget = 1;
	
	public static Double potatocarrotwheatbeetrootfarmMoney = 0.1;
	public static Double melonpumpkinfarmMoney = 0.1;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.FARMER) {
		if(!p.getWorld().getName().equals("world") || p.hasPermission("server.admin.test")) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) {
				
				if(e.getBlock().getType() == Material.POTATOES || e.getBlock().getType() == Material.CARROTS || e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.BEETROOTS) {
					Integer farmint=0;
					if(potatocarrotwheatbeetrootfarm.get(p) != null) farmint = potatocarrotwheatbeetrootfarm.get(p);
					farmint++;
					if(farmint == potatocarrotwheatbeetrootfarmBEN) {
						farmint=0;
						Jobs.addXP(p, potatocarrotwheatbeetrootfarmXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), potatocarrotwheatbeetrootfarmMoney*Jobs.level.get(p));
					}
					potatocarrotwheatbeetrootfarm.put(p, farmint);
					
				}else if(e.getBlock().getType() == Material.MELON || e.getBlock().getType() == Material.PUMPKIN) {
					
					Integer farmint=0;
					if(melonpumpkinfarm.get(p) != null) farmint = melonpumpkinfarm.get(p);
					farmint++;
					if(farmint == melonpumpkinfarmBEN) {
						farmint=0;
						Jobs.addXP(p, melonpumpkinfarmXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), melonpumpkinfarmMoney*Jobs.level.get(p));
					}
					melonpumpkinfarm.put(p, farmint);
					
				}
				
			}
		}		
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		blocklistofjob.add(Material.POTATOES);
		blocklistofjob.add(Material.CARROTS);
		blocklistofjob.add(Material.BEETROOTS);
		blocklistofjob.add(Material.WHEAT);
		blocklistofjob.add(Material.MELON);
		blocklistofjob.add(Material.PUMPKIN);
		if(blocklistofjob.contains(e.getBlock().getType())) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) Jobs.blockplaced.add(e.getBlock().getLocation());
		}
	}
	
	
}
