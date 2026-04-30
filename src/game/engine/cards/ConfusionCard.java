package game.engine.cards;
import game.engine.Role;
import game.engine.monsters.*;

public class ConfusionCard extends Card {
	private int duration;
	
	public ConfusionCard(String name, String description, int rarity, int duration) {
		super(name, description, rarity, false);
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration){
		this.duration = duration;
	}

	@Override
	public void performAction(Monster player, Monster target){
		Role p = player.getRole();
		player.setRole(target.getRole());
		target.setRole(p);

		player.setConfusionTurns(duration);
		target.setConfusionTurns(duration);  
	}
  
}
