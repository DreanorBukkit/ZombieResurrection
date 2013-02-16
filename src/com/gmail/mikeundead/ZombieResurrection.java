package com.gmail.mikeundead;

import org.bukkit.plugin.java.JavaPlugin;


import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Listeners.EntityDeathListener;

public class ZombieResurrection extends JavaPlugin
{
    public void onEnable()
    {
        ConfigHandler configHandler = new ConfigHandler(this);
        EntityDeathListener entityDeathListener = new EntityDeathListener(configHandler);
        this.getServer().getPluginManager().registerEvents(entityDeathListener, this);

        this.getLogger().info("ZombieResurrection has been enabled.");
	}

	public void onDisable()
    {
        this.getLogger().info("ZombieResurrection has been disabled.");
	}
}