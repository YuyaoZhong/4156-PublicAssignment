package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;


class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);
    
    GameBoard board = new GameBoard();
    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });
    
    // Player 1: start game
    app.post("/startgame", ctx -> {
      board.setInitState();
      String paras = ctx.body();
      // Get the type chosen by player 1
      char type1 = paras.charAt(paras.length() - 1);
      Player p1 = new Player(type1, 1);
      char type2 = (type1 == 'X') ? 'O' : 'X';
      Player p2 = new Player(type2, 2);
      board.setP1(p1);
      board.setP2(p2);
      String json = new Gson().toJson(board);
      ctx.result(json);
    });
    
    // Player 2: join game
    app.get("/joingame", ctx -> {
      board.setGameStarted();
      sendGameBoardToAllPlayers(new Gson().toJson(board));
      ctx.redirect("/tictactoe.html?p=2");
    });
    
    // Make move
    app.post("/move/:playerId", ctx -> {
      int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      Player curPlayer = board.getPlayer(playerId);
      String[] paras = ctx.body().split("&");
      HashMap<String, Integer> paraMap = new HashMap<String, Integer>();
      // A general way to get parameters 
      for (int i = 0; i < paras.length; i++) {
        String[] attrs = paras[i].split("=");
        paraMap.put(attrs[0], Integer.parseInt(attrs[1]));
      }
      int x = paraMap.get("x");
      int y = paraMap.get("y");
      Move newMove =  new Move(curPlayer, x, y);
      Message message = board.validMove(newMove);
     
      // update ui when valid
      if (message.getValid()) {
        board.makeMove(newMove);
        sendGameBoardToAllPlayers(new Gson().toJson(board));
      }
      
      ctx.result(new Gson().toJson(message));
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
