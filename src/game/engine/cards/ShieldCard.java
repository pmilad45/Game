package game.engine.cards;
<<<<<<< Updated upstream

public class ShieldCard extends Card{
	private static final boolean lucky = true;
	public ShieldCard(String name, String description, int rarity){
		 super(name,description,rarity,lucky);
	 }

=======
import game.engine.monsters.*;
public class ShieldCard extends Card {
	
	public ShieldCard(String name, String description, int rarity) {
		super(name, description, rarity, true); 
	}
>>>>>>> Stashed changes

	@Override
	public void performAction(Monster player, Monster target){
		if(target.isShielded()){
			target.setShielded(false);
		}
		player.setShielded(true);
	}
}
