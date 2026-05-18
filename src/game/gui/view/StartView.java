package game.gui.view;

import game.engine.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StartView extends VBox {

	private final ToggleGroup roleGroup = new ToggleGroup();
	private final RadioButton scarerButton = new RadioButton("SCARER — harness scream energy");
	private final RadioButton laugherButton = new RadioButton("LAUGHER — power of laughter (10× energy)");
	private final Button startButton = new Button("Start Game");

	public StartView() {
		setAlignment(Pos.CENTER);
		setSpacing(16);
		setPadding(new Insets(30));
		setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #3949ab);");

		Label title = new Label("DoorDasH");
		title.setFont(Font.font("System", FontWeight.BOLD, 36));
		title.setStyle("-fx-text-fill: white;");

		Label subtitle = new Label("Scare vs Laugh Touchdown");
		subtitle.setFont(Font.font("System", FontWeight.NORMAL, 18));
		subtitle.setStyle("-fx-text-fill: #bbdefb;");

		scarerButton.setToggleGroup(roleGroup);
		laugherButton.setToggleGroup(roleGroup);
		scarerButton.setSelected(true);
		scarerButton.setStyle("-fx-text-fill: white;");
		laugherButton.setStyle("-fx-text-fill: white;");

		VBox roleBox = new VBox(8, new Label("Choose your side:"), scarerButton, laugherButton);
		roleBox.getChildren().get(0).setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
		roleBox.setAlignment(Pos.CENTER);

		startButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 30;");

		TextArea instructions = new TextArea(getInstructionsText());
		instructions.setEditable(false);
		instructions.setWrapText(true);
		instructions.setPrefRowCount(12);
		instructions.setMaxWidth(520);
		instructions.setStyle("-fx-font-size: 12px;");

		Label instructionsTitle = new Label("How to Play");
		instructionsTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

		getChildren().addAll(title, subtitle, roleBox, startButton, instructionsTitle, instructions);
	}

	public Button getStartButton() {
		return startButton;
	}

	public Role getSelectedRole() {
		return scarerButton.isSelected() ? Role.SCARER : Role.LAUGHER;
	}

	private String getInstructionsText() {
		return "Navigate the 100-cell Floor to reach Boo's Door (cell 99) with at least 1000 energy.\n\n"
				+ "Each turn:\n"
				+ "1. Optionally activate your powerup (costs 500 energy).\n"
				+ "2. Roll a 6-sided die and move forward.\n"
				+ "3. Landing on special cells triggers doors, cards, belts, socks, or monster allies.\n\n"
				+ "Doors match SCARER/LAUGHER roles — matching doors boost your team's energy; "
				+ "mismatched doors drain it (shield blocks once). Doors exhaust after use.\n\n"
				+ "You cannot land on the opponent's cell — roll again if blocked.\n\n"
				+ "Win by reaching cell 99 with ≥ 1000 energy!";
	}
}
