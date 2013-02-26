package com.gmail.mikeundead.Listeners;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Handlers.ZombieHandler;

public class EntityDeathListener implements Listener
{
    private ConfigHandler configHandler;
	private HashMap<String, ItemStack[]> playerInventory;

    public EntityDeathListener(ConfigHandler config)
	{
		this.configHandler = config;
		this.playerInventory = new HashMap<String, ItemStack[]>();
	}

	@EventHandler
    public void onEntityDeath(EntityDeathEvent event)
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
		if(zombie.getEquipment().getHelmet().getType() == Material.SKULL_ITEM)
		{
			SkullMeta skullmeta = (SkullMeta) zombie.getEquipment().getHelmet().getItemMeta();
			String owner = skullmeta.getOwner();

			if(this.configHandler.getCanPickupInventory())
			{
				if(this.playerInventory.containsKey(owner))
				{
					for(ItemStack item : this.playerInventory.get(owner))
					{
						droppedItems.add(item);
					}

					this.playerInventory.remove(owner);
				}
			}
		}
	}

	private void HandlePlayerDeath(Player player, List<ItemStack> droppedItems)
	{
		if(player.hasPermission("zombieresurrection.zombiespawn"))
		{
			LivingEntity zombie = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
			ZombieHandler zombieHandler = new ZombieHandler(this.configHandler);
			zombieHandler.EquipZombie(zombie, player.getName(), player.getInventory());

			if(this.configHandler.getCanPickupInventory())
			{
				if(player.getInventory().getHelmet() != null)
				{
					player.getInventory().addItem(player.getInventory().getHelmet());
				}

				this.playerInventory.put(player.getName(), player.getInventory().getContents());
				droppedItems.clear();
			}
		}
	}
}
