package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  public boolean getMoveValidity() {
    return this.moveValidity;
  }
  
  public int getCode() {
    return this.code;
  }
 
  public String getMessage() {
    return this.message;
  }
  
  public void setMoveValidity(boolean valid) {
    this.moveValidity = valid;
  }
  
  public void setMessage(String m) {
    this.message = m;
  }
  
  public void setCode(int c) {
    this.code = c;
  }
}
