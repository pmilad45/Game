package game.engine;

import java.util.ArrayList;
import java.util.Collections;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.monsters.Monster;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards != null ? readCards : new ArrayList<Card>();
		setCardsByRarity();
		reloadCards();
	}

	private void setCardsByRarity() {
		if (originalCards == null) {
			originalCards = new ArrayList<Card>();
			return;
		}
		ArrayList<Card> expanded = new ArrayList<>();
		ArrayList<Card> source = new ArrayList<>(originalCards);
		for (Card c : source) {
			for (int i = 0; i < c.getRarity(); i++) {
				expanded.add(c);
			}
		}
		originalCards = expanded;
	}

	public static void reloadCards() {
		cards = new ArrayList<Card>();
		if (originalCards != null && !originalCards.isEmpty()) {
			cards.addAll(originalCards);
			Collections.shuffle(cards);
		}
	}

	public static Card drawCard() {
		if (cards == null || cards.isEmpty()) {
			reloadCards();
		}
		if (cards == null || cards.isEmpty()) {
			return null;
		}
		return cards.remove(0);
	}

	/** Same linear index ↔ row,col mapping as Milestone2PublicTests / moveMonster helpers. */
	private int[] indexToRowCol(int index) {
		int row = index / Constants.BOARD_COLS;
		int col = index % Constants.BOARD_COLS;
		if (row % 2 == 1) {
			col = Constants.BOARD_COLS - 1 - col;
		}
		return new int[]{row, col};
	}

	private static boolean indexInConstants(int idx, int[] table) {
		for (int v : table) {
			if (v == idx) {
				return true;
			}
		}
		return false;
	}

	private Cell getCell(int index) {
    int[] pos = indexToRowCol(index);
    int row = pos[0];
    int col = pos[1];
    return boardCells[row][col];
	}

 	private void setCell(int index, Cell cell){
	 int[]pos = indexToRowCol(index);
	  int row = pos[0];
	  int col = pos[1];
	  boardCells[row][col] = cell;
	}

 	public void initializeBoard(ArrayList<Cell> specialCells) {
 		ArrayList<DoorCell> doorOrder = new ArrayList<>();
 		ArrayList<ConveyorBelt> conveyorOrder = new ArrayList<>();
 		ArrayList<ContaminationSock> sockOrder = new ArrayList<>();
 		if (specialCells != null) {
 			for (Cell c : specialCells) {
 				if (c instanceof DoorCell) {
 					doorOrder.add((DoorCell) c);
 				} else if (c instanceof ConveyorBelt) {
 					conveyorOrder.add((ConveyorBelt) c);
 				} else if (c instanceof ContaminationSock) {
 					sockOrder.add((ContaminationSock) c);
 				}
 			}
 		}

 		ArrayList<Monster> stationed =
 				stationedMonsters != null ? stationedMonsters : new ArrayList<>();

 		for (int j = 0; j < Constants.MONSTER_CELL_INDICES.length; j++) {
 			int linear = Constants.MONSTER_CELL_INDICES[j];
 			Monster m = j < stationed.size() ? stationed.get(j) : null;
 			String nm = (m != null) ? m.getName() : "Monster";
 			setCell(linear, new MonsterCell(nm, m));
 			if (m != null) {
 				m.setPosition(linear);
 			}
 		}

 		int ci = 0;
 		for (int linear : Constants.CONVEYOR_CELL_INDICES) {
 			if (ci < conveyorOrder.size()) {
 				setCell(linear, conveyorOrder.get(ci++));
 			}
 		}
 		int si = 0;
 		for (int linear : Constants.SOCK_CELL_INDICES) {
 			if (si < sockOrder.size()) {
 				setCell(linear, sockOrder.get(si++));
 			}
 		}

 		for (int linear : Constants.CARD_CELL_INDICES) {
 			setCell(linear, new CardCell("Card Cell"));
 		}

 		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
 			if (indexInConstants(i, Constants.MONSTER_CELL_INDICES)
 					|| indexInConstants(i, Constants.CONVEYOR_CELL_INDICES)
 					|| indexInConstants(i, Constants.SOCK_CELL_INDICES)
 					|| indexInConstants(i, Constants.CARD_CELL_INDICES)) {
 				continue;
 			}
 			if (i % 2 == 1) {
 				setCell(i, doorOrder.get(i / 2));
 			} else {
 				setCell(i, new Cell("Rest"));
 			}
 		}
	}

    public Cell[][] getBoardCells() {
        return boardCells;
    }

    public static ArrayList<Monster> getStationedMonsters() {
        return stationedMonsters;
    }

    public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
        Board.stationedMonsters = stationedMonsters;
    }

	public static ArrayList<Card> getOriginalCards() {
		return originalCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}

    public void setBoardCells(Cell[][] boardCells) {
        this.boardCells = boardCells;
    }
}
