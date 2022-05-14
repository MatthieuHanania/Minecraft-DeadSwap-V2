package fr.Reycun.DeathSwap;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class DSEvent implements Listener {
	
	private Main main;
	
	public DSEvent(Main main) {
		this.main = main;
	}
	
	
	int abso(Player p){
		
	    for (PotionEffect pe : p.getActivePotionEffects()){
	    
	        if (pe.getType().equals(PotionEffectType.ABSORPTION)){
	        
	            int total = pe.getAmplifier() * 4 + 4;
	            return total;
	        }
	    } 
	    return 0;
	}
	
	private boolean IsNotInSpawn(Player player, int x, int y, int z) {
		
		Location locate = player.getLocation();
		if ( locate.getWorld().equals(Bukkit.getWorlds().get(0))  && locate.getBlockX() < x && locate.getBlockX() > (-x) && locate.getBlockZ() > (-z) && locate.getBlockZ() < z && locate.getBlockY() >(y-20) && locate.getBlockY() <(y+20) ) {
			
			return false;
		}
		else {
			
			return true;
		}
	}

	
	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		UUID u = p.getUniqueId();
		
		p.setHealth(p.getHealth());
		
		p.setPlayerListHeader("§6»»»» DeathSwap ««««\n§8§m                     ");
		p.setPlayerListFooter("§8§m                     \n§8Plugin » §7Reycun");
		
		//if (main.getKick().contains(u)) main.getKick().remove(u);
		if(main.getSavePlayer().containsKey(u)) main.getSavePlayer().remove(u,p.getName());
		
		if (main.IsState(DState.PLAYING) && !main.getUUID().contains(p.getUniqueId())) {
			
			p.sendMessage("§9§l[DeathSwap]§r La partie est en cours, teleporation dans le monde de jeu");
			World world = Bukkit.getWorld("Jeu");
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(new Location(world, p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ()));
			
			
		}
		else if(main.IsState(DState.PLAYING) && main.getUUID().contains(p.getUniqueId())) {
			
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §e"+p.getName()+"§r est de retour");
		}
		if(main.IsState(DState.WAITING) && IsNotInSpawn(p, 40,150,40 )) {
			
			p.teleport(new Location(Bukkit.getWorlds().get(0), 0,157,0));
		}

	}
	

	@EventHandler
	private void onInterract(PlayerInteractEntityEvent event) {
		
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		ItemStack vitreVide= new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta vitreMeta = vitreVide.getItemMeta();
		vitreMeta.setDisplayName(" ");
		vitreVide.setItemMeta(vitreMeta);
		
		if (entity instanceof Player && player.getGameMode().equals(GameMode.SPECTATOR)) {
			
			
			Player cible = (Player) entity;
			
			
			Inventory invspec = Bukkit.createInventory(null, 45, "Inventaire de joueur");
			
			invspec.setItem(0, cible.getInventory().getHelmet());
			invspec.setItem(1, cible.getInventory().getChestplate());
			invspec.setItem(2, cible.getInventory().getLeggings());
			invspec.setItem(3, cible.getInventory().getBoots());
			invspec.setItem(4, cible.getInventory().getItemInOffHand());
			
			invspec.setItem(5, vitreVide);
			
			ItemStack Vie = new ItemStack(Material.RED_DYE);
			ItemMeta VieMeta = Vie.getItemMeta();
			VieMeta.setDisplayName("Vie: §4"+(cible.getHealth()+abso(cible)));
			Vie.setItemMeta(VieMeta);
			invspec.setItem(6, Vie);
			
			for (int i = 0; i< cible.getInventory().getSize()-5; i++) {
				
				ItemStack item = cible.getInventory().getItem(i);
				//String nomItem = cible.getInventory().getItem(i).getItemMeta().getDisplayName();
				
				invspec.setItem(i+9, item);
				
			}
			
			
			player.openInventory(invspec);
		}
	}

	
	@EventHandler
	private void OnClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		String nomInv= event.getView().getTitle(); 
		ItemStack item = event.getCurrentItem();
		
		int slot = event.getSlot();
		
		
		if(item != null && item.getItemMeta() != null) {
			
			String nomItem = item.getItemMeta().getDisplayName();
			
			if (nomInv == "Inventaire de joueur" || nomInv == "Menu du DeathSwap") { 
				
				event.setCancelled(true);
			}
			
			if(nomInv == "Menu du DeathSwap") {
				
				
				if(item.getType() == Material.LIME_WOOL && nomItem.equals("§aCommencer la partie")) {
					
					if (Bukkit.getOnlinePlayers().size() >1) main.getCommands().doStart(player);
					else main.getCommands().doFakeStart(player);
					player.closeInventory();
					
				}
				else if(nomItem.equals("§3Reset")) {
					
					main.getCommands().doReset(player);
					player.closeInventory();
					
				}
				else if(nomItem.equals("§cArreter la partie")) {
					
					main.getCommands().doStop(player);
					player.closeInventory();
				}
				else if(nomItem.contains("Starter pack")) {
					
					main.getInvManagement().switchKit();
					main.getInvManagement().updateKit();
					

				}
				else if(nomItem.contains("Mode UHC :")) {
					
					main.getInvManagement().SwitchUhc();
					main.getInvManagement().updateUhc();
					
				}
				else if(nomItem.contains("§3Difficulté :")) {
									
					main.getInvManagement().switchDifficulty();
					main.getInvManagement().updateDifficulty();
				}
				else if (nomItem.contains("Alertes avant les TP :")){
					
					main.getInvManagement().switchTimeInfo();
					main.getInvManagement().updateTimeInfo();
					
				}
				else if(nomItem.contains("Temps minimal: §6+30/-30")) {
					
					if (slot == 29) {
												
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutFirstMin(-30);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutFirstMin(+30);
					}
					else if(slot == 38) {
						
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutOtherMin(-30);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutOtherMin(+30);
						
					}
					main.getInvManagement().updateFirstTime();
					main.getInvManagement().updateOtherTimer();
					
				}
				else if(nomItem.contains("Temps minimal: §6+5/-5")) {
					
					if (slot == 30) {
						
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutFirstMin(-5);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutFirstMin(+5);
					}
					if (slot == 39) {
						
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutOtherMin(-5);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutOtherMin(+5);
					}
					main.getInvManagement().updateFirstTime();
					main.getInvManagement().updateOtherTimer();
					
				}
				else if(nomItem.contains("Temps aléatoire: §c+5/-5")) {
					
					if(slot == 32) {
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutFirstMax(-5);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutFirstMax(+5);
					}
					else if(slot == 41) {
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutOtherMax(-5);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutOtherMax(+5);
					}
					main.getInvManagement().updateFirstTime();
					main.getInvManagement().updateOtherTimer();
					
				}
				else if(nomItem.contains("Temps aléatoire: §c+30/-30")) {
					
					if(slot == 33) {
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutFirstMax(-30);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutFirstMax(+30);
					}
					else if(slot == 42) {
						if (event.getAction().equals(InventoryAction.PICKUP_HALF)) main.getInvManagement().ajoutOtherMax(-30);
						else if (event.getAction().equals(InventoryAction.PICKUP_ALL)) main.getInvManagement().ajoutOtherMax(+30);
					}
					main.getInvManagement().updateFirstTime();
					main.getInvManagement().updateOtherTimer();
					
				}
				
				

			}
						
		}
		
		
	}
	
	@EventHandler
	private void onQuit(PlayerQuitEvent event) {
		
		Player p = event.getPlayer();
		UUID u = p.getUniqueId();
		String pseudo = p.getName();
		
		//if(main.getScorB().containsKey(p)) main.getScorB().get(p).destroy();
		
		if(main.getUUID().contains(u)) {
			
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r Le joueur §e"+ p.getName()+"§r vient de se déconnecter, il a deux minutes pour revenir");
			//main.getKick().add(u);
			main.getSavePlayer().put(u, pseudo);
			
			Bukkit.getScheduler().runTaskLater(main, () -> {
					
					
					if (main.getSavePlayer().containsKey(u) && main.getUUID().contains(u)){
						main.getUUID().remove(u);
						main.getSavePlayer().remove(u, p.getName());
						Bukkit.broadcastMessage("§9§l[DeathSwap] §r Le joueur §e"+pseudo+" §ra été éliminé, cheh");
						
						main.getDsScoreboard().updateScoreboardPlayer();
						
						main.CheckWin();
						
					}
					
					
	        }, 2400);
			
		}
				
	}
	
	@EventHandler
	private void OnPlayerPortal(PlayerPortalEvent event) {
		
		
		if(event.getFrom().getWorld().equals(Bukkit.getWorld("Jeu_Nether"))) {
			
			Location NewLocation = event.getTo();
			NewLocation.setWorld(Bukkit.getWorld("Jeu"));
			event.setTo(NewLocation);
			
		}
		else if( event.getFrom().getWorld().equals(Bukkit.getWorld("Jeu")) ) {
			
			Location NewLocation = event.getTo();
			NewLocation.setWorld(Bukkit.getWorld("Jeu_Nether"));
			event.setTo(NewLocation);
		}
	}
	
	@EventHandler
	private void OnEntityPortal(EntityPortalEvent event) {
		
		if(event.getFrom().getWorld().equals(Bukkit.getWorld("Jeu_Nether"))) {
			
			Location NewLocation = event.getTo();
			NewLocation.setWorld(Bukkit.getWorld("Jeu"));
			event.setTo(NewLocation);
			
		}	
		else if( event.getFrom().getWorld().equals(Bukkit.getWorld("Jeu")) ) {
		
			Location NewLocation = event.getTo();
			NewLocation.setWorld(Bukkit.getWorld("Jeu_Nether"));
			event.setTo(NewLocation);
		}
			
	}
	
	
	@EventHandler
	private void onDeath(PlayerDeathEvent event) {
				
		Entity e = event.getEntity();
		if (e instanceof Player) {
			
			Player p = (Player) e;
			UUID u = p.getUniqueId();
			
			if(main.IsState(DState.PLAYING)) {

				if(main.getUUID().contains(u)) {
					
					
					Bukkit.broadcastMessage("§9§l[DeathSwap]  §r Le joueur §e"+p.getName()+"§r est éliminé, il aurait pu ressusciter avec Twitch prime");
					p.setGameMode(GameMode.SPECTATOR);
					main.getUUID().remove(u);
					main.getDsScoreboard().updateScoreboardPlayer();
					main.CheckWin();
				
				}
				else {
					
					Bukkit.broadcastMessage("§9§l[DeathSwap]  §r §e"+ p.getName()+ "§r est mort mais il ne faisait pas partie de la partie");
					
				}
				
				
				
			}
		}
	
	}
	
}
