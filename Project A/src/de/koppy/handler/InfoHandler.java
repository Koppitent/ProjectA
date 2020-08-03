package de.koppy.handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class InfoHandler {
	
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
	
	public static String getRank(Player p) {
		if(p.hasPermission("server.owner")) {
			return "owner";
		}else if(p.hasPermission("server.admin")) {
			return "admin";
		}else if(p.hasPermission("server.dev") || p.hasPermission("server.developer")) {
			return "developer";
		}else if(p.hasPermission("server.mod") || p.hasPermission("server.moderator")) {
			return "moderator";
		}else if(p.hasPermission("server.sup") || p.hasPermission("server.supporter")) {
			return "supporter";
		}else if(p.hasPermission("server.builder")) {
			return "builder";
		}else if(p.hasPermission("server.content")) {
			return "content";
		}else if(p.hasPermission("server.azubi")) {
			return "azubi";
		}else if(p.hasPermission("server.yt") || p.hasPermission("server.youtuber")) {
			return "youtuber";
		}else if(p.hasPermission("server.legende")) {
			return "legende";
		}else if(p.hasPermission("server.hero")) {
			return "hero";
		}else if(p.hasPermission("server.premium")) {
			return "premium";
		}else {
			return "spieler";
		}
	}
	
	public static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        if(url.isEmpty())return head;
        
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
	
}
