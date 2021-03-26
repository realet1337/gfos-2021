package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

@Path("/chats")
public class ChatsResource {

    @GET
    @Path("/{id}")    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChat(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {
        
        //TODO: (LATER) REMOVE THIS METHOD IT ISN'T NECESSARY

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

        token = token.split(" ")[1];
        try {
            if( SessionsFacade.findUserIdByToken(token) == userId ){
                Optional<User> user = UsersFacade.findById(userId);
                if(user.isPresent()){
                    List<Chat> directChats = ChatsFacade.findDirectChatByUser(user.get());
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

}
