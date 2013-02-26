package com.gmail.mikeundead.Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.mikeundead.ZombieResurrection;
import com.gmail.mikeundead.util.SpawnConditions;

public class ConfigHandler
{
	private ArrayList<PotionEffect> potionEffects;
	private ZombieResurrection plugin;
	private File configFile;
    private FileConfiguration config;
	private boolean canDropArmor;
	private boolean canPickupInventory;
	private boolean canDropPlayerHead;
	private SpawnConditions zombieSpawnCondition;

    public ConfigHandler(ZombieResurrection plugin)
    {
    	this.plugin = plugin;
    	this.configFile = new File(this.plugin.getDataFolder(), "config.yml");

	    try
	    {
	    	this.FirstRun();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }

	    this.config = new YamlConfiguration();
	    this.LoadYamls();
    }

    public ArrayList<PotionEffect> getEffects()
	{
		return this.potionEffects;
	}

	public boolean getCanDropArmor()
	{
		return this.canDropArmor;
	}

	public SpawnConditions getZombieSpawnCondition()
	{
		return this.zombieSpawnCondition;
	}

	public boolean getCanPickupInventory()
	{
		return this.canPickupInventory;
	}

	public boolean getcanDropPlayerHead()
	{
		return this.canDropPlayerHead;
	}

	private void FirstRun() throws Exception
	{
	    if(!this.configFile.exists())
	    {
	        this.configFile.getParentFile().mkdirs();

	        this.config = new YamlConfiguration();

	        this.SaveYamls();

	        this.Copy(this.plugin.getResource("config.yml"), configFile);
	    }
	}

	private void Copy(InputStream in, File file)
	{
	    try
	    {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;

	        while((len=in.read(buf)) > 0)
	        {
	            out.write(buf, 0, len);
	        }

	        out.close();
	        in.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}

	private void SaveYamls()
	{
	    try
	    {
	        this.config.save(configFile);
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}

	private void LoadYamls()
	{
	    try
	    {
	        this.config.load(configFile);
	        this.LoadValues();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}

	private void LoadValues()
	{
		this.setPotionEffects();
		this.setCanPickupInventory();
		this.setCanDropPlayerHead();
		this.setCanDropArmor();
		this.setZombieSpawnCondition();
	}

	private void setZombieSpawnCondition()
	{
		boolean pvp = this.config.getBoolean("ZombieSpawnOnPvPDeath");
		boolean pve = this.config.getBoolean("ZombieSpawnOnPvEDeath");
		boolean env = this.config.getBoolean("ZombieSpawnOnEnvironmentDeath");

		this.zombieSpawnCondition = new SpawnConditions(pvp, pve, env);
	}

	private void setCanDropArmor()
	{
		this.canDropArmor = this.config.getBoolean("CanDropArmor");
	}

	private void setCanDropPlayerHead()
	{
		this.canDropPlayerHead = this.config.getBoolean("CanDropPlayerHead");
	}

	private void setCanPickupInventory()
	{
		this.canPickupInventory = this.config.getBoolean("CanPickupInventoryAndDrop");
	}

	private void setPotionEffects()
	{
		this.potionEffects = new ArrayList<PotionEffect>();

		if(this.config.getInt("Effects.SPEED") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, this.config.getInt("Effects.SPEED")));
		}
		if(this.config.getInt("Effects.INCREASE_DAMAGE") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, this.config.getInt("Effects.INCREASE_DAMAGE")));
		}
		if(this.config.getInt("Effects.DAMAGE_RESISTANCE") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, this.config.getInt("Effects.DAMAGE_RESISTANCE")));
		}
		if(this.config.getInt("Effects.FIRE_RESISTANCE") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, this.config.getInt("Effects.FIRE_RESISTANCE")));
		}
		if(this.config.getInt("Effects.JUMP") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, this.config.getInt("Effects.JUMP")));
		}
	}
}