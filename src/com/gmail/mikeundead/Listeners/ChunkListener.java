package com.gmail.mikeundead.Listeners;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.mikeundead.util.EntityDeathListenerModel;

public class ChunkListener implements Listener
{
	private EntityDeathListenerModel model;

	public ChunkListener(EntityDeathListenerModel model)
	{
		this.model = model;
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		for(Entry<LivingEntity, Location> entry : this.model.getZombieChunks().entrySet())
		{
			if(entry.getValue().getChunk().equals(event.getChunk()))
			{
				LivingEntity zombie = (LivingEntity) event.getWorld().spawnEntity(entry.getValue(), EntityType.ZOMBIE);

				zombie.getEquipment().setBoots(entry.getKey().getEquipment().getBoots());
				zombie.getEquipment().setChestplate(entry.getKey().getEquipment().getChestplate());
				zombie.getEquipment().setHelmet(entry.getKey().getEquipment().getHelmet());
				zombie.getEquipment().setLeggings(entry.getKey().getEquipment().getLeggings());
				zombie.getEquipment().setItemInHand(entry.getKey().getEquipment().getItemInHand());

				if(this.model.getZombiesWithInventory().contains(entry.getKey().getUniqueId()))
				{
					this.model.getZombiesWithInventory().remove(entry.getKey().getUniqueId());
					this.model.getZombiesWithInventory().add(zombie.getUniqueId());
				}

				if(this.model.getPlayerInventory().containsKey(entry.getKey().getUniqueId()))
				{
					ItemStack[] inventory = this.model.getPlayerInventory().get(entry.getKey().getUniqueId());
					this.model.getPlayerInventory().remove(entry.getKey().getUniqueId());
					this.model.getPlayerInventory().put(zombie.getUniqueId(), inventory);
				}
			}
		}
	}
}
