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

public class COMMAND_Tpaccept implements CommandExecutor, TabCompleter{
	
	private Main main;
	public COMMAND_Tpaccept(Main main) {
		this.main = main;
		main.getCommand("tpaccept").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 								
		Player p = (Player)sender;
		
		if(COMMAND_Tpa.tparequest.containsKey(p)) {
			
			Player t = COMMAND_Tpa.tparequest.get(p);
			COMMAND_Tpa.tparequest.remove(p, t);
			p.sendMessage(Main.prefix + "§7Du hast die §3TPA §7erfolgreich §aangenommen§7!");
			t.sendMessage(Main.prefix + "§7Der Spieler §3" + p.getName() + " §7hat deine §3TPA §7angenommen. Du wirst in §e3 Sekunden §7teleportiert.");
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				
				@Override
				public void run() {
					t.teleport(p);
				}
			}, 20*3);
			
		}else {
			p.sendMessage(Main.prefix + "§cDu hast derzeit keine Teleportationsanfragen.");
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
