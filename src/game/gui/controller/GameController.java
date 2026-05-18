package game.gui.controller;

import java.io.IOException;
import java.util.function.Consumer;

import game.engine.Constants;
import game.engine.Game;
import game.engine.Role;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import game.gui.model.GameModel;
import game.gui.model.GameModel.Phase;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameController {

	private final GameModel model;
	private Consumer<Void> onGameStateChanged;
	private Runnable onGameOver;

	public GameController(GameModel model) {
		this.model = model;
	}

	public void setOnGameStateChanged(Consumer<Void> listener) {
		this.onGameStateChanged = listener;
	}

	public void setOnGameOver(Runnable listener) {
		this.onGameOver = listener;
	}

	public void startGame(Role role) {
		try {
			model.startNewGame(role);
			notifyChanged();
		} catch (IOException e) {
			showError("Failed to load game data", e.getMessage());
		}
	}

	public void usePowerup() {
		if (!model.isGameRunning()) {
			return;
		}
		Game game = model.getGame();
		if (model.getPhase() != Phase.POWERUP) {
			showWarning("Invalid action", "Powerup can only be used before rolling the dice.");
			return;
		}
		if (game.getCurrent().isFrozen()) {
			showWarning("Invalid action", game.getCurrent().getName() + " is frozen and cannot use a powerup.");
			return;
		}
		try {
			game.usePowerup();
			model.log(game.getCurrent().getName() + " activated their powerup (-500 energy).");
			model.onPowerupUsed();
			notifyChanged();
		} catch (OutOfEnergyException e) {
			showWarning("Cannot use powerup", e.getMessage());
		}
	}

	public void rollDice() {
		if (!model.isGameRunning()) {
			return;
		}
		Game game = model.getGame();
		Monster current = game.getCurrent();

		if (current.isFrozen()) {
			try {
				game.playTurn();
				model.log(current.getName() + "'s turn was skipped (Freeze effect).");
				model.onTurnEnded();
				checkWinner();
				notifyChanged();
			} catch (InvalidMoveException e) {
				showWarning("Unexpected error", e.getMessage());
			}
			return;
		}

		if (model.getPhase() == Phase.POWERUP) {
			model.setPhase(Phase.ROLL);
		}

		try {
			int energyBeforePlayer = game.getPlayer().getEnergy();
			int energyBeforeOpponent = game.getOpponent().getEnergy();
			int posBeforePlayer = game.getPlayer().getPosition();
			int posBeforeOpponent = game.getOpponent().getPosition();
			boolean playerShield = game.getPlayer().isShielded();
			boolean opponentShield = game.getOpponent().isShielded();

			game.playTurn();

			model.onDiceRolled(game.getLastDiceRoll());
			model.refreshCardDisplay();
			logStateChanges(game, energyBeforePlayer, energyBeforeOpponent, posBeforePlayer, posBeforeOpponent);
			logShieldBlocks(game.getPlayer(), playerShield);
			logShieldBlocks(game.getOpponent(), opponentShield);

			if (game.getWinner() == null) {
				model.onTurnEnded();
			}
			checkWinner();
			notifyChanged();
		} catch (InvalidMoveException e) {
			model.log("Invalid move: " + e.getMessage() + " — roll again.");
			model.statusMessageProperty().set(
					game.getCurrent().getName() + " cannot land on the opponent. Roll again.");
			showWarning("Invalid move", e.getMessage() + "\nYou must roll again.");
			notifyChanged();
		}
	}

	private void logShieldBlocks(Monster monster, boolean wasShielded) {
		if (wasShielded && !monster.isShielded()) {
			model.log(monster.getName() + "'s shield blocked an energy loss!");
		}
	}

	private void logStateChanges(Game game, int energyBeforePlayer, int energyBeforeOpponent,
			int posBeforePlayer, int posBeforeOpponent) {
		Monster player = game.getPlayer();
		Monster opponent = game.getOpponent();

		if (player.getPosition() != posBeforePlayer) {
			model.log(player.getName() + " moved to cell " + player.getPosition() + ".");
		}
		if (opponent.getPosition() != posBeforeOpponent) {
			model.log(opponent.getName() + " moved to cell " + opponent.getPosition() + ".");
		}
		if (player.getEnergy() != energyBeforePlayer) {
			model.log(player.getName() + " energy: " + energyBeforePlayer + " → " + player.getEnergy() + ".");
		}
		if (opponent.getEnergy() != energyBeforeOpponent) {
			model.log(opponent.getName() + " energy: " + energyBeforeOpponent + " → " + opponent.getEnergy() + ".");
		}
		if (player.isConfused() && player.getRole() != player.getOriginalRole()) {
			model.log(player.getName() + " is confused (current role: " + player.getRole() + ").");
		}
		if (opponent.isConfused() && opponent.getRole() != opponent.getOriginalRole()) {
			model.log(opponent.getName() + " is confused (current role: " + opponent.getRole() + ").");
		}
	}

	private void checkWinner() {
		Monster winner = model.getGame().getWinner();
		if (winner != null) {
			model.setPhase(Phase.GAME_OVER);
			model.log(winner.getName() + " wins! Position " + winner.getPosition()
					+ ", energy " + winner.getEnergy() + ".");
			if (onGameOver != null) {
				onGameOver.run();
			}
		}
	}

	public void demoTeleportPlayerToWinCell() {
		if (!model.isGameRunning() || model.getPhase() == Phase.GAME_OVER) {
			return;
		}
		Game game = model.getGame();
		Monster player = game.getPlayer();
		player.setPosition(Constants.WINNING_POSITION);
		game.getBoard().updateMonsterPositions(player, game.getOpponent());
		model.log("[Demo] " + player.getName() + " moved to cell " + Constants.WINNING_POSITION + " (Boo's Door).");
		if (player.getEnergy() < Constants.WINNING_ENERGY) {
			model.statusMessageProperty().set("At Boo's Door — press E until energy ≥ "
					+ Constants.WINNING_ENERGY + " to win.");
		}
		checkWinner();
		notifyChanged();
	}

	public void demoIncreasePlayerEnergy() {
		if (!model.isGameRunning() || model.getPhase() == Phase.GAME_OVER) {
			return;
		}
		Game game = model.getGame();
		Monster player = game.getPlayer();
		int before = player.getEnergy();
		int boost = 200;
		player.setEnergy(before + boost);
		model.log("[Demo] " + player.getName() + " energy: " + before + " → " + player.getEnergy() + " (+" + boost + ").");
		checkWinner();
		notifyChanged();
	}

	private void notifyChanged() {
		if (onGameStateChanged != null) {
			onGameStateChanged.accept(null);
		}
	}

	private void showWarning(String title, String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showError(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
