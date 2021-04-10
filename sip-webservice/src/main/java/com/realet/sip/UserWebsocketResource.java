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

import com.google.protobuf.Option;

@ServerEndpoint("/users/{userId}/websockets")
public class UserWebsocketResource {

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

    @OnClose
    public void onClose(javax.websocket.Session session, CloseReason closeReason, @PathParam("userId") long userId){
        UserWebsocketManagement.removeSession(userId, session);
    }

    //we outttt
    @OnError
    public void onClose(javax.websocket.Session session, Throwable throwable, @PathParam("userId") long userId){
        UserWebsocketManagement.removeSession(userId, session);
    }
}
