package fr.Reycun.DeathSwap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class DSTimeTask extends BukkitRunnable{
	
	private Main main;
	private List<Player> Locate = new ArrayList<>();
	private int timer = 0;
	private int tempsTP = -1;
	private int tempsRestant;
	private int tempsMoy = 31;
	private int tempsMini = 90; 
	private int otherTempsMoy = 30;
	private int otherTempsMin = 90;
	
	
	
	private Random random = new Random();

	public DSTimeTask(Main main) {
		this.main = main;		
	}
		
	@Override
	public void run() {
		
				
		if (main.getUUID().size() <= 1) {
			
			main.CheckWin();
			cancel();
		}
		
		else if (Bukkit.getOnlinePlayers().size() > 0){
			
			Locate.clear();
			
			if (tempsTP == -1) {
				this.tempsMoy = main.getInvManagement().getFirstTempsMoy();
				this.tempsMini = main.getInvManagement().getFirstTempsMini();
				this.otherTempsMin = main.getInvManagement().getOtherTempsMini();
				this.otherTempsMoy = main.getInvManagement().getOtherTempsMoy();
				
				tempsTP = tempsMini+(random.nextInt(tempsMoy));
			}
			
			timer++;
			tempsRestant = tempsTP-timer;
			
			
			if (main.getInvManagement().getAlertTime()) {
						
				if (tempsRestant == 20 || tempsRestant == 10 || tempsRestant == 5 || tempsRestant == 3 || tempsRestant == 2 || tempsRestant == 1 ) {
					
					Bukkit.broadcastMessage("§9§l[DeathSwap] §7Téléportation dans §c"+tempsRestant+"§7s");
				}
				
			}
			
			if (tempsRestant == 1) {
				
				for (UUID uuid : main.getUUID()) {
					
					if(!main.getSavePlayer().containsKey(uuid)){
						
						Player p = Bukkit.getPlayer(uuid);
						p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
						p.getWorld().playEffect(p.getEyeLocation(), Effect.ENDER_SIGNAL, 2003);
					}
				}
			}
			
			if (tempsRestant == 0) {
								
				for (int i = 0; i < main.getUUID().size(); i++ ) {
					
					UUID u = main.getUUID().get(i);
					Player p = Bukkit.getPlayer(u);
					
					if (!main.getSavePlayer().containsKey(main.getUUID().get(i)) && p.isOnline()) {
						
						Locate.add(p);
					}
					
				}
								
				Collections.shuffle(Locate);
									
				int size = Locate.size();
				Location L0 = Locate.get(0).getLocation();
				
				for (int i = 0; i < size-1; i++) {
					
					Player p = Locate.get(i);
					Player p2 = Locate.get(i+1);
					p.teleport(p2.getLocation());
				}
				
				Player last = Locate.get(size-1);
				last.teleport(L0);
				
				tempsTP = otherTempsMin+(random.nextInt(otherTempsMoy));
				tempsRestant = tempsTP;
				timer = 0;
				
			}
			
		}
		

	}

}
