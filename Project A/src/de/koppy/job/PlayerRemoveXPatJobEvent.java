package de.koppy.job;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRemoveXPatJobEvent extends Event{

	public static HandlerList handlers = new HandlerList();
	Player player;
	Integer amount;
	
	public PlayerRemoveXPatJobEvent(Player player, Integer amount) {
		this.player = player;
		this.amount = amount;
	}
	
	@Override
	public HandlerList getHandlers() {
		return PlayerRemoveXPatJobEvent.handlers;
	}
	
	public static HandlerList getHandlerList() {
		return PlayerRemoveXPatJobEvent.handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Integer getAmount() {
		return amount;
	}
	
	
}
