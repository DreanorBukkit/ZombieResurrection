package com.gmail.mikeundead.Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.bukkit.World;
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
	private ArrayList<World> activatedInWorld;
	private String zombieName;
	private double zombieSpawnChance;
	private double playerHeadDropChance;

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

    public ArrayList<World> getActivatedInWorld()
    {
    	return this.activatedInWorld;
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

	public boolean getCanDropPlayerHead()
	{
		return this.canDropPlayerHead;
	}

	public String getZombieName()
	{
		return this.zombieName;
	}

	public double getZombieSpawnChance()
	{
		return this.zombieSpawnChance;
	}

	public double getPlayerHeadDropChance()
	{
		return this.playerHeadDropChance;
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

	        Writer s = new OutputStreamWriter(out);
	        for(World name : this.plugin.getServer().getWorlds())
	        {
	            s.write(name.getName());
	            s.write(System.getProperty("line.separator"));
	            if(this.plugin.getServer().getWorlds().indexOf(name) != this.plugin.getServer().getWorlds().size() -1)
	            {
	            	s.write("- ");
	            }
	        }

	        s.close();
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
		this.setEnabledInWorlds();
		this.setZombieName();
		this.setZombieSpawnChance();
		this.setPlayerHeadDropChance();
	}

	private void setPlayerHeadDropChance()
	{
		this.playerHeadDropChance = this.config.getDouble("PlayerHeadDropChance");
	}

	private void setZombieSpawnChance()
	{
		this.zombieSpawnChance = this.config.getDouble("ZombieSpawnChance");
	}

	private void setZombieName()
	{
		this.zombieName = this.config.getString("ZombieName");
	}

	private void setEnabledInWorlds()
	{
		this.activatedInWorld = new ArrayList<World>();

		for(String world : this.config.getStringList("ActivatedInWorld"))
		{
			this.activatedInWorld.add(this.plugin.getServer().getWorld(world));
		}
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

		if(this.config.getInt("Effects.POISON") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, this.config.getInt("Effects.POISON")));
		}
		if(this.config.getInt("Effects.REGENERATION") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, this.config.getInt("Effects.REGENERATION")));
		}
		if(this.config.getInt("Effects.INVISIBILITY") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, this.config.getInt("Effects.INVISIBILITY")));
		}
		if(this.config.getInt("Effects.WATER_BREATHING") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, this.config.getInt("Effects.WATER_BREATHING")));
		}
		if(this.config.getInt("Effects.WEAKNESS") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, this.config.getInt("Effects.WEAKNESS")));
		}
		if(this.config.getInt("Effects.BLINDNESS") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, this.config.getInt("Effects.BLINDNESS")));
		}
		if(this.config.getInt("Effects.SLOW") > 0)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, this.config.getInt("Effects.SLOW")));
		}
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