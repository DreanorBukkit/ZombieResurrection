package com.gmail.mikeundead.util;

public class SpawnConditions 
{
	private boolean PvP;
	private boolean PvE;
	private boolean Env;
	
	public SpawnConditions(boolean pvp, boolean pve, boolean env)
	{
		this.PvP = pvp;
		this.PvE = pve;
		this.Env = env;
	}
	
	public boolean getPvP()
	{
		return this.PvP;
	}
	
	public boolean getPvE()
	{
		return this.PvE;
	}
	
	public boolean getEnv()
	{
		return this.Env;
	}
}
