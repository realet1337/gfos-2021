package com.realet.sip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatMessageAdapter;



@Path("/chat-messages")
public class ChatMessagesResource {

    @DELETE
    @Path("/{id}")
    public Response removeMessage(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        long tokenUserId = 0;

        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();  
        }

        Optional<ChatMessage> chatMessage = ChatMessagesFacade.findById(id);
        
        if(chatMessage.isEmpty()){
            return Response.status(404).entity("Chat not found").build();
        }

        if(chatMessage.get().getAuthor().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        String chatMessageJSON = new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter()).create()
        .toJson(chatMessage.get());

        long chatId = chatMessage.get().getChat().getId();

        ChatMessagesFacade.remove(chatMessage.get());

        ArrayList<javax.websocket.Session> list = ChatWatcherManagement.getSessions(chatId);

        if(!(list == null || list.isEmpty())){
            for(javax.websocket.Session s: list){
                try {
                    s.getBasicRemote().sendText("removed: " + chatMessageJSON );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        }

        return Response.ok().build();

    }
}
