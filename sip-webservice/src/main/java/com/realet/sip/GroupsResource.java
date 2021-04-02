package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.GroupAdapter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


@Path("/groups")
public class GroupsResource {
    
    @GET
    @Path("/shared-groups/{user1Id}/{user2Id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSharedGroups(@PathParam("user1Id") long user1Id, @PathParam("user2Id") long user2Id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            return Response.status(403).build();
        }
        token = token.split(" ")[1];

        if(user1Id == user2Id){
            return Response.status(400).build();
        }

        Optional<User> user1 = UsersFacade.findById(user1Id);
        Optional<User> user2 = UsersFacade.findById(user2Id);

        if(user1.isEmpty() || user2.isEmpty()){
            return Response.status(404).build();
        }

        
        try {
            long reqUserId;
            reqUserId = SessionsFacade.findUserIdByToken(token);

            if(user1Id != reqUserId && user2Id != reqUserId){
                return Response.status(403).build();
            }
        } catch (IllegalAccessException e) {
            return Response.status(403).build();
        }

        List<Group> groups = GroupsFacade.findShared(user1.get(), user2.get());

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Group.class, new GroupAdapter()).create().toJson(groups)
        ).build();
        
    }
    
}
