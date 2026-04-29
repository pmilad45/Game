package game.engine.cards;
import game.engine.Role;
import game.engine.monsters.*;

public class ConfusionCard extends Card{
	private int duration;
	private static final boolean lucky = false;
	
	public ConfusionCard(String name, String description, int rarity, int duration){
		super(name,description,rarity,lucky);
		this.duration = duration;
		
		
	}
	

	public int getDuration(){
		return duration;
	}

<<<<<<< Updated upstream
	public void setDuration(int duration){
		this.duration = duration;
	}
=======
	@Override
	public void performAction(Monster player, Monster target){
		Role p = player.getRole();
		player.setRole(target.getRole());
		target.setRole(p);

		player.setConfusionTurns(duration);
		target.setConfusionTurns(duration);
	}
	

>>>>>>> Stashed changes
}
