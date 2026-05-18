package game.gui.view;

import game.engine.Board;
import game.engine.cards.Card;
import game.gui.model.GameModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CardDeckPanel extends VBox {

	private static final int DECK_SIZE = 25;

	private final Label titleLabel = new Label("Card pile (25 shuffled)");
	private final Label remainingLabel = new Label();
	private final Label drawnTitleLabel = new Label("Drawn cards");
	private final ListView<String> remainingList = new ListView<>();
	private final ListView<String> drawnList = new ListView<>();

	public CardDeckPanel() {
		setSpacing(6);
		setPadding(new Insets(8));
		setMinWidth(200);
		setMaxWidth(220);
		setStyle("-fx-background-color: #fff8e7; -fx-border-color: #8b4513; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6;");

		titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #4a3728;");
		remainingLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #5d4037;");
		drawnTitleLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #666;");

		remainingList.setPrefHeight(180);
		remainingList.setPlaceholder(new Label("No cards left in pile"));
		drawnList.setPrefHeight(90);
		drawnList.setPlaceholder(new Label("None yet"));

		ScrollPane remainingScroll = new ScrollPane(remainingList);
		remainingScroll.setFitToWidth(true);
		remainingScroll.setPrefHeight(180);
		VBox.setVgrow(remainingScroll, Priority.SOMETIMES);

		ScrollPane drawnScroll = new ScrollPane(drawnList);
		drawnScroll.setFitToWidth(true);
		drawnScroll.setPrefHeight(90);

		getChildren().addAll(titleLabel, remainingLabel, remainingScroll, drawnTitleLabel, drawnScroll);
	}

	public void refresh(GameModel model) {
		if (model.getGame() == null) {
			clear();
			return;
		}

		int remaining = Board.getCards() != null ? Board.getCards().size() : 0;
		remainingLabel.setText("Remaining in pile: " + remaining + " / " + DECK_SIZE);

		remainingList.getItems().clear();
		if (Board.getCards() != null) {
			int position = 1;
			for (Card card : Board.getCards()) {
				remainingList.getItems().add(position++ + ". " + card.getName());
			}
		}

		drawnList.getItems().setAll(model.getDrawnCardsDisplay());
	}

	public void clear() {
		remainingLabel.setText("Remaining in pile: — / " + DECK_SIZE);
		remainingList.getItems().clear();
		drawnList.getItems().clear();
	}
}
