package game.engine.cards;
import game.engine.monsters.*;

public class ShieldCard extends Card {
	
	public ShieldCard(String name, String description, int rarity) {
		super(name, description, rarity, true); 
	}

	@Override
	public void performAction(Monster player, Monster target){
		if(target.isShielded()){
			target.setShielded(false);
		}
		player.setShielded(true);
	}
}
