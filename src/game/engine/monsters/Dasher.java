package game.engine.monsters;

import game.engine.Role;

public class Dasher extends Monster {
	private int momentumTurns;

	public Dasher(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.momentumTurns = 0;
	}

	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		this.momentumTurns = 3;
	}

	@Override
	public void move(int distance) {
		int mult = (momentumTurns > 0) ? 3 : 2;
		setPosition(getPosition() + distance * mult);
		if (momentumTurns > 0) {
			momentumTurns--;
		}
	}

	public int getMomentumTurns() {
		return momentumTurns;
	}

	public void setMomentumTurns(int momentumTurns) {
		this.momentumTurns = momentumTurns;
	}
}
