package game.engine.cells;

import game.engine.monsters.*;

public class MonsterCell extends Cell {
	private Monster cellMonster;

	public MonsterCell(String name, Monster cellMonster) {
		super(name);
		this.cellMonster = cellMonster;
	}

	public Monster getCellMonster() {
		return cellMonster;
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);

		if (cellMonster == null || landingMonster == null) {
			return;
		}

		if (cellMonster.getRole() == landingMonster.getRole()) {
			landingMonster.executePowerupEffect(opponentMonster);
			return;
		}

		if (landingMonster.getEnergy() > cellMonster.getEnergy()) {
			int transferredEnergy = landingMonster.getEnergy() - cellMonster.getEnergy();
			cellMonster.alterEnergy(transferredEnergy);
			landingMonster.alterEnergy(-transferredEnergy);
		}
	}

}
