package de.koppy.project;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ChunkHelper {

	public static Chunk getChunk(Player p) {
		return p.getLocation().getChunk();
	}
	
	public static Chunk getChunk(String ID) {
		String[] IDsplit = ID.split(";");
		if(IDsplit.length == 2) {
			Integer x = Integer.valueOf(IDsplit[0]);
			Integer z = Integer.valueOf(IDsplit[1]);
			return Bukkit.getWorld("world").getChunkAt(x, z);
		}
		else return null;
	}
	
	public static Chunk getChunk(Integer X, Integer Z) {
		return Bukkit.getWorld("world").getChunkAt(X, Z);
	}
	
	public static String getChunkID(Chunk c) {
		return "x" + c.getX() + " | z" + c.getZ();
	}
	
	public static Chunk getChunk(Location loc) {
		return loc.getChunk();
	}
	
	public static Chunk getChunk(Integer X, Integer Z, World w) {
		return w.getChunkAt(X, Z);
	}
	
	public static Chunk getChunkInFrontOf(Player p) {
		Chunk c=null;
		float yaw = GetPlayerDirectionYaw(p);
		if(getFaceDirection(p).equals("SOUTH")) {
			// south
			c = p.getWorld().getChunkAt(p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ()+1);
		}else if(getFaceDirection(p).equals("WEST")) {
			// west
			c = p.getWorld().getChunkAt(p.getLocation().getChunk().getX()-1, p.getLocation().getChunk().getZ());
		}else if(getFaceDirection(p).equals("NORTH")) {
			// north
			c = p.getWorld().getChunkAt(p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ()-1);
		}else if(getFaceDirection(p).equals("EAST")) {
			// east
			c = p.getWorld().getChunkAt(p.getLocation().getChunk().getX()+1, p.getLocation().getChunk().getZ());
		}
		return c;
	}
	
	public static float GetPlayerDirectionYaw(Player playerSelf){
		float yaw = playerSelf.getLocation().getYaw();
		return yaw;
	}
	
	
	public static String getFaceDirection(Player p) {
		Block b = p.getTargetBlock(null, 5);
		Location loc = b.getLocation();
		
		int x1 = (int) loc.getX();
		int z1 = (int) loc.getZ();
		
		int x2 = (int) p.getLocation().getX();
		int z2 = (int) p.getLocation().getZ();
		int Z=0;
		
		// East or West
		if(x2 >= x1) {
			// West or
			if(z1 > z2) Z = z1 - z2;
			else Z = z2 - z1;
			if(x2 - x1 >= Z) {
			// West
				
				return "WEST";
				
			}else {
				// North or south
				if(z1 > z2) {
					// South
					return "SOUTH";
					
				}else {
					// North
					return "NORTH";
					
				}
			}
		}else {
			// East or 
			if(z1 > z2) Z = z1 - z2;
			else Z = z2 - z1;
			if(x1 - x2 >= Z) {
			// East
				return "EAST";
			}else {
				// North or south
				if(z1 > z2) {
					// South
					return "SOUTH";
					
				}else {
					// North
					return "NORTH";
					
				}
			}
		}
	}
	
	
	public static String getPlayerDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
	
}
