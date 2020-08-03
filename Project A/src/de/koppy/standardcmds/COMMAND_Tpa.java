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

import de.koppy.handler.ProfileHandler;
import de.koppy.project.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class COMMAND_Tpa implements CommandExecutor, TabCompleter{

	private Main main;
	public COMMAND_Tpa(Main main) {
		this.main = main;
		main.getCommand("tpa").setExecutor(this);
	}
	
	public static HashMap<Player, Player> tparequest = new HashMap<>();
						//From    //To
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if(args.length == 1) {
			String target = args[0];
			Player t = Bukkit.getPlayer(target);
			if(t != null) {
				if(ProfileHandler.isTeleportAllowed(t.getUniqueId().toString())) {
				if(tparequest.get(t) != p) {
					
					TextComponent accept = new TextComponent();
					accept.setText("§7[§aAkzeptieren§7]");
					accept.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tpaccept"));
					accept.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§nKlicken zum annehmen!").create()));
					
					TextComponent deny = new TextComponent();
					deny.setText("   §7[§cAblehnen§7]");
					deny.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tpdeny"));
					deny.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§nKlicken zum ablehnen!").create()));
					accept.addExtra(deny);
					
					tparequest.put(t, p);
					p.sendMessage(Main.prefix + "§7Die Teleportationsanfrage wurde an den Spieler §3" + t.getName() + " §7versandt.");
					t.sendMessage(Main.prefix + "§7Du hast eine Teleportationsanfrage von §3" + p.getName() + " §7erhalten.");
					t.spigot().sendMessage(accept);
					
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						
						@Override
						public void run() {
							if(tparequest.get(t) != null && tparequest.get(t) == p) {
								tparequest.remove(t, p);
								t.sendMessage(Main.prefix + "§cDie Teleportationsanfrage von " + p.getName() + " ist abgelaufen!");
								p.sendMessage(Main.prefix + "§cDeine Teleportationsanfrage an " + t.getName() + " ist abgelaufen!");
							}
						}
					}, 20*30);
					
				}else {
					p.sendMessage(Main.prefix + "§cDu hast bereits eine TPA-Anfrage an den Spieler " + t.getName() + " gesendet.");
				}
				}else {
					p.sendMessage(Main.prefix + "§cDer Spieler " + t.getName() + " hat Teleportationsanfragen deaktiviert!");
				}
			}else {
				p.sendMessage(Main.nichtonline.replace("%player%", target));
			}
		}
		
		return true;	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		if(args.length == 1) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				for(Player all : Bukkit.getOnlinePlayers()) if(all != p) tcomplete.add(all.getName());
			}else {
				tcomplete.add("nix für die Konsole!");
			}
		}
		return tcomplete;
	}
}
