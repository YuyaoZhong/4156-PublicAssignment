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
  
  public char getType() {
    return type;
  }
  
  public void setID(int id) {
    this.id = id;
  }
  
  public void setType(char type) {
    this.type = type;
  }
  
}
