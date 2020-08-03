package de.koppy.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

public class WorldAPI {

	//* Welt erstellen
	        
	public static void addToLoadingWorld(String worldname) {
		
		File file = new File("plugins/Server/Worlds", "Worldlist.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		List<String> worldlist = cfg.getStringList("worldlist");
		
		worldlist.add(worldname);
		
		cfg.set("worldlist", worldlist);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void removeWorldFromList(String worldname) {
		
		File file = new File("plugins/Server/Worlds", "Worlds.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set(worldname, null);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void removeToLoadingWorld(String worldname) {
		
		File file = new File("plugins/Server/Worlds", "Worldlist.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		List<String> worldlist = cfg.getStringList("worldlist");
		
		worldlist.remove(worldname);
		
		cfg.set("worldlist", worldlist);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createWorld(String worldname) {
		
		WorldCreator wc = new WorldCreator(worldname);
		wc.type(WorldType.NORMAL);
		wc.environment(Environment.NORMAL);
		wc.createWorld();
		addToLoadingWorld(worldname);
		
	}
	
	public static void createWorld(String worldname, boolean structures) {
		
		WorldCreator wc = new WorldCreator(worldname);
		wc.type(WorldType.NORMAL);
		wc.environment(Environment.NORMAL);
		wc.generateStructures(structures);
		wc.createWorld();
		addToLoadingWorld(worldname);
		
	}
	
	public static void createVoidWorld(String worldname) {
		WorldCreator wc = new WorldCreator(worldname);

		wc.type(WorldType.FLAT);
		wc.generatorSettings("2;0;1;");
		
		wc.createWorld();
		
	}
	
	
	public static void createworld(String worldname, Environment environment, WorldType worldtype, boolean structures) {
		
		WorldCreator c = new WorldCreator(worldname);
		c.environment(environment);
		c.type(worldtype);
		c.generateStructures(structures);
		c.createWorld();
		addToLoadingWorld(worldname);
		
	}
	
	//* Welt löschen
	
	public static void removeWorld(String worldname) {
		
		World w = Bukkit.getWorld(worldname);
		
		if(w != null) {
			Bukkit.unloadWorld(w, false);
			try {
				FileUtils.deleteDirectory(new File(worldname));
				removeToLoadingWorld(worldname);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	//* Welt laden
	
	public static void loadWorld(String worldname) {
		
		World w = Bukkit.getWorld(worldname);
		
		if(w == null) {
			
			WorldCreator c = new WorldCreator(worldname);
			c.type(WorldType.NORMAL);
			c.environment(Environment.NORMAL);
			c.createWorld();
			
		}
	}
	
	public static void loadNetherWorld(String worldname) {
		World w = Bukkit.getWorld(worldname);
		
		if(w == null) {
			
			WorldCreator c = new WorldCreator(worldname);
			c.type(WorldType.NORMAL);
			c.environment(Environment.NETHER);
			c.createWorld();
			
		}
	}
	
	//* Welt kopieren
	public static void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
	
	
	
	
}
