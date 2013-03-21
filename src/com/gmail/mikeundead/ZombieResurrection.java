package com.gmail.mikeundead;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.mikeundead.Listeners.ChunkListener;
import com.gmail.mikeundead.Listeners.EntityDeathListener;
import com.gmail.mikeundead.util.EntityDeathListenerModel;

public class ZombieResurrection extends JavaPlugin
{
    @Override
	public void onEnable()
    {
        EntityDeathListenerModel entityDeathListenerModel = new EntityDeathListenerModel(this);
        EntityDeathListener entityDeathListener = new EntityDeathListener(entityDeathListenerModel);
        ChunkListener chunkListener = new ChunkListener(entityDeathListenerModel);
        this.getServer().getPluginManager().registerEvents(entityDeathListener, this);
        this.getServer().getPluginManager().registerEvents(chunkListener, this);

        this.getLogger().info("ZombieResurrection has been enabled.");
	}

	@Override
	public void onDisable()
    {
        this.getLogger().info("ZombieResurrection has been disabled.");
	}
}