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

public class EVENT_Holzfaeller implements Listener{

	private Main main;
	public EVENT_Holzfaeller(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Material> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> oakbirchlog = new HashMap<>();
	public static HashMap<Player, Integer> spruceacacialog = new HashMap<>();
	public static HashMap<Player, Integer> jungledarklog = new HashMap<>();
	
	public static Integer oakbirchlogBEN = 32;
	public static Integer spruceacacialogBEN = 32;
	public static Integer jungledarklogBEN = 32;
	
	public static Integer oakbirchlogXPget = 1;
	public static Integer spruceacacialogXPget = 2;
	public static Integer jungledarklogXPget = 1;
	
	public static Double oakbirchlogMoney = 0.1;
	public static Double spruceacacialogMoney = 0.2;
	public static Double jungledarklogMoney = 0.15;

	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.HOLZFAELLER) {
		if(!p.getWorld().getName().equals("world") || p.hasPermission("server.admin.test")) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) {
				
				if(e.getBlock().getType() == Material.BIRCH_LOG || e.getBlock().getType() == Material.OAK_LOG) {
					Integer farmint=0;
					if(oakbirchlog.get(p) != null) farmint = oakbirchlog.get(p);
					farmint++;
					if(farmint == oakbirchlogBEN) {
						farmint=0;
						Jobs.addXP(p, oakbirchlogXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), oakbirchlogMoney*Jobs.level.get(p));
					}
					oakbirchlog.put(p, farmint);
					
				}else if(e.getBlock().getType() == Material.JUNGLE_LOG || e.getBlock().getType() == Material.DARK_OAK_LOG) {
					
					Integer farmint=0;
					if(jungledarklog.get(p) != null) farmint = jungledarklog.get(p);
					farmint++;
					if(farmint == jungledarklogBEN) {
						farmint=0;
						Jobs.addXP(p, jungledarklogXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), jungledarklogMoney*Jobs.level.get(p));
					}
					jungledarklog.put(p, farmint);
					
				}else if(e.getBlock().getType() == Material.SPRUCE_LOG || e.getBlock().getType() == Material.ACACIA_LOG) {
					
					Integer farmint=0;
					if(spruceacacialog.get(p) != null) farmint = spruceacacialog.get(p);
					farmint++;
					if(farmint == spruceacacialogBEN) {
						farmint=0;
						Jobs.addXP(p, spruceacacialogXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), spruceacacialogMoney*Jobs.level.get(p));
					}
					spruceacacialog.put(p, farmint);
					
				}
				
			}
		}		
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		blocklistofjob.add(Material.ACACIA_LOG);
		blocklistofjob.add(Material.BIRCH_LOG);
		blocklistofjob.add(Material.DARK_OAK_LOG);
		blocklistofjob.add(Material.JUNGLE_LOG);
		blocklistofjob.add(Material.SPRUCE_LOG);
		blocklistofjob.add(Material.OAK_LOG);
		if(blocklistofjob.contains(e.getBlock().getType())) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) Jobs.blockplaced.add(e.getBlock().getLocation());
		}
	}
	
	
}
