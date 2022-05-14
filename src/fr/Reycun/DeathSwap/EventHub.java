package fr.Reycun.DeathSwap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class EventHub implements Listener {

	
	private Main main;
	
	public EventHub(Main main) {
		
		this.main = main;
	}
	
	
	@EventHandler
	public void OnDeadEvent(EntityDamageEvent event) {
		
		if (main.IsState(DState.WAITING)){
			
			Entity entity = event.getEntity();
			if (entity instanceof Player) {
				
				Player player = (Player) entity;
				
				
				
				if (player.getHealth() - event.getDamage() <= 0) {
					
					event.setDamage(0);
					player.setHealth(20);
					player.setFoodLevel(20);
					player.getInventory().clear();
					player.teleport(new Location(Bukkit.getWorlds().get(0), 0, 155, 0));
				}
			}
		}
	}
	
	
	@EventHandler
	public void OnInterractEvent (PlayerInteractEvent event) {
		
	
		Player player = event.getPlayer();
		
		if (event.getClickedBlock() != null) {
			
			BlockState bs = event.getClickedBlock().getState();
			
			if (bs instanceof Sign) {
				
				Sign sign = (Sign) bs;
				
				if (sign.getLine(0).equalsIgnoreCase("[Clique]") && sign.getLine(2).equalsIgnoreCase("Zone PVP")) {
					
					if (main.IsState(DState.WAITING)){
						
						Location centre = new Location(player.getWorld(), 30 , 155,0);
						player.sendMessage("§9§l[DeathSwap]§r Teleportation dans la zone PVP");
						
						player.setHealth(20);
						player.setFoodLevel(20);
						player.getInventory().clear();
						player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
						player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
						player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
						player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
						player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
						player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
						player.teleport(centre);
					}
					
					
					
				}
				else if(sign.getLine(0).equalsIgnoreCase("[Clique]") && sign.getLine(2).equalsIgnoreCase("Hub")) {
					
					if (main.IsState(DState.WAITING)){
						
						Location zero = new Location(player.getWorld(), 0 , 155,0);
						player.sendMessage("§9§l[DeathSwap]§r Teleportation au Hub");
						player.getInventory().clear();
						player.teleport(zero);
					}					
				}
				else if(sign.getLine(0).equalsIgnoreCase("[Clique]") && sign.getLine(2).equalsIgnoreCase("Heal")) {
					
					if (main.IsState(DState.WAITING)){
						player.setHealth(20);
						player.setFoodLevel(20);
						
					}
					
				}
			}
		}
		
		
		
		
	}

}
