package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;


@Path("/chat-messages")
public class ChatMessagesResource {

    @GET
    @Path("/most-recent-by-chat/{chatId}")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMostRecentByChat(@PathParam("chatId") long chatId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

        token = token.split(" ")[1];
        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isPresent()){
            try {
                long user_id = SessionsFacade.findUserIdByToken(token);
                if(user_id == chat.get().getUser1().getId() || 
                user_id == chat.get().getUser2().getId()){
                    
                    Optional<ChatMessage> chatMessage = ChatMessagesFacade.findMostRecentByChat(chat.get());
                    if(chatMessage.isPresent()){
                        return Response.ok(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation().create()
                        .toJson(chatMessage.get())).build();
                    }else{
                        return Response.status(404).build();
                    }
                    
                }else{

                    return Response.status(403).build();

                }
            } catch (IllegalAccessException e) {

                return Response.status(403).build();
            }
        }else{
            return Response.status(404).build();
        }
    }
}
