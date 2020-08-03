package de.koppy.eco;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Moneytoggle implements CommandExecutor, TabCompleter{
	
	private Main main;
	public static ArrayList<Player> paypause = new ArrayList<>();
	public COMMAND_Moneytoggle(Main main) {
		this.main = main;
		main.getCommand("moneytoggle").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(Economy.isPublic(p.getUniqueId().toString())) {
			Economy.setPublic(p.getUniqueId().toString(), false);
			p.sendMessage(COMMAND_Eco.prefix + "§7Dein Geld ist nun §cprivat§7.");
		}else {
			Economy.setPublic(p.getUniqueId().toString(), true);
			p.sendMessage(COMMAND_Eco.prefix + "§7Dein Geld ist nun §aöffentlich§7.");
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		if(args.length == 1) {
			Player p = (Player) sender;
			List<String> check = new ArrayList<>();
			for(Player all : Bukkit.getOnlinePlayers()) if(all != p) check.add(all.getName());
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
			if(tcomplete.isEmpty()) tcomplete.add("§4§l✘ §r§cniemand online §4§l✘");
		}else if(args.length == 2) {
			tcomplete.add("§4§l✘ §r§cBegründung(ein Wort) §4§l✘");
		}
		return tcomplete;
	}
}
