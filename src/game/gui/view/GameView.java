package game.gui.view;

import game.engine.Board;
import game.engine.Game;
import game.engine.monsters.Monster;
import game.gui.controller.GameController;
import game.gui.model.GameModel;
import game.gui.model.GameModel.Phase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameView extends BorderPane {

	private final GameModel model;
	private final GameController controller;
	private final BoardView boardView = new BoardView();
	private final CardDeckPanel cardDeckPanel = new CardDeckPanel();
	private final MonsterPanel playerPanel = new MonsterPanel("Your Monster");
	private final MonsterPanel opponentPanel = new MonsterPanel("Opponent");
	private final Label turnLabel = new Label();
	private final Label statusLabel = new Label();
	private final Label diceLabel = new Label();
	private final Label cardLabel = new Label();
	private final Label deckLabel = new Label();
	private final Label phaseLabel = new Label();
	private final ListView<String> eventList = new ListView<>();
	private final Button powerupButton = new Button("Use Powerup (500)");
	private final Button rollButton = new Button("Roll Dice");

	public GameView(GameModel model, GameController controller) {
		this.model = model;
		this.controller = controller;
		buildLayout();
		bindModel();
	}

	private void buildLayout() {
		setPadding(new Insets(8));
		setStyle("-fx-background-color: #efe6d5;");

		turnLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
		statusLabel.setWrapText(true);
		statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
		diceLabel.setStyle("-fx-font-size: 13px;");
		cardLabel.setWrapText(true);
		cardLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #5d4037;");
		deckLabel.setStyle("-fx-font-size: 12px;");
		phaseLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #1565c0; -fx-font-weight: bold;");

		powerupButton.setStyle("-fx-background-color: #7e57c2; -fx-text-fill: white;");
		rollButton.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold;");

		powerupButton.setOnAction(e -> controller.usePowerup());
		rollButton.setOnAction(e -> controller.rollDice());

		HBox actions = new HBox(12, powerupButton, rollButton);
		actions.setAlignment(Pos.CENTER_LEFT);

		VBox topBar = new VBox(6, turnLabel, phaseLabel, statusLabel, diceLabel, cardLabel, deckLabel, actions);
		topBar.setPadding(new Insets(0, 0, 8, 0));
		setTop(topBar);

		ScrollPane boardScroll = new ScrollPane(boardView);
		boardScroll.setFitToWidth(true);
		boardScroll.setFitToHeight(true);
		boardScroll.setStyle("-fx-background: #2b2b2b; -fx-background-color: #2b2b2b;");
		boardScroll.setPannable(false);
		setCenter(boardScroll);

		VBox legend = buildLegend();
		VBox side = new VBox(10, cardDeckPanel, playerPanel, opponentPanel, legend);
		side.setPadding(new Insets(0, 0, 0, 8));
		setRight(side);

		eventList.setPrefHeight(120);
		eventList.setPlaceholder(new Label("Game events appear here…"));
		ScrollPane logScroll = new ScrollPane(eventList);
		logScroll.setPrefHeight(130);
		logScroll.setFitToWidth(true);

		Label logTitle = new Label("Event log");
		logTitle.setStyle("-fx-font-weight: bold;");
		VBox bottom = new VBox(4, logTitle, logScroll);
		bottom.setPadding(new Insets(8, 0, 0, 0));
		setBottom(bottom);
	}

	private void bindModel() {
		model.statusMessageProperty().addListener((obs, o, n) -> statusLabel.setText(n));
		model.lastCardInfoProperty().addListener((obs, o, n) -> cardLabel.setText(n.isEmpty() ? "" : "Last card: " + n));
		model.getEventLog().addListener((javafx.collections.ListChangeListener.Change<? extends String> c) -> {
			while (c.next()) {
				if (c.wasAdded()) {
					eventList.getItems().addAll(c.getAddedSubList());
					eventList.scrollTo(eventList.getItems().size() - 1);
				}
			}
		});
		model.phaseProperty().addListener((obs, o, n) -> updatePhaseLabel(n));
	}

	public void clearEventList() {
		eventList.getItems().clear();
	}

	public void syncEventLog() {
		eventList.getItems().setAll(model.getEventLog());
		if (!eventList.getItems().isEmpty()) {
			eventList.scrollTo(eventList.getItems().size() - 1);
		}
	}

	public void refresh() {
		if (model.getGame() == null) {
			cardDeckPanel.clear();
			return;
		}
		Game game = model.getGame();
		Monster current = game.getCurrent();

		turnLabel.setText("Turn " + model.turnNumberProperty().get()
				+ "  |  Current: " + current.getName()
				+ (current == game.getPlayer() ? " (You)" : " (Opponent)"));

		boardView.refresh(game);
		playerPanel.update(game.getPlayer(), game.getCurrent() == game.getPlayer(), true);
		opponentPanel.update(game.getOpponent(), game.getCurrent() == game.getOpponent(), false);

		int lastRoll = game.getLastDiceRoll();
		diceLabel.setText(lastRoll > 0 ? "Last dice roll: " + lastRoll : "");

		int cardsLeft = Board.getCards() != null ? Board.getCards().size() : 0;
		deckLabel.setText("Cards in deck: " + cardsLeft + " / 25 (see card pile panel →)");
		cardDeckPanel.refresh(model);

		boolean frozen = current.isFrozen();
		powerupButton.setDisable(frozen || model.getPhase() == Phase.GAME_OVER);
		rollButton.setDisable(model.getPhase() == Phase.GAME_OVER);

		if (frozen) {
			rollButton.setText("Skip Frozen Turn");
		} else {
			rollButton.setText("Roll Dice");
		}

		updatePhaseLabel(model.getPhase());
	}

	private VBox buildLegend() {
		VBox legend = new VBox(4);
		legend.setPadding(new Insets(8));
		legend.setStyle("-fx-background-color: #fff; -fx-border-color: #8b4513; -fx-border-width: 1; -fx-border-radius: 4;");
		Label title = new Label("Cell legend");
		title.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
		legend.getChildren().addAll(title,
				legendItem("#5b8fb9", "SCARER door"),
				legendItem("#e8c547", "LAUGHER door"),
				legendItem("#9eb3c7", "Used SCARER door"),
				legendItem("#b19cd9", "Monster cell"),
				legendItem("#f4a460", "Card cell"),
				legendItem("#90ee90", "Conveyor belt"),
				legendItem("#cd5c5c", "Contamination sock"),
				legendItem("#e8dcc8", "Normal corridor"),
				new Label("Demo: W → cell 99\nDemo: E → +200 energy"));
		legend.getChildren().get(legend.getChildren().size() - 1)
				.setStyle("-fx-font-size: 10px; -fx-text-fill: #555; -fx-font-style: italic;");
		return legend;
	}

	private Label legendItem(String color, String text) {
		Label label = new Label("■ " + text);
		label.setStyle("-fx-font-size: 10px; -fx-text-fill: " + color + ";");
		return label;
	}

	private void updatePhaseLabel(Phase phase) {
		switch (phase) {
		case POWERUP:
			phaseLabel.setText("Phase: Powerup (optional) → then Roll");
			break;
		case ROLL:
			phaseLabel.setText("Phase: Roll dice");
			break;
		case GAME_OVER:
			phaseLabel.setText("Phase: Game over");
			break;
		default:
			phaseLabel.setText("");
		}
	}
}
