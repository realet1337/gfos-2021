package com.realet.sip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;
import com.realet.sip.GsonTypeAdapter.GroupAdapter;
import com.realet.sip.GsonTypeAdapter.UserAdapter;

@Path("/users")
public class UsersResource {

    @GET
    @Path("/{id}")    
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@PathParam("id") long id) {
        Optional<User> user = UsersFacade.findById(id);
        if (user.isPresent()){

            return Response.ok( new Gson().toJson(user.get()) ).build();
            
        }
        else{

            return Response.status(404).build();

        }
    }
    
    @GET
    @Path("/{userId}/direct-chats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDirectChats(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];
        try {
            if( SessionsFacade.findUserIdByToken(token) == userId ){
                Optional<User> user = UsersFacade.findById(userId);
                if(user.isPresent()){
                    List<Chat> directChats = ChatsFacade.findDirectChatsByUser(user.get());
                    return Response.ok(new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create().toJson(directChats)).build();
                }else{
                    return Response.status(404).build();
                }
            }else{
                return Response.status(403).entity("Unauthorized").build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

    }

    @GET
    @Path("/{userId}/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroups(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).build();
        }

        token = token.split(" ")[1];

        try {
            if(SessionsFacade.findUserIdByToken(token) != userId){
                return Response.status(403).build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).build();
        }

        //if this was empty the token validation would have failed
        User user = UsersFacade.findById(userId).get();

        Set<Group> groups = user.getGroups();

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Group.class, new GroupAdapter()).create()
            .toJson(groups)
        ).build();

    }

    @GET
    @Path("/{userId}/blocked-users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlockedUsers(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        try {
            if(SessionsFacade.findUserIdByToken(token) != userId){
                return Response.status(403).entity("Unauthorized").build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(user.get().getBlockedUsers())
        ).build();

    }

    @POST
    @Path("/{userId}/blocked-users")
    public Response addBlockedUser(@PathParam("userId") long userId, User input, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        try {
            if(SessionsFacade.findUserIdByToken(token) != userId){
                return Response.status(403).entity("Unauthorized").build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> toBlock = UsersFacade.findById(input.getId());
        if(toBlock.isEmpty()){
            return Response.status(404).build();
        }

        user.get().getBlockedUsers().add(toBlock.get());

        UsersFacade.update(user.get());

        ArrayList<javax.websocket.Session> list = UserWebsocketManagement.getSessions(user.get().getId());
        if(!(list == null || list.isEmpty())){
            for(javax.websocket.Session s: list){
                try {
                    s.getBasicRemote().sendText("blocked: " + 
                        new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
                        .toJson(toBlock.get())
                    );
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Response.ok().build();

    }

    @DELETE
    @Path("/{userId}/blocked-users/{blockedUserId}")
    public Response removeBlockedUser(@PathParam("userId") long userId, @PathParam("blockedUserId") long blockedUserId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        try {
            if(SessionsFacade.findUserIdByToken(token) != userId){
                return Response.status(403).entity("Unauthorized").build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> blockedUser = UsersFacade.findById(blockedUserId);
        if(blockedUser.isEmpty()){
            return Response.status(404).build();
        }

        if(!user.get().getBlockedUsers().remove(blockedUser.get())){
            return Response.status(404).build();
        }

        UsersFacade.update(user.get());

        ArrayList<javax.websocket.Session> list = UserWebsocketManagement.getSessions(user.get().getId());
        if(!(list == null || list.isEmpty())){
            for(javax.websocket.Session s: list){
                try {
                    s.getBasicRemote().sendText("unblocked: " + 
                        new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
                        .toJson(blockedUser.get())
                    );
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Response.ok().build();

    }

    @GET
    @Path("/{userId}/blocked-by")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlockedBy(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        try {
            if(SessionsFacade.findUserIdByToken(token) != userId){
                return Response.status(403).entity("Unauthorized").build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(user.get().getBlockedBy())
        ).build();

    }

}
