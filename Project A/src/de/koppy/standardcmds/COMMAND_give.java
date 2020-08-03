package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.koppy.project.Main;

public class COMMAND_give implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_give(Main main) {
		this.main = main;
		main.getCommand("give").setExecutor(this);
		main.getCommand("i").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.give") || p.hasPermission("server.i")) {
			if(args.length == 1) {
				String material = args[0];
				Material m = Material.getMaterial(material.toUpperCase());
				if(m != null) {
					p.getInventory().addItem(new ItemStack(m, m.getMaxStackSize()));
					p.sendMessage(Main.prefix + "§7Du hast §3" + m.getMaxStackSize() + "x " + m.toString() + " §7erhalten");
				}else {
					p.sendMessage(Main.prefix + "§cDas Item existiert nicht!");
				}
				
			}else if(args.length == 2) {
				String material = args[0];
				Material m = Material.getMaterial(material.toUpperCase());
				if(m != null) {
					if(args[1].matches("[0-9]+")) {
						Integer anzahl = Integer.valueOf(args[1]);
						p.getInventory().addItem(new ItemStack(m, anzahl));
						p.sendMessage(Main.prefix + "§7Du hast §3" + anzahl + "x " + m.toString() + " §7erhalten.");
					}else {
						p.sendMessage(Main.prefix + "§cBitte eine Zahl angeben.");
					}
				}else {
					p.sendMessage(Main.prefix + "§cDas Item existiert nicht!");
				}
			}else if(args.length == 3) {
				if(p.hasPermission("server.give.other") || p.hasPermission("server.i.other")) {
				String material = args[0];
				Material m = Material.getMaterial(material.toUpperCase());
				if(m != null) {
					if(args[1].matches("[0-9]+")) {
						Integer anzahl = Integer.valueOf(args[1]);
						String target = args[2];
						Player t = Bukkit.getPlayer(args[2]);
						if(t != null) {
							if(t != p) {
							t.getInventory().addItem(new ItemStack(m, anzahl));
							t.sendMessage(Main.prefix + "§7Du hast §3" + anzahl + "x " + m.toString() + " §7erhalten.");
							p.sendMessage(Main.prefix + "§7Der Spieler §3" + t.getName() + " hat §3" + anzahl + "x " + m.toString() + " §7erhalten.");
							}else {
								p.performCommand("i " + m.toString() + " " + anzahl);
							}
						}else {
							p.sendMessage(Main.nichtonline.replace("%player%", target));
						}
					}else {
						p.sendMessage(Main.prefix + "§cBitte eine Zahl angeben.");
					}
				}else {
					p.sendMessage(Main.prefix + "§cDas Item existiert nicht!");
				}
				}else {
					p.sendMessage(Main.noperms);
				}
			}else {
				p.sendMessage(Main.prefix + "§cVerwende §e/give [Item] [Anzahl] [Name]");
			}
		}else {
			p.sendMessage(Main.noperms);
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		Player p = (Player) sender;
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			for(Material m : Material.values()) check.add(m.toString());
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			tcomplete.add("§4§l✘ §r§4Anzahl angeben §4§l✘");
		}else if(args.length == 3) {
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) if(all != p) check.add(all.getName());
			for(String s : check) if(s.startsWith(args[2])) tcomplete.add(s);
			if(tcomplete.isEmpty()) tcomplete.add("§4§l✘ §r§cniemand online §4§l✘");
		}
		return tcomplete;
	}
}
