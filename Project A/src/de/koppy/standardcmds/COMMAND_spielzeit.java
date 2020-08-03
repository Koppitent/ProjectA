package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.UUIDFetcher.API_UUID;
import de.koppy.handler.ProfileHandler;
import de.koppy.project.EVENT_Spiel;
import de.koppy.project.Main;

public class COMMAND_spielzeit implements CommandExecutor, TabCompleter{
	
	private Main main;
	public COMMAND_spielzeit(Main main) {
		this.main = main;
		main.getCommand("spielzeit").setExecutor(this);
		main.getCommand("playtime").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length == 0) {
			//own playtime
			Integer spielzeitticks = EVENT_Spiel.spielzeit.get(p);
			Integer minutengesamt = 0;
			Integer stundengesamt = 0;
			Integer sekundengesamt = spielzeitticks/20;
			if(sekundengesamt >= 60) {
				minutengesamt = sekundengesamt/60;
				sekundengesamt = sekundengesamt%60;
			}
			if(minutengesamt >= 60) {
				stundengesamt = minutengesamt/60;
				minutengesamt = minutengesamt % 60;
			}
			
			p.sendMessage(Main.prefix + "§7Du spielst seit §e" + stundengesamt + " Stunden " + minutengesamt + " Minuten §3und §e" + sekundengesamt + " Sekunden §3auf dem Server.");
		}else if(args.length == 1) {
			//Other playtime
			String target = args[0];
			Player t = Bukkit.getPlayer(target);
			if(t != null) {
				//Online 
				Integer spielzeitticks = EVENT_Spiel.spielzeit.get(t);
				Integer minutengesamt = 0;
				Integer stundengesamt = 0;
				Integer sekundengesamt = spielzeitticks/20;
				if(sekundengesamt >= 60) {
					minutengesamt = sekundengesamt/60;
					sekundengesamt = sekundengesamt%60;
				}
				if(minutengesamt >= 60) {
					stundengesamt = minutengesamt/60;
					minutengesamt = minutengesamt % 60;
				}
				p.sendMessage(Main.prefix + "§7Der Spieler spielt seit §e" + stundengesamt + " Stunden " + minutengesamt + " Minuten §3und §e" + sekundengesamt + " Sekunden §3auf dem Server.");
			}else if(API_UUID.existUUIDorNAME(target)) {
				if(target.length() != 36) target = API_UUID.getUUIDorNAME(target);
				//target ist nun UUID
				Integer spielzeitticks = ProfileHandler.getSpielzeit(target);
				Integer minutengesamt = 0;
				Integer stundengesamt = 0;
				Integer sekundengesamt = spielzeitticks/20;
				if(sekundengesamt >= 60) {
					minutengesamt = sekundengesamt/60;
					sekundengesamt = sekundengesamt%60;
				}
				if(minutengesamt >= 60) {
					stundengesamt = minutengesamt/60;
					minutengesamt = minutengesamt % 60;
				}
				p.sendMessage(Main.prefix + "§7Der Spieler spielt seit §e" + stundengesamt + " Stunden " + minutengesamt + " Minuten §3und §e" + sekundengesamt + " Sekunden §3auf dem Server.");
			}else {
				p.sendMessage(Main.prefix + "§cDer Spieler war noch nie auf dem Server!");
			}
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
