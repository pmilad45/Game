package game.engine.cards;

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

	public void setDuration(int duration){
		this.duration = duration;
	}
}
