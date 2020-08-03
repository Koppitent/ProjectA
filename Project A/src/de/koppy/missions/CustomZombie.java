package de.koppy.missions;

import org.bukkit.Location;

import net.minecraft.server.v1_16_R1.EntityZombie;
import net.minecraft.server.v1_16_R1.GenericAttributes;
import net.minecraft.server.v1_16_R1.World;

public class CustomZombie extends EntityZombie{

	public CustomZombie(World world, Location location) {
		super(world);
		setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		setCustomNameVisible(true);
		initAttributes();
	}
	
	public void initAttributes() {
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0);
	}
	
	
	
}