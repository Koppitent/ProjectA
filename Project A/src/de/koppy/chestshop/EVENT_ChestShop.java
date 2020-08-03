package de.koppy.chestshop;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.eco.COMMAND_Eco;
import de.koppy.eco.Economy;
import de.koppy.landsystem.API_Land;
import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

//Abbuchen von Konto und einzahlen bei Bankkonto

public class EVENT_ChestShop implements Listener{
	
	private Main main;
	public EVENT_ChestShop(Main main) {this.main = main;Bukkit.getPluginManager().registerEvents(this, main);}
	
	public static String prefix = "§8[§3C§best§3S§bhop§8] §r";
	DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
	
	public static ArrayList<Player> buypause = new ArrayList<>();
	public static ArrayList<Player> inv = new ArrayList<>();
	
	@EventHandler
	public void onCloseInv(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(inv.contains(p)) {
			inv.remove(p);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		File Sign = new File("plugins/ShopSystem/Signs.yml");
		YamlConfiguration ySign = YamlConfiguration.loadConfiguration(Sign);
		
		 if(e.getBlock().getType() == Material.CHEST) {
		      
		      List<String> Signs = ySign.getStringList("Locations");
		      List<String> Chest = ySign.getStringList("LocationsT");
		      
		      if(Chest.contains(locToString(e.getBlock().getLocation()))) {
		    	  Location signloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY()-1, e.getBlock().getLocation().getZ());
		      if(Signs.contains(locToString(signloc)) || Signs.contains(locToString(e.getBlock().getRelative(BlockFace.WEST).getLocation())) || Signs.contains(locToString(e.getBlock().getRelative(BlockFace.SOUTH).getLocation())) || Signs.contains(locToString(e.getBlock().getRelative(BlockFace.NORTH).getLocation())) || Signs.contains(locToString(e.getBlock().getRelative(BlockFace.EAST).getLocation()))){
			        e.setCancelled(true);
			        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
			        p.sendMessage(String.valueOf(prefix) + "§cBaue erst das Shop-Schild ab!");
		      }else {
		    	  Chest.remove(locToString(e.getBlock().getLocation()));
		    	  ySign.set("LocationsT", Chest);
		    	  try {ySign.save(Sign);} catch (IOException e1) {e1.printStackTrace();}
		      }
		      }
		    }
		 
		 if(e.getBlock().getState() instanceof Sign){
			 
			 Sign s = (Sign) e.getBlock().getState();
			 List<String> Signs = ySign.getStringList("Locations");
			 Location loc = new Location(e.getBlock().getWorld(), e.getBlock().getX(), e.getBlock().getY()-1, e.getBlock().getZ());
			 if (Signs.contains(locToString(e.getBlock().getLocation()))) {
				if(s.getLine(0).equals("§3" + p.getName())){
					 if(p.isSneaking()) {
					p.sendMessage(prefix + "§cDer Shop wurde abgebaut.");
					
					Block chest = null;
			        
			        if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
			          chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
			        }else if(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
			          chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
			        }else if(e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
			          chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
			        }else if(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
			          chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
			        } else if(loc.getBlock().getType() == Material.CHEST) {
			          chest = loc.getBlock();
			        }
			        
			        Signs.remove(locToString(e.getBlock().getLocation()));
			        
			        ySign.set("Locations", Signs);
			        
			        List<String> TruhenLoc = ySign.getStringList("LocationsT");
			        
			        TruhenLoc.remove(locToString(chest.getLocation()));
			        
			        ySign.set("LocationsT", TruhenLoc);
			        
			        try {
			          ySign.save(Sign);
			        } catch (IOException e1) {
			          e1.printStackTrace();
			        } 
					 } else {
						 e.setCancelled(true);
						 p.sendMessage(prefix + "§cSneake um den Shop zu entfernen!");
					 }
				}else if(s.getLine(0).startsWith("§3*")){
					String bank = s.getLine(0).replace("§3*", "");
					if(Economy.existAcc(bank) && Economy.getMembers(bank).contains(e.getPlayer().getUniqueId().toString())) {
					if(p.isSneaking()) {
						p.sendMessage(prefix + "§cDer Shop wurde abgebaut.");
						
						Block chest = null;
				        
				        if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
				          chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
				        }else if(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
				          chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
				        }else if(e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
				          chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
				        }else if(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
				          chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
				        } else if(loc.getBlock().getType() == Material.CHEST) {
				          chest = loc.getBlock();
				        }
				        
				        Signs.remove(locToString(e.getBlock().getLocation()));
				        
				        ySign.set("Locations", Signs);
				        
				        List<String> TruhenLoc = ySign.getStringList("LocationsT");
				        
				        TruhenLoc.remove(locToString(chest.getLocation()));
				        
				        ySign.set("LocationsT", TruhenLoc);
				        
				        try {
				          ySign.save(Sign);
				        } catch (IOException e1) {
				          e1.printStackTrace();
				        } 
						 } else {
							 e.setCancelled(true);
							 p.sendMessage(prefix + "§cSneake um den Shop zu entfernen!");
						 }
					}else {
						e.setCancelled(true);
						p.sendMessage(prefix + "§cDu musst Mitglied auf dem bankacc sein!");
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
					}
				}else{
					e.setCancelled(true);
					p.sendMessage(prefix + "§cDas ist nicht dein eigner Shop.");
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
				}
				}
			 
		 }
	}
	
	@EventHandler
	public void onPlaceTruhe(BlockPlaceEvent e){
		
		File Sign = new File("plugins/ShopSystem/Signs.yml");
		YamlConfiguration ySign = YamlConfiguration.loadConfiguration(Sign);
		List<String> TruhenLoc = ySign.getStringList("LocationsT");
		
		if(!e.getPlayer().isSneaking()) {
		if(e.getBlock().getType() == Material.CHEST) {
		
			Block chest = null;
			  if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST && TruhenLoc.contains(locToString(e.getBlock().getRelative(BlockFace.NORTH).getLocation()))) {
                
                chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix + "§cDu darfst hier keine Kiste platzieren!");
                
              }
              else if (e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST && TruhenLoc.contains(locToString(e.getBlock().getRelative(BlockFace.WEST).getLocation()))) {
                
                chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix + "§cDu darfst hier keine Kiste platzieren!");
                
              }
              else if (e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST && TruhenLoc.contains(locToString(e.getBlock().getRelative(BlockFace.EAST).getLocation()))) {
                
                chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix + "§cDu darfst hier keine Kiste platzieren!");
                
              }
              else if (e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST && TruhenLoc.contains(locToString(e.getBlock().getRelative(BlockFace.SOUTH).getLocation()))) {
                
                chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix + "§cDu darfst hier keine Kiste platzieren!");
                
              }
		}
		}
		
	}
	
	@EventHandler
	public void onSignAtChest(SignChangeEvent e) {
		
		File Sign = new File("plugins/ShopSystem/Signs.yml");
		YamlConfiguration ySign = YamlConfiguration.loadConfiguration(Sign);
		
		Player p = e.getPlayer();
		if(e.getLine(0).equalsIgnoreCase(e.getPlayer().getName()) || e.getLine(0).startsWith("*")){
		if(API_Land.isOwner(p.getUniqueId().toString(), p.getLocation().getChunk())) {
			if(Economy.hasEnoughMoney(p.getUniqueId().toString(), 40)) {
		if(e.getLine(0).equalsIgnoreCase(e.getPlayer().getName())){
				if (e.getLine(2).toLowerCase().startsWith("buy:") || e.getLine(2).toLowerCase().startsWith("b:") || e.getLine(2).toLowerCase().startsWith("sell:") || e.getLine(2).toLowerCase().startsWith("s:")) {
			          if(e.getLine(1).matches("[0-9]+")) {
			        	  String[] line2 = e.getLine(2).substring(2).split(" ");
			        		 if(line2[1].matches("[0.0-9.0]+")) {
			            	
						//if sell or buy
						if(!e.getLine(3).equals("")) {
							if(e.getLine(3).equals("?")) {
						
			Location signloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY()-1, e.getBlock().getLocation().getZ());
			
			Block chest = null;
			
			if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
				chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
			}else if(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
				chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
			}else if(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
				chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
			}else if(e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
				chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
			}else if(signloc.getBlock().getType() == Material.CHEST) {
				chest = signloc.getBlock();
			}else {
				e.getBlock().breakNaturally();
				p.sendMessage(prefix + "§cDu musst das Schild an einer Kiste platzieren!");
			}
			
			Inventory c = ((Chest)chest.getState()).getInventory();
			
			Material m = getFirstMaterial(c);
			if(m != null) {
			e.setLine(3, m.toString());
			
			List<String> Signs = ySign.getStringList("Locations");
            
            Signs.add(locToString(e.getBlock().getLocation()));
            
            ySign.set("Locations", Signs);
            
            List<String> TruhenLoc = ySign.getStringList("LocationsT");
            
            TruhenLoc.add(locToString(chest.getLocation()));
            
            ySign.set("LocationsT", TruhenLoc);
            
			try {
                ySign.save(Sign);
                p.sendMessage(prefix + "§7Dein Shop wurde erstellt.");
                Economy.removeMoney(p.getUniqueId().toString(), 40, "erstellen eines Shop-Schildes");
                e.setLine(0, "§3" + p.getName());
              } catch (IOException e1) {
                e1.printStackTrace();
                p.sendMessage(prefix + "§cFEHLER");
              }
			
			}else {
				 e.setCancelled(true);
                 p.closeInventory();
                 e.getBlock().breakNaturally();
                 p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                 p.sendMessage(prefix + "§cBitte lege ein Item in die Kiste!");
			}
				
				}else {
					
					Location signloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY()-1, e.getBlock().getLocation().getZ());
					
					Block chest = null;
					
					if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
						chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
					}else if(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
						chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
					}else if(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
						chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
					}else if(e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
						chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
					}else if(signloc.getBlock().getType() == Material.CHEST) {
						chest = signloc.getBlock();
					}else {
						e.getBlock().breakNaturally();
						p.sendMessage(prefix + "§cDu musst das Schild an einer Kiste platzieren!");
					}
					
					Inventory c = ((Chest)chest.getState()).getInventory();
					
					if(Material.getMaterial(e.getLine(3).toUpperCase()) != null) {
					
					List<String> Signs = ySign.getStringList("Locations");
		            
		            Signs.add(locToString(e.getBlock().getLocation()));
		            
		            ySign.set("Locations", Signs);
		            
		            List<String> TruhenLoc = ySign.getStringList("LocationsT");
		            
		            TruhenLoc.add(locToString(chest.getLocation()));
		            
		            ySign.set("LocationsT", TruhenLoc);
					try {
		                ySign.save(Sign);
		                p.sendMessage(prefix + "§7Dein Shop wurde erstellt.");
		                Economy.removeMoney(p.getUniqueId().toString(), 40, "erstellen eines Shop-Schildes");
		                e.setLine(0, "§3" + p.getName());
		              } catch (IOException e1) {
		                e1.printStackTrace();
		                p.sendMessage(prefix + "§cFEHLER");
		              }
					
					}else {
						 e.setCancelled(true);
		                 p.closeInventory();
		                 e.getBlock().breakNaturally();
		                 p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
		                 p.sendMessage(prefix + "§cBitte ein Item angeben!");
					}
					
				}
				}else {
					e.setCancelled(true);
	                 p.closeInventory();
	                 e.getBlock().breakNaturally();
	                 p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
	                 p.sendMessage(prefix + "§cBitte gib ein Fragezeichen an um die letzte Zeile zu Autofillen!");
				}
			            }else {
			            	
			            	 e.setCancelled(true);
			                 p.closeInventory();
			                 e.getBlock().breakNaturally();
			                 p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
			                 p.sendMessage(prefix + "§cBitte gebe als Preis eine Zahl (auch mit punkt als komma) an!");
			                 
			            }
			          }else {
			        	  
			        	  e.setCancelled(true);
			              p.closeInventory();
			              e.getBlock().breakNaturally();
			              p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
			              p.sendMessage(prefix  + "§cBitte gebe als Anzahl eine Zahl an!");
			        	  
			          }
			}else {
				e.setCancelled(true);
                p.closeInventory();
                e.getBlock().breakNaturally();
				p.sendMessage(prefix + "§cVerwende: sell oder buy");
			}
		
			//* Bank
		}else if(e.getLine(0).startsWith("*")) {
			String bank = e.getLine(0).replace("*", "");
			bank = bank.replace("*", "");
			if(Economy.existAcc(bank) && Economy.getMembers(bank).contains(e.getPlayer().getUniqueId().toString())) {
			
			if(e.getLine(2).toLowerCase().startsWith("buy:") || e.getLine(2).toLowerCase().startsWith("b:") || e.getLine(2).toLowerCase().startsWith("sell:") || e.getLine(2).toLowerCase().startsWith("s:")) {
		          if(e.getLine(1).matches("[0-9]+")) {
		        	  String[] line2 = e.getLine(2).substring(2).split(" ");
		        		 if(line2[1].matches("[0.0-9.0]+")) {
		            	
			//if sell or buy
			if(!e.getLine(3).equals("")) {
				if(e.getLine(3).equals("?")) {
					
		Location signloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY()-1, e.getBlock().getLocation().getZ());
		
		Block chest = null;
		
		if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
			chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
		}else if(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
			chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
		}else if(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
			chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
		}else if(e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
			chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
		}else if(signloc.getBlock().getType() == Material.CHEST) {
			chest = signloc.getBlock();
		}else {
			e.getBlock().breakNaturally();
			p.sendMessage(prefix + "§cDu musst das Schild an einer Kiste platzieren!");
		}
		
		Inventory c = ((Chest)chest.getState()).getInventory();
		
		Material m = getFirstMaterial(c);
		if(m != null) {
		e.setLine(3, m.toString());
		
		List<String> Signs = ySign.getStringList("Locations");
      
      Signs.add(locToString(e.getBlock().getLocation()));
      
      ySign.set("Locations", Signs);
      
      List<String> TruhenLoc = ySign.getStringList("LocationsT");
      
      TruhenLoc.add(locToString(chest.getLocation()));
      
      ySign.set("LocationsT", TruhenLoc);
      
		try {
          ySign.save(Sign);
          p.sendMessage(prefix + "§7Dein Shop wurde erstellt.");
          Economy.removeMoney(p.getUniqueId().toString(), 40, "erstellen eines Shop-Schildes");
          e.setLine(0, "§3*" + bank);
        } catch (IOException e1) {
          e1.printStackTrace();
          p.sendMessage(prefix + "§cFEHLER");
        }
		
		}else {
			 e.setCancelled(true);
           p.closeInventory();
           e.getBlock().breakNaturally();
           p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
           p.sendMessage(prefix + "§cBitte lege ein Item in die Kiste!");
		}
			
			}else {
				
				Location signloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY()-1, e.getBlock().getLocation().getZ());
				
				Block chest = null;
				
				if(e.getBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
					chest = e.getBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
				}else if(e.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
					chest = e.getBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
				}else if(e.getBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
					chest = e.getBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
				}else if(e.getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
					chest = e.getBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
				}else if(signloc.getBlock().getType() == Material.CHEST) {
					chest = signloc.getBlock();
				}else {
					e.getBlock().breakNaturally();
					p.sendMessage(prefix + "§cDu musst das Schild an einer Kiste platzieren!");
				}
				
				Inventory c = ((Chest)chest.getState()).getInventory();
				
				if(Material.getMaterial(e.getLine(3).toUpperCase()) != null) {
				
				List<String> Signs = ySign.getStringList("Locations");
	            
	            Signs.add(locToString(e.getBlock().getLocation()));
	            
	            ySign.set("Locations", Signs);
	            
	            List<String> TruhenLoc = ySign.getStringList("LocationsT");
	            
	            TruhenLoc.add(locToString(chest.getLocation()));
	            
	            ySign.set("LocationsT", TruhenLoc);
				try {
	                ySign.save(Sign);
	                p.sendMessage(prefix + "§7Dein Shop wurde erstellt.");
	                Economy.removeMoney(p.getUniqueId().toString(), 40, "erstellen eines Shop-Schildes");
	                e.setLine(0, "§3*" + bank);
	              } catch (IOException e1) {
	                e1.printStackTrace();
	                p.sendMessage(prefix + "§cFEHLER");
	              }
				
				}else {
					 e.setCancelled(true);
	                 p.closeInventory();
	                 e.getBlock().breakNaturally();
	                 p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
	                 p.sendMessage(prefix + "§cBitte ein Item angeben!");
				}
				
			}
			}else {
				e.setCancelled(true);
               p.closeInventory();
               e.getBlock().breakNaturally();
               p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
               p.sendMessage(prefix + "§cBitte gib ein Fragezeichen an um die letzte Zeile zu Autofillen!");
			}
		            }else {
		            	
		            	 e.setCancelled(true);
		                 p.closeInventory();
		                 e.getBlock().breakNaturally();
		                 p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
		                 p.sendMessage(prefix + "§cBitte gebe als Preis eine Zahl (auch mit punkt als komma) an!");
		                 
		            }
		          }else {
		        	  
		        	  e.setCancelled(true);
		              p.closeInventory();
		              e.getBlock().breakNaturally();
		              p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
		              p.sendMessage(prefix  + "§cBitte gebe als Anzahl eine Zahl an!");
		        	  
		          }
		}else {
			e.setCancelled(true);
          p.closeInventory();
          e.getBlock().breakNaturally();
			p.sendMessage(prefix + "§cVerwende: sell oder buy");
		}
		}else {
			e.setCancelled(true);
	          p.closeInventory();
	          e.getBlock().breakNaturally();
				p.sendMessage(prefix + "§cDer Account existiert nicht oder du hast keinen Zugriff darauf!");
		}
		}
			}else {
				p.sendMessage(prefix + "§cDu benötigst zum erstellen eines ChestShops §340 " + COMMAND_Eco.moneyformat + " !");
			}
	}else {
		p.sendMessage(prefix + "§cShopKisten können nur auf deinem GS aufgestellt werden!");
	}
		}
	}
	
	@EventHandler
	public void onSignInteract(PlayerInteractEvent e){
		
		File Sign = new File("plugins/ShopSystem/Signs.yml");
		YamlConfiguration ySign = YamlConfiguration.loadConfiguration(Sign);
		
		if(e.getClickedBlock() != null) {
		if(e.getClickedBlock().getState() instanceof Chest){
			
			List<String> TruhenLoc = ySign.getStringList("LocationsT");
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(TruhenLoc.contains(locToString(e.getClickedBlock().getLocation()))) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(prefix + "§cDies ist eine ShopKiste!");
			}
			}
		}
		
		 if(e.getClickedBlock().getState() instanceof Sign){
			 Sign s = (Sign) e.getClickedBlock().getState();
			List<String> Signs = ySign.getStringList("Locations");
			if(Signs.contains(locToString(e.getClickedBlock().getLocation()))) {
			if(e.getPlayer().getItemInHand().getType() == Material.STICK) {
				
				Player p = e.getPlayer();
				
				Block chest = null;
				List<String> TruhenLoc = ySign.getStringList("LocationsT");
                
                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
                  
                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
                }
                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
                  
                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
                }
                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
                  
                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
                }
                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
                  
                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
                } 
                inv.add(p);
                Inventory c;
                if(((Chest)chest.getState()).getInventory().getSize() == 9*3) {
                	c = Bukkit.createInventory(null, 9*3, "Spähkiste");
                }else {
                	c = Bukkit.createInventory(null, 9*6, "Spähkiste");
                }
                c.setContents(((Chest)chest.getState()).getInventory().getContents());
				p.openInventory(c);
				
			}else {
				if(s.getLine(0).equals("§3" + e.getPlayer().getName())) {
					Block chest = null;
					List<String> TruhenLoc = ySign.getStringList("LocationsT");
	                
	                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
	                  
	                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
	                }
	                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
	                  
	                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
	                }
	                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
	                  
	                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
	                }
	                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
	                  
	                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
	                } 
	                
	                
	                Inventory c = ((Chest)chest.getState()).getInventory();
					if(TruhenLoc.contains(locToString(chest.getLocation()))) {
						if(!e.getPlayer().isSneaking()) {
							e.getPlayer().openInventory(c);
						}
					}
					
				}else if(s.getLine(0).startsWith("§3*")){
					String bank = s.getLine(0).replace("§3*", "");
					bank = bank.replace("*", "");
					if(Economy.getMembers(bank).contains(e.getPlayer().getUniqueId().toString())) {
						
						Block chest = null;
						List<String> TruhenLoc = ySign.getStringList("LocationsT");
		                
		                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
		                } 
		                
		                Inventory c = ((Chest)chest.getState()).getInventory();
						if(TruhenLoc.contains(locToString(chest.getLocation()))) {
							if(!e.getPlayer().isSneaking()) {
								e.getPlayer().openInventory(c);
							}
						}
						
					}else {
						
						//Bank acc
						
						if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							Player p = e.getPlayer();
							
							//Kaufen
							if (s.getLine(2).toLowerCase().startsWith("buy:") || s.getLine(2).toLowerCase().startsWith("b:")) {
								String[] line2 = s.getLine(2).substring(2).split(" ");
								
								String anzahls = s.getLine(1);
								String preiss = line2[1];
								
								Integer anzahl = Integer.valueOf(anzahls);
								Double preis = Double.valueOf(preiss);
								
							Block chest = null;
							
			                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
			                }
			                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
			                }
			                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
			                }
			                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
			                }
			                
			                Inventory c = ((Chest)chest.getState()).getInventory();
			                				
			                String matS = s.getLine(3).toUpperCase();
			                Material mat = Material.getMaterial(matS);
			                
			                Integer vorhandeninKiste = getAmount(c, mat);
			                
			                if(!buypause.contains(p)) {
			                	if(vorhandeninKiste < anzahl) {
			                		p.sendMessage(prefix + "§cDieser Shop ist leider ausverkauft.");
			                	}else {
			                		
			                		if(de.koppy.eco.Economy.hasEnoughMoney(p.getUniqueId().toString(), preis)) {
			                				if(EVENT_Adminshop.isInvFullAfter(p, anzahl) == false) {
			                				de.koppy.eco.Economy.removeMoney(p.getUniqueId().toString(), preis, "gezahlt an Konto " + bank + " für Item aus ChestShop (" + anzahl + "x " + mat.toString() + ")");
				                			p.getInventory().addItem(new ItemStack(mat, anzahl));
				                			removeAmount(c, mat, anzahl);
				                			String fpreis = f.format(preis);
				                			Economy.addBankBalance(bank, preis, "ChestShop verkauf (" + anzahl + "x " + mat.toString() + ")");
				                			p.sendMessage(prefix + "§7Du hast gerade §3" + anzahl + " " + matS + " §7für §a" + fpreis + COMMAND_Eco.moneyformat + " §7gekauft!");
				                			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c-"+fpreis+COMMAND_Eco.moneyformat));
				                			
				                			buypause.add(p);
				                			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
												
												@Override
												public void run() {
													buypause.remove(p);
													
												}
											}, 20);
			                				}else {
			                					p.sendMessage(prefix + "§cIn deinem Inventar ist nicht genug Platz!");
			                				}
			                			
			                		}else {
			                			p.sendMessage(prefix + "§cDu hast nicht genug Geld!");
			                		}
			                	}
			                }else {
			                	p.sendMessage(prefix + "§cWarte kurz bevor du wieder einkaufst!");
			                }
			                
						}else {
							e.getPlayer().sendMessage(prefix + "§cHier gibt es nichts zu kaufen.");
						}
							
							
						}else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
							Player p = e.getPlayer();
							//Verkaufen
							if (s.getLine(2).toLowerCase().startsWith("sell:") || s.getLine(2).toLowerCase().startsWith("s:")) {
								String[] line2 = s.getLine(2).substring(2).split(" ");
								
								String anzahls = s.getLine(1);
								String preiss = line2[1];
								
								Integer anzahl = Integer.valueOf(anzahls);
								Double preis = Double.valueOf(preiss);
								
							Block chest = null;
							
			                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
			                }
			                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
			                }
			                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
			                }
			                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
			                  
			                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
			                }
			                
			                
			                Inventory c = ((Chest)chest.getState()).getInventory();
			                				
			                String matS = s.getLine(3).toUpperCase();
			                Material mat = Material.getMaterial(matS);
			                
			                Integer vorhandeninSpieler = getAmount(p.getInventory(), mat);
							
			                if(!buypause.contains(p)) {
			                	
			                	if(vorhandeninSpieler < anzahl) {
			                		p.sendMessage(prefix + "§cDu hast das Item nicht oft genug.");
			                	}else {
			                		if(de.koppy.eco.Economy.hasEnoughMoneyOnBank(bank, preis)) {
			                			if(EVENT_Adminshop.isInvFullAfter(c, anzahl) == false) {
			                			de.koppy.eco.Economy.removeMoney(bank, preis, "gezahlt an " + p.getName() + " im ChestShop für ("+anzahl+"x "+mat.toString() + ")");
			                			c.addItem(new ItemStack(mat, anzahl));
			                			removeAmount(p.getInventory(), mat, anzahl);
			                			de.koppy.eco.Economy.addMoney(p.getUniqueId().toString(), preis, "erhalten von Konto " + bank + " im ChestShop für ("+anzahl+"x "+mat.toString() + ")");
			                			
			                			String fpreis = f.format(preis);
			                			p.sendMessage(prefix + "§7Du hast gerade §3" + anzahl + " " + matS + " §7für §a" + fpreis +COMMAND_Eco.moneyformat + " §7verkauft!");
			                			p.sendMessage("§a+"+fpreis+COMMAND_Eco.moneyformat);
			                			buypause.add(p);
			                			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
											
											@Override
											public void run() {
												buypause.remove(p);
												
											}
										}, 20);
			                			}else {
			                				p.sendMessage(prefix + "§cDie Kiste ist voll !");
			                			}
			                		}else {
			                			p.sendMessage(prefix + "§cDer Spieler des Shops hat nicht genug Geld!");
			                		}
			                		
			                		
			                	}
			                		
			                	}else {
			                		p.sendMessage(prefix + "§cWarte bitte kurz bevor du wieder einkaufst!");
			                	}
			                	
			                	
								}else {
									e.getPlayer().sendMessage(prefix + "§cHier gibt es nichts zu verkaufen.");
								}
						}
						
					}
				}else {
					Player p = e.getPlayer();
					if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						//kaufen
						if (s.getLine(2).toLowerCase().startsWith("buy:") || s.getLine(2).toLowerCase().startsWith("b:")) {
							String[] line2 = s.getLine(2).substring(2).split(" ");
							
							String anzahls = s.getLine(1);
							String preiss = line2[1];
							
							Integer anzahl = Integer.valueOf(anzahls);
							Double preis = Double.valueOf(preiss);
							
						Block chest = null;
						
		                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
		                }
		                
		                Inventory c = ((Chest)chest.getState()).getInventory();
		                				
		                String matS = s.getLine(3).toUpperCase();
		                Material mat = Material.getMaterial(matS);
		                
		                Integer vorhandeninKiste = getAmount(c, mat);
		                Material m = getFirstMaterial(c);
		                
		                if(!buypause.contains(p)) {
		                	if(vorhandeninKiste < anzahl) {
		                		p.sendMessage(prefix + "§cDieser Shop ist leider ausverkauft.");
		                	}else {
		                		
		                		if(de.koppy.eco.Economy.hasEnoughMoney(p.getUniqueId().toString(), preis)) {
		                			Player t = Bukkit.getPlayer(s.getLine(0).replace("§3", ""));
		                			if(t != null) {
		                				if(EVENT_Adminshop.isInvFullAfter(p, anzahl) == false) {
		                				de.koppy.eco.Economy.removeMoney(p.getUniqueId().toString(), preis, "gezahlt an " + t.getName() + " für Item aus ChestShop (" + anzahl + "x " + mat.toString() + ")");
			                			p.getInventory().addItem(new ItemStack(mat, anzahl));
			                			removeAmount(c, mat, anzahl);
			                			de.koppy.eco.Economy.addMoney(t.getUniqueId().toString(), preis, "erhalten von " + p.getName() + " für Item aus ChestShop (" + anzahl + "x " + mat.toString() + ")");	//Auch wenn offline UUID bekommen!
			                			
			                			p.sendMessage(prefix + "§7Du hast gerade §3" + anzahl + " " + matS + " §7für §a" + preis + " Euro §7gekauft!");
			                			p.sendMessage("§c-" + preis);
			                			t.sendMessage(prefix + "§7Bei dir wurde §agerade §7eingekauft!");
			                			t.sendMessage("§a+" + preis);
			                			
			                			buypause.add(p);
			                			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
											
											@Override
											public void run() {
												buypause.remove(p);
												
											}
										}, 20);
		                				}else {
		                					p.sendMessage(prefix + "§cIn deinem Inventar ist nicht genug Platz!");
		                				}
		                			}else {
		                			
		                			
		                			String UUID = API_UUID.getUUIDorNAME(s.getLine(0).replace("§3", ""));
		                			
		                			if(API_UUID.existUUIDorNAME(UUID)) {
		                				if(EVENT_Adminshop.isInvFullAfter(p, anzahl) == false) {
		                			de.koppy.eco.Economy.removeMoney(p.getUniqueId().toString(), preis, "gezahlt an " + s.getLine(0) + " für Item aus ChestShop (" + anzahl + "x " + mat.toString() + ")");
		                			p.getInventory().addItem(new ItemStack(mat, anzahl));
		                			removeAmount(c, mat, anzahl);
		                			
		                			de.koppy.eco.Economy.addMoney(UUID, preis, "erhalten von " + p.getName() + " für Item aus ChestShop (" + anzahl + "x " + mat.toString() + ")");	//Auch wenn offline UUID bekommen!
		                			p.sendMessage(prefix + "§7Du hast gerade §3" + anzahl + " " + matS + " §7für §a" + preis + " Euro §7gekauft!");
		                			
		                			buypause.add(p);
		                			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
										
										@Override
										public void run() {
											buypause.remove(p);
											
										}
									}, 20);
		                				}else {
		                					p.sendMessage(prefix + "§cIn deinem Inventar ist nicht genug Platz!");
		                				}
		                			}else {
		                				p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Netzwerk. Ruf einen §4ADMIN§c!!!");
		                			}
		                			}
		                			
		                		}else {
		                			p.sendMessage(prefix + "§cDu hast nicht genug Geld!");
		                		}
		                	}
		                }else {
		                	p.sendMessage(prefix + "§cWarte kurz bevor du wieder einkaufst!");
		                }
		                	
							}else {
								e.getPlayer().sendMessage(prefix + "§cHier gibt es nichts zu kaufen.");
							}
					}else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
						//verkaufen
						if (s.getLine(2).toLowerCase().startsWith("sell:") || s.getLine(2).toLowerCase().startsWith("s:")) {
							String[] line2 = s.getLine(2).substring(2).split(" ");
							
							String anzahls = s.getLine(1);
							String preiss = line2[1];
							
							Integer anzahl = Integer.valueOf(anzahls);
							Double preis = Double.valueOf(preiss);
							
							
						Block chest = null;
						
		                if (e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.NORTH).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.WEST).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.EAST).getLocation().getBlock();
		                }
		                else if (e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST) {
		                  
		                  chest = e.getClickedBlock().getRelative(BlockFace.SOUTH).getLocation().getBlock();
		                }
		                
		                
		                Inventory c = ((Chest)chest.getState()).getInventory();
		                				
		                String matS = s.getLine(3).toUpperCase();
		                Material mat = Material.getMaterial(matS);
		                
		                Integer vorhandeninSpieler = getAmount(p.getInventory(), mat);
		                Material m = getFirstMaterial(c);
		                	
		                if(!buypause.contains(p)) {
		                	
		                	if(vorhandeninSpieler < anzahl) {
		                		p.sendMessage(prefix + "§cDu hast das Item nicht oft genug.");
		                	}else {
		                		Player t = Bukkit.getPlayer(s.getLine(0).replace("§3", ""));
		                		if(t != null) {
		                		if(de.koppy.eco.Economy.hasEnoughMoney(t.getUniqueId().toString(), preis)) {	//Auch wenn offline UUID bekommen!
		                			if(EVENT_Adminshop.isInvFullAfter(c, anzahl) == false) {
		                			de.koppy.eco.Economy.removeMoney(t.getUniqueId().toString(), preis, "gezahlt an " + p.getName() + " im ChestShop für ("+anzahl+"x "+mat.toString() + ")");	//Auch wenn offline UUID bekommen!
		                			c.addItem(new ItemStack(mat, anzahl));
		                			removeAmount(p.getInventory(), mat, anzahl);
		                			
		                			de.koppy.eco.Economy.addMoney(p.getUniqueId().toString(), preis, "erhalten von " + s.getLine(0) + " im ChestShop für ("+anzahl+"x "+mat.toString() + ")");
		                			
		                			p.sendMessage(prefix + "§7Du hast gerade §3" + anzahl + " " + matS + " §7für §a" + preis + " Euro §7verkauft!");
		                			p.sendMessage("§a+" + preis);
		                			t.sendMessage(prefix + "§7Bei dir wurde §agerade §7vergekauft!");
		                			t.sendMessage("§c-" + preis);
		                			buypause.add(p);
		                			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
										
										@Override
										public void run() {
											buypause.remove(p);
											
										}
									}, 20);
		                			}else {
		                				p.sendMessage(prefix + "§cDie Kiste ist voll !");
		                			}
		                		}else {
		                			p.sendMessage(prefix + "§cDer Spieler des Shops hat nicht genug Geld!");
		                		}
		                		
		                		}else {
		                			
		                			String UUID = API_UUID.getUUIDorNAME(s.getLine(0).replace("§3", ""));
		                			
		                			if(API_UUID.existUUIDorNAME(UUID)) {
		                			if(de.koppy.eco.Economy.hasEnoughMoney(UUID, preis)) {	//Auch wenn offline UUID bekommen!
		                				if(EVENT_Adminshop.isInvFullAfter(c, anzahl) == false) {
		                				de.koppy.eco.Economy.removeMoney(UUID, preis, "gezahlt an " + p.getName() + " im ChestShop ("+anzahl+"x "+mat.toString() + ")");	//Auch wenn offline UUID bekommen!
			                			c.addItem(new ItemStack(mat, anzahl));
			                			removeAmount(p.getInventory(), mat, anzahl);
			                			
			                			de.koppy.eco.Economy.addMoney(p.getUniqueId().toString(), preis, "erhalten von " + s.getLine(0) + " im ChestShop für ("+anzahl+"x "+mat.toString() + ")");
			                			
			                			p.sendMessage(prefix + "§7Du hast gerade §3" + anzahl + " " + matS + " §7für §a" + preis + " Euro §7verkauft!");
			                			
			                			buypause.add(p);
			                			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
											
											@Override
											public void run() {
												buypause.remove(p);
												
											}
										}, 20);
		                				}else {
			                				p.sendMessage(prefix + "§cDie Kiste ist voll!");
			                			}
			                		}else {
			                			p.sendMessage(prefix + "§cDer Spieler des Shops hat nicht genug Geld!");
			                		}
		                			}else {
		                				p.sendMessage(prefix + "§cDer Spieler war noch nie auf dem Netzwerk. Bitte melde dies einem §4Admin§c!!!");
		                			}
		                			
		                		}
		                		
		                	}
		                		
		                	}else {
		                		p.sendMessage(prefix + "§cWarte bitte kurz bevor du wieder einkaufst!");
		                	}
		                	
		                	
							}else {
								e.getPlayer().sendMessage(prefix + "§cHier gibt es nichts zu verkaufen.");
							}
							}
					
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(inv.contains(e.getWhoClicked())) {
			e.setCancelled(true);
		}
	}
	
	public static void removeAmount(Inventory c, ItemStack m, Integer toremove) {
		  
		  Integer torem = toremove;
		  
		  ItemStack test = null;
		  for(int i = 0 ; i < c.getSize() ; i++) {
	          	
	          	if(c.getItem(i) != null) {
	          		if(c.getItem(i).getType() == m.getType()) {
	          			
	          				test = c.getItem(i);
	          				
	          				if(torem > test.getAmount()) {
		          				torem=torem-test.getAmount();
		          				c.setItem(i, new ItemStack(Material.AIR));
		          				
		          			}else if(torem < test.getAmount() && torem > 0){
		          				Integer t = test.getAmount()-torem;
		          				torem = 0;
		          				ItemStack neues = new ItemStack(m.getType(), t);
		          				c.setItem(i, neues);
		          			}
	          				
	          				
	          			}
	          		}
	          	}
		  
	  }
	  public static void removeAmount(Inventory c, Material m, Integer toremove) {
		  
		  Integer torem = toremove;
		  
		  ItemStack test = null;
		  for(int i = 0 ; i < c.getSize() ; i++) {
	          	
	          	if(c.getItem(i) != null) {
	          		if(c.getItem(i).getType() == m) {
	          			test = c.getItem(i);
	          			
	          			if(torem >= test.getAmount()) {
	          				torem=torem-test.getAmount();
	          				c.setItem(i, new ItemStack(Material.AIR));
	          				
	          			}else if(torem < test.getAmount() && torem > 0){
	          				Integer t = test.getAmount()-torem;
	          				torem = 0;
	          				ItemStack neues = new ItemStack(m, t);
	          				c.setItem(i, neues);
	          			}
	          		}
	          	}
	          	
	          }
		  
	  }
	  
	  public static Integer getAmount(Inventory c, Material m) {
		  
		  Integer zahl = 0;
		  ItemStack test = null;
        for(int i = 0 ; i < c.getSize() ; i++) {
        	
        	if(c.getItem(i) != null) {
        		if(c.getItem(i).getType() == m) {
        			test = c.getItem(i);
        			
        	 		zahl = zahl+test.getAmount();
        	 		
        		}
        	}
        	
        }
        return zahl;
		  
	  }
	
	public static String locToString(Location l) { return String.valueOf(l.getWorld().getName()) + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ(); }
	
	  public static Location stringToLoc(String s) {
	    String[] a = s.split("\\,");
	    
	    World w = Bukkit.getWorld(a[0]);
	    
	    float x = Float.parseFloat(a[1]);
	    float y = Float.parseFloat(a[2]);
	    float z = Float.parseFloat(a[3]);
	    
	    return new Location(w, x, y, z);
	  }
	  
	  public static Material getFirstMaterial(Inventory c) {
		  Material m = null;
		  ItemStack test = null;
		  for(int i = 0 ; i < 27 ; i++) {
	          	
	          	if(c.getItem(i) != null) {
	          			test = c.getItem(i);
	          			m = test.getType();
	          			break;
	          	}
	         }
		return m;
	  }
	
}
