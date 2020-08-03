package de.koppy.job;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.koppy.landsystem.LandMenu;

public class JobMenu {

	public static ArrayList<Player> jobinv = new ArrayList<>();
	
	public static Inventory createJobInv(Player p, String name) {
		jobinv.add(p);
		Inventory inv = Bukkit.getServer().createInventory(null, 6*9, name);
		LandMenu.fillUpWithGlass(inv);
		
		ItemStack miner = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta minerm = miner.getItemMeta();
		minerm.setDisplayName("§3Miner");
		miner.setItemMeta(minerm);
		
		ItemStack schmied = new ItemStack(Material.ANVIL);
		ItemMeta schmiedm = schmied.getItemMeta();
		schmiedm.setDisplayName("§3Schmied");
		schmied.setItemMeta(schmiedm);
		
		ItemStack erbauer = new ItemStack(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
		ItemMeta erbauerm = erbauer.getItemMeta();
		erbauerm.setDisplayName("§3Erbauer");
		erbauer.setItemMeta(erbauerm);
		
		ItemStack graeber = new ItemStack(Material.COARSE_DIRT);
		ItemMeta graeberm = graeber.getItemMeta();
		graeberm.setDisplayName("§3Graber");
		graeber.setItemMeta(graeberm);
		
		ItemStack holzfaeller = new ItemStack(Material.OAK_SAPLING);
		ItemMeta holzfaellerm = holzfaeller.getItemMeta();
		holzfaellerm.setDisplayName("§3Holzfaeller");
		holzfaeller.setItemMeta(holzfaellerm);
		
		ItemStack farmer = new ItemStack(Material.WHEAT);
		ItemMeta farmerm = farmer.getItemMeta();
		farmerm.setDisplayName("§3Farmer");
		farmer.setItemMeta(farmerm);
		
		ItemStack nholzfaeller = new ItemStack(Material.WARPED_STEM);
		ItemMeta nholzfaellerm = nholzfaeller.getItemMeta();
		nholzfaellerm.setDisplayName("§3NetherHolzfaeller");
		nholzfaeller.setItemMeta(nholzfaellerm);
		
		inv.setItem(10, miner);
		inv.setItem(12, erbauer);
		inv.setItem(14, graeber);
		inv.setItem(16, holzfaeller);
		inv.setItem(29, farmer);
		inv.setItem(31, nholzfaeller);
		inv.setItem(33, schmied);
		
		ItemStack info = new ItemStack(Material.SPRUCE_SIGN);
		ItemMeta infom = info.getItemMeta();
		infom.setDisplayName("§7Job: §e" + Jobs.getJob(p.getUniqueId().toString().toString()));
		List<String> infolore = new ArrayList<String>();
		if(Jobs.getJob(p.getUniqueId().toString()) != Job.ARBEITSLOS) {
			infolore.add("§7» Level: " + Jobs.level.get(p));
			infolore.add("§7» XP: " + Jobs.xp.get(p));
		}
		infom.setLore(infolore);
		info.setItemMeta(infom);
		
		ItemStack kuendigung = new ItemStack(Material.BARRIER);
		ItemMeta kuendigungm = kuendigung.getItemMeta();
		kuendigungm.setDisplayName("§cJob kündigen! §7§o(Rechtsklick)");
		kuendigung.setItemMeta(kuendigungm);
		
		inv.setItem(44, info);
		inv.setItem(44+9, kuendigung);
		
		return inv;
	}
	
	
}
