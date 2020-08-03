package de.koppy.UUIDFetcher;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.koppy.handler.ProfileHandler;
import de.koppy.project.Main;

public class EVENT_UUID implements Listener{
	
	private Main main;
	
	public EVENT_UUID(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		API_UUID.updatePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		ProfileHandler.saveLastOnlineDate(e.getPlayer().getUniqueId().toString());
	}
}
