package com.gmail.mikeundead;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Listeners.PlayerDeathListener;

public class ZombieResurrection extends JavaPlugin
{
    public void onEnable()
    {
        ConfigHandler configHandler = new ConfigHandler(this);
        PlayerDeathListener entityDeathListener = new PlayerDeathListener(configHandler);
        this.getServer().getPluginManager().registerEvents(entityDeathListener, this);

        this.getLogger().info("ZombieResurrection has been enabled.");
	}

	public void onDisable()
    {
        this.getLogger().info("ZombieResurrection has been disabled.");
	}
}