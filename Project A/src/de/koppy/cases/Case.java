package de.koppy.cases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.koppy.chestshop.EVENT_Adminshop;
import de.koppy.eco.COMMAND_Eco;
import de.koppy.eco.Economy;
import de.koppy.handler.ProfileHandler;
import de.koppy.landsystem.API_Land;
import de.koppy.landsystem.COMMAND_Claims;
import de.koppy.landsystem.LandMenu;
import de.koppy.project.Main;
import de.koppy.standardcmds.COMMAND_Warp;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Case {
	
	private static Main main;
	public static HashMap<Player, String> inCase = new HashMap<>();
	public static ArrayList<Player> inCaseInv = new ArrayList<>();
	
	public static Cases getCase(ItemStack cases) {
		if(cases.getItemMeta().getDisplayName().startsWith("§5§lVote")) {
			return Cases.VOTE;
		}else if(cases.getItemMeta().getDisplayName().startsWith("§3§lRandom")) {
			return Cases.RANDOM;
		}else {
			return null;
		}
	}
	
	public static boolean isKeyInInv(Player p, Cases cases) {
		for(int i=0; i<p.getInventory().getSize(); i++) {
			if(p.getInventory().getItem(i) != null) {
			if(p.getInventory().getItem(i).getItemMeta().getDisplayName().equals(giveCaseKey(cases).getItemMeta().getDisplayName())) {
				if(p.getInventory().getItem(i).getItemMeta().getEnchants().equals(giveCaseKey(cases).getItemMeta().getEnchants())){
					return true;
				}
			}
			}
		}
		return false;
	}
	
	public static void removeKeyInInv(Player p, Cases cases) {
		for(int i=0; i<p.getInventory().getSize(); i++) {
			if(p.getInventory().getItem(i) != null) {
			if(p.getInventory().getItem(i).getItemMeta().getDisplayName().equals(giveCaseKey(cases).getItemMeta().getDisplayName())) {
				if(p.getInventory().getItem(i).getItemMeta().getEnchants().equals(giveCaseKey(cases).getItemMeta().getEnchants())){
					p.getInventory().remove(p.getInventory().getItem(i));
					break;
				}
			}
			}
		}
	}
	
	public static boolean isCase(ItemStack cases) {
		if(getCase(cases) != null) return true;
		else return false;
	}
	
	public static boolean existCase(String cases) {
		for(Cases Ccases : Cases.values()) if(Ccases.toString().equalsIgnoreCase(cases)) return true;
		return false;
	}
	
	public static ItemStack giveCase(Cases cases) {
		if(cases == Cases.VOTE) {
			ItemStack vote = new ItemStack(Material.WHITE_SHULKER_BOX);
			ItemMeta voteM = vote.getItemMeta();
			voteM.setDisplayName("§5§lVote");
			vote.setItemMeta(voteM);
			return vote;
		}else if(cases == Cases.RANDOM) {
			ItemStack random = new ItemStack(Material.WHITE_SHULKER_BOX);
			ItemMeta randomM = random.getItemMeta();
			randomM.setDisplayName("§3§lRandom");
			random.setItemMeta(randomM);
			return random;
		}else {
			return null;
		}
	}
	
	public static ItemStack giveCaseKey(Cases cases) {
		if(cases == Cases.VOTE) {
			ItemStack vote = new ItemStack(Material.TRIPWIRE_HOOK);
			ItemMeta voteM = vote.getItemMeta();
			voteM.setDisplayName("§5§lVote-Key");
			voteM.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			Random rndm = new Random();
			voteM.setLocalizedName(""+rndm.nextInt(100000000));
			voteM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			vote.setItemMeta(voteM);
			return vote;
		}else if(cases == Cases.RANDOM) {
			ItemStack random = new ItemStack(Material.TRIPWIRE_HOOK);
			ItemMeta randomM = random.getItemMeta();
			randomM.setDisplayName("§3§lRandom-Key");
			randomM.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			Random rndm = new Random();
			randomM.setLocalizedName(""+rndm.nextInt(100000000));
			randomM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			random.setItemMeta(randomM);
			return random;
		}else {
			return null;
		}
	}
	
	
	public static List<ItemStack> getCaseWins(Cases cases) {
		List<ItemStack> casewins = new ArrayList<>();
		
		if(cases == Cases.VOTE) {
			
			ItemStack paper100 = new ItemStack(Material.SUNFLOWER);
			ItemMeta paper100m = paper100.getItemMeta();
			paper100m.setDisplayName("§a+ 100" + COMMAND_Eco.moneyformat + " §7!!!");
			List<String> paper100mlore = new ArrayList<>();
			paper100mlore.add("");
			paper100mlore.add("§8| §6Seltenheit: §7Common");
			paper100mlore.add("§8| §6Typ: §3Money");
			paper100mlore.add("§8| §5Geschätzter Wert: §3100" + COMMAND_Eco.moneyformat);
			paper100m.setLore(paper100mlore);
			paper100.setItemMeta(paper100m);
			
			ItemStack paper1000 = new ItemStack(Material.HONEYCOMB);
			ItemMeta paper1000m = paper1000.getItemMeta();
			paper1000m.setDisplayName("§a+ 500" + COMMAND_Eco.moneyformat + " §7!!!");
			List<String> paper1000mlore = new ArrayList<>();
			paper1000mlore.add("");
			paper1000mlore.add("§8| §6Seltenheit: §3Uncommon");
			paper1000mlore.add("§8| §6Typ: §3Money");
			paper1000mlore.add("§8| §5Geschätzter Wert: §3500" + COMMAND_Eco.moneyformat);
			paper1000m.setLore(paper1000mlore);
			paper1000.setItemMeta(paper1000m);
			
			ItemStack randomCase = new ItemStack(Material.WHITE_SHULKER_BOX);
			ItemMeta randomCasem = randomCase.getItemMeta();
			randomCasem.setDisplayName("§6§k!!! §r§3RandomCase §6§k!!!");
			List<String> randomCasemlore = new ArrayList<>();
			randomCasemlore.add("");
			randomCasemlore.add("§8| §6Seltenheit: §5Epic");
			randomCasemlore.add("§8| §6Typ: §3Case");
			randomCasemlore.add("§8| §5Geschätzter Wert: §32.000" + COMMAND_Eco.moneyformat);
			randomCasem.setLore(randomCasemlore);
			randomCase.setItemMeta(randomCasem);
			
			ItemStack randomKey = new ItemStack(Material.TRIPWIRE_HOOK);
			ItemMeta randomKeym = randomKey.getItemMeta();
			randomKeym.setDisplayName("§6§k!!! §r§3Random-Key §6§k!!!");
			randomKeym.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			randomKeym.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			randomKeym.setLocalizedName("1");
			List<String> randomKeymlore = new ArrayList<>();
			randomKeymlore.add("");
			randomKeymlore.add("§8| §6Seltenheit: §6Legendary");
			randomKeymlore.add("§8| §6Typ: §3Key");
			randomKeymlore.add("§8| §5Geschätzter Wert: §310.000" + COMMAND_Eco.moneyformat);
			randomKeym.setLore(randomKeymlore);
			randomKey.setItemMeta(randomKeym);
			
			ItemStack gold = new ItemStack(Material.GOLD_INGOT, 16);
			ItemMeta goldm = gold.getItemMeta();
			goldm.setDisplayName("§6Gold");
			List<String> goldmlore = new ArrayList<>();
			goldmlore.add("");
			goldmlore.add("§8| §6Seltenheit: §3Uncommon");
			goldmlore.add("§8| §6Typ: §3Item");
			goldmlore.add("§8| §5Geschätzter Wert: §3~ 500" + COMMAND_Eco.moneyformat);
			goldm.setLore(goldmlore);
			gold.setItemMeta(goldm);
			
			ItemStack claims = new ItemStack(Material.MUSIC_DISC_PIGSTEP);
			ItemMeta claimsm = claims.getItemMeta();
			claimsm.setDisplayName("§e3x §lLand-Claims §r§7!!!");
			claimsm.setLocalizedName("3");
			List<String> claimsmlore = new ArrayList<>();
			claimsmlore.add("");
			claimsmlore.add("§8| §6Seltenheit: §5Epic");
			claimsmlore.add("§8| §6Typ: §3Claims");
			claimsmlore.add("§8| §5Geschätzter Wert: §310.000" + COMMAND_Eco.moneyformat);
			claimsm.setLore(claimsmlore);
			claims.setItemMeta(claimsm);
			
			casewins.add(paper100);
			casewins.add(paper1000);
			casewins.add(randomCase);
			casewins.add(randomKey);
			casewins.add(claims);
			casewins.add(gold);
			
		}else if(cases == Cases.RANDOM) {
			
			ItemStack paper100 = new ItemStack(Material.SUNFLOWER);
			ItemMeta paper100m = paper100.getItemMeta();
			paper100m.setDisplayName("§a+ 100" + COMMAND_Eco.moneyformat + " §7!!!");
			List<String> paper100mlore = new ArrayList<>();
			paper100mlore.add("");
			paper100mlore.add("§8| §6Seltenheit: §7Common");
			paper100mlore.add("§8| §6Typ: §3Money");
			paper100mlore.add("§8| §5Geschätzter Wert: §3100" + COMMAND_Eco.moneyformat);
			paper100m.setLore(paper100mlore);
			paper100.setItemMeta(paper100m);
			
			ItemStack claims = new ItemStack(Material.MUSIC_DISC_PIGSTEP);
			ItemMeta claimsm = claims.getItemMeta();
			claimsm.setDisplayName("§e3x §lLand-Claims §r§7!!!");
			claimsm.setLocalizedName("3");
			List<String> claimsmlore = new ArrayList<>();
			claimsmlore.add("");
			claimsmlore.add("§8| §6Seltenheit: §5Epic");
			claimsmlore.add("§8| §6Typ: §3Claims");
			claimsmlore.add("§8| §5Geschätzter Wert: §310.000" + COMMAND_Eco.moneyformat);
			claimsm.setLore(claimsmlore);
			claims.setItemMeta(claimsm);
			
			ItemStack paper1000 = new ItemStack(Material.HONEYCOMB);
			ItemMeta paper1000m = paper1000.getItemMeta();
			paper1000m.setDisplayName("§a+ 1.000" + COMMAND_Eco.moneyformat + " §7!!!");
			List<String> paper1000mlore = new ArrayList<>();
			paper1000mlore.add("");
			paper1000mlore.add("§8| §6Seltenheit: §3Uncommon");
			paper1000mlore.add("§8| §6Typ: §3Money");
			paper1000mlore.add("§8| §5Geschätzter Wert: §32.500" + COMMAND_Eco.moneyformat);
			paper1000m.setLore(paper1000mlore);
			paper1000.setItemMeta(paper1000m);
			
			ItemStack warp = new ItemStack(Material.HOPPER);
			ItemMeta warpm = warp.getItemMeta();
			warpm.setDisplayName("§e1x §lUserWarp §r§7!!!");
			warpm.setLocalizedName("1");
			List<String> warpmlore = new ArrayList<>();
			warpmlore.add("");
			warpmlore.add("§8| §6Seltenheit: §6Exotic");
			warpmlore.add("§8| §6Typ: §3UserWarp");
			warpmlore.add("§8| §5Geschätzter Wert: §3100.000" + COMMAND_Eco.moneyformat);
			warpm.setLore(warpmlore);
			warp.setItemMeta(warpm);
			
			ItemStack randomKey = new ItemStack(Material.TRIPWIRE_HOOK, 4);
			ItemMeta randomKeym = randomKey.getItemMeta();
			randomKeym.setDisplayName("§6§k!!! §r§34x Vote-Key §6§k!!!");
			randomKeym.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			randomKeym.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			randomKeym.setLocalizedName("4");
			List<String> randomKeymlore = new ArrayList<>();
			randomKeymlore.add("");
			randomKeymlore.add("§8| §6Seltenheit: §6Legendary");
			randomKeymlore.add("§8| §6Typ: §3Key");
			randomKeymlore.add("§8| §5Geschätzter Wert: §31.000" + COMMAND_Eco.moneyformat);
			randomKeym.setLore(randomKeymlore);
			randomKey.setItemMeta(randomKeym);
			
			casewins.add(paper100);
			casewins.add(paper1000);
			casewins.add(warp);
			casewins.add(claims);
			casewins.add(randomKey);
			
		}
		
		return casewins;
	}
	
	public static String getRarity(ItemStack istack) {
		return istack.getItemMeta().getLore().get(1).split(" ")[2].substring(2);
	}
	
	public static String getType(ItemStack istack) {
		return istack.getItemMeta().getLore().get(2).split(" ")[2].replace("§3", "");
	}
	
	public static List<String> getRaritys() {
		List<String> raritys = new ArrayList<>();
		raritys.add("Common");
		raritys.add("Uncommon");
		raritys.add("Epic");
		raritys.add("Legendary");
		raritys.add("Exotic");
		return raritys;
	}
	
	public static Inventory openCaseInv(Player p, Cases cases) {
		Integer invgr = getCaseWins(cases).size()/7;
		invgr+=2;
		if(getCaseWins(cases).size()%7 != 0) invgr++;
		Inventory inv = Bukkit.createInventory(null, invgr*9, giveCase(cases).getItemMeta().getDisplayName()+"-Preview");
		LandMenu.fillRandWithGlass(inv);
		for(String rarity : getRaritys()) for(ItemStack istack : getCaseWins(cases)) if(getRarity(istack).equals(rarity)) inv.addItem(istack);
		inCase.put(p, cases.toString());
		
		ItemStack useKey = new ItemStack(Material.END_CRYSTAL);
		ItemMeta useKeym = useKey.getItemMeta();
		useKeym.setDisplayName("§3Kiste mit Key öffnen. §7§o(Rechtsklick)");
		useKey.setItemMeta(useKeym);
		
		ItemStack hasntKey = new ItemStack(Material.END_CRYSTAL);
		ItemMeta hasntKeym = hasntKey.getItemMeta();
		hasntKeym.setDisplayName("§cDu hast keinen Key für diese Kiste im Inventar!");
		hasntKey.setItemMeta(hasntKeym);
		
		if(isKeyInInv(p, cases)) inv.setItem(inv.getSize()-2, useKey);
		else inv.setItem(inv.getSize()-2, hasntKey);
		inCaseInv.add(p);
		
		return inv;
	}
	
	private static HashMap<Integer, ItemStack> gewinnplatzierung = new HashMap<>();
	
	public static Inventory openCase(Player p, Cases cases) {
		
		ProfileHandler.addOpenedCases(p.getUniqueId().toString(), 1);
		Inventory inv = Bukkit.createInventory(null, 5*9, giveCase(cases).getItemMeta().getDisplayName()+"-Case");
		
		ItemStack blueglass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		
		ItemStack gewinnanzeige = new ItemStack(Material.CAMPFIRE);
		ItemMeta gewinnanzeigeM = gewinnanzeige.getItemMeta();
		gewinnanzeigeM.setDisplayName("§eDein Gewinn!");
		gewinnanzeige.setItemMeta(gewinnanzeigeM);
		
		for(int i=0; i<12; i++){
			inv.setItem(i, blueglass);
		}
		
		inv.setItem(15, blueglass);
		inv.setItem(16, blueglass);
		inv.setItem(17, blueglass);
		inv.setItem(18, blueglass);
		inv.setItem(19, blueglass);
		inv.setItem(21, blueglass);
		inv.setItem(22, gewinnanzeige);
		inv.setItem(23, blueglass);
		inv.setItem(25, blueglass);
		inv.setItem(26, blueglass);
		inv.setItem(27, blueglass);
		inv.setItem(28, blueglass);
		inv.setItem(30, blueglass);
		inv.setItem(31, blueglass);
		inv.setItem(32, blueglass);
		inv.setItem(34, blueglass);
		inv.setItem(35, blueglass);
		inv.setItem(36, blueglass);
		inv.setItem(37, blueglass);
		inv.setItem(38, blueglass);
		inv.setItem(40, blueglass);
		inv.setItem(42, blueglass);
		inv.setItem(43, blueglass);
		inv.setItem(44, blueglass);
		
		createWinFor(inv, cases);
		
		Random rndm = new Random();
		Integer anzahl = rndm.nextInt(7);
		anzahl += 33;
		
		final Integer zahl = anzahl;
		for(int i=1; i<anzahl; i++) {
			final Integer ii = i;
			if(i <= 20) {
				Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
					
					@Override
					public void run() {
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
						updateWIN(inv, cases);
						
					}
				}, 2*i);
			}else if(i <= 30 && i > 20) {
				Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
					
					@Override
					public void run() {
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
						updateWIN(inv, cases);
						
					}
				}, 5*i-60);
				
			}else{
				Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
					
					@Override
					public void run() {
						if(ii != zahl-1) {
							p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
							updateWIN(inv, cases);
						}else {
							
							p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
							p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
							p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
							p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
							updateWIN(inv, cases);
							ItemStack gewinnitem = inv.getItem(13);
							p.sendMessage(COMMAND_Case.prefix + "§7Du hast das Item §3" + gewinnitem.getItemMeta().getDisplayName() + " §7gewonnen.");
							if(getType(gewinnitem).equals("Case")) {
								if(EVENT_Adminshop.isInvFullAfter(p, gewinnitem.getAmount()) == false) {
									p.getInventory().addItem(Case.giveCase(Cases.valueOf(gewinnitem.getItemMeta().getDisplayName().split(" ")[1].replace("Case", "").replace("§3", "").toUpperCase())));
								}else {
									p.getWorld().dropItem(p.getLocation(), Case.giveCase(Cases.valueOf(gewinnitem.getItemMeta().getDisplayName().split(" ")[1].replace("Case", "").replace("§3", "").toUpperCase())));
								}
							}else if(getType(gewinnitem).equals("Money")) {
								
								Double money = Double.valueOf(gewinnitem.getItemMeta().getDisplayName().split(" ")[1].replace(".", ""));
								Economy.addMoney(p.getUniqueId().toString(), money, "geluckt in ner Kiste");
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(gewinnitem.getItemMeta().getDisplayName()));
								
							}else if(getType(gewinnitem).equals("Item")) {
								
								ItemStack istack = new ItemStack(gewinnitem.getType(), gewinnitem.getAmount());
								if(EVENT_Adminshop.isInvFullAfter(p, gewinnitem.getAmount()) == false) {
									p.getInventory().addItem(istack);
								}else {
									p.getWorld().dropItem(p.getLocation(), istack);
								}
								
							}else if(getType(gewinnitem).equals("Permission")) {
								String perm = gewinnitem.getItemMeta().getLocalizedName();
								if(p.hasPermission(perm) == false) {
									PermissionsEx.getUser(p).addPermission(perm);
								}else {
									Economy.addMoney(p.getUniqueId().toString(), 100, "auszahlung von Kiste");
									p.sendMessage(COMMAND_Case.prefix + "§cDa du die Permission bereits besitzt, wurde dir Geld gutgeschrieben.");
								}
								
							}else if(getType(gewinnitem).equals("Key")) {
								String amountS = gewinnitem.getItemMeta().getLocalizedName();
								Integer amount = Integer.valueOf(amountS);
								if(EVENT_Adminshop.isInvFullAfter(p, amount) == false) {
									for(int i=0; i<amount; i++) p.getInventory().addItem(Case.giveCaseKey(Cases.valueOf(gewinnitem.getItemMeta().getDisplayName().split(" ")[1].replace("-Key", "").replace("§3", "").toUpperCase())));
								}else {
									for(int i=0; i<amount; i++) p.getWorld().dropItem(p.getLocation(), Case.giveCaseKey(Cases.valueOf(gewinnitem.getItemMeta().getDisplayName().split(" ")[1].replace("-Key", "").replace("§3", "").toUpperCase())));
								}
							}else if(getType(gewinnitem).equals("UserWarp")) {
								String amountS = gewinnitem.getItemMeta().getLocalizedName();
								Integer amount = Integer.valueOf(amountS);
								COMMAND_Warp.addWarpAmount(p.getUniqueId().toString(), amount);
							}else if(getType(gewinnitem).equals("Claims")) {
								String amountS = gewinnitem.getItemMeta().getLocalizedName();
								Integer amount = Integer.valueOf(amountS);
								API_Land.addLandClaims(p.getUniqueId().toString(), amount);
							}
							
						}
					}
				}, 10*i-210);
				}
		}
		
		inCaseInv.add(p);
		return inv;
	}
	
	public static void updateWIN(Inventory inv, Cases cases) {
			//* Case
			
			HashMap<Integer, ItemStack> gewinnmom = new HashMap<>();
				
				for(int i=1; i<9; i++) {
					if(gewinnplatzierung.get(i) != null) {
						ItemStack istack = gewinnplatzierung.get(i);
						gewinnmom.put(i, istack);
					}
				}
				
				for(int i=1; i<10; i++) {
					if(i == 1) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(29, istack);
						}
					}else if(i == 2) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(20, istack);
						}
					}else if(i == 3) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(12, istack);
						}
					}else if(i == 4) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(13, istack);
						}
					}else if(i == 5) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(14, istack);
						}
					}else if(i == 6) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(24, istack);
						}
					}else if(i == 7) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(33, istack);
						}
					}else if(i == 8) {
						if(gewinnmom.get(i) != null) {
							ItemStack istack = gewinnmom.get(i);
							inv.setItem(41, istack);
						}
					}
					
				}
				
				for(int i=1; i<9; i++) {
					if(gewinnmom.get(i) != null) {
						ItemStack istack = gewinnmom.get(i);
						gewinnplatzierung.put(i+1, istack);
					}
				}
				
				createWinFor(inv, cases);
			}
	
	public static void createRealWinFor(Inventory inv, Cases cases) {
		Random rndm2 = new Random();
		List<ItemStack> winlist = getCaseWins(cases);
		Integer random = rndm2.nextInt(winlist.size());
		
		Random rndm = new Random();
		Integer itemcontrol = rndm.nextInt(1000)+1;
		
		if(getRarity(winlist.get(random)).equals("Exotic")) {
			if(itemcontrol < 10) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else if(getRarity(winlist.get(random)).equals("Legendary")) {
			if(itemcontrol < 150) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else if(getRarity(winlist.get(random)).equals("Epic")) {
			if(itemcontrol < 350) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else if(getRarity(winlist.get(random)).equals("Uncommon")) {
			if(itemcontrol < 950) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else {
			inv.setItem(39, winlist.get(random));
			gewinnplatzierung.put(1, winlist.get(random));
		}
		
		// Je nach gewöhnlichkeit nach gewisser wahrscheinlichkeit wdh!
		
		
	}
	
	public static void createWinFor(Inventory inv, Cases cases) {
		Random rndm2 = new Random();
		List<ItemStack> winlist = getCaseWins(cases);
		Integer random = rndm2.nextInt(winlist.size());
		
		Random rndm = new Random();
		Integer itemcontrol = rndm.nextInt(1000)+1;
		
		if(getRarity(winlist.get(random)).equals("Exotic")) {
			if(itemcontrol < 10) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else if(getRarity(winlist.get(random)).equals("Legendary")) {
			if(itemcontrol < 150) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else if(getRarity(winlist.get(random)).equals("Epic")) {
			if(itemcontrol < 350) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else if(getRarity(winlist.get(random)).equals("Uncommon")) {
			if(itemcontrol < 950) {
				inv.setItem(39, winlist.get(random));
				gewinnplatzierung.put(1, winlist.get(random));
			}else {
				createWinFor(inv, cases);
			}
		}else {
			inv.setItem(39, winlist.get(random));
			gewinnplatzierung.put(1, winlist.get(random));
		}
		
		// Je nach gewöhnlichkeit nach gewisser wahrscheinlichkeit wdh!
		
		
	}
	
	
}
