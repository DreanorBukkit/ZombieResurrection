package com.gmail.mikeundead.Handlers;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
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

    public void EquipZombie(LivingEntity livingEntity, String playername, PlayerInventory playerInventory)
	{
    	this.setHelmet(livingEntity, playername);
		this.setChestplate(livingEntity, playerInventory);
		this.setBoots(livingEntity, playerInventory);
		this.setPants(livingEntity, playerInventory);
        this.setWeapon(livingEntity, playerInventory);
        
//		if(this.configHandler.getCanDropEquip())
//		{
//			EntityEquipment ee = livingEntity.getEquipment();
//			ee.setBootsDropChance(100);
//			ee.setChestplateDropChance(100);
//			ee.setHelmetDropChance(100);
//			ee.setItemInHandDropChance(100);
//			ee.setLeggingsDropChance(100);
//		}
     
//		if(this.configHandler.getEffects().size() > 0)
//		{
//			this.SetPotionEffects(livingEntity);
//		}
	}

	private void SetPotionEffects(LivingEntity livingEntity)
	{
		for(PotionEffect effect : this.configHandler.getEffects())
		{
			livingEntity.addPotionEffect(effect);
		}
	}

	private void setWeapon(LivingEntity monster, PlayerInventory playerInventory)
	{
		EntityEquipment ee = monster.getEquipment();
		ee.setItemInHand(playerInventory.getItemInHand());
	}

	private void setHelmet(LivingEntity monster, String playername)
	{
        EntityEquipment ee = monster.getEquipment();

		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		String skullName = playername;
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwner(skullName);
		skull.setItemMeta(skullMeta);
		
        ee.setHelmet(skull);
	}

	private void setChestplate(LivingEntity monster, PlayerInventory playerInventory)
	{
		EntityEquipment ee = monster.getEquipment();
		ee.setChestplate(playerInventory.getChestplate());
	}

	private void setPants(LivingEntity monster, PlayerInventory playerInventory)
	{
		EntityEquipment ee = monster.getEquipment();
		ee.setLeggings(playerInventory.getLeggings());
	}

	private void setBoots(LivingEntity monster, PlayerInventory playerInventory)
	{
		EntityEquipment ee = monster.getEquipment();
		ee.setBoots(playerInventory.getBoots());
	}
}
