package com.realet.sip;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chats/{chatId}/watch")
public class ChatWatcherResource {

    @OnOpen
    public void onOpen(javax.websocket.Session session/*, @PathParam("chatId") long chatId*/){
        //FIXME: IMPLEMENT AUTHENTICATION
        ChatWatcherManagement.addSession(chatId, session);
    }

    @OnClose
    public void onClose(javax.websocket.Session session, CloseReason closeReason, @PathParam("chatId") long chatId){
        ChatWatcherManagement.removeSession(chatId, session);
    }

    //no need for message handler, the client isn't supposed to send any messages
    
}
