package de.koppy.missions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.landsystem.COMMAND_Land;

public class Explorer {
	
	public static List<String> getAllRegions(){
		List<String> regions = new ArrayList<String>();
		for(Regions region : Regions.values()) regions.add(region.toString());
		return regions;
	}
	
	public static Regions getRegion(Location loc) {
		File fileSpawn = new File("plugins/Server/SaveLocations", "locations.yml");
		FileConfiguration cfgSpawn = YamlConfiguration.loadConfiguration(fileSpawn);
		Location spawnpos1 = cfgSpawn.getLocation("SPAWN" + ".location1");
		Location spawnpos2 = cfgSpawn.getLocation("SPAWN" + ".location2");
		if(loc.getWorld().getName().equals("world")) {
			if(RegionHandler.isIn(loc, spawnpos1, spawnpos2)) {
				return Regions.Hauptstadt;
			}else if(RegionHandler.isIn(loc, COMMAND_Land.pos1, COMMAND_Land.pos2)) {
				return Regions.Grundstückswelt;
			}else {
				for(Regions region : Regions.values()){
					if(region != Regions.Farmwelten) if(RegionHandler.isInRegion(loc, region)) return region;
				}
				return Regions.Wilderness;
			}
		}else {
			return Regions.Farmwelten;
		}
	}
	
	public static void addRegion(Player p, Regions region){
		File file = new File("plugins/Missions/Regions", "user.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			List<String> regions = new ArrayList<>();
			if(cfg.getStringList(p.getUniqueId().toString() + ".regions") != null) regions.addAll(cfg.getStringList(p.getUniqueId().toString() + ".regions"));
			if(regions.contains(region.toString()) == false) regions.add(region.toString());
			cfg.set(p.getUniqueId().toString() + ".regions", regions);
			RegionHandler.saveFile(file, cfg);
	}
	
	public static boolean wasInRegion(Player p, Regions region){
		File file = new File("plugins/Missions/Regions", "user.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.getStringList(p.getUniqueId().toString() + ".regions") != null) {
			List<String> regions = cfg.getStringList(p.getUniqueId().toString() + ".regions");
			if(regions.contains(region.toString())) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}
	
}