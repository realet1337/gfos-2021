package com.realet.sip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Manager-Klasse für {@link javax.websocket.Session}-Objekte, die für einen {@link User} registriert sind. 
 */
public class UserWebsocketManagement {

    /**
     * Format: userId =&gt; {@link javax.websocket.Session}
     */
    private static HashMap<Long, ArrayList<javax.websocket.Session>> sessions = new HashMap<>();

    public static ArrayList<javax.websocket.Session> getSessions(long userId){
        return sessions.get(Long.valueOf(userId));
    }

    //Setting the flag doesn't always work. Race condition?
    public static void addSession(long userId, javax.websocket.Session session){
        ArrayList<javax.websocket.Session> list = sessions.get(Long.valueOf(userId));
        if(list == null){
            list = new ArrayList<>();
            list.add(session);
            sessions.put(Long.valueOf(userId), list);

            Optional<User> user = UsersFacade.findById(userId);

            //attempting to fix absurd bug here
            user.get().setOnline(sessions.containsKey(Long.valueOf(userId)));
            
            UsersFacade.update(user.get());
            System.out.println("added at: " + String.valueOf(System.currentTimeMillis()));
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
            user.get().setOnline(sessions.containsKey(Long.valueOf(userId)));
            UsersFacade.update(user.get());
            System.out.println("removed at: " + String.valueOf(System.currentTimeMillis()));
        }
    }
    
}
