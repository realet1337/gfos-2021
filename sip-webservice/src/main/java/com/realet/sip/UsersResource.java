package com.realet.sip;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
