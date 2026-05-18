package game.gui.model;

import java.io.IOException;

import game.engine.Board;
import game.engine.Game;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.monsters.Monster;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class GameModel {

	public enum Phase {
		POWERUP, ROLL, GAME_OVER
	}

	private Game game;
	private final IntegerProperty turnNumber = new SimpleIntegerProperty(1);
	private final ObjectProperty<Phase> phase = new SimpleObjectProperty<>(Phase.POWERUP);
	private final StringProperty statusMessage = new SimpleStringProperty("Choose whether to use your powerup, then roll the dice.");
	private final StringProperty lastCardInfo = new SimpleStringProperty("");
	private final ObservableList<String> eventLog = FXCollections.observableArrayList();
	private final ObservableList<String> drawnCardsDisplay = FXCollections.observableArrayList();
	private Card lastDisplayedCard;

	public void startNewGame(Role playerRole) throws IOException {
		game = new Game(playerRole);
		turnNumber.set(1);
		phase.set(Phase.POWERUP);
		lastDisplayedCard = null;
		eventLog.clear();
		drawnCardsDisplay.clear();
		lastCardInfo.set("");
		log("Game started! You are " + game.getPlayer().getName() + " (" + playerRole + ").");
		log("Opponent: " + game.getOpponent().getName() + " (" + game.getOpponent().getRole() + ").");
		log("Both monsters start at cell 0.");
		captureShuffledDeck();
		updateStatusForCurrentTurn();
	}

	private void captureShuffledDeck() {
		ArrayList<Card> pile = Board.getCards();
		if (pile == null) {
			return;
		}
		log("Card pile ready: " + pile.size() + " shuffled cards for card cells.");
		int i = 1;
		for (Card card : pile) {
			log("  " + i++ + ". " + card.getName() + " — " + card.getDescription());
		}
	}

	public ObservableList<String> getDrawnCardsDisplay() {
		return drawnCardsDisplay;
	}

	public Game getGame() {
		return game;
	}

	public boolean isGameRunning() {
		return game != null && phase.get() != Phase.GAME_OVER;
	}

	public IntegerProperty turnNumberProperty() {
		return turnNumber;
	}

	public ObjectProperty<Phase> phaseProperty() {
		return phase;
	}

	public Phase getPhase() {
		return phase.get();
	}

	public StringProperty statusMessageProperty() {
		return statusMessage;
	}

	public StringProperty lastCardInfoProperty() {
		return lastCardInfo;
	}

	public ObservableList<String> getEventLog() {
		return eventLog;
	}

	public void log(String message) {
		eventLog.add(message);
	}

	public void setPhase(Phase newPhase) {
		phase.set(newPhase);
	}

	public Monster getCurrentMonster() {
		return game.getCurrent();
	}

	public void updateStatusForCurrentTurn() {
		if (game.getWinner() != null) {
			phase.set(Phase.GAME_OVER);
			statusMessage.set("Game over!");
			return;
		}
		Monster current = game.getCurrent();
		if (current.isFrozen()) {
			statusMessage.set(current.getName() + " is frozen and will skip this turn. Click Roll to continue.");
			return;
		}
		phase.set(Phase.POWERUP);
		statusMessage.set("Turn " + turnNumber.get() + " — " + current.getName()
				+ "'s turn. Optionally activate powerup (500 energy), then roll.");
	}

	public void onPowerupUsed() {
		phase.set(Phase.ROLL);
		statusMessage.set("Powerup activated. Roll the dice to move.");
	}

	public void onDiceRolled(int roll) {
		statusMessage.set(game.getCurrent().getName() + " rolled a " + roll + ".");
	}

	public void onTurnEnded() {
		turnNumber.set(turnNumber.get() + 1);
		updateStatusForCurrentTurn();
	}

	public void refreshCardDisplay() {
		Card drawn = Board.getLastDrawnCard();
		if (drawn != null && drawn != lastDisplayedCard) {
			lastDisplayedCard = drawn;
			String info = drawn.getName() + " — " + drawn.getDescription();
			lastCardInfo.set(info);
			drawnCardsDisplay.add(drawn.getName() + " — " + drawn.getDescription());
			log("Card drawn: " + info);
		}
	}
}
