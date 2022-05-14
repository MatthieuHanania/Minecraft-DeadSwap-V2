package fr.Reycun.DeathSwap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;


public class LoadReymatic {

	private Main main;
	
	public LoadReymatic(Main main) {
		this.main = main;
	}
	
	@SuppressWarnings("deprecation")
	public void loadAt(Location locate) {
		
		int x = locate.getBlockX();
		int y = locate.getBlockY();
		int z = locate.getBlockZ();
		
		
		
	    InputStream input = main.getResource("reymatic.txt");	    
		
		if (input == null) {
			System.out.print("§cIl n'y a pas de spawn");
            //folder.mkdir();
        }
		else {
			
			
			try {
				//BufferedReader reader = new BufferedReader(new FileReader(file));
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String line;
				while((line = reader.readLine()) != null) {
					
					
					String[] arg = line.split("//");
					
					
					int a = Integer.parseInt(arg[0]);
					int b = Integer.parseInt(arg[1]);
					int c = Integer.parseInt(arg[2]);
					
					String data = arg[3];
					
					
					final BlockData blockD =  main.getServer().createBlockData(data);
					
					Location locateBLock = new Location(Bukkit.getWorlds().get(0), a+x,b+y,c+z);
					locateBLock.getBlock().setBlockData(blockD);
					BlockState blockstate = locateBLock.getBlock().getState();
					
					
					if (blockD instanceof Rail) {
						
						
						for(Entity entity : locateBLock.getChunk().getEntities()){
							
							if (entity.getLocation().getBlock().equals(locateBLock.getBlock()) && entity.getType().equals(EntityType.MINECART))  {
								entity.remove();
							}
						}
						
						locateBLock.add(0.5, 0, 0).getWorld().spawnEntity(locateBLock, EntityType.MINECART);
						
						
					}
					else if(blockstate instanceof Sign){
						Sign sign = (Sign) blockstate;
						sign.setLine(0, arg[4]);
						sign.update();
						sign.setLine(1, arg[5]);
						sign.update();
						sign.setLine(2, arg[6]);
						sign.update();
						sign.setLine(3, arg[7]);
						sign.update();
					}
					else if (blockstate instanceof Skull) {
						
						String name = arg[4];
																
						Skull skull = (Skull) blockstate;
						skull.setOwner(name);
						skull.update();
					}
					
				}
				
			} catch (FileNotFoundException e) {
				System.out.println("Pas de fichier");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("cassé");
				e.printStackTrace();
			}
		}
		
		
	}

}
