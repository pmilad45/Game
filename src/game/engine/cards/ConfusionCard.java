package game.engine.cards;

public class ConfusionCard extends Card{
	private int duration;
	private static final boolean lucky = false;
	
	ConfusionCard(String name, String description, int rarity, int duration){
		super(name,description,rarity,lucky);
		this.duration = duration;
		
		
	}
	

}
