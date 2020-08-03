package de.koppy.job;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import de.koppy.eco.Economy;
import de.koppy.project.Main;

public class EVENT_Schmied implements Listener{

	private Main main;
	public EVENT_Schmied(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	public static ArrayList<Material> blocklistofjob = new ArrayList<>();
	
	public static HashMap<Player, Integer> enchantitem = new HashMap<>();
	
	public static Integer enchantitemBEN = 20;
	public static Integer enchantitemXPget = 1;
	public static Double enchantitemMoney = 0.1;
	
	@EventHandler
	public void onBlockBreak(EnchantItemEvent e) {
		Player p = e.getEnchanter();
		if(Jobs.getJob(p.getUniqueId().toString()) == Job.SCHMIED) {
		if(!p.getWorld().getName().equals("world") || p.hasPermission("server.admin.test")) {
					
					Integer farmint=0;
					if(enchantitem.get(p) != null) farmint = enchantitem.get(p);
					farmint++;
					if(farmint == enchantitemBEN) {
						farmint=0;
						Jobs.addXP(p, enchantitemXPget);
						Economy.addMoneyWithoutReason(p.getUniqueId().toString(), enchantitemMoney*Jobs.level.get(p));
					}
					enchantitem.put(p, farmint);
					
		}
		}
	}
	
}