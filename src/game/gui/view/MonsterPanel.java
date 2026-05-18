package game.gui.view;

import game.engine.Constants;
import game.engine.Role;
import game.engine.monsters.Monster;
import game.gui.util.MonsterDisplay;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;

public class MonsterPanel extends VBox {

	private final Label titleLabel = new Label();
	private final Label nameLabel = new Label();
	private final Label originalRoleLabel = new Label();
	private final Label currentRoleLabel = new Label();
	private final Label typeLabel = new Label();
	private final Label energyLabel = new Label();
	private final Label positionLabel = new Label();
	private final Label effectsLabel = new Label();
	private final Circle token = new Circle(12);

	public MonsterPanel(String panelTitle) {
		setSpacing(4);
		setPadding(new Insets(10));
		setStyle("-fx-background-color: #f5f0e6; -fx-border-color: #8b4513; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6;");
		setMinWidth(220);

		titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		titleLabel.setText(panelTitle);

		token.setStroke(Color.web("#4a3728"));
		token.setStrokeWidth(2);

		HBox header = new HBox(8, token, titleLabel);
		header.setAlignment(Pos.CENTER_LEFT);

		nameLabel.setWrapText(true);
		originalRoleLabel.setWrapText(true);
		currentRoleLabel.setWrapText(true);
		typeLabel.setWrapText(true);
		energyLabel.setWrapText(true);
		positionLabel.setWrapText(true);
		effectsLabel.setWrapText(true);
		effectsLabel.setStyle("-fx-font-size: 11px;");

		getChildren().addAll(header, nameLabel, originalRoleLabel, currentRoleLabel,
				typeLabel, energyLabel, positionLabel, new Label("Status effects:"), effectsLabel);
	}

	public void update(Monster monster, boolean isCurrent, boolean isPlayer) {
		nameLabel.setText("Name: " + monster.getName());
		originalRoleLabel.setText("Original role: " + monster.getOriginalRole());
		currentRoleLabel.setText("Current role: " + monster.getRole()
				+ (monster.isConfused() ? " (CONFUSED)" : ""));
		typeLabel.setText("Type: " + MonsterDisplay.getTypeName(monster));
		energyLabel.setText("Energy: " + monster.getEnergy() + " / " + Constants.WINNING_ENERGY);
		positionLabel.setText("Position: cell " + monster.getPosition());

		if (monster.getRole() == Role.SCARER) {
			token.setFill(Color.web("#2e5c8a"));
		} else {
			token.setFill(Color.web("#c9a227"));
		}

		if (isPlayer) {
			token.setStroke(Color.web("#1a6b1a"));
			token.setStrokeWidth(3);
		} else {
			token.setStroke(Color.web("#8b0000"));
			token.setStrokeWidth(3);
		}

		effectsLabel.setText(MonsterDisplay.getStatusEffects(monster));

		String highlight = isCurrent ? " — ACTIVE TURN" : "";
		titleLabel.setText((isPlayer ? "Your Monster" : "Opponent") + highlight);
		if (isCurrent) {
			setStyle("-fx-background-color: #fff8dc; -fx-border-color: #ff8c00; -fx-border-width: 3; -fx-border-radius: 6; -fx-background-radius: 6;");
		} else {
			setStyle("-fx-background-color: #f5f0e6; -fx-border-color: #8b4513; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6;");
		}

		if (monster.isConfused()) {
			currentRoleLabel.setStyle("-fx-text-fill: #8b008b; -fx-font-weight: bold;");
		} else {
			currentRoleLabel.setStyle("");
		}
	}
}
