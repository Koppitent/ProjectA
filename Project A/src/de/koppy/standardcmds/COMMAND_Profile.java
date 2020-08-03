package de.koppy.standardcmds;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.eco.COMMAND_Eco;
import de.koppy.eco.Economy;
import de.koppy.eco.EconomyMenu;
import de.koppy.handler.ProfileHandler;
import de.koppy.job.Job;
import de.koppy.job.Jobs;
import de.koppy.landsystem.API_Land;
import de.koppy.landsystem.LandMenu;
import de.koppy.project.Main;

public class COMMAND_Profile implements CommandExecutor, TabCompleter{

	//Adding spielzeit plus scoreboard on / off TPA on / off
	
	private Main main;
	public COMMAND_Profile(Main main) {
		this.main = main;
		main.getCommand("profile").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length == 0) {
			//own  profile
			
			Inventory inv = openProfileInv(p, p.getUniqueId().toString(), "§3Dein Profil:");
			p.openInventory(inv);
			
		}else if(args.length == 1) {
			if(p.hasPermission("server.admin.seeotherprofiles")) {
				//Other profile
				String target = args[0];
				if(API_UUID.existUUIDorNAME(target)) {
					if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
					//target ist nun UUID
					Inventory inv = openProfileInv(p, target, "§3" + API_UUID.getUUIDorNAME(target) + "§3's Profil:");
					p.openInventory(inv);
					
				}else {
					p.sendMessage(Main.prefix + "§cDer Spieler war noch nie auf dem Server!");
				}
			}
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		return tcomplete;
	}
	
	public static Inventory openProfileInv(Player p, String targetUUID, String name) {
		Inventory inv = Bukkit.createInventory(null, 6*9, name);
		LandMenu.fillUpWithGlass(inv);
		if(!EconomyMenu.isinMenu.contains(p)) EconomyMenu.isinMenu.add(p);
		
		String playername = API_UUID.getUUIDorNAME(targetUUID);
		
		ItemStack profileSkull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta profileSkullM = (SkullMeta) profileSkull.getItemMeta();
		profileSkullM.setOwner(playername);
		profileSkullM.setDisplayName("§eProfil: §3" + playername);
		List<String> profileSkullMlore = new ArrayList<>();
		profileSkullMlore.add("§7» §3UUID: §7" + targetUUID);
		//VLLT SPÄTER NOCH SPIELSTUNDEN
		profileSkullM.setLore(profileSkullMlore);
		profileSkull.setItemMeta(profileSkullM);
		
		ItemStack signLastOnline = new ItemStack(Material.OAK_SIGN);
		ItemMeta signLastOnlineM = signLastOnline.getItemMeta();
		if(Bukkit.getPlayer(playername) == null) signLastOnlineM.setDisplayName("§7» Last-Online: §3" + ProfileHandler.getLastOnlineDate(targetUUID));
		else signLastOnlineM.setDisplayName("§7» Last-Online: §a§oonline");
		signLastOnline.setItemMeta(signLastOnlineM);
		
		ItemStack ClanTag = new ItemStack(Material.NETHERITE_HELMET);
		ItemMeta ClanTagM = ClanTag.getItemMeta();
		if(ProfileHandler.getClanTag(targetUUID) != null) ClanTagM.setDisplayName("§7» Clan-Tag: §3" + ProfileHandler.getClanTag(targetUUID));
		else ClanTagM.setDisplayName("§7» §cDu besitzt keinen Clan-Tag §e/setClanTag");
		ClanTag.setItemMeta(ClanTagM);
		
		ItemStack moneybal = new ItemStack(Material.SPRUCE_SIGN);
		ItemMeta moneybalM = moneybal.getItemMeta();
		DecimalFormat f = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMANY));
		Double money = Economy.getMoney(targetUUID);
		String fmoney = f.format(money);
		moneybalM.setDisplayName("§7» Balance: §3" + fmoney + COMMAND_Eco.moneyformat);
		moneybal.setItemMeta(moneybalM);
		
		ItemStack bankacc = new ItemStack(Material.DARK_OAK_SIGN);
		ItemMeta bankaccM = bankacc.getItemMeta();
		bankaccM.setDisplayName("§7» Bank-Konten: ");
		List<String> bankaccMlore = new ArrayList<>();
		if(Economy.getAccountNames(targetUUID).size() == 0) bankaccMlore.add("§cDu hast keine BankKonten.");
		else for(String banks : Economy.getAccountNames(targetUUID)) bankaccMlore.add("§7- §3"+banks);
		bankaccM.setLore(bankaccMlore);
		bankacc.setItemMeta(bankaccM);
		
		ItemStack landanzahl = new ItemStack(Material.PAPER);
		ItemMeta landanzahlM = landanzahl.getItemMeta();
		landanzahlM.setDisplayName("§7» Grundstücke: §3" + API_Land.getChunkList(targetUUID).size());
		landanzahl.setItemMeta(landanzahlM);
		
		ItemStack currentjob = new ItemStack(Material.FLINT_AND_STEEL);
		ItemMeta currentjobM = currentjob.getItemMeta();
		currentjobM.setDisplayName("§7» Derzeitiger Job: §3" + Jobs.getJob(targetUUID));
		List<String> currentjobMlore = new ArrayList<>();
		if(Jobs.getJob(targetUUID) != Job.ARBEITSLOS) {
			currentjobMlore.add("");
			currentjobMlore.add("§6Level: §e" + Jobs.getJobLevel(targetUUID, Jobs.getJob(targetUUID)));
			currentjobMlore.add("§6XP: §e" + Jobs.getJobXP(targetUUID, Jobs.getJob(targetUUID)));
			currentjobMlore.add("§8----------------");
		}
		currentjobM.setLore(currentjobMlore);
		currentjob.setItemMeta(currentjobM);
		
		ItemStack GeldVis = new ItemStack(Material.NAME_TAG);
		ItemMeta GeldVisM = GeldVis.getItemMeta();
		GeldVisM.setDisplayName("§7» §5Geld-Sichtbarkeit §6");
		List<String> GeldVisLore = new ArrayList<>();
		if(Economy.isPublic(targetUUID)) GeldVisLore.add("§aöffentlich");
		else GeldVisLore.add("§cprivat");
		GeldVisM.setLore(GeldVisLore);
		GeldVis.setItemMeta(GeldVisM);
		
		ItemStack warpanzahl = new ItemStack(Material.HOPPER);
		ItemMeta warpanzahlM = warpanzahl.getItemMeta();
		warpanzahlM.setDisplayName("§7» UserWarps: §3");
		List<String> warpanzahlMlore = new ArrayList<>();
		if(COMMAND_Warp.getWarpAmount(targetUUID) != 0 && COMMAND_Warp.getWarpListOfPlayer(targetUUID).size() != 0) {
			warpanzahlMlore.add("");
			warpanzahlMlore.add("§7» Gesetzt: §e" + COMMAND_Warp.getWarpListOfPlayer(targetUUID).size());
			warpanzahlMlore.add("§7» Verfügbar: §e" + COMMAND_Warp.getWarpAmount(targetUUID));
		}else {
			warpanzahlMlore.add("§c» §cDu besitzt derzeit noch keine Warps. §e/warp help");
		}
		warpanzahlM.setLore(warpanzahlMlore);
		warpanzahl.setItemMeta(warpanzahlM);
		
		ItemStack openedCase = new ItemStack(Material.WHITE_SHULKER_BOX);
		ItemMeta openedCaseM = openedCase.getItemMeta();
		openedCaseM.setDisplayName("§7» Kisten gehebelt: §3" + ProfileHandler.getOpenedCases(targetUUID));
		openedCase.setItemMeta(openedCaseM);
		
		ItemStack ec = new ItemStack(Material.ENDER_CHEST);
		ItemMeta ecM = ec.getItemMeta();
		ecM.setDisplayName("§7» §5Enderchest §7§o(Rechtsklick)");
		ec.setItemMeta(ecM);
		
		inv.setItem(10, profileSkull);
		inv.setItem(13, ClanTag);
		inv.setItem(14, signLastOnline);
		inv.setItem(15, moneybal);
		inv.setItem(16, bankacc);
		
		inv.setItem(29, landanzahl);
		inv.setItem(30, currentjob);
		inv.setItem(38, GeldVis);
		inv.setItem(39, warpanzahl);
		inv.setItem(40, openedCase);
		
		inv.setItem(43, ec);
		
		return inv;
	}
	
}
