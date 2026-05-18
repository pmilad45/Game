package game.gui;

import game.gui.controller.GameController;
import game.gui.model.GameModel;
import game.gui.view.GameOverView;
import game.gui.view.GameView;
import game.gui.view.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 820;

	private final GameModel model = new GameModel();
	private final GameController controller = new GameController(model);
	private final StackPane root = new StackPane();
	private final StartView startView = new StartView();
	private final GameView gameView = new GameView(model, controller);
	private final GameOverView gameOverView = new GameOverView();

	@Override
	public void start(Stage primaryStage) {
		root.getChildren().addAll(startView, gameView, gameOverView);
		showStart();

		controller.setOnGameStateChanged(v -> gameView.refresh());
		controller.setOnGameOver(() -> showGameOver());

		startView.getStartButton().setOnAction(e -> {
			gameView.clearEventList();
			controller.startGame(startView.getSelectedRole());
			if (model.isGameRunning()) {
				gameView.syncEventLog();
				showGame();
				gameView.refresh();
			}
		});

		gameOverView.getBackButton().setOnAction(e -> showStart());

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setOnKeyPressed(e -> {
			if (!gameView.isVisible()) {
				return;
			}
			if (e.getCode() == KeyCode.W) {
				controller.demoTeleportPlayerToWinCell();
			} else if (e.getCode() == KeyCode.E) {
				controller.demoIncreasePlayerEnergy();
			}
		});
		primaryStage.setTitle("DoorDasH — Scare vs Laugh Touchdown");
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(WIDTH);
		primaryStage.setMinHeight(HEIGHT);
		primaryStage.show();
	}

	private void showStart() {
		startView.setVisible(true);
		gameView.setVisible(false);
		gameOverView.setVisible(false);
	}

	private void showGame() {
		startView.setVisible(false);
		gameView.setVisible(true);
		gameOverView.setVisible(false);
	}

	private void showGameOver() {
		gameOverView.showResult(
				model.getGame().getWinner(),
				model.getGame().getPlayer(),
				model.getGame().getOpponent());
		startView.setVisible(false);
		gameView.setVisible(false);
		gameOverView.setVisible(true);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
