package game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.dataloader.DataLoader;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());

		this.allMonsters = DataLoader.readMonsters();

		this.player = selectRandomMonsterByRole(playerRole);
		this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.current = player;

		if (player != null) {
			allMonsters.remove(player);
		}
		if (opponent != null) {
			allMonsters.remove(opponent);
		}

		Board.setStationedMonsters(allMonsters);
		board.initializeBoard(DataLoader.readCells());
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return allMonsters; 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}

	private Monster getCurrentOpponent() {
		return current == player ? opponent : player;
	}

	private boolean checkWinCondition(Monster monster) {
		return monster != null
				&& monster.getPosition() == Constants.WINNING_POSITION
				&& monster.getEnergy() >= Constants.WINNING_ENERGY;
	}

	public Monster getWinner() {
		if (checkWinCondition(player)) {
			return player;
		}
		if (checkWinCondition(opponent)) {
			return opponent;
		}
		return null;
	}

	public void playTurn() {
		if (current != null && current.isFrozen()) {
			current.setFrozen(false);
			switchTurn();
			return;
		}
		rollDice();
	}

	private void switchTurn() {
		current = getCurrentOpponent();
	}

	private int rollDice() {
		return 1 + (int) (Math.random() * 6);
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		Collections.shuffle(allMonsters);
	    return allMonsters.stream()
	    		.filter(m -> m.getRole() == role)
	    		.findFirst()
	    		.orElse(null);
	}

	private void usePowerup() throws OutOfEnergyException {
		if (current.getEnergy() < Constants.POWERUP_COST) {
			throw new OutOfEnergyException();
		}
		current.alterEnergy(-Constants.POWERUP_COST);
		current.executePowerupEffect(getCurrentOpponent());
	}
	
}