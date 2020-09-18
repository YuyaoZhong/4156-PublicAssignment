package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  
  
  // getters
  
  /**
   * Get player participating in the game by id.
   * @param playerID the ID of the player, not null
   * @return Player
   */
  public Player getPlayer(int playerID) {
    if (playerID == 1) {
      return this.p1;
    }
    return this.p2;
  }
  
  public int getCurPlayer() {
    return this.turn;
  }
  
  public char[][] getBoardState() {
    return this.boardState;
  }
  
  public char getBoardStateAt(int x, int y) {
    return this.boardState[x][y];
  }
  
  public boolean getGameStarted() {
    return this.gameStarted;
  }
  
  public boolean getDraw() {
    return this.isDraw;
  }
  
  public int getWinner() {
    return this.winner;
  }

  public void setP1(Player player1) {
    this.p1 = player1;
  }
  
  public void setP2(Player player2) {
    this.p2 = player2;
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
  
  public void setGameStarted(boolean started) {
    this.gameStarted = started;
  }
  
  public void setBoardState(char[][] newBoard) {
    this.boardState = newBoard;
  }
  
  public void setBoardStateAt(int x, int y, char type) {
    this.boardState[x][y] = type;
  }
  
}
