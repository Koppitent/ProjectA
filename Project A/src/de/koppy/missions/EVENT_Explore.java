package de.koppy.missions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import de.koppy.project.Main;

public class EVENT_Explore implements Listener {

	private Main main;
	public EVENT_Explore(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	
	public static HashMap<Location, Material> resetBlocks = new HashMap<>();
	public static ArrayList<Location> blocklocs = new ArrayList<>();
	public static HashMap<Player, String> explorer = new HashMap<>();
	
	@EventHandler
	public void onJ(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		explorer.put(p, Explorer.getRegion(p.getLocation()).toString());
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(!explorer.get(p).equals(Regions.Grundstückswelt.toString())) {
			if(!p.hasPermission("server.build")) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(!p.hasPermission("server.build")) {
		if(!explorer.get(p).equals(Regions.Grundstückswelt.toString())) {
		List<Material> materialsTObreak = new ArrayList<Material>();
		materialsTObreak.add(Material.OAK_LOG);
		materialsTObreak.add(Material.BIRCH_LOG);
		materialsTObreak.add(Material.JUNGLE_LOG);
		materialsTObreak.add(Material.ACACIA_LOG);
		materialsTObreak.add(Material.DARK_OAK_LOG);
		materialsTObreak.add(Material.SPRUCE_LOG);
		materialsTObreak.add(Material.OAK_WOOD);
		materialsTObreak.add(Material.BIRCH_WOOD);
		materialsTObreak.add(Material.JUNGLE_WOOD);
		materialsTObreak.add(Material.ACACIA_WOOD);
		materialsTObreak.add(Material.DARK_OAK_WOOD);
		materialsTObreak.add(Material.SPRUCE_WOOD);
		if(explorer.get(p).equals(Regions.Wilderness.toString())) {
		if(materialsTObreak.contains(e.getBlock().getType())) {
				e.setCancelled(false);
				Material m = e.getBlock().getType();
				resetBlocks.put(e.getBlock().getLocation(), m);
				blocklocs.add(e.getBlock().getLocation());
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						e.getBlock().setType(m);
						resetBlocks.remove(e.getBlock().getLocation(), m);
						blocklocs.remove(e.getBlock().getLocation());
					}
				}, 20*60*2);
			}else {
				if(!p.hasPermission("server.build")) e.setCancelled(true);
			}
		}else {
			if(!p.hasPermission("server.build")) e.setCancelled(true);
		}
		}
		}
	}
	
	@EventHandler
	public void onExplore(PlayerMoveEvent e) {
		if(e.getTo().getX() != e.getFrom().getX() ||
				e.getTo().getY() != e.getFrom().getY() ||
					e.getTo().getZ() != e.getFrom().getZ()) {
					Player p = e.getPlayer();
					Regions region = Explorer.getRegion(p.getLocation());
					if(explorer.containsKey(p)) {
						if(explorer.get(p).equals(region.toString()) == false) {
							explorer.put(p, region.toString());
							if(!explorer.get(p).equals(Regions.Wilderness.toString())) p.sendTitle("§3"+region.toString(), "§aRegion betreten.");
						}
					}
					if(Explorer.wasInRegion(p, region) == false) { Explorer.addRegion(p, region); p.sendTitle("§6"+region.toString(), "§5Neues Reiseziel freigeschaltet!");}
					
		}
		
	}
	
	@EventHandler
	public void onExplore(InventoryCloseEvent e) {
		if(COMMAND_FastTravel.inInv.contains(e.getPlayer())) COMMAND_FastTravel.inInv.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onExplore(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(COMMAND_FastTravel.inInv.contains(p)) {
			e.setCancelled(true);
			if(e.getCurrentItem() != null && e.getClick() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains("§3")) {
				String travelpointname = e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].replace("§3", "");
				p.performCommand("travel " + travelpointname);
			}
			
		}
	}
	
	@EventHandler
	public void onMonsterSpawn(EntityDamageEvent e) {
		if(e.getEntity() instanceof Monster) {
			if(e.getEntity().getType() == EntityType.ZOMBIE) {
				
				Zombie zombie = (Zombie) e.getEntity();
				String name = zombie.getCustomName();
				Integer lives = (int) (zombie.getHealth()-e.getDamage());
				if(lives < 0) lives = 0;
				name = name.replace("§3"+(int)zombie.getHealth(), "§3"+lives);
				zombie.setCustomName(name);
				
			}else {
				
				LivingEntity mob = (LivingEntity) e.getEntity();
				String name = mob.getCustomName();
				Integer lives = (int) (mob.getHealth()-e.getDamage());
				if(lives < 0) lives = 0;
				name = name.replace("§3"+(int)mob.getHealth(), "§3"+lives);
				mob.setCustomName(name);
				
			}
		}else {
			
			LivingEntity mob = (LivingEntity) e.getEntity();
			String name = mob.getCustomName();
			Integer lives = (int) (mob.getHealth()-e.getDamage());
			if(lives < 0) lives = 0;
			name = name.replace("§3"+(int)mob.getHealth(), "§3"+lives);
			mob.setCustomName(name);
			
		}
	}
	
	@EventHandler
	public void onMonsterSpawn(CreatureSpawnEvent e) {
		
		if(Explorer.getRegion(e.getLocation()) != Regions.Wilderness) e.setCancelled(true);
		
		if(e.getEntityType() == EntityType.ZOMBIE) {
			
			Zombie zombie = (Zombie) e.getEntity();
			zombie.setBaby(false);
			zombie.setCustomNameVisible(true);
			Random rndm = new Random();
			if(rndm.nextInt(99)+1 < 20) {
				zombie.setMaxHealth(30);
				zombie.setHealth(30);
				zombie.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				zombie.setCustomName("§8[§7Lvl: §22§8] §7Zombie §8| §3"+(int) zombie.getHealth()+" §4❤");
			}else if(rndm.nextInt(99)+1 < 20) {
				zombie.setMaxHealth(40);
				zombie.setHealth(40);
				zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				zombie.setCustomName("§8[§7Lvl: §23§8] §7Zombie §8| §3"+(int) zombie.getHealth()+" §4❤");
			}else zombie.setCustomName("§8[§7Lvl: §21§8] §7Zombie §8| §3"+(int) zombie.getHealth()+" §4❤");
			
		}else if(e.getEntityType() == EntityType.SKELETON) {
			
			Skeleton mob = (Skeleton) e.getEntity();
			mob.setCustomNameVisible(true);
			Random rndm = new Random();
			if(rndm.nextInt(99)+1 < 20) {
				mob.setMaxHealth(30);
				mob.setHealth(30);
				mob.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				mob.setCustomName("§8[§7Lvl: §22§8] §7Sekelett §8| §3"+(int) mob.getHealth()+" §4❤");
			}else if(rndm.nextInt(99)+1 < 20) {
				mob.setMaxHealth(40);
				mob.setHealth(40);
				mob.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				mob.setCustomName("§8[§7Lvl: §23§8] §7Sekelett §8| §3"+(int) mob.getHealth()+" §4❤");
			}else mob.setCustomName("§8[§7Lvl: §21§8] §7Sekelett §8| §3"+(int) mob.getHealth()+" §4❤");
			
		}else if(e.getEntityType() == EntityType.CREEPER) {
			
			Creeper mob = (Creeper) e.getEntity();
			mob.setCustomNameVisible(true);
			Random rndm = new Random();
			if(rndm.nextInt(99)+1 < 20) {
				mob.setMaxHealth(30);
				mob.setHealth(30);
				mob.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				mob.setCustomName("§8[§7Lvl: §22§8] §7Creeper §8| §3"+(int) mob.getHealth()+" §4❤");
			}else if(rndm.nextInt(99)+1 < 20) {
				mob.setExplosionRadius(10);
				mob.setMaxHealth(40);
				mob.setHealth(40);
				mob.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				mob.setCustomName("§8[§7Lvl: §23§8] §7Creeper §8| §3"+(int) mob.getHealth()+" §4❤");
			}else mob.setCustomName("§8[§7Lvl: §21§8] §7Creeper §8| §3"+(int) mob.getHealth()+" §4❤");
			
		}else if(e.getEntityType() == EntityType.SPIDER) {
			
			Spider mob = (Spider) e.getEntity();
			mob.setCustomNameVisible(true);
			Random rndm = new Random();
			if(rndm.nextInt(99)+1 < 20) {
				mob.setMaxHealth(30);
				mob.setHealth(30);
				mob.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				mob.setCustomName("§8[§7Lvl: §22§8] §7Spinne §8| §3"+(int) mob.getHealth()+" §4❤");
			}else if(rndm.nextInt(99)+1 < 20) {
				mob.setMaxHealth(40);
				mob.setHealth(40);
				mob.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				mob.setCustomName("§8[§7Lvl: §23§8] §7Spinne §8| §3"+(int) mob.getHealth()+" §4❤");
			}else mob.setCustomName("§8[§7Lvl: §21§8] §7MegaSpinne §8| §3"+(int) mob.getHealth()+" §4❤");
			
		}else if(e.getEntityType() == EntityType.SLIME || e.getEntityType() == EntityType.BAT) {
			e.setCancelled(true);
		}else {
			
			LivingEntity mob = e.getEntity();
			mob.setCustomNameVisible(true);
			String mobname = mob.getType().toString();
			mobname = mobname.substring(0, mobname.length()-(mobname.length()-1)).toUpperCase() + mobname.substring(1).toLowerCase();
			mob.setCustomName("§7" + mobname + " §8| §3"+(int) mob.getHealth()+" §4❤");
			
		}
		
	}
	
}
