package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  private int moveNumbers;
  
  /**
   * Generate a new board.
   */
  
  public GameBoard() {
    turn = 1;
    winner = 0;
    isDraw = false;
    gameStarted = false;
    boardState = new char[3][3];
    moveNumbers = 9;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        boardState[i][j] = '\u0000';
      }
    }
  }
  
  public void setP1(Player player1) {
    this.p1 = player1;
  }
  
  public void setP2(Player player2) {
    this.p2 = player2;
  }
  
  public void setGameStarted() {
    this.gameStarted = true;
  }
  
  public void setTurn(int playerID) {
    this.turn = playerID;
  }
  
  public void setDraw(boolean draw) {
    this.isDraw = draw;
  }
  
  public void setWinner(int playerID) {
    this.winner = playerID;
  }
  
  public int getCurPlayer() {
    return this.turn;
  }
  
  public Player getPlayer(int playerID) {
    if (playerID == 1) {
      return this.p1;
    }
    return this.p2;
  }
  
  public Message validMove(Move newMove) {
    int x = newMove.getX();
    int y = newMove.getY();
    Message newMessage = new Message();
    newMessage.setCode(100);
    if (newMove.getPlayer().getID() != this.turn) {
      newMessage.setMoveValidity(false);
      newMessage.setMessage("Please wait until your turn.");
    } else if (x < 0 || y < 0 || x >= 3 || y >= 3) {
      newMessage.setMoveValidity(false);
      newMessage.setMessage("Click is out of board.");
    } else if (this.boardState[x][y] != '\u0000') {
      newMessage.setMoveValidity(false);
      newMessage.setMessage("Can not change non-empty cell.");
    } else {
      newMessage.setMoveValidity(true);
      newMessage.setMessage("Filled!");	
    }
    return newMessage;
  }
  
  
  public boolean formRow(int x, int y) {
    char type = this.boardState[x][y];
   
    // check the row
    for (int i = 0; i < 3; i++) {
      if (this.boardState[i][y] != type) {
        return false;
      }
    }
    return true;
  }
  
  public boolean formColumn(int x, int y) {
    char type = this.boardState[x][y];
	
    // check the column
    for (int j = 0; j < 3; j++) {
      if (this.boardState[x][j] != type) {
        return false;
      }
    }
    return true;
  }
  
  public boolean formDiag(int x, int y) {
    if (x != y || x != 2 - y) {
      return false;
    }
    char type = this.boardState[x][y];
    if (x == y) {
      for (int i = 0; i < 3; i++) {
        if (this.boardState[i][i] != type) {
          return false;
        }
      }
    } else if (x == 2 - y) {
      for (int i = 0; i < 3; i++) {
        if (this.boardState[i][2 - i] != type) {
          return false;
        }
      }
    }
    return true;
  }
  
  
  public boolean formLine(int x, int y) {
    return this.formRow(x, y) || this.formColumn(x, y) || this.formDiag(x, y);
  }
  
  public void makeMove(Move newMove) {
    char type = newMove.getPlayer().getType();
    int x = newMove.getX();
    int y = newMove.getY();
    this.boardState[x][y] = type;
    this.moveNumbers--;
    if (this.moveNumbers == 0) {
      // set the board to be full;
      this.setDraw(true);
    }
    
    int curPlayer = newMove.getPlayer().getID();
    if (this.formLine(x, y)) {
      this.setWinner(curPlayer);   
    }
    
    if (!this.isDraw && this.winner == 0) {
      if (curPlayer == 1) {
        this.setTurn(2);
      } else {
        this.setTurn(1);
      }
    }
  }

}
