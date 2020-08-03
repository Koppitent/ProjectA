package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Msg implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Msg(Main main) {
		this.main = main;
		main.getCommand("msg").setExecutor(this);
	}
	
	public static HashMap<Player, Player> gotsendedMessage = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length >= 2) {
			String target = args[0];
			Player t = Bukkit.getPlayer(target);
			if(t != null) {
				if(t != p) {
					String msg = "";
					for(int i=1; i<args.length; i++) msg = msg + " " + args[i];
					t.sendMessage("§8[§6Nachricht§8] §8[§3"+p.getName()+" §8-> §3Dir§8]§e" + msg);
					p.sendMessage("§8[§6Nachricht§8] §8[§3Du §8-> §3" + t.getName() + "§8]§e" + msg);
					gotsendedMessage.put(t, p);
					gotsendedMessage.put(p, t);
				}else {
					p.sendMessage("§8[§6Nachricht§8] §cSelbstgespräche sind nicht förderlich!");
				}
			}else {
				p.sendMessage("§8[§6Nachricht§8] §cDer Spieler muss online sein.");
			}
			
		}else {
			p.sendMessage("§8[§6Nachricht§8] §cVerwende §e/msg [Spieler] [Nachricht]");
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		List<String> check = new ArrayList<>();
		for(Player all : Bukkit.getOnlinePlayers()) check.add(all.getName());
		for(String s : check) if(s.startsWith(args[0])) tcomplete.add(s);
		
		return tcomplete;
	}
}
