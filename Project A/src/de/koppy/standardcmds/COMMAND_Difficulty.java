package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Difficulty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Difficulty implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Difficulty(Main main) {
		this.main = main;
		main.getCommand("difficulty").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(p.hasPermission("server.difficulty")) {
			
			if(args.length == 1) {
				
				if(args[0].equalsIgnoreCase("peaceful")) {
					p.getWorld().setDifficulty(Difficulty.PEACEFUL);
					p.sendMessage(Main.prefix + "§7Die Schwierigkeit wurde auf §a" + p.getWorld().getDifficulty().toString() + " §7gestellt.");
				}else if(args[0].equalsIgnoreCase("easy")) {
					p.getWorld().setDifficulty(Difficulty.EASY);
					p.sendMessage(Main.prefix + "§7Die Schwierigkeit wurde auf §a" + p.getWorld().getDifficulty().toString() + " §7gestellt.");
				}else if(args[0].equalsIgnoreCase("normal")) {
					p.getWorld().setDifficulty(Difficulty.NORMAL);
					p.sendMessage(Main.prefix + "§7Die Schwierigkeit wurde auf §a" + p.getWorld().getDifficulty().toString() + " §7gestellt.");
				}else if(args[0].equalsIgnoreCase("hard")) {
					p.getWorld().setDifficulty(Difficulty.HARD);
					p.sendMessage(Main.prefix + "§7Die Schwierigkeit wurde auf §a" + p.getWorld().getDifficulty().toString() + " §7gestellt.");
				}else {
					p.sendMessage(Main.prefix + "§cverwende: §e/difficulty peaceful|easy|normal|hard");
				}
				
			}else {
				p.sendMessage(Main.prefix + "§cverwende: §e/difficulty peaceful|easy|normal|hard");
			}
			
		}else {
			p.sendMessage(Main.noperms);
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("peaceful");
			check.add("easy");
			check.add("normal");
			check.add("hard");
			for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
		}
		return tcomplete;
	}
}
