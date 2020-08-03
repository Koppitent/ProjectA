package de.koppy.missions;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RegionHandler {
	
	public static String getIPAdress(Player p) {
		return p.getAddress().getAddress().getHostAddress();
	}
	
	public static void saveFile(File file, FileConfiguration cfg) {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isIn(Location loc, Location pos1, Location pos2) {
		
		Double maxX = (pos1.getX() > pos2.getX() ? pos1.getX() : pos2.getX());
		Double minX = (pos1.getX() < pos2.getX() ? pos1.getX() : pos2.getX());
		Double maxY = (pos1.getY() > pos2.getY() ? pos1.getY() : pos2.getY());
		Double minY = (pos1.getY() < pos2.getY() ? pos1.getY() : pos2.getY());
		Double maxZ = (pos1.getZ() > pos2.getZ() ? pos1.getZ() : pos2.getZ());
		Double minZ = (pos1.getZ() < pos2.getZ() ? pos1.getZ() : pos2.getZ());
		
		if(loc.getX() <= maxX && loc.getX() >= minX) {
			if(loc.getY() <= maxY && loc.getY() >= minY) {
				if(loc.getZ() <= maxZ && loc.getZ() >= minZ) {
					
					return true;
					
				}
			}
		}
		return false;
	}
	
	public static boolean isInRegion(Location loc, Regions region) {
		
		File file = new File("plugins/Mission", "locations.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.getLocation(region.toString() + ".location1") != null && cfg.getLocation(region.toString() + ".location2") != null) {
			Location pos1 = cfg.getLocation(region.toString() + ".location1");
			Location pos2 = cfg.getLocation(region.toString() + ".location2");
			
			Double maxX = (pos1.getX() > pos2.getX() ? pos1.getX() : pos2.getX());
			Double minX = (pos1.getX() < pos2.getX() ? pos1.getX() : pos2.getX());
			Double maxY = (pos1.getY() > pos2.getY() ? pos1.getY() : pos2.getY());
			Double minY = (pos1.getY() < pos2.getY() ? pos1.getY() : pos2.getY());
			Double maxZ = (pos1.getZ() > pos2.getZ() ? pos1.getZ() : pos2.getZ());
			Double minZ = (pos1.getZ() < pos2.getZ() ? pos1.getZ() : pos2.getZ());
			
			if(loc.getX() <= maxX && loc.getX() >= minX) {
				if(loc.getY() <= maxY && loc.getY() >= minY) {
					if(loc.getZ() <= maxZ && loc.getZ() >= minZ) {
						
						return true;
						
					}
				}
			}
		}
		return false;
	}
	
}
