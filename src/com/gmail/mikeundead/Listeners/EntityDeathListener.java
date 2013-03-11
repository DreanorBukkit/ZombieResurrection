package com.gmail.mikeundead.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import multiworld.api.MultiWorldAPI;
import multiworld.api.MultiWorldWorldData;
import multiworld.api.flag.FlagName;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Handlers.ZombieHandler;

public class EntityDeathListener implements Listener
{
    private ConfigHandler configHandler;
	private HashMap<UUID, ItemStack[]> playerInventory;
	private ArrayList<UUID> zombieWithInventory;
	private MultiWorldAPI multiWorld;

    public EntityDeathListener(ConfigHandler config)
	{
		this.configHandler = config;
		this.playerInventory = new HashMap<UUID, ItemStack[]>();
		this.zombieWithInventory = new ArrayList<UUID>();
	}

	@EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
		if(this.multiWorld != null)
		{
			MultiWorldWorldData worldData = this.multiWorld.getWorld(event.getEntity().getWorld().getName());

			if(worldData.getOptionValue(FlagName.SPAWNMONSTER))
			{
				this.HandleEntityDeath(event);
			}
		}
		else
		{
			this.HandleEntityDeath(event);
		}
	}

	private void HandleEntityDeath(EntityDeathEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			DamageCause damageCause = player.getLastDamageCause().getCause();

	        if(this.IsPvPKill(damageCause, player))
	        {
	        	this.HandlePlayerDeath(player, event.getDrops());
	        	return;
	        }
	        else if(this.IsPvEKill(damageCause))
	        {
		     	this.HandlePlayerDeath(player, event.getDrops());
	        	return;
	        }
	        else if(this.IsEnviromentKill(damageCause))
			{
		     	this.HandlePlayerDeath(player, event.getDrops());
	        	return;
			}
		}
		else if(event.getEntity() instanceof Zombie)
		{
			Zombie zombie = (Zombie) event.getEntity();
			this.HandleZombieDeath(zombie, event.getDrops());
		}
	}

	private boolean IsPvPKill(DamageCause damageCause, Player player)
	{
		boolean pvp = this.configHandler.getZombieSpawnCondition().getPvP();

		if(pvp && player.getKiller() instanceof Player)
		{
			return true;
		}

		return false;
	}

	private boolean IsPvEKill(DamageCause damageCause)
	{
		boolean pve = this.configHandler.getZombieSpawnCondition().getPvE();

		if(pve)
		{
			if(damageCause == DamageCause.ENTITY_ATTACK || damageCause == DamageCause.ENTITY_EXPLOSION || damageCause == DamageCause.PROJECTILE)
			{
				return true;
			}
		}

		return false;
	}

	private boolean IsEnviromentKill(DamageCause damageCause)
	{
		boolean env = this.configHandler.getZombieSpawnCondition().getEnv();

		if(env)
		{
			if(damageCause == DamageCause.ENTITY_ATTACK || damageCause == DamageCause.ENTITY_EXPLOSION)
			{
				return false;
			}

			return true;
		}

		return false;
	}

	private void HandleZombieDeath(Zombie zombie, List<ItemStack> droppedItems)
	{
		if(this.zombieWithInventory.contains(zombie.getUniqueId()))
		{
			if(this.configHandler.getCanPickupInventory())
			{
				for(ItemStack item : this.playerInventory.get(zombie.getUniqueId()))
				{
					droppedItems.add(item);
				}

				this.playerInventory.remove(zombie.getUniqueId());
			}
			this.zombieWithInventory.remove(zombie.getUniqueId());
		}
	}

	private void HandlePlayerDeath(Player player, List<ItemStack> droppedItems)
	{
		if(player.hasPermission("zombieresurrection.zombiespawn"))
		{
			LivingEntity zombie = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
			this.zombieWithInventory.add(zombie.getUniqueId());

			ZombieHandler zombieHandler = new ZombieHandler(this.configHandler);
			zombieHandler.EquipZombie(zombie, player.getName(), player.getInventory());

			if(this.configHandler.getCanPickupInventory())
			{
				if(player.getInventory().getHelmet() != null)
				{
					player.getInventory().addItem(player.getInventory().getHelmet());
				}

				this.playerInventory.put(zombie.getUniqueId(), player.getInventory().getContents());
				droppedItems.clear();
			}
		}
	}

	public void SetMultiWorld(MultiWorldAPI multiWorld)
	{
		this.multiWorld = multiWorld;
	}
}
