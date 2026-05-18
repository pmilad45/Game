package game.gui.util;

import game.engine.Constants;

public final class BoardLayout {

	private BoardLayout() {
	}

	public static int[] indexToRowCol(int index) {
		int cols = Constants.BOARD_COLS;
		int row = index / cols;
		int col = index % cols;
		if (row % 2 == 1) {
			col = cols - 1 - col;
		}
		return new int[] { row, col };
	}

	public static int rowColToIndex(int row, int col) {
		if (row % 2 == 1) {
			col = Constants.BOARD_COLS - 1 - col;
		}
		return row * Constants.BOARD_COLS + col;
	}

	/** GridPane row index so logical row 0 (start) appears at the bottom. */
	public static int toDisplayGridRow(int logicalRow) {
		return Constants.BOARD_ROWS - 1 - logicalRow;
	}
}
