package game.engine.cards;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream

public class ShieldCard extends Card{
	private static final boolean lucky = true;
	public ShieldCard(String name, String description, int rarity){
		 super(name,description,rarity,lucky);
	 }

=======
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
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
