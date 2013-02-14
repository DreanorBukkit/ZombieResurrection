package com.gmail.mikeundead.Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.mikeundead.ZombieResurrection;

public class ConfigHandler
{
	private ArrayList<PotionEffect> potionEffects;
	private ZombieResurrection plugin;
	private File configFile;
    private FileConfiguration config;
    
    public ConfigHandler(ZombieResurrection plugin)
    {
//    	this.plugin = plugin;
//    	this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
//    	
//	    try 
//	    {
//	    	this.FirstRun();
//	    } 
//	    catch (Exception e) 
//	    {
//	        e.printStackTrace();
//	    }
//	    
//	    this.config = new YamlConfiguration();
//	    this.LoadYamls();
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
		List<String> list = this.config.getStringList("Effects");
		for(String effect : list)
		{
			this.potionEffects.add(new PotionEffect(PotionEffectType.getByName(effect), Integer.MAX_VALUE, 0));
		}
	}
	
	public ArrayList<PotionEffect> getEffects() 
	{
		return this.potionEffects;
	}

	public int getModifiedHp() 
	{
		return 0;
	}

	public boolean getCanDropEquip() 
	{
		return false;
	}
}