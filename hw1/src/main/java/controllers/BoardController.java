package controllers;

import com.google.gson.Gson;
import models.GameBoard;
import models.Message;
import models.Move;

class BoardController {
  /**
   * Judge whether a move is valid.
   * @param newMove move including position and player, not null
   * @return Message
   */
  private GameBoard board;
  
  private int moveNumbers; // add an attribute to judge whether the board is full easily
  
  public BoardController() {
    this.board = new GameBoard();
  }
  
  public GameBoard getBoard() {
    return this.board;
  }
  
 
  /**
   * Judge whether a move is valid.
   * @param newMove move including position and player, not null
   * @return Message
   */
  public Message validMove(Move newMove) {
    int x = newMove.getX();
    int y = newMove.getY();
    Message newMessage = new Message();
    
    if (newMove.getPlayer().getID() != this.board.getCurPlayer()) {
      // Click when it is the other's turn
      newMessage.setCode(200);
      newMessage.setMoveValidity(false);
      newMessage.setMessage("Please wait until your turn.");
    } else if (x < 0 || y < 0 || x >= 3 || y >= 3) {
      // Get a position out of the board
      newMessage.setCode(300);
      newMessage.setMoveValidity(false);
      newMessage.setMessage("Click is out of board.");
    } else if (this.board.getBoardStateAt(x, y) != '\u0000') {
      // Click on a non-empty cell 
      newMessage.setCode(400);
      newMessage.setMoveValidity(false);
      newMessage.setMessage("Can not change non-empty cell.");
    } else {
      newMessage.setCode(100);
      newMessage.setMoveValidity(true);
      newMessage.setMessage("Filled!");
    }
    return newMessage;
  }
  
  /**
   * Reset the board to the initial state.
   */
  public void resetBoard() {
    this.board.setTurn(1);
    this.board.setWinner(0);
    this.board.setDraw(false);
    this.board.setGameStarted(false);
    char[][] boardState = new char[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        boardState[i][j] = '\u0000';
      }
    }
    this.board.setBoardState(boardState);
  }
 
  
  // Judge whether form a line;

  /**
   * Judge whether the row forms a line.
   * @param x row position, not null
   * @param y column position, not null
   * @return boolean, false when not forming a line
   */
  public boolean formRow(int x, int y) {
    char type = this.board.getBoardStateAt(x, y);
   
    // check the row
    for (int i = 0; i < 3; i++) {
      if (this.board.getBoardStateAt(i, y) != type) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Judge whether the column forms a line.
   * @param x row position, not null
   * @param y column position, not null
   * @return boolean, false when not forming a line
   */
  public boolean formColumn(int x, int y) {
    char type = this.board.getBoardStateAt(x, y);
    
    // check the column
    for (int j = 0; j < 3; j++) {
      if (this.board.getBoardStateAt(x, j) != type) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Judge whether the diagonal forms a line.
   * @param x row position, not null
   * @param y column position, not null
   * @return boolean, false when not forming a line
   */
  public boolean formDiag(int x, int y) {
    if (x != y && x != 2 - y) {
      return false;
    }
    char type = this.board.getBoardStateAt(x, y);
    boolean mainDiag = false;
    boolean subDiag = false;
    if (x == y) { // judge the main diagonal
      mainDiag = true;
      for (int i = 0; i < 3; i++) {
        if (this.board.getBoardStateAt(i, i) != type) {
          mainDiag = false;
        }
      }
    }
    if (x == 2 - y) { // judge the sub diagonal
      subDiag = true;
      for (int i = 0; i < 3; i++) {
        if (this.board.getBoardStateAt(i, 2 - i) != type) {
          subDiag = false;
        }
      }
    } 
    return mainDiag || subDiag;
  }
  
  /**
   * Judge whether the position can form a line.
   * @param x row position, not null
   * @param y column position, not null
   * @return boolean
   */
  
  public boolean formLine(int x, int y) {
    return this.formRow(x, y) || this.formColumn(x, y) || this.formDiag(x, y);
  }
  
  /**
   * Make a move when the new move is valid,
   * update the board status.
   * @param newMove new move, not null
   */
  public void makeMove(Move newMove) {
    char type = newMove.getPlayer().getType();
    int x = newMove.getX();
    int y = newMove.getY();
    this.board.setBoardStateAt(x, y, type);
    this.moveNumbers--;
    if (this.moveNumbers == 0) {
      // set the board to be full;
      this.board.setDraw(true);
    }
    
    int curPlayer = newMove.getPlayer().getID();
    if (this.formLine(x, y)) {
      this.board.setWinner(curPlayer);   
    }
    
    if (!this.board.getDraw() && this.board.getWinner() == 0) {
      if (curPlayer == 1) {
        this.board.setTurn(2);
      } else {
        this.board.setTurn(1);
      }
    }
  }
  
  public String getBoardJsonString() {
    return new Gson().toJson(this.board);
  }
}
