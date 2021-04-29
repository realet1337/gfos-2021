package com.realet.sip;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manager-Klasse für {@link javax.websocket.Session}-Objekte, die für einen {@link Chat} registriert sind. 
 */
public class ChatWebsocketManagement {

    /**
     * Format: chatId =&gt; {@link javax.websocket.Session}.
     */
    static HashMap<Long, ArrayList<javax.websocket.Session>> sessions = new HashMap<>();

    public static ArrayList<javax.websocket.Session> getSessions(long chatId){
        return sessions.get(Long.valueOf(chatId));
    }

    public static void addSession(long chatId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> list = sessions.get(Long.valueOf(chatId));
        if(list == null){
            list = new ArrayList<>();
            list.add(session);
            sessions.put(Long.valueOf(chatId), list);
        }
        else{
            list.add(session);
        }
    }

    public static void removeSession(long chatId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> list = sessions.get(Long.valueOf(chatId));

        //nullpointerexception but who cares
        list.remove(session);

        if(list.isEmpty()){
            sessions.remove(Long.valueOf(chatId));
        }
    }
    
}
