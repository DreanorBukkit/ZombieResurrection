package com.gmail.mikeundead.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.mikeundead.ZombieResurrection;
import com.gmail.mikeundead.Handlers.ConfigHandler;

public class EntityDeathListenerModel
{
	private ConfigHandler configHandler;
	private HashMap<UUID, ItemStack[]> playerInventory;
	private ArrayList<UUID> zombieWithInventory;
	private HashMap<LivingEntity, Location> zombieChunks;

	public EntityDeathListenerModel(ZombieResurrection plugin)
	{
		this.configHandler = new ConfigHandler(plugin);
		this.playerInventory = new HashMap<UUID, ItemStack[]>();
		this.zombieWithInventory = new ArrayList<UUID>();
		this.zombieChunks = new HashMap<LivingEntity, Location>();
	}

    public ConfigHandler getConfig()
    {
		return configHandler;
	}

	public HashMap<UUID, ItemStack[]> getPlayerInventory()
	{
		return playerInventory;
	}

	public ArrayList<UUID> getZombiesWithInventory()
	{
		return zombieWithInventory;
	}

	public HashMap<LivingEntity, Location> getZombieChunks()
	{
		return zombieChunks;
	}
}
