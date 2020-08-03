package de.koppy.project;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.koppy.handler.InfoHandler;

public class EVENT_Savepoints implements Listener{
	
	private Main main;
	public EVENT_Savepoints(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onSave(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			File file = new File("plugins/Server/SaveLocations", "locations.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			Location farmpos1 = cfg.getLocation("FARMWELT" + ".location1");
			Location farmpos2 = cfg.getLocation("FARMWELT" + ".location2");
			Location netherpos1 = cfg.getLocation("NETHER" + ".location1");
			Location netherpos2 = cfg.getLocation("NETHER" + ".location2");
			Location spawnpos1 = cfg.getLocation("SPAWN" + ".location1");
			Location spawnpos2 = cfg.getLocation("SPAWN" + ".location2");
			if(InfoHandler.isIn(e.getEntity().getLocation(), farmpos1, farmpos2)) {
				e.setCancelled(true);
			}else if(InfoHandler.isIn(e.getEntity().getLocation(), netherpos1, netherpos2)) {
				e.setCancelled(true);
			}else if(InfoHandler.isIn(e.getEntity().getLocation(), spawnpos1, spawnpos2)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSaveInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("server.admin")) {
		File file = new File("plugins/Server/SaveLocations", "locations.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Location farmpos1 = cfg.getLocation("FARMWELT" + ".location1");
		Location farmpos2 = cfg.getLocation("FARMWELT" + ".location2");
		Location netherpos1 = cfg.getLocation("NETHER" + ".location1");
		Location netherpos2 = cfg.getLocation("NETHER" + ".location2");
		Location spawnpos1 = cfg.getLocation("SPAWN" + ".location1");
		Location spawnpos2 = cfg.getLocation("SPAWN" + ".location2");
		if(InfoHandler.isIn(p.getLocation(), farmpos1, farmpos2)) {
			e.setCancelled(true);
		}else if(InfoHandler.isIn(p.getLocation(), netherpos1, netherpos2)) {
			e.setCancelled(true);
		}else if(InfoHandler.isIn(p.getLocation(), spawnpos1, spawnpos2)) {
			e.setCancelled(true);
		}
		}
	}
	
}
