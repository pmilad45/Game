package game.engine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
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
		ArrayList<Card> loadedCards = DataLoader.readCards();
		this.board = new Board(loadedCards);
		try {
			Field originalCardsField = Board.class.getDeclaredField("originalCards");
			originalCardsField.setAccessible(true);
			originalCardsField.set(null, loadedCards);
			Board.reloadCards();
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		this.allMonsters = DataLoader.readMonsters();

		Monster selectedPlayer = selectRandomMonsterByRole(playerRole);
		Monster selectedOpponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.player = createMonsterCopy(selectedPlayer);
		this.opponent = createMonsterCopy(selectedOpponent);
		this.current = player;

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

	private Monster createMonsterCopy(Monster source) {
		if (source == null) {
			return null;
		}
		String name = source.getName();
		String description = source.getDescription();
		Role role = source.getRole();
		int energy = source.getEnergy();
		if (source instanceof Dynamo) {
			return new Dynamo(name, description, role, energy);
		}
		if (source instanceof Dasher) {
			return new Dasher(name, description, role, energy);
		}
		if (source instanceof MultiTasker) {
			return new MultiTasker(name, description, role, energy);
		}
		if (source instanceof Schemer) {
			return new Schemer(name, description, role, energy);
		}
		return null;
	}

	private void usePowerup() throws OutOfEnergyException {
		if (current.getEnergy() < Constants.POWERUP_COST) {
			throw new OutOfEnergyException();
		}
		current.alterEnergy(-Constants.POWERUP_COST);
		current.executePowerupEffect(getCurrentOpponent());
	}
	
}