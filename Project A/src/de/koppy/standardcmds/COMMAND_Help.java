package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.koppy.project.Main;

public class COMMAND_Help implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Help(Main main) {
		this.main = main;
		main.getCommand("help").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		p.sendMessage("§8§m-------------§r§8[§6H§eilfe§7 / Seite 1§8]§m-------------");
		p.sendMessage("§e/land help §7- Siehe alle Land-Befehle.");
		p.sendMessage("§e/eco help §7- Siehe alle Economy-Befehle.");
		p.sendMessage("§e/bank help §7- Siehe alle Bank-Befehle.");
		p.sendMessage("§e/job help §7- Siehe alle Job-Befehle.");
		p.sendMessage("§e/warp help §7- Zeigt wie du einen Warp erstellst.");
		p.sendMessage("§e/help §7- Siehe alle möglichen Hile-Befehle.");	
		p.sendMessage("§8§m------------------------------------");
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
