package fr.Reycun.DeathSwap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin{

	
	private DState state;
	private List<UUID> Players = new ArrayList<>();
	private HashMap<UUID, String> savePlayer = new HashMap<UUID, String>();
	private HashMap<UUID, Integer> KillCount = new HashMap<UUID, Integer>();
	World world;
	
	
	
	private Commands commands = new Commands(this);
	
	private DSTimeTask start;
	private DSInitialTP tp= new DSInitialTP(this);
	private DSScoreboard scorboard = new DSScoreboard();
	private LoadReymatic reymatic = new LoadReymatic(this);
	private InventoryManagement InvManagement;
	
	private static Main main;

	public static Main getInstanceMain() {
		return main;
	}
	
	@Override
	public void onEnable() {
		main = this;
		 
	    CreerMap();
	    InvManagement = new InventoryManagement(this);
	    start = new DSTimeTask(this);
	    InvManagement.changeDifficulty(Difficulty.NORMAL);
	    
	    Bukkit.getWorlds().get(0).setTime(0);
	    Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
	    Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
	        
		System.out.println("§2Monde de jeu créé , lancement du Plugin DeathSwap by Reycun (basé sur le concept de SethBling)");
		
		setState(DState.WAITING);
		reymatic.loadAt(new Location(Bukkit.getWorlds().get(0), -23, 152, -19));
		
		Bukkit.getWorlds().get(0).setSpawnLocation(new Location(Bukkit.getWorlds().get(0), 0, 153, 0));
		
		scorboard.deleteScoreboard();
		getCommand("deathswap").setExecutor(commands);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new DSEvent(this), this);
		pm.registerEvents(new EventHub(this), this);
				
	}

	
	@Override
	public void onDisable() {
		System.out.println("Au revoir");     
		scorboard.deleteScoreboard();
		
	}
	
	public void NouvelleMap() {
		
			
		World worldtodelete = Bukkit.getWorld("Jeu");
		World worldtodeleteNether = Bukkit.getWorld("Jeu_Nether");
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.teleport(new Location(Bukkit.getWorlds().get(0), 0, 155, 0));
			player.setGameMode(GameMode.SURVIVAL);
			
		}
		Bukkit.getScheduler().runTaskLater(this, () -> {
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §aCréation de la nouvelle map de jeu et du Nether en cours...");
			File fichierJeu = worldtodelete.getWorldFolder();
			File fichierNether = worldtodeleteNether.getWorldFolder();
			
			
			Bukkit.getServer().unloadWorld((worldtodelete), false);
			Bukkit.getServer().unloadWorld((worldtodeleteNether), false);
			supprimer(fichierJeu); 
			supprimer(fichierNether);
			
			Bukkit.getScheduler().runTaskLater(this, () -> {
				
				CreerMap();
				
			},40);
			
		},20);
		
	}
	
	public void CreerMap() {
		
		WorldCreator worldJeu = new WorldCreator("Jeu");
		worldJeu.type(WorldType.NORMAL);
		worldJeu.generateStructures(true);
		world = worldJeu.createWorld();
		
		WorldCreator worldNether= new WorldCreator("Jeu_Nether");
	    worldNether.type(WorldType.NORMAL);
	    worldNether.environment(Environment.NETHER);
	    worldNether.generateStructures(true);
	    world = worldNether.createWorld();
	    
	    
	    Bukkit.broadcastMessage("§9§l[DeathSwap]§r §aNouvelles maps créées !");
		
	}
	
	 public static boolean supprimer(File fichier) {
         if( fichier.exists() ) {
             File files[] = fichier.listFiles();
             for(int i=0; i<files.length; i++) {
                 if(files[i].isDirectory()) {
             supprimer(files[i]);
             }
                 else {
                     files[i].delete();
                 } //end else
             }
             }
         return( fichier.delete() );
	 }
	 

	public Commands getCommands() {
		return commands;
	}
	
	public DSTimeTask getStart() {
		return start;
	}

	public DSInitialTP getTp() {
		return tp;
	}

	public DSScoreboard getDsScoreboard() {
		return scorboard;
	}
	
	public void setState(DState state) {
		
		this.state = state;
	}
	
	public boolean IsState(DState state) {
		
		return this.state == state;
	}
	
	public List<UUID> getUUID(){
		
		return Players;
	}
	
	
	public HashMap<UUID, String> getSavePlayer(){
		
		return savePlayer;
	}
	
	public InventoryManagement getInvManagement() {
		
		return InvManagement;
	}
	
	public HashMap<UUID, Integer> getKillCount(){
		
		return KillCount;
	}
	
	public void CheckWin() {
		
		if(getUUID().size() == 0) {
			
			Bukkit.broadcastMessage("§9[DeathSwap] §r §6Pas de gagnant ! bande de tardos");
			setState(DState.WAITING);
			getUUID().clear();
			savePlayer.clear();
			start.cancel();
			tp.cancel();
			scorboard.deleteScoreboard();
			Bukkit.reload();
		}
	
		
		if(getUUID().size() == 1) {
			
			UUID u = getUUID().get(0);
			Player p = Bukkit.getPlayer(u);
			String pseudo = getSavePlayer().get(u);
			
			
			if (!(p == null)) Bukkit.broadcastMessage("§9§l[DeathSwap] §r §6Bravo ! §e"+ p.getName()+ " §6a gagné la partie ! Les autres sont nuls !");
			else if (pseudo == null) Bukkit.broadcastMessage("§9§l[DeathSwap] §r §6Fin de la partie");
			else Bukkit.broadcastMessage("§9§l[DeathSwap] §r §6Bravo ! §e"+ pseudo+ " §6a gagné la partie ! Les autres sont nuls !");
			
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §e/DeathSwap §areset §rpour recharger la partie et la map de jeu");
			
			getUUID().clear();
			setState(DState.WAITING);
			start.cancel();
			//getKick().clear();
			savePlayer.clear();
			tp.cancel();
			scorboard.deleteScoreboard();
			Bukkit.reload();
		}
		
		
		
	}



}
