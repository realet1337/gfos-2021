package com.realet.sip;

import java.io.IOException;
import java.util.Optional;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Diese Klasse akzeptiert WebSockets-Verbindungen nach "/users/{userId}/websockets". 
 * Durch diese wird übermittelt, wenn eine neue Nachricht, welche für diesen Nutzer lesbar ist gesendet wurde, sowie wenn ein Nutzer blockiert wurde oder eine Blockade aufgehoben wurde.
 * <br>Dies geschieht in folgendem Format:<br>
 * Neuer Nutzer blockiert: "blocked: &lt;{@link User} als JSON&gt;" <br>
 * Blockade aufgehoben: "unblocked: &lt;{@link User} als JSON&gt;" <br>
 * Neue Nachricht: "new-message: &lt;{@link ChatMessage} als JSON&gt;"
 * 
 */
@ServerEndpoint("/users/{userId}/websockets")
public class UserWebsocketResource {

    /**
     * Ueberprueft, ob der jeweilige {@link User} existiert und sendet andernfalls eine entsprechende Nachricht, woraufhin die Verbindung geschlossen wird.
     */
    @OnOpen
    public void onOpen(javax.websocket.Session session, @PathParam("userId") long userId){
        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            try {
                session.getBasicRemote().sendText("This user doesn't exist.");
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ueberprueft Gueltigkeit und Vorrechte einer im Format "Bearer &lt;token&gt;" übermittelten {@link Session} und registriert die WebSockets-Session im {@link UserWebsocketManagement}.
     * Sendet gegebenenfalls eine Fehlermeldung zurück.
     */
    @OnMessage
    public void OnMessage(javax.websocket.Session session, String message, @PathParam("userId") long userId){
        if(!message.startsWith("Bearer ") || message.length() < 8){
            try {
                session.getBasicRemote().sendText("Use: Bearer <token>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        message = message.split(" ")[1];
        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(message);
        } catch (IllegalAccessException e) {
            try {
                session.getBasicRemote().sendText("This session doesn't exist.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }

        if(userId != tokenUserId){
            try {
                session.getBasicRemote().sendText("You're not authorized to watch this user.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        UserWebsocketManagement.addSession(userId, session);
        try {
            session.getBasicRemote().sendText("Subscribed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entfernt die WebSockets-Session aus dem {@link UserWebsocketManagement}.
     */
    @OnClose
    public void onClose(javax.websocket.Session session, CloseReason closeReason, @PathParam("userId") long userId){
        UserWebsocketManagement.removeSession(userId, session);
    }

    /**
     * Entfernt die WebSockets-Session aus dem {@link UserWebsocketManagement}.
     */
    @OnError
    public void onError(javax.websocket.Session session, Throwable throwable, @PathParam("userId") long userId){
        UserWebsocketManagement.removeSession(userId, session);
    }
}
