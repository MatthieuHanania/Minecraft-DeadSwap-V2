package fr.Reycun.DeathSwap;

import java.util.List;
import java.util.Random;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Lists;



public class Commands implements CommandExecutor, TabCompleter {

	private Main main;
	int timer = 0;

	
	public Commands(Main main) {
		this.main = main;
	}
	
	public int GetTimer() {
		
		return timer;
	}
	
	public boolean doStop(Player player){
		
		if(main.IsState(DState.WAITING)) player.sendMessage("§9§l[DeathSwap]§r§c Il n'y a pas de partie en cours !");
		else {
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cArrêt de la partie");
			Bukkit.reload();
//			main.getStart().cancel();
//			main.getUUID().clear();
//			main.getSavePlayer().clear();
//			main.setState(DState.WAITING);
//			main.getStart().cancel();
//			main.getTp().cancel();
			
			
		}
		return true;
		
	}
	
	public boolean doReset(Player player) {
		
		if(main.IsState(DState.PLAYING)) player.sendMessage("§9§l[DeathSwap]§r §cLa partie doit être arrétée avant de reset la map");
		
		else {
			main.NouvelleMap();
		}
		return true;
		
		
	}
	
	public boolean doFakeStart(Player player) {
		
		player.sendMessage("§9§l[DeathSwap] §r§cIl faut être au moins deux pour lancer la partie");
		return true;
		
	}
	
	public boolean doStart(Player player) {
		
		if(main.IsState(DState.WAITING)){
			
			// -> /DeathSwap start
			main.getDsScoreboard().createScoreboard();
			
			Bukkit.getScheduler().runTaskTimer(main, () -> main.getDsScoreboard().updateScoreboardTime(), 20, 20);
			
			UUID u = null;
			int totalPlayer = Bukkit.getOnlinePlayers().size();
			if (totalPlayer > 5) totalPlayer = 5;
							
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				
				u = p.getUniqueId();
				if(!main.getUUID().contains(u)) {
					
					main.getUUID().add(u);						
					
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					for (PotionEffect effect : p.getActivePotionEffects()) p.removePotionEffect(effect.getType());
					p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));
					p.setGameMode(GameMode.SURVIVAL);
					
					
					p.setHealth(p.getHealth());
					
					if (main.getInvManagement().getStateKit()) {
						
						//giveKit(p,totalPlayer);
						giveItem(p);
					}
				
					
					if (!main.getInvManagement().getStateUHC()) {
						
						p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1*totalPlayer));
						
					}
					
				}
				
			}
			

			Bukkit.broadcastMessage("§9§l[DeathSwap] §r §6Lancement de la partie et génération des spawns dans le monde de jeu, les joueurs qui rejoignent seront en spectateur");
			main.getDsScoreboard().updateScoreboardPlayer();
			
			//p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			main.setState(DState.PLAYING);		
			

			World world = Bukkit.getWorld("Jeu");
			world.setTime(0);
			
			
			main.getTp().runTaskTimer(main, 5, 5); 
		
			
			return true;					
		}
		else player.sendMessage("§9§l[DeathSwap]§r §cIl y a deja une partie en cours");
		
		return true;
	}
	

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		
		
		if(sender instanceof Player && label.equalsIgnoreCase("deathswap")) {
			
			Player player = (Player) sender;
			
			if(arg.length != 1 || (arg.length == 1 && arg[0].equalsIgnoreCase("option"))) {
				
				main.getInvManagement().openDeathSwapManagment(player);
				
				
				return true;
			}
			
			if (Bukkit.getOnlinePlayers().size() == 1 && arg[0].equalsIgnoreCase("start")) {
				
				return doFakeStart(player);
			}
			else if(arg[0].equalsIgnoreCase("dim")) {
				player.sendMessage("dim"+player.getLocation().getWorld().getName());
			}
			else if(arg.length == 1 && arg[0].equalsIgnoreCase("reset")){
				
				return doReset(player);
				
			}
			
			else if(arg.length == 1 && arg[0].equalsIgnoreCase("start") && Bukkit.getOnlinePlayers().size() >1) {
				
				return doStart(player);
			
			}
			else if(arg.length == 1 && arg[0].equalsIgnoreCase("stop") ) {
				
				return doStop(player);
			}
			else {
				// -> /DeathSwap
				player.sendMessage("§9§l[DeathSwap]§r §e/DeathSwap§r pour ouvrir le menu");
				player.sendMessage("§9§l[DeathSwap]§r §e/DeathSwap §astart §r pour lancer la partie");
				player.sendMessage("§9§l[DeathSwap]§r §e/DeathSwap §astop §rpour arreter la partie");
				player.sendMessage("§9§l[DeathSwap]§r §e/DeathSwap §areset §rpour recharger la partie et la map de jeu");
				return true;
			}
		
		}
		return false;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = Lists.newArrayList();
        
        if(args.length > 1) {
            return list;
        }
        
       list.add("stop");
       list.add("start");
       list.add("reset");
       list.add("option");
        
       //return list;
       return StringUtil.copyPartialMatches(args[args.length - 1], list, Lists.newArrayList());
    }
	
	
	public void giveItem(Player player) {
		
		addPlayerItem(player, Material.REDSTONE_BLOCK, 10);
		addPlayerItem(player, Material.STRING, 10);
		addPlayerItem(player, Material.IRON_INGOT, 20);
		addPlayerItem(player, Material.WATER_BUCKET, 1);
		addPlayerItem(player, Material.COBWEB, 5);
		addPlayerItem(player, Material.IRON_PICKAXE, 1);
		addPlayerItem(player, Material.STICKY_PISTON, 10);
		
	}
	
	public void addPlayerItem(Player player, Material material, int number) {
	
		player.getInventory().addItem(new ItemStack(material,number));
	}
	
	public void giveKit(Player p,int totalPlayer) {
		
		Random r = new Random();
		
		
		
		
		int alea = r.nextInt(8);
		
		if (alea == 0) {
			p.sendMessage("§6§m                                                     ");
			p.sendMessage("§6 Vous avez le kit : §2Dresseur de monstre !");
			p.sendMessage("§3   Parce que le PVE c'est dangereux !");
			p.sendMessage("§6§m                                                     ");
		
			
			
			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 12));
			p.getInventory().addItem(new ItemStack(Material.STRING, 64));
			p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 10));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
			
			
				
			p.getInventory().addItem(new ItemStack(Material.SKELETON_SPAWN_EGG, 2*totalPlayer));
			p.getInventory().addItem(new ItemStack(Material.WITCH_SPAWN_EGG, 2*totalPlayer));
			p.getInventory().addItem(new ItemStack(Material.GUARDIAN_SPAWN_EGG, 1*totalPlayer));
			p.getInventory().addItem(new ItemStack(Material.SPIDER_SPAWN_EGG, 1*totalPlayer));
				
		}
				
		else if (alea == 1) {	
			p.sendMessage("§6§m                                                ");
			p.sendMessage("§6 Vous avez le kit : §2Pro des enclumes !");
			p.sendMessage("§3   Parce qu'une enclume fait mal");
			p.sendMessage("§6§m                                                ");
				
			p.getInventory().addItem(new ItemStack(Material.ANVIL, 6));
			p.getInventory().addItem(new ItemStack(Material.SLIME_BALL, 15));
			
			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 32));
			p.getInventory().addItem(new ItemStack(Material.STRING, 32));
			p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 10));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		}
			
		else if (alea == 2) {
			
			p.sendMessage("§6§m                                                 ");
			p.sendMessage("§6 Vous avez le kit : §2Machines volantes !");
			p.sendMessage("§3   Faîtes envoler vos adversaires !");
			p.sendMessage("§6§m                                                 ");
			
			p.getInventory().addItem(new ItemStack(Material.SLIME_BLOCK, 64));
			
			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 32));
			p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 20));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
			p.getInventory().addItem(new ItemStack(Material.OBSERVER,5));
		}
				
		else if (alea == 3) {
			p.sendMessage("§6§m                                           ");
			p.sendMessage("§6 Vous avez le kit : §2Maître nageur");
			p.sendMessage("§3   Glou Glou !");
			p.sendMessage("§6§m                                           ");
			
									
			for (int i = 0; i<8 ; i++)p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
			
			ItemStack IS = new ItemStack(Material.IRON_HELMET);
			ItemMeta IM = IS.getItemMeta();
			IM.addEnchant(Enchantment.WATER_WORKER, 10, true);
			IM.addEnchant(Enchantment.OXYGEN, 10, true);
			IM.setDisplayName("Casque de plongée");
			IS.setItemMeta(IM);
			p.getInventory().setHelmet(IS);
			
			
			
			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 32));
			p.getInventory().addItem(new ItemStack(Material.STRING, 32));
			
		
		}	
										
			
		else if (alea == 4) {	
			p.sendMessage("§6§m                                        ");
			p.sendMessage("§6 Vous avez le kit :§2 Le tisseur");
			p.sendMessage("§3   Tissez vos toiles");
			p.sendMessage("§6§m                                        ");
			p.getInventory().addItem(new ItemStack(Material.COBWEB, 10*totalPlayer));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
				
		}
		else if (alea == 5) {
			p.sendMessage("§6§m                                      ");
			p.sendMessage("§6 Vous avez le kit : §2Sorcière");
			p.sendMessage("§3   Jettez des potion partout");
			p.sendMessage("§6§m                                      ");
				
			ItemStack Popo=new ItemStack(Material.SPLASH_POTION, 3);
			PotionMeta Pmeta = (PotionMeta) Popo.getItemMeta();
			PotionMeta Pmeta2 = (PotionMeta) Popo.getItemMeta();
											
			Pmeta.setDisplayName("Potion de Wither");
			Pmeta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 45, 20*10), false);
			Pmeta.setColor(Color.BLACK);
			Popo.setItemMeta(Pmeta);
			p.getInventory().addItem(Popo);
			
			
			Pmeta2.setDisplayName("Potion de dégat");
			Pmeta2.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 0, 0), false);
			Pmeta2.setColor(Color.PURPLE);
			Popo.setItemMeta(Pmeta2);
			p.getInventory().addItem(Popo);
			

			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 32));
			p.getInventory().addItem(new ItemStack(Material.STRING, 32));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		}
		else if (alea == 6) {
			p.sendMessage("§6§m                                      ");
			p.sendMessage("§6 Vous avez le kit : §2Pchhhhit");
			p.sendMessage("§3   Kaboum");
			p.sendMessage("§6§m                                      ");
			
									
			p.getInventory().addItem(new ItemStack(Material.TNT,3*totalPlayer));			
			
			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 32));
			p.getInventory().addItem(new ItemStack(Material.STRING, 32));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		}
		else if (alea == 7) {
			p.sendMessage("§6§m                                    ");
			p.sendMessage("§6 Vous avez le kit : §2Nether");
			p.sendMessage("§3   Overworld is so 2011");
			p.sendMessage("§6§m                                    ");
			
									
			p.getInventory().addItem(new ItemStack(Material.OBSIDIAN,12*totalPlayer));
			p.getInventory().addItem(new ItemStack(Material.BUCKET,3));
			p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
											
			
			
			p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, 32));
			p.getInventory().addItem(new ItemStack(Material.STRING, 32));
		}
	}
}
