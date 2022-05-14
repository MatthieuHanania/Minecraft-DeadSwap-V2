package fr.Reycun.DeathSwap;

//import java.util.ArrayList;
//import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DSInitialTP extends BukkitRunnable{

	private Main main;
	
	public  DSInitialTP(Main main) {
		this.main = main;
	}
	
	private int i = 0;
	
	
	@Override
	public void run() {
		
		
		
		if (i < main.getUUID().size()) {
			
			
			if(!main.getSavePlayer().containsKey(main.getUUID().get(i))) {
				
				Player p3 = Bukkit.getPlayer(main.getUUID().get(i));
				
				int x = -2000 + new Random().nextInt(4000);
				int y = 130;
				int z = -2000 + new Random().nextInt(4000);
				World world = Bukkit.getWorld("Jeu");
				
				
				Location loc = (new Location(world,x,y,z));
				
				while(loc.getBlock().getBiome().name().contains("OCEAN")){
					
//while(loc.getBlock().getBiome().equals(Biome.WARM_OCEAN) || loc.getBlock().getBiome().equals(Biome.FROZEN_OCEAN) || loc.getBlock().getBiome().equals(Biome.LUKEWARM_OCEAN) || loc.getBlock().getBiome().equals(Biome.FROZEN_OCEAN) || loc.getBlock().getBiome().equals(Biome.DEEP_WARM_OCEAN) || loc.getBlock().getBiome().equals(Biome.DEEP_LUKEWARM_OCEAN) ||loc.getBlock().getBiome().equals(Biome.DEEP_COLD_OCEAN) || loc.getBlock().getBiome().equals(Biome.DEEP_OCEAN) || loc.getBlock().getBiome().equals(Biome.OCEAN) || loc.getBlock().getBiome().equals(Biome.FROZEN_OCEAN) || loc.getBlock().getBiome().equals(Biome.COLD_OCEAN) || loc.getBlock().getBiome().equals(Biome.DEEP_COLD_OCEAN)){
					x = -2000 + new Random().nextInt(4000);
					y = 162;
					z = -2000 + new Random().nextInt(4000);
					
					loc = (new Location(world,x,y,z));
					
				}
				
				
				p3.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 50));
				p3.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 600, 3));
				p3.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0));
				p3.setHealth(20);
				p3.setFoodLevel(20);
				
				loc.getChunk().load();
				
				p3.teleport(loc);
				
			}
			
			
			
		}
		else {
			main.getStart().runTaskTimer(main, 20, 20);
			cancel();
			
		}
		i++;
	}
}
