package com.gmail.mikeundead.Listeners;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.mikeundead.Handlers.ZombieHandler;
import com.gmail.mikeundead.util.EntityDeathListenerModel;

public class EntityDeathListener implements Listener
{
    private EntityDeathListenerModel model;
    public EntityDeathListener(EntityDeathListenerModel model)
	{
		this.model = model;
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
		boolean pvp = this.model.getConfig().getZombieSpawnCondition().getPvP();

		if(pvp && player.getKiller() instanceof Player)
		{
			return true;
		}

		return false;
	}

	private boolean IsPvEKill(DamageCause damageCause)
	{
		boolean pve = this.model.getConfig().getZombieSpawnCondition().getPvE();

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
		boolean env = this.model.getConfig().getZombieSpawnCondition().getEnv();

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
		if(this.model.getZombiesWithInventory().contains(zombie.getUniqueId()))
		{
			if(this.model.getConfig().getCanPickupInventory())
			{
				for(ItemStack item : this.model.getPlayerInventory().get(zombie.getUniqueId()))
				{
					droppedItems.add(item);
				};
			}
			this.model.getZombiesWithInventory().remove(zombie.getUniqueId());
			this.model.getZombieChunks().remove(zombie);
		}
	}

	private void HandlePlayerDeath(Player player, List<ItemStack> droppedItems)
	{
		if(player.hasPermission("zombieresurrection.zombiespawn"))
		{
			if(this.model.getConfig().getActivatedInWorld().contains(player.getWorld()))
			{
				LivingEntity zombie = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
				this.model.getZombiesWithInventory().add(zombie.getUniqueId());

				ZombieHandler zombieHandler = new ZombieHandler(this.model.getConfig());
				zombieHandler.EquipZombie(zombie, player.getName(), player.getInventory());

				if(this.model.getConfig().getCanPickupInventory())
				{
					if(player.getInventory().getHelmet() != null)
					{
						player.getInventory().addItem(player.getInventory().getHelmet());
					}

					this.model.getPlayerInventory().put(zombie.getUniqueId(), player.getInventory().getContents());
					droppedItems.clear();
				}

				this.model.getZombieChunks().put(zombie, zombie.getLocation());
			}
		}
	}
}
