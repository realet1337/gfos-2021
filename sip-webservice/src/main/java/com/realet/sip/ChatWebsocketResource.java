package com.realet.sip;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Diese Klasse akzeptiert WebSockets-Verbindungen nach "/chats/{chatId}/websockets". 
 * Diese werden benutzt, um live Ergeignisse aus einen bestimmten Chat an den Client zu übermitteln.
 * Das schliesst neue Nachrichten aus, da diese durch die {@link UserWebsocketResource} gesendet werden.
 * <br>Dies geschieht in folgendem Format:<br>
 * Nachricht geändert: "updated: &lt;{@link ChatMessage} als JSON&gt;"
 * Nachricht geändert: "removed: &lt;{@link ChatMessage} als JSON&gt;"
 */
@ServerEndpoint("/chats/{chatId}/websockets")
public class ChatWebsocketResource {

    /**
     * Ueberprueft, ob der jeweilige {@link Chat} existiert und sendet andernfalls eine entsprechende Nachricht, woraufhin die Verbindung geschlossen wird.
     */
    @OnOpen
    public void onOpen(javax.websocket.Session session, @PathParam("chatId") long chatId){
        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            try {
                session.getBasicRemote().sendText("This chat doesn't exist.");
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ueberprueft Gueltigkeit und Vorrechte einer im Format "Bearer &lt;token&gt;" übermittelten {@link Session} und registriert die WebSockets-Session im {@link ChatWebsocketManagement}.
     * Sendet gegebenenfalls eine Fehlermeldung zurück.
     */
    @OnMessage
    public void OnMessage(javax.websocket.Session session, String message, @PathParam("chatId") long chatId){
        if(!message.startsWith("Bearer ") || message.length() < 8){
            try {
                session.getBasicRemote().sendText("This message doesn't follow sip websocket authentication protocol.");
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
        
        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if(chat.get().getGroup() ==  null){
            if(chat.get().getUser1().getId() != tokenUserId && chat.get().getUser2().getId() != tokenUserId){
                try {
                    session.getBasicRemote().sendText("This session doesn't have access to this chat.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        else{
            if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(tokenUserId).get())){
                try {
                    session.getBasicRemote().sendText("This session doesn't have access to this chat.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            List<Permission> permissions = PermissionsFacade.findGroupChatPermissions(chatId, tokenUserId);

            boolean canRead = false;

            for(Permission p: permissions){
                canRead = canRead || p.isCanRead();
            }


            if(!canRead){
                try {
                    session.getBasicRemote().sendText("Insufficient permissions.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        ChatWebsocketManagement.addSession(chatId, session);
        try {
            session.getBasicRemote().sendText("Subscribed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entfernt die WebSockets-Session aus dem {@link ChatWebsocketManagement}.
     */
    @OnClose
    public void onClose(javax.websocket.Session session, CloseReason closeReason, @PathParam("chatId") long chatId){
        ChatWebsocketManagement.removeSession(chatId, session);
    }

    /**
     * Entfernt die WebSockets-Session aus dem {@link ChatWebsocketManagement}.
     */
    @OnError
    public void onError(javax.websocket.Session session, Throwable throwable, @PathParam("chatId") long chatId){
        ChatWebsocketManagement.removeSession(chatId, session);
    }
}
