package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  
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
}
