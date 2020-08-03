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

public class EVENT_Miner implements Listener{

	private Main main;
	public EVENT_Miner(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Material> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> coalandnetherore = new HashMap<>();
	public static HashMap<Player, Integer> ironlapisgoldore = new HashMap<>();
	public static HashMap<Player, Integer> redstoneore = new HashMap<>();
	public static HashMap<Player, Integer> diamondemeraldore = new HashMap<>();
	
	public static Integer coalandnetheroreBEN = 50;
	public static Integer ironlapisgoldoreBEN = 20;
	public static Integer redstoneoreBEN = 20;
	public static Integer diamondemeraldoreBEN = 5;
	
	public static Integer coalandnetheroreXPget = 2;
	public static Integer ironlapisgoldoreXPget = 2;
	public static Integer redstoneoreXPget = 20;
	public static Integer diamondemeraldoreXPget = 5;
	
	public static Double coalandnetheroreMoney = 0.1;
	public static Double ironlapisgoldoreMoney = 0.2;
	public static Double redstoneoreMoney = 0.1;
	public static Double diamondemeraldoreMoney = 0.5;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.MINER) {
		if(!p.getWorld().getName().equals("world")) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) {
				
				if(e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.NETHER_QUARTZ_ORE || e.getBlock().getType() == Material.NETHER_GOLD_ORE) {
					Integer coalandnetheroreint=0;
					if(coalandnetherore.get(p) != null) coalandnetheroreint = coalandnetherore.get(p);
					coalandnetheroreint++;
					if(coalandnetheroreint == coalandnetheroreBEN) {
						coalandnetheroreint=0;
						Jobs.addXP(p, coalandnetheroreXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), coalandnetheroreMoney*Jobs.level.get(p));
					}
					coalandnetherore.put(p, coalandnetheroreint);
					
				}else if(e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.LAPIS_ORE || e.getBlock().getType() == Material.GOLD_ORE) {
					
					Integer oreint=0;
					if(ironlapisgoldore.get(p) != null) oreint = ironlapisgoldore.get(p);
					oreint++;
					if(oreint == ironlapisgoldoreBEN) {
						oreint=0;
						Jobs.addXP(p, ironlapisgoldoreXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), ironlapisgoldoreMoney*Jobs.level.get(p));
					}
					ironlapisgoldore.put(p, oreint);
					
				}else if(e.getBlock().getType() == Material.REDSTONE_ORE) {
					
					Integer oreint=0;
					if(redstoneore.get(p) != null) oreint = redstoneore.get(p);
					oreint++;
					if(oreint == redstoneoreBEN) {
						oreint=0;
						Jobs.addXP(p, redstoneoreXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), redstoneoreMoney*Jobs.level.get(p));
					}
					redstoneore.put(p, oreint);
					
				}else if(e.getBlock().getType() == Material.DIAMOND_ORE || e.getBlock().getType() == Material.EMERALD_ORE) {
					
					Integer oreint=0;
					if(diamondemeraldore.get(p) != null) oreint = diamondemeraldore.get(p);
					oreint++;
					if(oreint == diamondemeraldoreBEN) {
						oreint=0;
						Jobs.addXP(p, diamondemeraldoreXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), diamondemeraldoreMoney*Jobs.level.get(p));
					}
					diamondemeraldore.put(p, oreint);
					
				}
				
			}
		}		
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		blocklistofjob.add(Material.NETHER_GOLD_ORE);
		blocklistofjob.add(Material.NETHER_QUARTZ_ORE);
		blocklistofjob.add(Material.COAL_ORE);
		blocklistofjob.add(Material.IRON_ORE);
		blocklistofjob.add(Material.GOLD_ORE);
		blocklistofjob.add(Material.LAPIS_ORE);
		blocklistofjob.add(Material.REDSTONE_ORE);
		blocklistofjob.add(Material.DIAMOND_ORE);
		blocklistofjob.add(Material.EMERALD_ORE);
		if(blocklistofjob.contains(e.getBlock().getType())) {
			if(!Jobs.blockplaced.contains(e.getBlock().getLocation())) Jobs.blockplaced.add(e.getBlock().getLocation());
		}
	}
	
}
