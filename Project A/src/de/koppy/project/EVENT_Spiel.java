package de.koppy.project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.handler.InfoHandler;
import de.koppy.handler.ProfileHandler;
import de.koppy.standardcmds.COMMAND_Fly;
import de.koppy.standardcmds.COMMAND_Head;
import de.koppy.standardcmds.COMMAND_Home;
import de.koppy.standardcmds.COMMAND_Warp;

public class EVENT_Spiel implements Listener{
	
private Main main;
	
	public EVENT_Spiel(Main main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	ArrayList<Player> sitonchair = new ArrayList<>();
	HashMap<Player, Entity> chairs = new HashMap<>();
	HashMap<Player, Location> saveloc = new HashMap<>();
	HashMap<Location, Entity> chairsblock = new HashMap<>();
	public static HashMap<Player, Integer> spielzeit = new HashMap<>();
	
	@EventHandler
	public void DropSichtbarkeitEvent(ItemSpawnEvent e) {
		e.getEntity().setGlowing(true);
		
		e.getEntity().setCustomNameVisible(true);
		e.getEntity().setCustomName("§e" + e.getEntity().getItemStack().getAmount() + "x §7"+e.getEntity().getName());
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				e.getEntity().setCustomNameVisible(true);
				e.getEntity().setCustomName("");
				e.getEntity().setCustomName("§e" + e.getEntity().getItemStack().getAmount() + "x §7"+e.getEntity().getName());
				
			}
		}, 20);
	}
	
//	@EventHandler
//	public void DropSichtbarkeitEvent(PlayerDropItemEvent e) {
//		e.getItemDrop().setGlowing(true);
//		e.getItemDrop().setCustomNameVisible(true);
//		e.getItemDrop().setCustomName("§e" + e.getItemDrop().getItemStack().getAmount() + "x §7"+e.getItemDrop().getName());
//	}
	
	@EventHandler
	public void SpielZeit(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		spielzeit.put(p, ProfileHandler.getSpielzeit(p.getUniqueId().toString()));
	}
	
	@EventHandler
	public void SpielZeit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(spielzeit.get(p) != null) ProfileHandler.saveSpielzeit(p.getUniqueId().toString(), spielzeit.get(p));
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(COMMAND_Home.isinHomeMenu.contains(p)) COMMAND_Home.isinHomeMenu.remove(p);
	}
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer(); 
		if(e.getOffHandItem() == null || e.getOffHandItem().getType() == Material.AIR) {
			e.setCancelled(true);
			p.performCommand("profile");
		}
	}
	
	@EventHandler
	public void onClickHomes(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(COMMAND_Home.isinHomeMenu.contains(p)) {
			if(e.getClick() != null) {
				if(e.getCurrentItem() != null) {
					
					String home = e.getCurrentItem().getItemMeta().getDisplayName().replace("§e", "");
					p.performCommand("home " + home);
					
				}
			}
		}
	}
	
	@EventHandler
	public void onSit(PlayerInteractEvent e) {
		if(e.getClickedBlock() != null) {
			Player p = e.getPlayer();
			
			if(e.getClickedBlock().getType() == Material.HOPPER) {
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Hopper hopper = (Hopper) e.getClickedBlock().getState();
				if(hopper.getCustomName().equals("Müll")) {
					e.setCancelled(true);
					Inventory müllinv = Bukkit.createInventory(null, 1*9, "§2Mülleimer");
					p.openInventory(müllinv);
				}
				}
			}
			
			if(p.getItemInHand().getType() == Material.AIR) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(!chairsblock.containsKey(e.getClickedBlock().getLocation())) {
				if(!sitonchair.contains(p)) {
					if(chairs.get(p) == null) {
						Material m = e.getClickedBlock().getType();
						
						File file = new File("plugins/Server/Sits", "sitlist.yml");
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
						
					if(cfg.getKeys(false).contains(m.toString())) {
						Entity chair = p.getWorld().spawnEntity(p.getLocation(), EntityType.ARROW);
			            chair.teleport(e.getClickedBlock().getLocation().add(0.5D, -0.2D, 0.5D));
			            chair.setPassenger(p);
			            chair.setGravity(false);
			            chairs.put(p, chair);
			            chairsblock.put(e.getClickedBlock().getLocation(), chair);
			            saveloc.put(p, e.getClickedBlock().getLocation());
			            sitonchair.add(p);
			            e.setCancelled(true);
					}
					}
				}
				}
			}
			}
		}
	}
	
	@EventHandler
	public void onSit(BlockBreakEvent e) {
		if(chairsblock.get(e.getBlock().getLocation()) != null) {
			Entity chair = chairsblock.get(e.getBlock().getLocation());
			Player t = (Player) chair.getPassenger();
			if(chairs.get(t)!= null) chairs.remove(t, chair);
			if(sitonchair.contains(t)) sitonchair.remove(t);
			if(saveloc.containsKey(t)) saveloc.remove(t, e.getBlock().getLocation());
			chair.remove();
			chairsblock.remove(e.getBlock().getLocation(), chair);
		}
	}
	
	@EventHandler
	public void onSit(PlayerMoveEvent e) {
		if(e.getFrom().getX() != e.getTo().getX() || 
			e.getFrom().getY() != e.getTo().getY() ||
			e.getFrom().getZ() != e.getTo().getZ()) {
			Player p = e.getPlayer();
			if(sitonchair.contains(p)) {
				if(chairs.get(p) != null) {
				Entity chair = chairs.get(p);
				chair.remove();
				chairs.remove(p, chair);
				
				Location loc = saveloc.get(p);
				saveloc.remove(p, loc);
				if(chairsblock.containsKey(loc)) chairsblock.remove(loc, chair);
				
				sitonchair.remove(p);
				e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onSit(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if(sitonchair.contains(p)) {
			if(chairs.get(p) != null) {
			Entity chair = chairs.get(p);
			chair.remove();
			chairs.remove(p, chair);
			
			Location loc = saveloc.get(p);
			saveloc.remove(p, loc);
			if(chairsblock.containsKey(loc)) chairsblock.remove(loc, chair);
			
			sitonchair.remove(p);
			e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if(e.getResult() != Result.ALLOWED) Bukkit.getServer().getConsoleSender().sendMessage("§3" + p.getName() + " §7versuchte beizutreten. §cReason: §3" + e.getResult());
	}
	
	@EventHandler
	public void onClick(InventoryCloseEvent e) {
		if(Main.activatestandardCMDS == true) {
			Player p = (Player) e.getPlayer();
			if(COMMAND_Head.isinHeadList.contains(p)) {COMMAND_Head.isinHeadList.remove(p);}
			if(COMMAND_Warp.notclicking.contains(p)) {COMMAND_Warp.notclicking.remove(p);}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(Main.activatestandardCMDS == true) {
			if(e.getClick() != null && e.getCurrentItem() != null) {
			Player p = (Player) e.getWhoClicked();
			if(COMMAND_Warp.notclicking.contains(p)) {
				e.setCancelled(true);
				if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7> ")) {
					p.closeInventory();
					String warp[] = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
					String realwarp = warp[1].replace("§3", "");
					p.performCommand("warp " + realwarp);
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Hauptstadt §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp spawn");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Bank §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp bank");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Job §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp job");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Farmwelt §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp farmwelt");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3End-Farmwelt §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp endfarmwelt");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Nether-Farmwelt §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp netherfarmwelt");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§7» §3Grundstücke §7§o(Rechtsklick)")) {
					p.closeInventory();
					p.performCommand("warp gs");
				}
			}
			
			if(COMMAND_Head.isinHeadList.contains(p)) {
				e.setCancelled(true);
				
				if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3Seite")) {
					String[] pfeilsplit = e.getCurrentItem().getItemMeta().getDisplayName().split(" ");
					Integer seite = Integer.valueOf(pfeilsplit[2]);
					COMMAND_Head.setHeadSeite(p, seite, e.getInventory());
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§3Kopf von §2§o")) {
					String headname = e.getCurrentItem().getItemMeta().getDisplayName().replace("§3Kopf von §2§o", "");
					File file = new File("plugins/Server/HeadDatabase", "heads.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					
					if(cfg.getKeys(false).contains(headname)) {
						String skullMetaDataUrl = cfg.getString(headname + ".URL");
						
						ItemStack skull = InfoHandler.getSkull(skullMetaDataUrl);
						SkullMeta skullm = (SkullMeta) skull.getItemMeta();
						skullm.setDisplayName("§3" + headname);
						skull.setItemMeta(skullm);
						
						p.getInventory().addItem(skull);
						p.sendMessage(Main.prefix + "§7Du hast den Kopf §3" + headname + " §7aus der §eDatenbank §7erhalten.");
						
					}else {
						p.sendMessage(Main.prefix + "§cDieser Kopf existiert nicht in der §eDatenbank§c!");
					}
					
				}
				
			}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onJoinMSG(PlayerJoinEvent e) {
		e.setJoinMessage(null);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onLeaveMSG(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		PluginDescriptionFile pdf = main.getDescription();
		Main.servername = pdf.getName();
		
		if(p.isOp()) p.sendMessage("§3Vielen Dank fürs herunterladen von §e" + pdf.getName() + " (" + pdf.getVersion() + ")§3. Made by " + pdf.getAuthors().toString().replace("[", "").replace("]", "") + ".");
		if(p.isOp()) p.sendMessage("§6" + pdf.getDescription());
		e.setJoinMessage(null);
		
		boolean registered = false;
		if(API_UUID.existUUIDorNAME(p.getUniqueId().toString())) registered = true;
		
		Bukkit.getServer().getConsoleSender().sendMessage("§3" + p.getName() + " §7ist beigetreten. §3UUID: §2" + p.getUniqueId().toString() + " §3IP: §2" + InfoHandler.getIPAdress(p) + " §3Registered: §2" + registered + " §3Rank: §2" + InfoHandler.getRank(p).toUpperCase());
		
	}
	
	@EventHandler
	public void onQuit(PlayerJoinEvent e) {
		e.setJoinMessage(null);
	}
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent e) {
		if(Main.activatestandardCMDS == true) {
		Player p = e.getPlayer();
		p.sendMessage(Main.prefix + "§7Dein Spielmodus wurde zu §a" + e.getNewGameMode() + " §7geändert.");
			if(e.getNewGameMode() == GameMode.SURVIVAL || e.getNewGameMode() == GameMode.ADVENTURE) {
				if(COMMAND_Fly.flylist.contains(p)) {
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						@Override
						public void run() {
							p.setAllowFlight(true);
							p.setFlying(true);
						}
					}, 2);
				}
			}
		}
	}
	
	
	//* Nur Plugin CMDS
//	e.getCommands().clear();
//	PluginDescriptionFile pdf = main.getDescription();
//	List<String> cmds = getUsages(main);
//	e.getCommands().addAll(cmds);
	
	@EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent e) {
		if(!e.getPlayer().hasPermission("server.admin")) {
				Player p = e.getPlayer();
				
				if(!p.isOp()) e.getCommands().remove("chunk");
				if(!p.hasPermission("server.weather")) e.getCommands().remove("weather");
				if(!p.hasPermission("server.difficulty")) e.getCommands().remove("difficulty");
				if(!p.hasPermission("server.gamemode")) e.getCommands().remove("gm");
				if(!p.hasPermission("server.gamemode")) e.getCommands().remove("gamemode");
				if(!p.hasPermission("server.fly")) e.getCommands().remove("fly");
				if(!p.hasPermission("server.head")) e.getCommands().remove("kopf");
				if(!p.hasPermission("server.head")) e.getCommands().remove("head");
				if(!p.hasPermission("server.teleport")) e.getCommands().remove("tp");
				if(!p.hasPermission("server.teleport")) e.getCommands().remove("teleport");
				if(!p.hasPermission("server.give") && !p.hasPermission("server.i")) e.getCommands().remove("give");
				if(!p.hasPermission("server.give") && !p.hasPermission("server.i")) e.getCommands().remove("i");
				if(!p.hasPermission("server.claims")) e.getCommands().remove("claims");
				if(!p.hasPermission("server.claims")) e.getCommands().remove("claims");
				if(!p.hasPermission("server.claims")) e.getCommands().remove("claims");
				if(!p.hasPermission("server.addsit")) e.getCommands().remove("addsit");
				if(!p.hasPermission("server.claims")) e.getCommands().remove("claims");
				if(!p.hasPermission("server.config")) e.getCommands().remove("config");
				if(!p.hasPermission("server.time")) e.getCommands().remove("time");
				if(!p.hasPermission("server.setspawn")) e.getCommands().remove("setspawn");
				if(!p.hasPermission("server.clear")) e.getCommands().remove("clear");
				if(!p.hasPermission("server.givemecock")) e.getCommands().remove("givemecock");
				
		}
	}
	
	public static List getUsages(Plugin plugin) {
		  List<String> parsedCommands = new ArrayList<>();
		  Map commands = plugin.getDescription().getCommands();
		  if (commands != null) {
		    Iterator commandsIt = commands.entrySet().iterator();
		    while (commandsIt.hasNext()) {
		      Map.Entry thisEntry = (Map.Entry) commandsIt.next();
		      if (thisEntry != null) {
		        parsedCommands.add((String) thisEntry.getKey());
		      }
		    }
		  }
		  if (parsedCommands.isEmpty()) return null;
		  else return parsedCommands;
	}
	
}
