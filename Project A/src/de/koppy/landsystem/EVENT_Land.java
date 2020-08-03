package de.koppy.landsystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.koppy.handler.InfoHandler;
import de.koppy.project.ChunkEditor;
import de.koppy.project.Main;

public class EVENT_Land implements Listener{
	
	private ArrayList<Player> breakblockout = new ArrayList<Player>();
	private ArrayList<Player> breakpause = new ArrayList<Player>();
	
	private Main main;
	public EVENT_Land(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onFlagTNT(EntityExplodeEvent e) {
		if(e.getLocation().getWorld().getName().equals("world") && InfoHandler.isIn(e.getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		if(e.getEntityType() == EntityType.CREEPER || e.getEntityType() == EntityType.WITHER || e.getEntityType() == EntityType.WITHER_SKULL) {
			e.setCancelled(true);
		}else {
			List<Block> blocks = e.blockList();
			List<Block> blockstorem = new ArrayList<>();
			for(Block b : blocks) {
				if(e.getEntity().getLocation().getChunk() != b.getChunk()) {
					if(API_Land.existChunk(b.getChunk()) == true && API_Land.existChunk(e.getEntity().getLocation().getChunk()) == false) {
						blockstorem.add(b);
					}else if(API_Land.existChunk(b.getChunk()) == false && API_Land.existChunk(e.getEntity().getLocation().getChunk()) == true) {
						
					}else if(API_Land.existChunk(b.getChunk()) == false && API_Land.existChunk(e.getEntity().getLocation().getChunk()) == false) {
						
					}else if(API_Land.existChunk(b.getChunk()) == true && API_Land.existChunk(e.getEntity().getLocation().getChunk()) == true) {
						if(API_Land.getOwner(b.getChunk()).equals(API_Land.getOwner(e.getEntity().getLocation().getChunk()))) {
							// If flag on
							if(API_Land.getFlag("tnt", b.getChunk()) == false) {
								blockstorem.add(b);
							}
						}else {
							blockstorem.add(b);
						}
					}
				}
			}
			e.blockList().removeAll(blockstorem);
			if(API_Land.existChunk(e.getEntity().getLocation().getChunk()) == true) {
				if(API_Land.getFlag("tnt", e.getEntity().getLocation().getChunk()) == false) {
					e.setCancelled(true);
				}
			}
			
		}
		}
	}
	
	@EventHandler
	public void onFlagPvPvE(BlockFromToEvent e) {
		if(e.getBlock().getWorld().getName().equals("world") && InfoHandler.isIn(e.getBlock().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		if(e.getBlock().getType() == Material.WATER || e.getBlock().getType() == Material.LAVA) {
			if(e.getBlock().getChunk() != e.getToBlock().getChunk()) {
				if(API_Land.getOwner(e.getBlock().getChunk()) != null) {
					if(API_Land.getOwner(e.getToBlock().getChunk()) != null) {
						if(API_Land.getOwner(e.getBlock().getChunk()).equals(API_Land.getOwner(e.getToBlock().getChunk())) == false){
							e.setCancelled(true);
						}
					}else {
						if(API_Land.getOwner(e.getBlock().getChunk()) != null) {
							e.setCancelled(true);
						}
					}
				}else {
					if(API_Land.getOwner(e.getToBlock().getChunk()) != null) {
						e.setCancelled(true);
					}
				}
			}
		}
		}
	}
	
	@EventHandler
	public void onFlag(EntityDamageByEntityEvent e) {
		if(e.getEntity().getWorld().getName().equals("world") && InfoHandler.isIn(e.getEntity().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
			Entity opfer = e.getEntity();
			Entity damager = e.getDamager();
			
			if(opfer instanceof Player) {
				if(damager instanceof Player) {
					if(API_Land.getFlag("pvp", opfer.getLocation().getChunk()) == false) {
						e.setCancelled(true);
					}
				}else if(damager instanceof Arrow) {
					if(((Arrow) damager).getShooter() instanceof Player) {
						if(API_Land.getFlag("pvp", opfer.getLocation().getChunk()) == false) {
							e.setCancelled(true);
						}
					}
				}else if(damager instanceof Trident) {
					if(((Trident) damager).getShooter() instanceof Player) {
						if(API_Land.getFlag("pvp", opfer.getLocation().getChunk()) == false) {
							e.setCancelled(true);
						}
					}
				}
			}else if(opfer instanceof Animals) {
				if(damager instanceof Player) {
					if(API_Land.getFlag("pve", opfer.getLocation().getChunk()) == false) {
						if(API_Land.isOwner(damager.getUniqueId().toString(), damager.getLocation().getChunk()) == false && API_Land.isMember(damager.getUniqueId().toString(), damager.getLocation().getChunk()) == false) {
							e.setCancelled(true);
						}
					}
				}else if(damager instanceof Arrow) {
					if(((Arrow) damager).getShooter() instanceof Player) {
						if(API_Land.getFlag("pve", opfer.getLocation().getChunk()) == false) {
							Player p = (Player) ((Arrow) damager).getShooter();
							if(API_Land.isOwner(p.getUniqueId().toString(), p.getLocation().getChunk()) == false && API_Land.isMember(p.getUniqueId().toString(), p.getLocation().getChunk()) == false) {
								e.setCancelled(true);
							}
						}
					}else {
						e.setCancelled(true);
					}
				}else if(damager instanceof Trident) {
					if(((Trident) damager).getShooter() instanceof Player) {
						if(API_Land.getFlag("pve", opfer.getLocation().getChunk()) == false) {
							Player p = (Player) ((Trident) damager).getShooter();
							if(API_Land.isOwner(p.getUniqueId().toString(), p.getLocation().getChunk()) == false && API_Land.isMember(p.getUniqueId().toString(), p.getLocation().getChunk()) == false) {
								e.setCancelled(true);
							}
						}
					}else {
						e.setCancelled(true);
					}
				}else {
					e.setCancelled(true);
				}
			}else {
				e.setCancelled(false);
			}
		}
		}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(e.getBlockPlaced().getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
			String pUUID = p.getUniqueId().toString();
			if(API_Land.canBuildonChunk(pUUID, e.getBlockPlaced().getLocation().getChunk()) == true || p.hasPermission("server.land.admin.build")) {
				if(e.getBlockPlaced().getType() == Material.CHEST && ChunkEditor.getRand(e.getBlockPlaced().getLocation().getChunk()).contains(e.getBlockPlaced().getLocation())) {
					if(API_Land.isOwner(pUUID, e.getBlockPlaced().getChunk()) || API_Land.isMember(pUUID, e.getBlockPlaced().getChunk())) {
						p.sendMessage(COMMAND_Land.prefix + "§4ACHTUNG!!! §7Die Kiste wurde an einer Chunkgrenze §7platziert! Dies kann zu Problemen führen!");
					}
				}
			}else {
				e.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(e.getBlock().getWorld().getName().equals("world") && InfoHandler.isIn(p.getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		String pUUID = p.getUniqueId().toString();
			if(API_Land.canBuildonChunk(pUUID, e.getBlock().getLocation().getChunk()) == false && p.hasPermission("server.land.admin.build") == false) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onItemFrameManipulate(PlayerInteractEntityEvent e) {
		if(e.getPlayer().getWorld().getName().equals("world") && InfoHandler.isIn(e.getPlayer().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		final Entity clicked = e.getRightClicked();
		
		if(clicked != null && clicked instanceof ItemFrame) {
			Location loc = e.getRightClicked().getLocation();
			Player p = e.getPlayer();
			
			if(API_Land.canBuildonChunk(p.getUniqueId().toString(), loc.getChunk()) || p.hasPermission("server.land.admin.ignoreitemframs")) {
				
			}else {
				e.setCancelled(true);
			}
		}
      }
	}
	
	@EventHandler
	public void onSchutz(PlayerInteractEvent e) {
		if(e.getPlayer().getWorld().getName().equals("world") && InfoHandler.isIn(e.getPlayer().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		Player p = e.getPlayer();
		if(!p.hasPermission("server.land.admin.place")) {
		if(e.getClickedBlock() != null) {
		Location loc = e.getClickedBlock().getLocation();
		World w = p.getLocation().getWorld();
		if(w.getName().equals("world") && InfoHandler.isIn(p.getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		if(API_Land.canBuildonChunk(p.getUniqueId().toString(), loc.getChunk()) == true) {
					e.setCancelled(false);
					if(API_Land.existChunk(loc.getChunk()) == false) {
						e.setCancelled(true);
						if(!breakblockout.contains(p)) {
						
						breakblockout.add(p);
						p.sendMessage(COMMAND_Land.prefix + "§cDu kannst hier nicht abbauen. Das Grundstück gehört dir nicht! §4(2)");
						Bukkit.getScheduler().runTaskLater(main, new Runnable() {
							
							@Override
							public void run() {
								breakblockout.remove(p);
							}
						}, 20*10);
						}
					}
				
		}else if(API_Land.canBuildonChunk(p.getUniqueId().toString(), loc.getChunk()) == false){
			if(p.hasPermission("server.land.admin.place")) {
				e.setCancelled(false);
			}else {
				e.setCancelled(true);
			if(!breakpause.contains(p)) {
				p.sendMessage(COMMAND_Land.prefix + "§cDu kannst auf diesem Grundstück nicht ab/bauen! §4(0)");
				breakpause.add(p);
				Bukkit.getScheduler().runTaskLater(main, new Runnable() {
					
					@Override
					public void run() {
						breakpause.remove(p);
						
					}
				}, 20*3);
				}
		}}}}}}
	}
	
	@EventHandler
	public void onFramesandmore(HangingBreakByEntityEvent e) {
		if(e.getEntity().getWorld().getName().equals("world") && InfoHandler.isIn(e.getEntity().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		if(e.getRemover() instanceof Player) {
			if(e.getEntity() instanceof ItemFrame) {
				
				Location loc = e.getEntity().getLocation();
				Player p = (Player) e.getRemover();
				
				if(API_Land.canBuildonChunk(p.getUniqueId().toString(), loc.getChunk()) || p.hasPermission("server.land.admin.ignoreitemframs")) {
				}else {
					e.setCancelled(true);
				}
				
			}
		}
		}
	}
	
	@EventHandler
	public void onFlag(PlayerArmorStandManipulateEvent e) {
		if(e.getPlayer().getWorld().getName().equals("world") && InfoHandler.isIn(e.getPlayer().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		Player p = e.getPlayer();
		
		if(API_Land.isOwner(p.getUniqueId().toString(), e.getRightClicked().getLocation().getChunk()) == false) {
			if(API_Land.isMember(p.getUniqueId().toString(), e.getRightClicked().getLocation().getChunk()) == false) {
				if(API_Land.getFlag("pve", e.getRightClicked().getLocation().getChunk()) == false) {
					e.setCancelled(true);
				}
			}
		}
		}
	}
	
	@EventHandler
	public void MoveEventTPA(PlayerMoveEvent e) {
		if(e.getPlayer().getWorld().getName().equals("world") && InfoHandler.isIn(e.getPlayer().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		Player p = e.getPlayer();
		
			if(e.getFrom().getBlockX() != e.getTo().getBlockX() 
				|| e.getFrom().getBlockY() != e.getTo().getBlockY() 
				|| e.getFrom().getBlockZ() != e.getTo().getBlockZ()){
				
				if(API_Land.isBanned(p.getUniqueId().toString(), e.getTo().getChunk())) {
					if(!p.hasPermission("server.land.admin.bypass")) {
					e.setCancelled(true);
					}
				}
				
			}
		}
	}
	
	@EventHandler
	public void MoveEventTPA(PlayerTeleportEvent e) {
		if(e.getPlayer().getWorld().getName().equals("world") && InfoHandler.isIn(e.getPlayer().getLocation(), COMMAND_Land.pos1, COMMAND_Land.pos2)) {
		Player p = e.getPlayer();
		
				if(API_Land.isBanned(p.getUniqueId().toString(), e.getTo().getChunk())) {
					if(!p.hasPermission("server.land.admin.bypass")) {
					e.setCancelled(true);
					}
				}
		}
	}
	
	
	
}
