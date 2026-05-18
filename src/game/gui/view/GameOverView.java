package game.gui.view;

import game.engine.monsters.Monster;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameOverView extends VBox {

	private final Label headline = new Label("Game Won!");
	private final Label winnerLabel = new Label();
	private final Label playerEnergyLabel = new Label();
	private final Label opponentEnergyLabel = new Label();
	private final Button backButton = new Button("Return to Main Menu");

	public GameOverView() {
		setAlignment(Pos.CENTER);
		setSpacing(14);
		setPadding(new Insets(40));
		setStyle("-fx-background-color: linear-gradient(to bottom, #1b5e20, #43a047);");

		headline.setFont(Font.font("System", FontWeight.BOLD, 32));
		headline.setStyle("-fx-text-fill: white;");

		winnerLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
		winnerLabel.setStyle("-fx-text-fill: #fff9c4;");

		playerEnergyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
		opponentEnergyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

		backButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 24;");

		getChildren().addAll(headline, winnerLabel, playerEnergyLabel, opponentEnergyLabel, backButton);
	}

	public void showResult(Monster winner, Monster player, Monster opponent) {
		headline.setText("Game Over!");
		winnerLabel.setText("Winner: " + winner.getName() + " (" + winner.getRole() + ")");
		playerEnergyLabel.setText(player.getName() + " — final energy: " + player.getEnergy()
				+ " (cell " + player.getPosition() + ")");
		opponentEnergyLabel.setText(opponent.getName() + " — final energy: " + opponent.getEnergy()
				+ " (cell " + opponent.getPosition() + ")");
	}

	public Button getBackButton() {
		return backButton;
	}
}
