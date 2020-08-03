package de.koppy.job;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRemoveLevelatJobEvent extends Event{

	public static HandlerList handlers = new HandlerList();
	Player player;
	Integer amount;
	
	public PlayerRemoveLevelatJobEvent(Player player, Integer amount) {
		this.player = player;
		this.amount = amount;
	}
	
	@Override
	public HandlerList getHandlers() {
		return PlayerRemoveLevelatJobEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return PlayerRemoveLevelatJobEvent.handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Integer getAmount() {
		return amount;
	}
	
	
}
