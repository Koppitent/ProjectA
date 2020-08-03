package de.koppy.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.chestshop.EVENT_ChestShop;
import de.koppy.handler.InfoHandler;

public class ChunkEditor {
	
	public static HashMap<String, BlockData> savedChunk = new HashMap<>();
	
	public static void pasteChunk(Chunk c, HashMap<String, BlockData> toCopy) {
		clearChunkEmtpy(c);
		
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
						if(toCopy.get(x + ";" + y + ";" + z) != null) {
							Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setBlockData(toCopy.get(x + ";" + y + ";" + z));
						}else {
							Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setType(Material.AIR);
						}
					}
				}
			}
		
	}
	
	public static Location translateChunkTOcoordinates(Chunk c, World w, Integer x, Integer z) {
		int realX = c.getX() * 16 + x;
		int realZ = c.getZ() * 16 + z;
		return new Location(w, realX, 0, realZ);
	}
	
	public static Integer translateChunkTOcoordinateX(Chunk c, Integer x) {
		return c.getX() * 16 + x;
	}
	
	public static Integer translateChunkTOcoordinateZ(Chunk c, Integer z) {
		return c.getZ() * 16 + z;
	}
	
	public static void saveChunk(Chunk c) {
		savedChunk.clear();
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
					savedChunk.put(x + ";" + y + ";" + z, Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).getBlockData());
				}
			}
		}
		
	}
	
	public static void loadChunk(Chunk c) {
		HashMap<String, BlockData> toCopy = savedChunk;
		clearChunkEmtpy(c);
		
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
						if(toCopy.get(x + ";" + y + ";" + z) != null) {
							Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setBlockData(toCopy.get(x + ";" + y + ";" + z));
						}else {
							Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setType(Material.AIR);
						}
					}
				}
			}
		
	}
	
	public static HashMap<String, BlockData> saveBlockDataFrom(Chunk c) {
		HashMap<String, BlockData> finish = new HashMap<>();
		
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
					finish.put(x + ";" + y + ";" + z, Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).getBlockData());
				}
			}
		}
		
		return finish;
	}
	
	public static void clearChunkEmtpy(Chunk c) {
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
					c.getBlock(x, y, z).setType(Material.AIR);
				}
			}
		}
	}
	
	public static void removeBedrockFromChunk(Chunk c) {
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
					if(c.getBlock(x, y, z).getType() == Material.BEDROCK) {
						c.getBlock(x, y, z).setType(Material.STONE);
					}
				}
			}
		}
		
	}
	
	public static void updownChunk(Chunk c, Integer hoehe) {
		HashMap<String, BlockData> toCopy = saveBlockDataFrom(c);
		clearChunkEmtpy(c);
		
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=1; y<255 ; y++) {
					if(y <= hoehe) {
						c.getBlock(x, y, z).setType(Material.AIR);
					}else {
						if(y+hoehe <= 255) {
							if(toCopy.get(x + ";" + (y-hoehe) + ";" + z) != null) {
								Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setBlockData(toCopy.get(x + ";" + (y-hoehe) + ";" + z));
							}else {
								Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setType(Material.AIR);
							}
						}else {
							Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).setType(Material.AIR);
						}
					}
				}
			}
		}
	}
	
	public static void moveChunk(Chunk c1, Chunk c2) {
		//von c1
		//zu c2
		
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=0; y<256 ; y++) {
					if(c2.getBlock(x, y, z).getBlockData() != c1.getBlock(x, y, z).getBlockData()) {
						c2.getBlock(x, y, z).setBlockData(c1.getBlock(x, y, z).getBlockData());
					}
				}
			}
		}
		
		resetChunk(c1);
		
	}
	
	public static void resetChunkChestsAndStuff(Chunk c) {
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=0; y<256 ; y++) {
					int realx = translateChunkTOcoordinateX(c, x);
					int realz = translateChunkTOcoordinateZ(c, z);
					Location loc = new Location(c.getWorld(), realx, y, realz);
					if(c.getWorld().getBlockAt(loc).getType() == Material.CHEST){
						File Sign = new File("plugins/ShopSystem/Signs.yml");
						YamlConfiguration ySign = YamlConfiguration.loadConfiguration(Sign);
						      
						Block b = c.getWorld().getBlockAt(loc);
						
						      List<String> Chest = ySign.getStringList("LocationsT");
						      List<String> Signs = ySign.getStringList("Locations");
						      if(Chest.contains(EVENT_ChestShop.locToString(loc))) {
						    	  Chest.remove(EVENT_ChestShop.locToString(loc));
						    	  ySign.set("LocationsT", Chest);
						      }
						      
						      Location signloc = new Location(loc.getWorld(), loc.getX(), loc.getY()-1, loc.getZ());
						      if(Signs.contains(EVENT_ChestShop.locToString(signloc))) {
						    	  Signs.remove(EVENT_ChestShop.locToString(signloc));
						    	  c.getWorld().getBlockAt(signloc).setType(Material.AIR);
						      }else if(Signs.contains(EVENT_ChestShop.locToString(b.getRelative(BlockFace.WEST).getLocation()))){
						    	  Signs.remove(EVENT_ChestShop.locToString(b.getRelative(BlockFace.WEST).getLocation()));
						    	  b.getRelative(BlockFace.WEST).setType(Material.AIR);
						      }else if(Signs.contains(EVENT_ChestShop.locToString(b.getRelative(BlockFace.SOUTH).getLocation()))){
						    	  Signs.remove(EVENT_ChestShop.locToString(b.getRelative(BlockFace.SOUTH).getLocation()));
						    	  b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
						      }else if(Signs.contains(EVENT_ChestShop.locToString(b.getRelative(BlockFace.NORTH).getLocation()))) {
						    	  Signs.remove(EVENT_ChestShop.locToString(b.getRelative(BlockFace.NORTH).getLocation()));
						    	  b.getRelative(BlockFace.NORTH).setType(Material.AIR);
						      }else if(Signs.contains(EVENT_ChestShop.locToString(b.getRelative(BlockFace.EAST).getLocation()))){
						    	  Signs.remove(EVENT_ChestShop.locToString(b.getRelative(BlockFace.EAST).getLocation()));
						    	  b.getRelative(BlockFace.EAST).setType(Material.AIR);
						      }
						      ySign.set("Locations", Signs);
						      c.getWorld().getBlockAt(loc).setType(Material.DRIED_KELP_BLOCK);
						      
						      InfoHandler.saveFile(Sign, ySign);
						      
					}
					
				}
			}
		}
	}
	
	public static void resetChunk(Chunk c) {
		if(!c.getWorld().getName().equals("world_nether")) {
		double X = c.getX()*16;
		double Y = 100;
		double Z = c.getZ()*16;
		Location copyloc = new Location(Bukkit.getWorld("worldCOPY"), X, Y, Z);
		
		Chunk copy = copyloc.getChunk();
		
		for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
				for(int y=0; y<256 ; y++) {
					if(c.getBlock(x, y, z).getBlockData() != copy.getBlock(x, y, z).getBlockData()) {
						c.getBlock(x, y, z).setBlockData(copy.getBlock(x, y, z).getBlockData());
					}
				}
			}
		}
		}
	}
	
	public static void setParticlesEcken(Chunk c, Particle part) {
		World w = c.getWorld();
		
		for(int j=0; j<10; j++) {
			Integer X = c.getX() * 16 + 15;
			Integer Z = c.getZ() * 16 + 0;
			Integer Y = w.getHighestBlockYAt(X, Z);
			Location loc = new Location(w, X, Y, Z);
			w.spawnParticle(part ,loc.add(0, j, 0), 10);
			
			Integer X2 = c.getX() * 16 + 0;
			Integer Z2 = c.getZ() * 16 + 15;
			Integer Y2 = w.getHighestBlockYAt(X2, Z2);
			Location loc2 = new Location(w, X2, Y2, Z2);
			w.spawnParticle(part ,loc2.add(0, j, 0), 10);
			
			Integer X3 = c.getX() * 16 + 0;
			Integer Z3 = c.getZ() * 16 + 0;
			Integer Y3 = w.getHighestBlockYAt(X3, Z3);
			Location loc3 = new Location(w, X3, Y3, Z3);
			w.spawnParticle(part ,loc3.add(0, j, 0), 10);
			
			Integer X4 = c.getX() * 16 + 15;
			Integer Z4 = c.getZ() * 16 + 15;
			Integer Y4 = w.getHighestBlockYAt(X4, Z4);
			Location loc4 = new Location(w, X4, Y4, Z4);
			w.spawnParticle(part ,loc4.add(0, j, 0), 10);
		}
	}
	
	public static List<Location> getRand(Chunk c) {
		List<Location> randloc = new ArrayList<>();
		World w = c.getWorld();
		for(int i=0; i<16; i++) {
			Integer X = c.getX() * 16 + i;
			Integer Z = c.getZ() * 16 + 0;
			Integer Y = w.getHighestBlockYAt(X, Z);
			Location loc = new Location(w, X, Y, Z);
			randloc.add(loc);
			
			Integer X2 = c.getX() * 16 + i;
			Integer Z2 = c.getZ() * 16 + 15;
			Integer Y2 = w.getHighestBlockYAt(X2, Z2);
			Location loc2 = new Location(w, X2, Y2, Z2);
			randloc.add(loc2);
			
			if(i != 0 && i != 15) {
				Integer X3 = c.getX() * 16 + 0;
				Integer Z3 = c.getZ() * 16 + i;
				Integer Y3 = w.getHighestBlockYAt(X3, Z3);
				Location loc3 = new Location(w, X3, Y3, Z3);
				randloc.add(loc3);
				
				Integer X4 = c.getX() * 16 + 15;
				Integer Z4 = c.getZ() * 16 + i;
				Integer Y4 = w.getHighestBlockYAt(X4, Z4);
				Location loc4 = new Location(w, X4, Y4, Z4);
				randloc.add(loc4);
			}
		}
		return randloc;
	}
	
	public static void setParticles(Chunk c, Particle part) {
		World w = c.getWorld();
		for(int i=0; i<16; i++) {
			for(int j=0; j<10; j++) {
			Integer X = c.getX() * 16 + i;
			Integer Z = c.getZ() * 16 + 0;
			Integer Y = w.getHighestBlockYAt(X, Z);
			Location loc = new Location(w, X, Y, Z);
			w.spawnParticle(part ,loc.add(0, j, 0), 10);
			
			Integer X2 = c.getX() * 16 + i;
			Integer Z2 = c.getZ() * 16 + 15;
			Integer Y2 = w.getHighestBlockYAt(X2, Z2);
			Location loc2 = new Location(w, X2, Y2, Z2);
			w.spawnParticle(part ,loc2.add(0, j, 0), 10);
			
			if(i != 0 && i != 15) {
				Integer X3 = c.getX() * 16 + 0;
				Integer Z3 = c.getZ() * 16 + i;
				Integer Y3 = w.getHighestBlockYAt(X3, Z3);
				Location loc3 = new Location(w, X3, Y3, Z3);
				w.spawnParticle(part ,loc3.add(0, j, 0), 10);
				
				Integer X4 = c.getX() * 16 + 15;
				Integer Z4 = c.getZ() * 16 + i;
				Integer Y4 = w.getHighestBlockYAt(X4, Z4);
				Location loc4 = new Location(w, X4, Y4, Z4);
				w.spawnParticle(part ,loc4.add(0, j, 0), 10);
			}
			}
		}
		
		
	}
	
	public static   void setRandBlock(Player p, Material b) {
		
		Chunk c = p.getLocation().getChunk();
		World w = p.getWorld();
		
		for(int i=0; i<16; i++) {
			
			c.getBlock(i, w.getHighestBlockYAt(c.getX() * 16 + i,c.getZ() * 16 + 0)+1 , 0).setType(b);
			c.getBlock(i, w.getHighestBlockYAt(c.getX() * 16 + i,c.getZ() * 16 + 15)+1 ,15).setType(b);
			
			if(i != 0 && i != 15) {
			c.getBlock(0, w.getHighestBlockYAt(c.getX() * 16 + 0,c.getZ() * 16 + i)+1 , i).setType(b);
			c.getBlock(15, w.getHighestBlockYAt(c.getX() * 16 + 15,c.getZ() * 16 + i)+1 ,i).setType(b);
			}
		}
		
	}
	
	public static  void setChunkBiome(Player p, Biome biome) {
		Chunk c = p.getLocation().getChunk();
		
		for(int x = 0 ; x < 16; x++) {
			for(int z = 0 ; z < 16; z++) {
				for(int y = 0 ; y < 256; y++) {
				final Block block = c.getBlock(x, y, z);
				block.setBiome(biome);
				}
			}
		}
	}
	
	public static Biome getChunkBiome(Player p) {
		Chunk c = p.getLocation().getChunk();
		for(int x = 0 ; x < 16; x++){
			for(int z = 0 ; z < 16; z++){
				Biome biome = c.getBlock(x, 0, z).getBiome();
				return biome;
			}
		}
		return null;

	}
	
	// Chunk File-Save-System
	public static boolean existPublicChunk(String name) {
		File file = new File("plugins/Chunk/global", name+".yml");
		if(file.exists())return true;
		else return false;
	}
	
	public static void deleteChunkInPublicFiles(String name) {
		if(existPublicChunk(name)) {
			File file = new File("plugins/Chunk/global", name+".yml");
			file.delete();
		}
	}
	
	public static void saveChunkInPublicFiles(String name, Chunk c) {
		File file = new File("plugins/Chunk/global", name+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
			for(int y=1; y<255 ; y++) {
				BlockData bd = Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).getBlockData();
				cfg.set(x+";"+y+";"+z, bd.getAsString());
			}}}
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static HashMap<String, BlockData> loadChunkFromPublicFiles(String name) {
		HashMap<String, BlockData> finish = new HashMap<>();
		File file = new File("plugins/Chunk/global", name+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
			for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
			for(int y=1; y<255 ; y++) {
				BlockData bd = Bukkit.getServer().createBlockData(cfg.getString(x+";"+y+";"+z));
				finish.put(x + ";" + y + ";" + z, bd);
			}}}
			
		return finish;
	}
	
	public static String getStringOfPublicChunks() {
		File file = new File("plugins/Chunk/global");
		if(file.exists()) {
		String finish = "";
		for(File argument : file.listFiles()) {
			if(!finish.contains(argument.getName())) {
				finish = finish + argument.getName().replace(".yml", "") + ", ";
			}
		}
		finish.trim();
		if(finish.equals("")) return null;
		else return finish;
		}else{
			return null;
		}
	}
	
	// Save for person itself
	
	public static void deleteChunkInFiles(Player p, String name) {
		if(existChunkFromPlayer(p, name)) {
			File file = new File("plugins/Chunk/"+p.getName(), name+".yml");
			file.delete();
		}
	}
	
	public static void saveChunkInFiles(Player p, String name, Chunk c) {
		File file = new File("plugins/Chunk/"+p.getName(), name+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
			for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
			for(int y=1; y<255 ; y++) {
				BlockData bd = Bukkit.getWorld("world").getBlockAt(translateChunkTOcoordinateX(c, x), y, translateChunkTOcoordinateZ(c, z)).getBlockData();
				cfg.set(x+";"+y+";"+z, bd.getAsString());
			}}}
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static HashMap<String, BlockData> loadChunkFromFiles(Player p, String name) {
		HashMap<String, BlockData> finish = new HashMap<>();
		File file = new File("plugins/Chunk/"+p.getName(), name+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
			for(int x=0; x<16 ; x++) {
			for(int z=0; z<16 ; z++) {
			for(int y=1; y<255 ; y++) {
				BlockData bd = Bukkit.getServer().createBlockData(cfg.getString(x+";"+y+";"+z));
				finish.put(x + ";" + y + ";" + z, bd);
			}}}
			
		return finish;
	}
	
	public static boolean existChunkFromPlayer(Player p, String name) {
		File file = new File("plugins/Chunk/"+p.getName(), name+".yml");
		if(file.exists())return true;
		else return false;
	}
	
	public static String getStringOfOwnSavedChunks(Player p) {
		File file = new File("plugins/Chunk/"+p.getName());
		if(file.exists()) {
		String finish = "";
		for(File argument : file.listFiles()) {
			if(!finish.contains(argument.getName())) {
				finish = finish + argument.getName().replace(".yml", "") + ", ";
			}
		}
		finish.trim();
		if(finish.equals("")) return null;
		else return finish;
		}else {
			return null;
		}
	}
	
}
