package game.engine.cards;

public class EnergyStealCard extends Card {
 private int energy;
 private static final boolean lucky= true;
 
	public EnergyStealCard(String name, String description, int rarity, int energy){
	 super (name,description,rarity,lucky);
	 this.energy = energy;
	 
	 
	  
	 
 }

public int getEnergy() {
	return energy;
}
}
