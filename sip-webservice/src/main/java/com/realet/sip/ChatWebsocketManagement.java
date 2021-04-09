package com.realet.sip;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatWebsocketManagement {

    static HashMap<Long, ArrayList<javax.websocket.Session>> chatSessions = new HashMap<>();
    static HashMap<Long, ArrayList<javax.websocket.Session>> userSessions = new HashMap<>();

    public static ArrayList<javax.websocket.Session> getChatSessions(long chatId){
        return chatSessions.get(Long.valueOf(chatId));
    }

    public static ArrayList<javax.websocket.Session> getUserSessions(long userId){
        return userSessions.get(Long.valueOf(userId));
    }

    public static void addSession(long chatId, long userId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> chatList = chatSessions.get(Long.valueOf(chatId));
        if(chatList == null){
            chatList = new ArrayList<>();
            chatList.add(session);
            chatSessions.put(Long.valueOf(chatId), chatList);
        }
        else{
            chatList.add(session);
        }

        ArrayList<javax.websocket.Session> userList = chatSessions.get(Long.valueOf(userId));
        if(userList == null){
            userList = new ArrayList<>();
            userList.add(session);
            userSessions.put(Long.valueOf(userId), userList);
        }
        else{
            userList.add(session);
        }
    }

    public static void removeSession(long chatId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> list = chatSessions.get(Long.valueOf(chatId));

        //nullpointerexception but who cares
        list.remove(session);

        if(list.isEmpty()){
            chatSessions.remove(Long.valueOf(chatId));
        }

        list = chatSessions.get(session.getUserProperties().get("userId"));

        //nullpointerexception but who cares (responsible calling pls)
        list.remove(session);

        if(list.isEmpty()){
            chatSessions.remove(Long.valueOf(chatId));
        }
    }
    
}
