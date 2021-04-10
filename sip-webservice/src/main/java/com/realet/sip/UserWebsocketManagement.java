package com.realet.sip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class UserWebsocketManagement {

    private static HashMap<Long, ArrayList<javax.websocket.Session>> sessions = new HashMap<>();

    public static ArrayList<javax.websocket.Session> getSessions(long userId){
        return sessions.get(Long.valueOf(userId));
    }

    public static void addSession(long userId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> list = sessions.get(Long.valueOf(userId));
        if(list == null){
            list = new ArrayList<>();
            list.add(session);
            sessions.put(Long.valueOf(userId), list);
            Optional<User> user = UsersFacade.findById(userId);
            user.get().setOnline(true);
            UsersFacade.update(user.get());
        }
        else{
            list.add(session);
        }
    }

    public static void removeSession(long userId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> list = sessions.get(Long.valueOf(userId));

        //nullpointerexception but who cares
        list.remove(session);

        if(list.isEmpty()){
            sessions.remove(Long.valueOf(userId));
            Optional<User> user = UsersFacade.findById(userId);
            user.get().setOnline(false);
            UsersFacade.update(user.get());
        }
    }
    
}
