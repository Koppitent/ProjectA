package de.koppy.eco;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.koppy.MySQL.MySQL;
import de.koppy.MySQL.MySQL_SaveEco;
import de.koppy.project.Main;

public class EVENT_Eco implements Listener{
	
	private Main main;
	public EVENT_Eco(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(Main.activateMySQL == true && Main.activateEconomy == true) {
			if(MySQL.isConnected()){
				Economy.setMoneyWithoutReason(p.getUniqueId().toString(), MySQL_SaveEco.getAmount(p.getUniqueId()));
			}else {
				Bukkit.getServer().getConsoleSender().sendMessage("§8|  §4MySQL-Connection not found!");
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(Main.activateMySQL == true && Main.activateEconomy == true) {
			if(MySQL.isConnected()){
				MySQL_SaveEco.setAmount(p.getUniqueId().toString(), Economy.getMoney(p.getUniqueId().toString()));
			}else {
				Bukkit.getServer().getConsoleSender().sendMessage("§8|  §4MySQL-Connection not found!");
			}
		}
	}
	
	
}
