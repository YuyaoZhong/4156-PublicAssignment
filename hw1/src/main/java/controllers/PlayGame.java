package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    BoardController boardController = new BoardController();
    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });
    
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });
    
    // Player 1: start game
    app.post("/startgame", ctx -> {
      boardController.resetBoard();
      String paras = ctx.body();
      // Get the type chosen by player 1
      char type1 = paras.charAt(paras.length() - 1);
      char type2 = (type1 == 'X') ? 'O' : 'X';
      Player p1 = new Player(type1, 1);
      Player p2 = new Player(type2, 2);
      boardController.getBoard().setP1(p1);
      boardController.getBoard().setP2(p2);
      String boardJson = boardController.getBoardJsonString();
      ctx.result(boardJson);
    });
    
    // Player 2: join game
    app.get("/joingame", ctx -> {
      boardController.getBoard().setGameStarted(true);
      String boardJson = boardController.getBoardJsonString();
      sendGameBoardToAllPlayers(boardJson);
      ctx.redirect("/tictactoe.html?p=2");
    });
    
    // Make move
    app.post("/move/:playerId", ctx -> {
      int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      Player curPlayer = boardController.getBoard().getPlayer(playerId);
      HashMap<String, String> paraMap = getParametersMap(ctx.body());
      int x = Integer.parseInt(paraMap.get("x"));
      int y = Integer.parseInt(paraMap.get("y"));
      Move newMove =  new Move(curPlayer, x, y);
      Message message = boardController.validMove(newMove);
     
      // update UI when valid
      if (message.getMoveValidity()) {
        boardController.makeMove(newMove);
        String boardJson = boardController.getBoardJsonString();
        sendGameBoardToAllPlayers(boardJson);
      }
      
      ctx.result(new Gson().toJson(message));
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /**
   * A general way to parse body as Json string.
   * @param paraJson parameters in Json string format, not null
   * @return
   */
  private static HashMap<String, String> getParametersMap(String paraJson) {
    String[] paras = paraJson.split("&");
    HashMap<String, String> paraMap = new HashMap<String, String>();
    for (int i = 0; i < paras.length; i++) {
      String[] attrs = paras[i].split("=");
      paraMap.put(attrs[0], attrs[1]);
    }
    return paraMap;
  }
  
  /** Send message to all players.
   * @param gameBoardJson GameBoard JSON
   * @throws IOException WebSocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    final Logger logger = LoggerFactory.getLogger(PlayGame.class);
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
        logger.error(e.toString());
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
