package de.koppy.project;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class COMMAND_chunk implements CommandExecutor, TabCompleter{

	public static String prefix = "§8[§6C§ehunk§8] §r";
	
	public static List<String> arguments = new ArrayList<>();
	private Main main;
	public COMMAND_chunk(Main main) {
		this.main = main;
		main.getCommand("chunk").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		if(p.isOp()) {
		if(args.length == 0) {
			
			Chunk c = ChunkHelper.getChunk(p);
			p.sendMessage(prefix + "§dChunk: x" + c.getX() + " | z" + c.getZ());
			
		}else if(args.length == 1) {
			arguments.add("removebedrock");
			arguments.add("move");
			arguments.add("reset");
			arguments.add("id");
			arguments.add("save");
			arguments.add("load");
			arguments.add("empty");
			arguments.add("list");
			if(args[0].equalsIgnoreCase("test")) {
				Chunk c = ChunkHelper.getChunk(p);
				
				for(int i=0; i<20; i++) {
					Bukkit.getScheduler().runTaskLater(main, new Runnable() {
						@Override
						public void run() {
							ChunkEditor.setParticles(c, Particle.VILLAGER_ANGRY);
						}
					}, 20*i);
				}
				
			}else if(args[0].equalsIgnoreCase("move")) {
				ChunkEditor.moveChunk(ChunkHelper.getChunk(p), ChunkHelper.getChunkInFrontOf(p));
				p.sendMessage(prefix + "§dDer Chunk wurde eins nach vorne verschoben.");
			}else if(args[0].equalsIgnoreCase("reset")) {
				ChunkEditor.resetChunk(ChunkHelper.getChunk(p));
				p.sendMessage(prefix + "§dDer Chunk wurde erfolgreich resettet.");
			}else if(args[0].equalsIgnoreCase("biome")) {
				p.sendMessage(prefix + "§dDer Chunk hast das Biome: " + ChunkEditor.getChunkBiome(p).toString());
			}else if(args[0].equalsIgnoreCase("id")) {
				Chunk c = ChunkHelper.getChunk(p);
				p.sendMessage(prefix + "§dChunkID: x" + c.getX() + " | z" + c.getZ());
			}else if(args[0].equalsIgnoreCase("save")) {
				ChunkEditor.saveChunk(ChunkHelper.getChunk(p));
				p.sendMessage(prefix + "§dDer Chunk " + ChunkHelper.getChunkID(ChunkHelper.getChunk(p)) + " wurde erfolgreich gespeichert.");
			}else if(args[0].equalsIgnoreCase("load")) {
				ChunkEditor.loadChunk(ChunkHelper.getChunk(p));
				p.sendMessage(prefix + "§dDer Chunk " + ChunkHelper.getChunkID(ChunkHelper.getChunk(p)) + " wurde erfolgreich überschrieben.");
			}else if(args[0].equalsIgnoreCase("empty")) {
				ChunkEditor.clearChunkEmtpy(ChunkHelper.getChunk(p));
				p.sendMessage(prefix + "§dDer Chunk wurde geleert.");
			}else if(args[0].equalsIgnoreCase("removebedrock")) {
				ChunkEditor.removeBedrockFromChunk(ChunkHelper.getChunk(p));
				p.sendMessage(prefix + "§dDer Chunk wurde vom Bedrock befreit.");
			}else if(args[0].equalsIgnoreCase("list")) {
				if(ChunkEditor.getStringOfPublicChunks() != null) {
				p.sendMessage(prefix + "§dGespeicherte Öffentliche Chunks: §e" + ChunkEditor.getStringOfPublicChunks());
				}else {
					p.sendMessage(prefix + "§cEs existieren derzeit keine Chunks in der Public-Liste.");
				}
			}else {
				p.sendMessage(prefix + "§cVerfügbare Argumente: §d" + getStringOfArgumentList(arguments));
			}
			
		}else if(args.length == 2) {
			arguments.add("down");
			arguments.add("up");
			arguments.add("save");
			arguments.add("paste");
			arguments.add("list");
			arguments.add("delete");
			
			if(args[0].equalsIgnoreCase("up") || args[0].equalsIgnoreCase("anheben")) {
				Integer i = Integer.valueOf(args[1]);
				ChunkEditor.updownChunk(ChunkHelper.getChunk(p), i);
				p.sendMessage(prefix + "§dDer Chunk wurde um §e" + i + " Blöcke §dangehoben."); 
			}else if(args[0].equalsIgnoreCase("down") || args[0].equalsIgnoreCase("absenken")) {
				Integer i = Integer.valueOf(args[1]);
				ChunkEditor.updownChunk(ChunkHelper.getChunk(p), -i);
				p.sendMessage(prefix + "§dDer Chunk wurde um §e" + i + " Blöcke §dabgesenkt."); 
			}else if(args[0].equalsIgnoreCase("save")) {
				String chunk = args[1].toLowerCase();
				if(ChunkEditor.existPublicChunk(chunk) == false) {
					
					ChunkEditor.saveChunkInPublicFiles(chunk, ChunkHelper.getChunk(p));
					p.sendMessage(prefix + "§dDu hast den Chunk §e" + chunk + " §din der Public-Liste gespeichert.");
					
				}else {
					p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert bereits in der Public-Liste.");
				}
			}else if(args[0].equalsIgnoreCase("tp")) {
				p.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
			}else if(args[0].equalsIgnoreCase("paste")) {
				String chunk = args[1].toLowerCase();
				if(ChunkEditor.existPublicChunk(chunk) == true) {
					
					ChunkEditor.pasteChunk(ChunkHelper.getChunk(p) ,ChunkEditor.loadChunkFromPublicFiles(chunk));
					p.sendMessage(prefix + "§dDer Chunk wurde erfolgreisch einkopiert!");
					
				}else {
					p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert nicht in der Public-Liste.");
				}
			}else if(args[0].equalsIgnoreCase("list")) {
				if(args[1].equalsIgnoreCase("mine")) {
					if(ChunkEditor.getStringOfOwnSavedChunks(p) != null) {
						p.sendMessage(prefix + "§dGespeicherte Private Chunks: §e" + ChunkEditor.getStringOfOwnSavedChunks(p));
					}else {
						p.sendMessage(prefix + "§cEs existieren derzeit keine Chunks in der Private-Liste.");
					}
				}else if(args[1].equalsIgnoreCase("global") || args[1].equalsIgnoreCase("public")) {
					if(ChunkEditor.getStringOfPublicChunks() != null) {
						p.sendMessage(prefix + "§dGespeicherte Öffentliche Chunks: §e" + ChunkEditor.getStringOfPublicChunks());
					}else {
						p.sendMessage(prefix + "§cEs existieren derzeit keine Chunks in der Public-Liste.");
					}
				}else {
					p.sendMessage(prefix + "§cBitte verwende §e/chunk list mine|global");
				}
			}else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
				String chunk = args[1].toLowerCase();
				if(ChunkEditor.existPublicChunk(chunk)) {
					
					ChunkEditor.deleteChunkInPublicFiles(chunk);
					p.sendMessage(prefix + "§dDer Saved-Chunk §e" + chunk + " §dwurde erfolgreich gelöscht.");
					
				}else {
					p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert nicht in der Public-Liste.");
				}
			}else {
				p.sendMessage(prefix + "§cVerfügbare Argumente: §d" + getStringOfArgumentList(arguments));
			}
		}else if(args.length == 3) {
			arguments.add("save");
			arguments.add("paste");
			
			if(args[0].equalsIgnoreCase("save")) {
				if(args[2].equalsIgnoreCase("mine")) {
				String chunk = args[1].toLowerCase();
				if(ChunkEditor.existChunkFromPlayer(p, chunk) == false) {
					
					ChunkEditor.saveChunkInFiles(p, chunk, ChunkHelper.getChunk(p));
					p.sendMessage(prefix + "§dDu hast den Chunk §e" + chunk + " §din der Public-Liste gespeichert.");
					
				}else {
					p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert bereits in der Public-Liste.");
				}
				}else if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("public")) {
					String chunk = args[1].toLowerCase();
					if(ChunkEditor.existPublicChunk(chunk) == false) {
						
						ChunkEditor.saveChunkInPublicFiles(chunk, ChunkHelper.getChunk(p));
						p.sendMessage(prefix + "§dDu hast den Chunk §e" + chunk + " §din der Public-Liste gespeichert.");
						
					}else {
						p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert bereits in der Public-Liste.");
					}
				}else {
					p.sendMessage(prefix + "§cBitte verwende §e/chunk save [name] mine|global");
				}
			}else if(args[0].equalsIgnoreCase("paste")) {
				if(args[2].equalsIgnoreCase("mine")) {
				String chunk = args[1].toLowerCase();
				if(ChunkEditor.existChunkFromPlayer(p, chunk) == true) {
					
					ChunkEditor.pasteChunk(ChunkHelper.getChunk(p) ,ChunkEditor.loadChunkFromFiles(p, chunk));
					p.sendMessage(prefix + "§dDer Chunk wurde erfolgreisch einkopiert!");
					
				}else {
					p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert nicht in der Public-Liste.");
				}
				}else if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("public")) {
					String chunk = args[1].toLowerCase();
					if(ChunkEditor.existPublicChunk(chunk) == true) {
						
						ChunkEditor.pasteChunk(ChunkHelper.getChunk(p) ,ChunkEditor.loadChunkFromPublicFiles(chunk));
						p.sendMessage(prefix + "§dDer Chunk wurde erfolgreisch einkopiert!");
						
					}else {
						p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert nicht in der Public-Liste.");
					}
				}else {
					p.sendMessage(prefix + "§cBitte verwende §e/chunk paste [name] mine|global");
				}
			}else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
				String chunk = args[1].toLowerCase();
				if(args[2].equalsIgnoreCase("mine")) {
				if(ChunkEditor.existChunkFromPlayer(p, chunk)) {
					
					ChunkEditor.deleteChunkInFiles(p, chunk);
					p.sendMessage(prefix + "§dDer Saved-Chunk §e" + chunk + " §dwurde erfolgreich aus der Private-Liste gelöscht.");
					
				}else {
					p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert nicht in der Private-Liste.");
				}
				}else if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("public")) {
					if(ChunkEditor.existPublicChunk(chunk)) {
						
						ChunkEditor.deleteChunkInPublicFiles(chunk);
						p.sendMessage(prefix + "§dDer Saved-Chunk §e" + chunk + " §dwurde erfolgreich gelöscht.");
						
					}else {
						p.sendMessage(prefix + "§cDer Chunk §d" + chunk + " §cexistiert nicht in der Public-Liste.");
					}
				}else {
					p.sendMessage(prefix + "§cBitte verwende §e/chunk delete [name] mine|global");
				}
			}else {
				p.sendMessage(prefix + "§cVerfügbare Argumente: §d" + getStringOfArgumentList(arguments));
			}
		}
		}else {
			p.sendMessage(prefix + "§dDu hast leider keine Rechte auf das Chunk-System!");
		}
		return true;
	}
	
	public static String getStringOfArgumentList(List<String> argumentlist) {
		String finish = "";
		for(String argument : argumentlist) {
			if(!finish.contains(argument)) {
				finish = finish + argument + ", ";
			}
		}
		return finish;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tcomplete = new ArrayList<>();
		
		if(args.length == 1) {
			List<String> check = new ArrayList<>();
			check.add("down");
			check.add("up");
			check.add("removebedrock");
			check.add("move");
			check.add("paste");
			check.add("reset");
			check.add("id");
			check.add("save");
			check.add("load");
			check.add("empty");
			check.add("list");
			for(String s : check) if(s.toLowerCase().startsWith(args[0].toLowerCase())) tcomplete.add(s);
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("down")) {
				tcomplete.add("§4§l✘ §r§4Number here §4§l✘");
			}else if(args[0].equalsIgnoreCase("up")) {
				tcomplete.add("§4§l✘ §r§4Number here §4§l✘");
			}else if(args[0].equalsIgnoreCase("save")) {
				tcomplete.add("§4§l✘ §r§4Name §4§l✘");
			}else if(args[0].equalsIgnoreCase("paste")) {
				tcomplete.add("§4§l✘ §r§4Name §4§l✘");
			}else if(args[0].equalsIgnoreCase("list")) {
				List<String> check = new ArrayList<>();
				check.add("mine");
				check.add("global");
				for(String s : check) if(s.toLowerCase().startsWith(args[1].toLowerCase())) tcomplete.add(s);
			}else if(args[0].equalsIgnoreCase("delete")) {
				tcomplete.add("§4§l✘ §r§4Name §4§l✘");
			}
		}else if(args.length == 3) {
			
			if(args[0].equalsIgnoreCase("save")) {
				List<String> check = new ArrayList<>();
				check.add("mine");
				check.add("global");
				for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
			}else if(args[0].equalsIgnoreCase("delete")) {
				List<String> check = new ArrayList<>();
				check.add("mine");
				check.add("global");
				for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
			}else if(args[0].equalsIgnoreCase("paste")) {
				List<String> check = new ArrayList<>();
				check.add("mine");
				check.add("global");
				for(String s : check) if(s.toLowerCase().startsWith(args[2].toLowerCase())) tcomplete.add(s);
			}
			
		}
		
		return tcomplete;
	}

}
