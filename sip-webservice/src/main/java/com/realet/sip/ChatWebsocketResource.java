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

@ServerEndpoint("/chats/{chatId}/watch")
public class ChatWebsocketResource {

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
        //FIXME: IMPLEMENT ROLE PERMISSION CHECKS
        else{
            if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(tokenUserId).get())){
                try {
                    session.getBasicRemote().sendText("This session doesn't have access to this chat.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        session.getUserProperties().put("userId", Long.valueOf(tokenUserId));
        ChatWebsocketManagement.addSession(chatId, tokenUserId, session);
        try {
            session.getBasicRemote().sendText("Subscribed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(javax.websocket.Session session, CloseReason closeReason, @PathParam("chatId") long chatId){
        ChatWebsocketManagement.removeSession(chatId, session);
    }

    //we outttt
    @OnError
    public void onClose(javax.websocket.Session session, Throwable throwable, @PathParam("chatId") long chatId){
        ChatWebsocketManagement.removeSession(chatId, session);
    }

    //no need for message handler, the client isn't supposed to send any messages
    
}
