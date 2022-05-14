package fr.Reycun.DeathSwap;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class DSScoreboard {
	
	private static String OBJECTIVE_NAME = "DeathSwap";

	private String currentLineTime;
	private String currentLinePlayer;
	private int second;
	
	public DSScoreboard () {
		this.currentLineTime = ChatColor.GOLD+"Temps: §r00:00";
		this.currentLinePlayer = ChatColor.GOLD+"Joueur restants: "+ChatColor.WHITE+"";
		this.second = 0;
	}
	
	public void createScoreboard() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		
		Objective objectiveSidebar = scoreboard.getObjective(OBJECTIVE_NAME);
		
		if(objectiveSidebar != null)
			objectiveSidebar.unregister();
		
		objectiveSidebar = scoreboard.registerNewObjective(OBJECTIVE_NAME,"dummy", ChatColor.YELLOW+ "DeathSwap");
			
		objectiveSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Score tiret = objectiveSidebar.getScore(ChatColor.DARK_GRAY+"§8§m                           §r");
		tiret.setScore(15);
		
		Score temps = objectiveSidebar.getScore(currentLineTime);
		temps.setScore(14);
		
		Score nul = objectiveSidebar.getScore("");
		nul.setScore(13);
		
		Score JoueurRest = objectiveSidebar.getScore(currentLinePlayer);
		JoueurRest.setScore(12);
		
		
		Objective objectiveList = scoreboard.registerNewObjective("truc", "health", "Vie des joueurs");
		objectiveList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		objectiveList.setRenderType(RenderType.HEARTS); 
	}
	
	public void deleteScoreboard() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective obj2 = scoreboard.getObjective("truc");
		Objective obj = scoreboard.getObjective(OBJECTIVE_NAME);
		
		if (obj2 != null) obj2.unregister();
		if (obj != null) obj.unregister();
		
	}
	
	
	public void updateScoreboardTime() {
		final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		final Objective obj = scoreboard.getObjective(OBJECTIVE_NAME);
		
		scoreboard.resetScores(currentLineTime); 
		
		this.second++;
		
        this.currentLineTime = ChatColor.GOLD+ "Temps: §r"+ getTime();
        
        obj.getScore(this.currentLineTime).setScore(14);
	}
	
	
	public void updateScoreboardPlayer() {
		final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		final Objective obj = scoreboard.getObjective(OBJECTIVE_NAME);
		
		scoreboard.resetScores(currentLinePlayer); 

        this.currentLinePlayer = ChatColor.GOLD+"Joueur restants: "+ChatColor.WHITE+ Main.getInstanceMain().getUUID().size();
        
        obj.getScore(this.currentLinePlayer).setScore(12);
	}
	
	public String addZero(int nombre) {
		return ((nombre < 10) ? "0": "") + nombre;
	} 
	
	public String getTime() {
		int heure = this.second / 3600;
        int minute = this.second % 3600 / 60;
        int seconde = this.second % 60;
        
        if (heure < 1 && minute < 1) {
            return "00:"+addZero(seconde); 
        } else if (heure < 1) {
            return addZero(minute) + ":" + addZero(seconde);
        } else {
            return addZero(heure) + ":" + addZero(minute);
        }
	}
}
