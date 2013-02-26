package com.gmail.mikeundead.Handlers;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;


public class ZombieHandler
{
	private ConfigHandler configHandler;

	public ZombieHandler(ConfigHandler config)
	{
		this.configHandler = config;
	}

	public void EquipZombie(LivingEntity monster, String playername, PlayerInventory playerInventory)
	{
		this.setHelmet(monster, playername);
		this.setChestplate(monster, playerInventory);
		this.setBoots(monster, playerInventory);
		this.setPants(monster, playerInventory);
		this.setWeapon(monster, playerInventory);

		if(this.configHandler.getCanDropArmor())
		{
			if(this.configHandler.getcanDropPlayerHead())
			{
				monster.getEquipment().setHelmetDropChance(100);
			}
			else
			{
				monster.getEquipment().setHelmetDropChance(0);
			}

			monster.getEquipment().setChestplateDropChance(100);
			monster.getEquipment().setLeggingsDropChance(100);
			monster.getEquipment().setBootsDropChance(100);
		}
		else
		{
			monster.getEquipment().setHelmetDropChance(0);
			monster.getEquipment().setChestplateDropChance(0);
			monster.getEquipment().setLeggingsDropChance(0);
			monster.getEquipment().setBootsDropChance(0);
		}

		monster.getEquipment().setItemInHandDropChance(0);

		if(this.configHandler.getEffects().size() > 0)
		{
			this.SetPotionEffects(monster);
		}
	}

	private void SetPotionEffects(LivingEntity monster)
	{
		for(PotionEffect effect : this.configHandler.getEffects())
		{
			monster.addPotionEffect(effect);
		}
	}

	private void setWeapon(LivingEntity monster, PlayerInventory playerInventory)
	{
		monster.getEquipment().setItemInHand(playerInventory.getItemInHand());
	}

	private void setHelmet(LivingEntity monster, String playername)
	{
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		String skullName = playername;
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwner(skullName);
		skull.setItemMeta(skullMeta);

		monster.getEquipment().setHelmet(skull);
	}

	private void setChestplate(LivingEntity monster, PlayerInventory playerInventory)
	{
		monster.getEquipment().setChestplate(playerInventory.getChestplate());
	}

	private void setPants(LivingEntity monster, PlayerInventory playerInventory)
	{
		monster.getEquipment().setLeggings(playerInventory.getLeggings());
	}

	private void setBoots(LivingEntity monster, PlayerInventory playerInventory)
	{
		monster.getEquipment().setBoots(playerInventory.getBoots());
	}
}
