package game.engine.cards;
<<<<<<< Updated upstream

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
=======
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;

	}
	
	public int getEnergy() {
		return energy;
	}

	@Override
	public void performAction(Monster player, Monster target){
		if(target.isShielded()){
			target.setShielded(false);
			return;
		}
		int steal = Math.min(target.getEnergy(), energy);
		if(steal <= 0){
			return;
		}
		modifyCanisterEnergy(target, -steal);
		modifyCanisterEnergy(player, steal);
	}

	@Override
	public void modifyCanisterEnergy(Monster monster , int canisterValue){
		monster.alterEnergy (canisterValue);
	}
>>>>>>> Stashed changes
}



