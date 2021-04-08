package com.realet.sip;

import java.util.Optional;

import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;



@Path("/chat-messages")
public class ChatMessagesResource {

    @DELETE
    @Path("/{id}")
    public Response removeMessage(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).build();
        }

        token = token.split(" ")[1];

        long tokenUserId = 0;

        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).build();  
        }

        Optional<ChatMessage> chatMessage = ChatMessagesFacade.findById(id);
        
        if(chatMessage.isEmpty()){
            return Response.status(404).build();
        }

        if(chatMessage.get().getAuthor().getId() != tokenUserId){
            return Response.status(403).build();
        }

        String chatMessageJSON = new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter())

        ChatMessagesFacade.remove(chatMessage.get());

        return Response.ok().build();

    }
}
