package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  /**
   * Construct a move with given player and position.
   * @param p player, not null
   * @param x row position, not null
   * @param y column position, not null
   */
  public Move(Player p, int x, int y) {
    this.player = p;
    this.moveX = x;
    this.moveY = y;
  }
  
  public int getX() {
    return this.moveX;
  }
  
  public int getY() {
    return this.moveY;
  }
  
  public Player getPlayer() {
    return this.player;
  }
  
  public void setX(int x) {
    this.moveX = x;
  }
  
  public void setY(int y) {
    this.moveX = y;
  }
  
  public void setPlayer(Player p) {
    this.player = p;
  }
}
