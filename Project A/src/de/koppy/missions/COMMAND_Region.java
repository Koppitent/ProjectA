package de.koppy.missions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;

import de.koppy.project.Main;
import net.minecraft.server.v1_16_R1.World;

public class COMMAND_Region implements CommandExecutor, TabCompleter {
	
	private Main main;
	public COMMAND_Region(Main main) {
		this.main = main;
		main.getCommand("region").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		Regions region = Explorer.getRegion(p.getLocation());
		p.sendMessage(Main.prefix + "§7Du befindest dich in der Region §3" + region.toString() + "§7!");
		
		if(args[0].equalsIgnoreCase("test")) {
			 World world = ((CraftWorld)p.getWorld()).getHandle();
	         CustomZombie zombie = new CustomZombie(world, ((Player)sender).getLocation());
	         world.addEntity(zombie);
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		return tcomplete;
	}
}
