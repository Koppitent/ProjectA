package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_r implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_r(Main main) {
		this.main = main;
		main.getCommand("r").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length >= 1) {
		if(COMMAND_Msg.gotsendedMessage.get(p) != null) {
			
			String msg = "";
			for(int i=0; i<args.length; i++) msg = msg + " " + args[i];
			
			Player t = COMMAND_Msg.gotsendedMessage.get(p);
			t.sendMessage("§8[§6Nachricht§8] §8[§3"+p.getName()+" §8-> §3Dir§8]§e" + msg);
			p.sendMessage("§8[§6Nachricht§8] §8[§3Du §8-> §3" + t.getName() + "§8]§e" + msg);
			COMMAND_Msg.gotsendedMessage.put(t, p);
			COMMAND_Msg.gotsendedMessage.put(p, t);
			
		}else {
			p.sendMessage("§8[§6Nachrichten§8] §cEs hat dir niemand eine Nachricht gesendet!");
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
