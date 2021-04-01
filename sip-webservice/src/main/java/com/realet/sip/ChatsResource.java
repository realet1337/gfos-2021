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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;

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
    @Path("/directchats-by-user/{userId}")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDirectChatsByUser(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

        if(token == null){
            return Response.status(403).build();
        }

        token = token.split(" ")[1];
        try {
            if( SessionsFacade.findUserIdByToken(token) == userId ){
                Optional<User> user = UsersFacade.findById(userId);
                if(user.isPresent()){
                    List<Chat> directChats = ChatsFacade.findDirectChatsByUser(user.get());
                    return Response.ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(directChats)).build();
                }else{
                    return Response.status(404).build();
                }
            }else{
                return Response.status(403).build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).build();
        }

    }

    @GET
    @Path("/directchat-by-users/{user1Id}/{user2Id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDirectChatByUsers(@PathParam("user1Id") long user1Id, @PathParam("user2Id") long user2Id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

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

}
