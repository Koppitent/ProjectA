package de.koppy.standardcmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import de.koppy.handler.ProfileHandler;
import de.koppy.project.Main;
import de.koppy.project.Tablistener;

public class COMMAND_Togglescoreboard implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Togglescoreboard(Main main) {
		this.main = main;
		main.getCommand("togglescoreboard").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(ProfileHandler.getScoreboardAllow(p.getUniqueId().toString())) {
			ProfileHandler.setScoreboard(p.getUniqueId().toString(), false);
			p.sendMessage(Main.prefix + "§7Du hast das Scoreboard §cdeaktiviert§7.");
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3, 1);
		}else {
			ProfileHandler.setScoreboard(p.getUniqueId().toString(), true);
			p.sendMessage(Main.prefix + "§7Du hast das Scoreboard §aaktiviert§7.");
			Tablistener.setScoreboard(p);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3, 1);
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
