package de.koppy.standardcmds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.koppy.chestshop.EVENT_ChestShop;
import de.koppy.handler.InfoHandler;
import de.koppy.project.Main;

public class COMMAND_Setspawn implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Setspawn(Main main) {
		this.main = main;
		main.getCommand("setspawn").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		File file = new File("plugins/Server", "spawn.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(p.hasPermission("server.setspawn")) {
			cfg.set("loaction", EVENT_ChestShop.locToString(p.getLocation()));
			InfoHandler.saveFile(file, cfg);
			Main.spawnloc = p.getLocation();
			p.sendMessage(Main.prefix + "§7Du hast den Spawn hierhin gesetzt.");
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		return tcomplete;
	}
	
	public static Location loadLoc() {
		File file = new File("plugins/Server", "spawn.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String locS="";
		if(cfg.getString("loaction") != null) {
			locS = cfg.getString("loaction");
			Location loc = EVENT_ChestShop.stringToLoc(locS);
			return loc;
		}else{
			Location loc=null;
			return loc;
		}
	}
}
