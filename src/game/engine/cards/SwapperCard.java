package game.engine.cards;
import game.engine.monsters.*;

public class SwapperCard extends Card {
	private static final boolean lucky = true;

	public SwapperCard(String name, String description, int rarity) {
		super(name, description, rarity, true);
	}

	@Override
	public void performAction(Monster player, Monster target){
		int t = player.getPosition();
		if(t < target.getPosition()){
			int temp = target.getPosition();
			player.setPosition(temp);
			target.setPosition(t);
			
		}
	}
	
}
