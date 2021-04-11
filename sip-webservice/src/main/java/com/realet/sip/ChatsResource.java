package com.realet.sip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

        if(user1Id == user2Id){
            return Response.status(400).build();
        }

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
    public Response getChatMessages(@PathParam("chatId") long chatId, @QueryParam("count") int count, @QueryParam("before") long beforeId, @QueryParam("after") long afterId, @QueryParam("authorUnblockedBy") long userId, @QueryParam("reverseBlocking") long reverseBlocking, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

        if(count > 1000 || count < 1){
            return Response.status(400).entity("Count must be between 1 and 1000").build();
        }

        if(afterId != 0 && beforeId != 0){
            return Response.status(400).entity("Can't use both \"before\" and \"after\" filters in one request.").build();
        }

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        Optional<Chat> chat = ChatsFacade.findById(chatId);

        if(chat.isEmpty()){
            return Response.status(404).entity("Chat not found").build();
        }

        try {
            long user_id = SessionsFacade.findUserIdByToken(token);

            if(userId != user_id && userId != 0){
                return Response.status(403).entity("Unauthorized").build();
            }

            //if this is a direct chat, check access
            if(chat.get().getGroup() == null){
                if(user_id != chat.get().getUser1().getId() &&
                user_id != chat.get().getUser2().getId()){
                    return Response.status(403).entity("Unauthorized").build();
                }
            }
            //if this is a group chat, check access (membership)
            //FIXME: IMPLEMENT ROLE PERMISSION CHECKS
            else{
                if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(user_id).get())){
                    return Response.status(403).entity("Unauthorized").build();
                }
            }


        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }


        //we have ensured access
        if(beforeId != 0){
            Optional<ChatMessage> beforeMessage = ChatMessagesFacade.findById(beforeId);
            if(beforeMessage.isEmpty()){
                return Response.status(404).entity("Limiter not found").build();
            }
        }

        if(afterId != 0){
            Optional<ChatMessage> afterMessage = ChatMessagesFacade.findById(afterId);
            if(afterMessage.isEmpty()){
                return Response.status(404).entity("Limiter not found").build();
            }
        }

        List<ChatMessage> messages = ChatMessagesFacade.find(chat.get(), count, beforeId, afterId, userId, reverseBlocking);

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter(1)).create()
            .toJson(messages)
        ).build();
    }

    @POST
    @Path("/{chatId}/chat-messages")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addChatMessage(ChatMessage chatMessage, @PathParam("chatId") long chatId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(chatMessage.getContent().equals("")){
            return Response.status(400).build();
        }

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

        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            return Response.status(404).build();
        }

        if(chat.get().getGroup() ==  null){
            if(chat.get().getUser1().getId() != tokenUserId && chat.get().getUser2().getId() != tokenUserId){
                return Response.status(403).entity("Unauthorized").build();
                                
            }
        }
        //FIXME: IMPLEMENT ROLE PERMISSION CHECKS
        else{
            if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(tokenUserId).get())){
                return Response.status(403).entity("Unauthorized").build();               
            }
        }

        if(chatMessage.getContent() == null){
            return Response.status(400).entity("Message must contain text").build();
        }

        //bit of sanitization (and some more validation)
        //this pattern just strips whitespaces at the start/end of string
        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);

        Matcher m = p.matcher(chatMessage.getContent());

        if(m.find()){
            chatMessage.setContent(m.group(0));
        }
        else{
            return Response.status(400).entity("Message cannot entirely consist of whitespaces").build();
        }

        chatMessage.setAuthor(UsersFacade.findById(tokenUserId).get());

        chatMessage.setChat(chat.get());
        
        chatMessage.setSent(new Date());

        ChatMessagesFacade.add(chatMessage);

        if(chat.get().getGroup() == null){
            ArrayList<javax.websocket.Session> list = UserWebsocketManagement.getSessions(chat.get().getUser1().getId());
            if(!(list == null || list.isEmpty())){
                for(javax.websocket.Session s: list){
                    try {
                        s.getBasicRemote().sendText("new: " + 
                            new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter(2)).create()
                            .toJson(chatMessage)
                        );
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }  
            }

            list = UserWebsocketManagement.getSessions(chat.get().getUser2().getId());
            if(!(list == null || list.isEmpty())){
                for(javax.websocket.Session s: list){
                    try {
                        s.getBasicRemote().sendText("new: " + 
                            new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter(2)).create()
                            .toJson(chatMessage)
                        );
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }  
            }
        }
        else{
            //FIXME: IMPLEMENT GROUP PUSH
        }


        
        
        return Response.status(201).build();

    }

}
