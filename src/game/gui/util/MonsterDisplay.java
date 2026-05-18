package game.gui.util;

import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Schemer;

public final class MonsterDisplay {

	private MonsterDisplay() {
	}

	public static String getTypeName(Monster monster) {
		if (monster instanceof Dasher) {
			return "Dasher";
		}
		if (monster instanceof Dynamo) {
			return "Dynamo";
		}
		if (monster instanceof MultiTasker) {
			return "MultiTasker";
		}
		if (monster instanceof Schemer) {
			return "Schemer";
		}
		return "Unknown";
	}

	public static String getStatusEffects(Monster monster) {
		StringBuilder effects = new StringBuilder();
		if (monster.isShielded()) {
			effects.append("Shield active\n");
		}
		if (monster.isFrozen()) {
			effects.append("Frozen (skip next turn)\n");
		}
		if (monster.isConfused()) {
			effects.append("Confused (").append(monster.getConfusionTurns()).append(" turns left)\n");
		}
		if (monster instanceof Dasher) {
			Dasher dasher = (Dasher) monster;
			if (dasher.getMomentumTurns() > 0) {
				effects.append("Momentum Rush (").append(dasher.getMomentumTurns()).append(" turns)\n");
			}
		}
		if (monster instanceof MultiTasker) {
			MultiTasker multitasker = (MultiTasker) monster;
			if (multitasker.getNormalSpeedTurns() > 0) {
				effects.append("Focus Mode (").append(multitasker.getNormalSpeedTurns()).append(" turns)\n");
			}
		}
		if (effects.length() == 0) {
			return "None";
		}
		return effects.toString().trim();
	}
}
