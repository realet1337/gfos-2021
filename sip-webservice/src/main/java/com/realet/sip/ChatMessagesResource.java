package com.realet.sip;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatMessageAdapter;


@Path("/chat-messages")
public class ChatMessagesResource {

    @GET
    @Path("")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatMessages(@QueryParam("chat") long chatId, @QueryParam("count") int count, @QueryParam("before") long beforeId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

        if(count > 1000 || count < 1){
            return Response.status(400).entity("Count must be between 1 and 1000").build();
        }

        if(token == null){
            return Response.status(403).build();
        }

        token = token.split(" ")[1];

        Optional<Chat> chat = ChatsFacade.findById(chatId);

        if(chat.isEmpty()){
            return Response.status(404).build();
        }

        try {
            long user_id = SessionsFacade.findUserIdByToken(token);

            //if this is a direct chat, check access
            if(chat.get().getGroup() == null){
                if(user_id != chat.get().getUser1().getId() &&
                user_id != chat.get().getUser2().getId()){
                    return Response.status(403).build();
                }
            }
            //if this is a group chat, check access (membership)
            //FIXME: IMPLEMENT ROLE PERMISSION CHECKS
            else{
                if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(user_id).get())){
                    return Response.status(403).build();
                }
            }


        } catch (IllegalAccessException e) {
            Response.status(403).build();
        }

        //we have ensured access

        Date before = null;

        Optional<ChatMessage> beforeMessage = ChatMessagesFacade.findById(beforeId);
        if(beforeMessage.isPresent()){
            before = beforeMessage.get().getSent();
        }

        List<ChatMessage> messages = ChatMessagesFacade.find(chat.get(), count, before);

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter()).create()
            .toJson(messages)
        ).build();
    }
}
