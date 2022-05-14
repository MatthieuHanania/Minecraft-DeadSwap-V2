package fr.Reycun.DeathSwap;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManagement {
	
	private Main main;
	private Inventory inventaire;
	private boolean stateKit = true;
	private boolean stateRegen = Bukkit.getWorlds().get(0).getGameRuleValue(GameRule.NATURAL_REGENERATION);
	private Difficulty stateDifficulty = Bukkit.getWorld("Jeu").getDifficulty();
	private boolean alertTime = false;
	private int firstTempsMini = 90;
	private int firstTempsMoy = 31;
	private int OtherTempsMini = 90;
	private int OtherTempsMoy = 31;
	
	
	
	public Inventory getInventaire() {
		return inventaire;
	}
	public boolean getStateUHC() {
		return stateRegen;
	}
	public boolean getStateKit() {
		return stateKit;
	}
	public boolean getAlertTime() {
		return alertTime;
	}
	public int getFirstTempsMini() {
		return firstTempsMini;
	}
	public int getFirstTempsMoy() {
		return firstTempsMoy;
	}
	public int getOtherTempsMoy() {
		return OtherTempsMoy;
	}
	public int getOtherTempsMini() {
		return OtherTempsMini;
	}

	
		
	public InventoryManagement(Main main) {
		
		this.main = main;
		
		this.inventaire = Bukkit.createInventory(null, 45, "Menu du DeathSwap");
		
		ItemStack commencer = new ItemStack(Material.LIME_WOOL, 1);
		ItemMeta commencerMeta = commencer.getItemMeta();
		commencerMeta.setDisplayName("§aCommencer la partie");
		commencerMeta.setLore(Arrays.asList("§7Appuyer pour commencer la partie"));
		commencer.setItemMeta(commencerMeta);
		inventaire.setItem(1, commencer);
		
		
		ItemStack arreter = new ItemStack(Material.RED_WOOL, 1);
		ItemMeta arreterMeta = arreter.getItemMeta();
		arreterMeta.setDisplayName("§cArreter la partie");
		arreterMeta.setLore(Arrays.asList("§7Appuyer pour arreter la partie"));
		arreter.setItemMeta(arreterMeta);
		inventaire.setItem(10, arreter);
		
		
		ItemStack resetMap = new ItemStack(Material.FILLED_MAP, 1);
		ItemMeta resetMapMeta = resetMap.getItemMeta();
		resetMapMeta.setDisplayName("§3Reset");
		resetMapMeta.setLore(Arrays.asList("§7Retour au spawn","§7Création de nouvelles maps"));
		resetMap.setItemMeta(resetMapMeta);
		inventaire.setItem(3, resetMap);
		
		ItemStack BannerMoins30 = new ItemStack(Material.ORANGE_BANNER, 30);
		BannerMeta BannerMoinsMeta = (BannerMeta) BannerMoins30.getItemMeta();	
		BannerMoinsMeta.setDisplayName("§3Temps minimal: §6+30/-30");
		BannerMoinsMeta.setLore(Arrays.asList("§7Clique droit: §f+30s","§7Clique gauche: §f-30s"));
		BannerMoins30.setItemMeta(BannerMoinsMeta);
		inventaire.setItem(29, BannerMoins30);
		inventaire.setItem(38, BannerMoins30);
		
		ItemStack BannerMoins5 = new ItemStack(Material.ORANGE_BANNER, 5);
		BannerMeta BannerMoins5Meta = (BannerMeta) BannerMoins5.getItemMeta();	
		BannerMoins5Meta.setDisplayName("§3Temps minimal: §6+5/-5");
		BannerMoins5Meta.setLore(Arrays.asList("§7Clique droit: §f+5s","§7Clique gauche: §f-5s"));
		BannerMoins5.setItemMeta(BannerMoins5Meta);
		inventaire.setItem(30, BannerMoins5);
		inventaire.setItem(39, BannerMoins5);
		
		ItemStack BannerAlea30 = new ItemStack(Material.RED_BANNER, 30);
		BannerMeta BannerAlea30Meta = (BannerMeta) BannerAlea30 .getItemMeta();	
		BannerAlea30Meta.setDisplayName("§3Temps aléatoire: §c+30/-30");
		BannerAlea30Meta.setLore(Arrays.asList("§7Clique droit: §f+30s","§7Clique gauche: §f-30s"));
		BannerAlea30.setItemMeta(BannerAlea30Meta);
		inventaire.setItem(33, BannerAlea30);
		inventaire.setItem(42, BannerAlea30);
		
		ItemStack BannerAlea5 = new ItemStack(Material.RED_BANNER, 5);
		BannerMeta BannerAlea5Meta = (BannerMeta) BannerAlea5 .getItemMeta();	
		BannerAlea5Meta.setDisplayName("§3Temps aléatoire: §c+5/-5");
		BannerAlea5Meta.setLore(Arrays.asList("§7Clique droit: §f+5s","§7Clique gauche: §f-5s"));
		BannerAlea5.setItemMeta(BannerAlea5Meta);
		inventaire.setItem(32, BannerAlea5);
		inventaire.setItem(41, BannerAlea5);
		
		
		
		int[] place = {18,19,20,21,22,23,24,25,26};
		
		for(int index : place) {
			
			SetVitreVide(index);
		}
		
	}
	
	public void openDeathSwapManagment(Player player) {
		player.openInventory(inventaire);
		
		updateKit();
		updateUhc();
		updateDifficulty();
		updateTimeInfo();
		updateFirstTime();
		updateOtherTimer();
	}

	public void switchKit() {
		
		if (main.IsState(DState.WAITING)) {
			
			this.stateKit= !this.stateKit;
			if(stateKit) Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Le starter pack est §aactivé ! ");
			else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Le starter pack est §cdésactivé ! ");
			
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de changer le starter pack, la partie a déjà commencé");
	}
	
	public void updateKit() {
		
		
		ItemStack kit = new ItemStack(Material.CHEST, 1);
		ItemMeta kitMeta = kit.getItemMeta();
		
		if (stateKit == true) {
			kitMeta.setDisplayName("§3Starter pack : §aactivés");
			kitMeta.setLore(Arrays.asList("§7Appuyer pour le §cdésactiver"));
		}
		else {
			
			kitMeta.setDisplayName("§3Starter pack : §cdésactivé");
			kitMeta.setLore(Arrays.asList("§7Appuyer pour l'§aactiver "));

		}		
		kit.setItemMeta(kitMeta);
		inventaire.setItem(15, kit);
		
		
	}
	
	public void autoRegen(boolean value) {
		
	
		for (World world : Bukkit.getWorlds()) {
			
			world.setGameRule(GameRule.NATURAL_REGENERATION, value);
		}
	}
	
	public void SwitchUhc() {
		
		if (main.IsState(DState.WAITING)) {
			
			this.stateRegen = !this.stateRegen;
			
			if (stateRegen) {
				Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Mode UHC §cdésactivé !");
				autoRegen(true);
			}
			else {
				Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Mode UHC §aactivé !");
				autoRegen(false);
			}
			
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de changer le mode UHC, la partie a déjà commencé");
		
		
	}
	public void updateUhc() {
								
		ItemStack uhcMode = new ItemStack(Material.GOLDEN_APPLE, 1);
		ItemMeta uhcModeMeta = uhcMode.getItemMeta();
		
		if (stateRegen == false) {
			
			uhcModeMeta.setDisplayName("§3Mode UHC : §aactivé");
			uhcModeMeta.setLore(Arrays.asList("§7Appuyer pour §cdésactiver","§7le mode sans régen"));
		}
		else {
			
			uhcModeMeta.setDisplayName("§3Mode UHC : §cdésactivé");
			uhcModeMeta.setLore(Arrays.asList("§7Appuyer pour §aactiver","§7le mode sans régen"));
		}
		
		uhcMode.setItemMeta(uhcModeMeta);
		inventaire.setItem(6, uhcMode);
		
	}
	
	public void changeDifficulty(Difficulty diff) {
		
		for (World world : Bukkit.getWorlds()) {
			
			world.setDifficulty(diff);
		}
		
	}
	
	public void switchDifficulty(){
		
			
		if (stateDifficulty == Difficulty.EASY) {
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Difficilté§e normale !");
			
			changeDifficulty(Difficulty.NORMAL);
			
		}
		else if (stateDifficulty == Difficulty.NORMAL) {
			
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Difficilté§c difficile !");
			changeDifficulty(Difficulty.HARD);
		}
		else if (stateDifficulty == Difficulty.HARD){
			
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Difficilté§b peaceful !");
			changeDifficulty(Difficulty.PEACEFUL);
		}
		else if (stateDifficulty == Difficulty.PEACEFUL){
			Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7Difficilté§a facile !");
			changeDifficulty(Difficulty.EASY);
		}
			
				
	}
	
	public void updateDifficulty() {
			
		ItemStack difficulty = new ItemStack(Material.WITHER_ROSE, 1);
		ItemMeta difficultyMeta = difficulty.getItemMeta(); 
		stateDifficulty = Bukkit.getWorlds().get(0).getDifficulty();
		
		
					
		if (stateDifficulty == Difficulty.EASY) {
			
			difficultyMeta.setDisplayName("§3Difficulté : §afacile");
			
		}
		else if (stateDifficulty == Difficulty.NORMAL) {
			
			difficultyMeta.setDisplayName("§3Difficulté : §enormale");
		}
		else if (stateDifficulty == Difficulty.HARD){
			difficultyMeta.setDisplayName("§3Difficulté : §cdifficile");
		}
		else if (stateDifficulty == Difficulty.PEACEFUL){
			difficultyMeta.setDisplayName("§3Difficulté : §bpeaceful");
		}
		difficultyMeta.setLore(Arrays.asList("§7Appuyer pour changer la difficulté !"));
		difficulty.setItemMeta(difficultyMeta);
		inventaire.setItem(14, difficulty);
		
			
	}
	
	public void switchTimeInfo() {
		
		if (main.IsState(DState.WAITING)) {
					
			this.alertTime = !this.alertTime;
			if(alertTime) Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7L'alerte de temps est §aactivée ! ");
			else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §7L'alerte de temps est §cdésactivée ! ");
			
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de changer l'alerte de temps, la partie a déjà commencé");
		
	}
	public void updateTimeInfo() {
		
		ItemStack timeInfo = new ItemStack(Material.FIREWORK_ROCKET, 1);
		ItemMeta timeInfoMeta = timeInfo.getItemMeta();
		
		if(alertTime) {
			timeInfoMeta.setDisplayName("§3Alertes avant les TP : §aactivées");
			timeInfoMeta.setLore(Arrays.asList("§7Appuyer pour §cdésactiver","§7Les alertes de 20, 10, 5 secondes avant les TP"));
		}
		else {
			timeInfoMeta.setDisplayName("§3Alertes avant les TP : §cdésactivées");
			timeInfoMeta.setLore(Arrays.asList("§7Appuyer pour §aactiver","§7Les alertes de 20, 10, 5 secondes avant les TP"));
		}
		
		timeInfo.setItemMeta(timeInfoMeta);
		inventaire.setItem(5, timeInfo);
		
	}
	
	public void ajoutFirstMin(int nombre) {
		
		if (main.IsState(DState.WAITING)) {
			
			if (this.firstTempsMini + nombre >= 1) {
				this.firstTempsMini = this.firstTempsMini + nombre;
			}
			else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cErreur temps nul");
			
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de modifier les paramètres de temps, la partie a déjà commencé");
		
		
	}
	public void ajoutFirstMax(int nombre) {
		
		if (main.IsState(DState.WAITING)) {
			
			if(this.firstTempsMoy + nombre >= 1) {
				this.firstTempsMoy = this.firstTempsMoy + nombre;
			}
			else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cErreur temps négatif");
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de modifier les paramètres de temps, la partie a déjà commencé");
		
		
	}
	
	public void updateFirstTime() {
		
		ItemStack firstTime = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta firstTimeMeta = firstTime.getItemMeta();
		firstTimeMeta.setDisplayName("§3Premier TP entre: §6"+betterTime(firstTempsMini)+"s + §c[0;"+betterTime(firstTempsMoy-1)+"s]" );
		//firstTimeMeta.setLore(Arrays.asList("§2Clique gauche: §7Temps minimal §f+1s ","§aClique droit: §7Temps minimal §f-1s ","§5Shift clique gauche: §7Temps maximal §f+1s","§dShift clique droit: §7Temps maximal §f-1s"));
		firstTime.setItemMeta(firstTimeMeta);
		inventaire.setItem(31, firstTime);
		
	}
	
	public void ajoutOtherMin(int nombre) {
		
		if (main.IsState(DState.WAITING)){
			
			if (this.OtherTempsMini + nombre >= 1) {
				this.OtherTempsMini = this.OtherTempsMini + nombre;
			}
			else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cErreur temps nul");
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de modifier les paramètres de temps, la partie a déjà commencé");
		
		
	}
	public void ajoutOtherMax(int nombre) {
		
		if (main.IsState(DState.WAITING)) {
			
			if(this.OtherTempsMoy + nombre >= 1) {
				this.OtherTempsMoy = this.OtherTempsMoy + nombre;
			}
			else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cErreur temps négatif");
			
		}
		else Bukkit.broadcastMessage("§9§l[DeathSwap]§r §cImpossible de modifier les paramètres de temps, la partie a déjà commencé");
		
	}
	
	public void updateOtherTimer() {
		
		ItemStack OtherTime = new ItemStack(Material.ENDER_EYE, 1);
		ItemMeta OtherTimeMeta = OtherTime.getItemMeta();
		OtherTimeMeta.setDisplayName("§3Autres TP entre: §6"+betterTime(OtherTempsMini)+"s + §c[0;"+betterTime(OtherTempsMoy-1)+"s]" );
		//firstTimeMeta.setLore(Arrays.asList("§2Clique gauche: §7Temps minimal §f+1s ","§aClique droit: §7Temps minimal §f-1s ","§5Shift clique gauche: §7Temps maximal §f+1s","§dShift clique droit: §7Temps maximal §f-1s"));
		OtherTime.setItemMeta(OtherTimeMeta);
		inventaire.setItem(40, OtherTime);
		
	}
	
	
	
	public void SetVitreVide(int place) {
		
		ItemStack vitreVide = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta vitreVideMeta = vitreVide.getItemMeta();
		vitreVideMeta.setDisplayName(" ");
		vitreVide.setItemMeta(vitreVideMeta);
		
		inventaire.setItem(place, vitreVide);
		
	}
	
	public String addZero(int nombre) {
		return ((nombre < 10) ? "0": "") + nombre;
	} 
	
	public String betterTime(int second) {
		int heure = second / 3600;
        int minute = second % 3600 / 60;
        int seconde = second % 60;
        
        if (heure < 1 && minute < 1) {
            return addZero(seconde); 
        } else if (heure < 1) {
            return minute + "min" + addZero(seconde);
        } else {
            return heure + "min" + minute;
        }
	}
	
	

}
