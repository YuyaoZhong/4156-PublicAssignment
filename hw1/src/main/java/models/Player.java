package models;

public class Player {

  private char type;

  private int id;
  
  public Player(char t, int n) {
    setType(t);
    id = n;
  }
  
  public int getID() {
    return id;
  }
  
  public void setID(int n) {
    this.id = n;
  }
  
  public char getType() {
    return type;
  }

  public void setType(char type) {
    this.type = type;
  }
}
