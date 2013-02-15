package com.gmail.mikeundead.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Handlers.ZombieHandler;

public class PlayerDeathListener implements Listener
{
    private ConfigHandler configHandler;
	
    public PlayerDeathListener(ConfigHandler config)
	{
		this.configHandler = config;
	}

	@EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
		this.HandlePlayerDeath(event);
	}

	private void HandlePlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		if(player.hasPermission("zombieresurrection.zombiespawn"))
		{   		
			LivingEntity zombie = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
			ZombieHandler zombieHandler = new ZombieHandler(this.configHandler);
			zombieHandler.EquipZombie(zombie, player.getName(), player.getInventory());
		}
	}
}
