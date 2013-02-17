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
			this.HandlePlayerDeath(player, event.getDrops());
		}
		else if(event.getEntity() instanceof Zombie)
		{
			Zombie zombie = (Zombie) event.getEntity();
			this.HandleZombieDeath(zombie, event.getDrops());
		}
	}

	private void HandleZombieDeath(Zombie zombie, List<ItemStack> droppedItems) 
	{
		if(this.configHandler.getCanDropEquip())
		{
			if(zombie.getEquipment().getHelmet().getType() == Material.SKULL_ITEM)
			{
				SkullMeta skullmeta = (SkullMeta) zombie.getEquipment().getHelmet().getItemMeta();
				String owner = skullmeta.getOwner();

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
			
			if(this.configHandler.getCanDropEquip())
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
