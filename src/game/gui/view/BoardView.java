package game.gui.view;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Game;
import game.engine.Role;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.ContaminationSock;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
import game.engine.monsters.Monster;
import game.gui.util.BoardLayout;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BoardView extends StackPane {

	private static final int CELL = BoardTheme.CELL_SIZE;

	private final GridPane grid = new GridPane();
	private final StackPane[][] cellPanes = new StackPane[Constants.BOARD_ROWS][Constants.BOARD_COLS];
	private final Label[][] typeLabels = new Label[Constants.BOARD_ROWS][Constants.BOARD_COLS];
	private final Label[][] valueLabels = new Label[Constants.BOARD_ROWS][Constants.BOARD_COLS];
	private final Circle playerToken = createToken(Color.web("#1e88e5"));
	private final Circle opponentToken = createToken(Color.web("#e53935"));

	public BoardView() {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(20));
		setStyle("-fx-background-color: " + BoardTheme.BOARD_BG + ";");

		grid.setHgap(BoardTheme.GRID_GAP);
		grid.setVgap(BoardTheme.GRID_GAP);
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: " + BoardTheme.GRID_LINE + ";");

		buildCells();

		double gridWidth = CELL * Constants.BOARD_COLS + BoardTheme.GRID_GAP * (Constants.BOARD_COLS - 1);
		StackPane logo = MonsterBoardLogo.create(gridWidth * 1.12);
		StackPane boardStack = new StackPane(logo, grid);
		boardStack.setAlignment(Pos.CENTER);
		getChildren().add(boardStack);
	}

	private void buildCells() {
		for (int logicalRow = 0; logicalRow < Constants.BOARD_ROWS; logicalRow++) {
			for (int col = 0; col < Constants.BOARD_COLS; col++) {
				int gridRow = toGridRow(logicalRow);

				Label indexLabel = new Label();
				indexLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: " + BoardTheme.INDEX_TEXT + ";");
				GridPane.setHalignment(indexLabel, HPos.LEFT);
				GridPane.setValignment(indexLabel, VPos.TOP);
				GridPane.setMargin(indexLabel, new Insets(2, 0, 0, 4));

				Label typeLabel = new Label();
				typeLabel.setMaxWidth(CELL - 4);
				typeLabel.setAlignment(Pos.CENTER);
				typeLabel.setStyle("-fx-font-size: 7px; -fx-font-weight: bold; -fx-text-alignment: center;");

				Label valueLabel = new Label();
				valueLabel.setMaxWidth(CELL - 4);
				valueLabel.setAlignment(Pos.CENTER);
				valueLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-alignment: center;");

				VBox textColumn = new VBox(0, typeLabel, valueLabel);
				textColumn.setAlignment(Pos.CENTER);
				textColumn.setMaxWidth(CELL);
				textColumn.setPrefWidth(CELL);

				Region spacer = new Region();
				VBox.setVgrow(spacer, Priority.ALWAYS);
				VBox body = new VBox(spacer, textColumn);
				body.setAlignment(Pos.BOTTOM_CENTER);
				body.setPrefSize(CELL, CELL);
				body.setMaxSize(CELL, CELL);
				body.setPadding(new Insets(0, 2, 3, 2));

				BorderPane cellContent = new BorderPane();
				cellContent.setTop(indexLabel);
				cellContent.setCenter(body);
				cellContent.setPrefSize(CELL, CELL);
				cellContent.setMaxSize(CELL, CELL);
				cellContent.setMinSize(CELL, CELL);

				StackPane cellPane = new StackPane(cellContent);
				cellPane.setPrefSize(CELL, CELL);
				cellPane.setMaxSize(CELL, CELL);
				cellPane.setMinSize(CELL, CELL);
				cellPane.setOpacity(1.0);
				cellPane.setClip(new Rectangle(CELL, CELL));

				cellPanes[logicalRow][col] = cellPane;
				typeLabels[logicalRow][col] = typeLabel;
				valueLabels[logicalRow][col] = valueLabel;
				indexLabelsStore(logicalRow, col, indexLabel);

				grid.add(cellPane, col, gridRow);
			}
		}
	}

	private final Label[][] indexLabels = new Label[Constants.BOARD_ROWS][Constants.BOARD_COLS];

	private void indexLabelsStore(int row, int col, Label label) {
		indexLabels[row][col] = label;
	}

	private static int toGridRow(int logicalRow) {
		return Constants.BOARD_ROWS - 1 - logicalRow;
	}

	private Circle createToken(Color color) {
		Circle circle = new Circle(9);
		circle.setFill(color);
		circle.setStroke(Color.web("#1a1a1a"));
		circle.setStrokeWidth(2);
		return circle;
	}

	public void refresh(Game game) {
		Board board = game.getBoard();
		Cell[][] cells = board.getBoardCells();
		Monster player = game.getPlayer();
		Monster opponent = game.getOpponent();

		for (int logicalRow = 0; logicalRow < Constants.BOARD_ROWS; logicalRow++) {
			for (int col = 0; col < Constants.BOARD_COLS; col++) {
				int index = BoardLayout.rowColToIndex(logicalRow, col);
				Cell cell = cells[logicalRow][col];
				StackPane pane = cellPanes[logicalRow][col];
				Label typeLabel = typeLabels[logicalRow][col];
				Label valueLabel = valueLabels[logicalRow][col];

				indexLabels[logicalRow][col].setText(String.valueOf(index));
				applyCellStyle(pane, typeLabel, valueLabel, cell);

				pane.getChildren().remove(playerToken);
				pane.getChildren().remove(opponentToken);

				if (player.getPosition() == index) {
					pane.getChildren().add(playerToken);
					positionToken(playerToken, opponent.getPosition() == index);
				}
				if (opponent.getPosition() == index) {
					pane.getChildren().add(opponentToken);
					positionToken(opponentToken, player.getPosition() == index);
				}
			}
		}
	}

	private void positionToken(Circle token, boolean sharedCell) {
		StackPane.setAlignment(token, Pos.CENTER);
		if (sharedCell) {
			StackPane.setMargin(token, new Insets(-4, 0, 0, token == playerToken ? -11 : 11));
		} else {
			StackPane.setMargin(token, new Insets(-4, 0, 0, 0));
		}
	}

	private void applyCellStyle(StackPane pane, Label typeLabel, Label valueLabel, Cell cell) {
		String bg = BoardTheme.CELL_NORMAL;
		String typeText = "";
		String valueText = "";
		String typeColor = BoardTheme.INDEX_TEXT;
		String valueColor = "#1a1a1a";

		if (cell instanceof DoorCell) {
			DoorCell door = (DoorCell) cell;
			if (door.getRole() == Role.SCARER) {
				bg = door.isActivated() ? BoardTheme.DOOR_SCARER_USED : BoardTheme.DOOR_SCARER;
				typeColor = "#ffffff";
				valueColor = "#e8f4ff";
			} else {
				bg = door.isActivated() ? BoardTheme.DOOR_LAUGHER_USED : BoardTheme.DOOR_LAUGHER;
				typeColor = "#3d2f00";
				valueColor = "#2a2000";
			}
			typeText = door.getRole() == Role.SCARER ? "SCARER" : "LAUGH";
			valueText = String.valueOf(door.getEnergy());
			if (door.isActivated()) {
				typeText = "USED";
			}
		} else if (cell instanceof MonsterCell) {
			bg = BoardTheme.CELL_MONSTER;
			typeText = "MONSTER";
			typeColor = "#2d1f4a";
			valueText = abbreviate(((MonsterCell) cell).getCellMonster().getName(), 7);
			valueColor = "#2d1f4a";
		} else if (cell instanceof CardCell) {
			bg = BoardTheme.CELL_CARD;
			typeText = "CARD";
			typeColor = "#4a2c0a";
		} else if (cell instanceof ConveyorBelt) {
			bg = BoardTheme.CELL_BELT;
			typeText = "BELT";
			typeColor = "#0d3d0d";
			valueText = "+" + ((ConveyorBelt) cell).getEffect();
			valueColor = "#0d3d0d";
		} else if (cell instanceof ContaminationSock) {
			bg = BoardTheme.CELL_SOCK;
			typeText = "SOCK";
			typeColor = "#ffffff";
			valueText = String.valueOf(((ContaminationSock) cell).getEffect());
			valueColor = "#ffffff";
		}

		pane.setStyle("-fx-background-color: " + bg + "; -fx-background-insets: 0; -fx-padding: 0;");
		typeLabel.setText(typeText);
		typeLabel.setStyle(String.format(
				"-fx-font-size: 7px; -fx-font-weight: bold; -fx-text-fill: %s; -fx-text-alignment: center;", typeColor));
		valueLabel.setText(valueText);
		valueLabel.setStyle(String.format(
				"-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: %s; -fx-text-alignment: center;", valueColor));
	}

	private String abbreviate(String name, int maxLen) {
		if (name.length() <= maxLen) {
			return name;
		}
		return name.substring(0, maxLen - 1) + "…";
	}
}
