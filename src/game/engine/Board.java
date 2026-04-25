package game.engine;

import java.util.ArrayList;

import game.engine.cards.Card;
import game.engine.cells.Cell;
import game.engine.monsters.Monster;

public class Board {
<<<<<<< Updated upstream
=======
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> originalCards;
	public static ArrayList<Card> cards;
	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		originalCards = readCards;
		cards = new ArrayList<Card>();
	}

	private int[] indexToRowCol(int index){

		int x =index / Constants.BOARD_COLS; 
		int col;
		int row = (Constants.BOARD_ROWS - 1) - x;
	if(x%2 == 0){col = index%10;
	}else{
		col = (Constants.BOARD_ROWS - 1)-(index%10);
	}
	int[] position = new int[]{row,col};
	
	return position;
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
 public void initializeBoard(ArrayList<Cell> specialCells){ //=--->Door cells @ even indices else the "Rest of cells are @ odd indices".<---=//
	Cell Door = null;
	Cell Rest = null;
	for(int i=0; i<=99;i++){
		if(i%2 ==0)setCell(i,Door);
		else{
			setCell(i,Rest);
		}
	}
	for (int i=0; i<specialCells.size();i++){
		Cell c = specialCells.get(i);
		//Pending ..
		
	}
	
	
	 
 }
	private void setCardsByRarity(){ 

	
	
	}
	public static void reloadCards(){
	 
	}

	//public static Card drawCard(){}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}
>>>>>>> Stashed changes

    private final Cell[][] boardCells;
    private static ArrayList<Monster> stationedMonsters;
    private static ArrayList<Card> originalCards;
    public static ArrayList<Card> cards;

    public Board(ArrayList<Card> readCards) {
        this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
        Board.stationedMonsters = new ArrayList<>();
        Board.cards = new ArrayList<>();
        Board.originalCards = readCards;
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
}
