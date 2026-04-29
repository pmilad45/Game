package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class MultiTasker extends Monster {
	private int normalSpeedTurns;

	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		this.normalSpeedTurns = 2;
	}

	@Override
	public void move(int distance) {
		if (normalSpeedTurns > 0) {
			super.move(distance);
			normalSpeedTurns--;
		} else {
			setPosition(getPosition() + distance / 2);
		}
	}

	@Override
	int applyTypeEnergyDelta(int delta) {
		return delta + Constants.MULTITASKER_BONUS;
	}

	public int getNormalSpeedTurns() {
		return normalSpeedTurns;
	}

	public void setNormalSpeedTurns(int normalSpeedTurns) {
		this.normalSpeedTurns = normalSpeedTurns;
		int x=0;
	}
}
