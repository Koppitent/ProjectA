package de.koppy.job;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import de.koppy.eco.Economy;
import de.koppy.project.Main;

public class EVENT_Erbauer implements Listener{
	
	private Main main;
	public EVENT_Erbauer(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Location> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> bauenbauen = new HashMap<>();
	
	public static Integer bauenbauenBEN = 500;
	public static Integer bauenbauenXPget = 3;
	public static Double bauenbauenMoney = 0.2;
	
	@EventHandler
	public void onBlockBreak(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.ERBAUER) {
		if(p.getWorld().getName().equals("world")) {
			if(!blocklistofjob.contains(e.getBlock().getLocation())) {
				
				Integer farmint=0;
				if(bauenbauen.get(p) != null) farmint = bauenbauen.get(p);
				farmint++;
				if(farmint == bauenbauenBEN) {
					farmint=0;
					Jobs.addXP(p, bauenbauenXPget);
					Economy.addMoneyWithoutReason(p.getUniqueId().toString(), bauenbauenMoney*Jobs.level.get(p));
				}
				bauenbauen.put(p, farmint);
				
			}else {
				blocklistofjob.remove(e.getBlock().getLocation());
			}
		}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(Jobs.getJob(e.getPlayer().getUniqueId().toString()) == Job.ERBAUER) blocklistofjob.add(e.getBlock().getLocation());
	}
	
}