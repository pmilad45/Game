package game.engine.monsters;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
=======
>>>>>>> Stashed changes
import game.engine.Board;
import game.engine.Constants;
>>>>>>> Stashed changes
import game.engine.Role;
import java.util.ArrayList;

public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy){
		super(name, description, role, energy);
	}

<<<<<<< Updated upstream
=======
	private int stealEnergyFrom(Monster target) {
		int energy = target.getEnergy();
		int take = Math.min(energy, Constants.SCHEMER_STEAL);
		if (take == 0 && energy == 0) {
			if (target instanceof Schemer) {
				target.setEnergy(10);
			}
			return 0;
		}
		if (take <= 0) {
			return 0;
		}
		target.alterEnergy(-take);
		return take;
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		ArrayList<Monster> sm = Board.getStationedMonsters();
		if (sm == null) {
			return;
		}
		int rawTotal = 0;
		rawTotal += stealEnergyFrom(opponentMonster);
		for (Monster m : sm) {
			if (m != this) {
				rawTotal += stealEnergyFrom(m);
			}
		}
		if (rawTotal > 0) {
			alterEnergy(rawTotal);
		}
	}

	@Override
	int applyTypeEnergyDelta(int delta) {
		return delta + Constants.SCHEMER_STEAL;
	}
>>>>>>> Stashed changes
}
