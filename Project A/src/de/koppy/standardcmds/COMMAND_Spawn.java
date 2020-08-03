package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class COMMAND_Spawn implements CommandExecutor, TabCompleter{

	public static ArrayList<Player> inSpawnsec = new ArrayList<>();
	private static Main main;
	public COMMAND_Spawn(Main main) {
		this.main = main;
		main.getCommand("spawn").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(!inSpawnsec.contains(p)) {
			if(Main.spawnloc != null) {
				inSpawnsec.add(p);
				teleportPlayer(p);
			}else {
				p.sendMessage(Main.prefix + "§cDer Spawn existiert noch nicht.");
			}
		}else {
			inSpawnsec.remove(p);
			p.sendMessage(Main.prefix + "§7Die Teleportation wurde abgebrochen!");
		}
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
	
		
	
	public static void teleportPlayer(Player p) {
		p.sendMessage(Main.prefix + "§7Du wirst in §33 Sekunden §7teleportiert.");
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§33..."));
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				if(inSpawnsec.contains(p)) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§32..."));
				
			}
		}, 20*1);
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				if(inSpawnsec.contains(p)) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§31..."));
				
			}
		}, 20*2);
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				if(inSpawnsec.contains(p)) p.teleport(Main.spawnloc); inSpawnsec.remove(p);
				if(inSpawnsec.contains(p)) p.sendMessage(Main.prefix + "§7Du wurdest an den Spawn teleportiert.");
				
			}
		}, 20*3);
		
	}
	
}
