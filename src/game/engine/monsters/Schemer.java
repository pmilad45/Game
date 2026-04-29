package game.engine.monsters;

import java.util.ArrayList;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Role;

public class Schemer extends Monster {

	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}

	private int stealEnergyFrom(Monster target) {
		int e = target.getEnergy();
		int take = Math.min(e, Constants.SCHEMER_STEAL);
		if (take == 0 && e == 0) {
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
}
