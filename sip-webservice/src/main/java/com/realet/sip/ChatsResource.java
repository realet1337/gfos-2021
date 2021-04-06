package com.realet.sip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;
import com.realet.sip.GsonTypeAdapter.ChatMessageAdapter;

@Path("/chats")
public class ChatsResource {

    @GET
    @Path("/{id}")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChat(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {
        //TODO: (LATER) REMOVE THIS METHOD IT ISN'T NECESSARY

        if(token == null){
            return Response.status(403).build();
        }

        token = token.split(" ")[1];

        Optional<Chat> chat = ChatsFacade.findById(id);
        if(chat.isPresent()){

            //validate access
            try {
                long user_id = SessionsFacade.findUserIdByToken(token);
                if(user_id == chat.get().getUser1().getId() || 
                    user_id == chat.get().getUser2().getId()){
                    return Response.ok(new Gson().toJson(chat.get())).build();
                }else{

                    return Response.status(403).build();

                }
            } catch (IllegalAccessException e) {

                return Response.status(403).build();
            }

        }

        return Response.status(404).build();
        
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChats(@QueryParam("user1") long user1Id, @QueryParam("user2") long user2Id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            return Response.status(403).build();
        }

        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).build();
        } 

        if(tokenUserId != user1Id && tokenUserId != user2Id){

            return Response.status(403).build();

        }

        Optional<User> user1 = UsersFacade.findById(user1Id);
        Optional<User> user2 = UsersFacade.findById(user2Id);

        if(user1.isEmpty() || user2.isEmpty()){
            return Response.status(404).build();
        }

        Optional<Chat> chat = ChatsFacade.findByUsers(user1.get(), user2.get());

        if(chat.isPresent()){
            return Response.ok(
                new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create().toJson(chat.get())
            ).build();
        }

        Chat newChat = new Chat(null, user1.get(), user2.get());
        ChatsFacade.add(newChat);


        //just act like it was always there
        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create().toJson(newChat)
        ).build();


    }

    @GET
    @Path("/{chatId}/chat-messages")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatMessages(@PathParam("chatId") long chatId, @QueryParam("count") int count, @QueryParam("before") long beforeId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

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
            return Response.status(403).build();
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

    @POST
    @Path("/{chatId}/chat-messages")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addChatMessage(ChatMessage chatMessage, @PathParam("chatId") long chatId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

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

        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            return Response.status(404).build();
        }

        if(chat.get().getGroup() ==  null){
            if(chat.get().getUser1().getId() != tokenUserId && chat.get().getUser2().getId() != tokenUserId){
                return Response.status(403).build();
                                
            }
        }
        //FIXME: IMPLEMENT ROLE PERMISSION CHECKS
        else{
            if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(tokenUserId).get())){
                return Response.status(403).build();               
            }
        }

        chatMessage.setAuthor(UsersFacade.findById(tokenUserId).get());

        chatMessage.setChat(chat.get());
        
        chatMessage.setSent(new Date());

        ChatMessagesFacade.add(chatMessage);

        ArrayList<AsyncResponse> responses = PollingResource.getResponses(chatId);
        if(responses == null){
            return Response.status(201).build();
        }
        
        while(!responses.isEmpty()){
            AsyncResponse response = responses.get(0);
            
            /*
            READ BEFORE YOU JUDGE
            ---------------------
            The reason this is done in this way (returning added ID) instead of just returning the message is so that
            the client doesn't "miss" any messages. If two messages are sent very quickly after one another, it would be
            possible for the second message to reach the server and be added to the database before the client reestablishes
            the polling connection. The client would therefore not know that this second message arrived. Bad.
            
            Here's a visualization:

            -->message 1 reaches server
            -->message 1 persisted
            -->polling returns message 1
            -->message 2 reaches server
            -->message 2 is persisted
            -->polling connection is reestablished
            -->whoops
            */

            response.resume(Response.ok(chatMessage.getId()).build());
            responses.remove(response); 
        }

        return Response.status(201).build();

    }

}
