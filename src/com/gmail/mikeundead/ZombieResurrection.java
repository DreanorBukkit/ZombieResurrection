package com.gmail.mikeundead;

import multiworld.MultiWorldPlugin;
import multiworld.api.MultiWorldAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Listeners.EntityDeathListener;

public class ZombieResurrection extends JavaPlugin implements Listener
{
	private EntityDeathListener entityDeathListener;

    @Override
	public void onEnable()
    {
		ConfigHandler configHandler = new ConfigHandler(this);
        this.entityDeathListener = new EntityDeathListener(configHandler);

        this.getServer().getPluginManager().registerEvents(this.entityDeathListener, this);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("ZombieResurrection has been enabled.");
	}

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
    	MultiWorldAPI multiWorld = ((MultiWorldPlugin)this.getServer().getPluginManager().getPlugin("MultiWorld")).getApi();
    	if(multiWorld != null)
    	{
			this.entityDeathListener.SetMultiWorld(multiWorld);
    	}
    }

	@Override
	public void onDisable()
    {
        this.getLogger().info("ZombieResurrection has been disabled.");
	}
}