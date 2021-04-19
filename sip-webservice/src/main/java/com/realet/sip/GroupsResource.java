package com.realet.sip;

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
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;
import com.realet.sip.GsonTypeAdapter.GroupAdapter;
import com.realet.sip.GsonTypeAdapter.PermissionAdapter;
import com.realet.sip.GsonTypeAdapter.RoleAdapter;
import com.realet.sip.GsonTypeAdapter.UserAdapter;

import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


@Path("/groups")
public class GroupsResource {

    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroup(@PathParam("groupId") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        } 

        Optional<Group> group = GroupsFacade.findById(groupId);
        if(group.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> user = UsersFacade.findById(tokenUserId);
        if(!group.get().getUsers().contains(user.get())){
            return Response.status(403).entity("Unauthorized").build();   
        }

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Group.class, new GroupAdapter()).create()
            .toJson(group.get())
        ).build();

    }
    
    @GET
    @Path("/{groupId}/chats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChats(@PathParam("groupId") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        } 

        Optional<Group> group = GroupsFacade.findById(groupId);
        if(group.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> user = UsersFacade.findById(tokenUserId);
        if(!group.get().getUsers().contains(user.get())){
            return Response.status(403).entity("Unauthorized").build();   
        }

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create()
            .toJson(group.get().getChats())
        ).build();
    }

    @GET
    @Path("/{groupId}/basic-users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@PathParam("groupId") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

        Optional<Group> group = GroupsFacade.findById(groupId);
        if(group.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> user = UsersFacade.findById(tokenUserId);
        if(!group.get().getUsers().contains(user.get())){
            return Response.status(403).entity("Unauthorized").build();   
        }

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(UsersFacade.findBasicGroupMembers(groupId))
        ).build();
    }

    @GET
    @Path("/{groupId}/chats/{chatId}/permissions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatPermissions(@PathParam("groupId") long groupId, @PathParam("chatId") long chatId, @QueryParam("user") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        } 
        
        if(userId != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        Optional<Permission> permission = PermissionsFacade.findGroupChatPermissions(chatId, groupId, userId);

        if(permission.isEmpty()){
            return Response.noContent().entity("No corresponding entries found. (This might mean that there is something wrong with your query or that this user isn't part of any roles.").build();
        }

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Permission.class, new PermissionAdapter()).create()
            .toJson(permission.get())
        ).build();

    }

    @GET
    @Path("/{groupId}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(@PathParam("groupId") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        } 

        Optional<User> user = UsersFacade.findById(tokenUserId);
        Optional<Group> group = GroupsFacade.findById(groupId);

        if(group.isEmpty()){
            return Response.status(404).build();
        }

        if(!group.get().getUsers().contains(user.get())){
            return Response.status(403).entity("Unauthorized").build();
        }

        List<Role> roles = RolesFacade.findGroupRolesOrderedByPriority(group.get());

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Role.class, new RoleAdapter(1)).create()
            .toJson(roles)
        ).build();
    }

    @POST
    public Response createGroup(Group group, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try{
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        }
        catch(IllegalAccessException e){
            return Response.status(403).entity("Unauthenticated").build();
        }

        if(group.getName() == null){
            return Response.status(400).entity("Group must have name").build();
        }

        //bit of sanitization (and some more validation)
        //this pattern just strips whitespaces at the start/end of string
        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);

        Matcher m = p.matcher(group.getName());

        if(m.find()){
            group.setName(m.group(0));
        }
        else{
            return Response.status(400).entity("Name cannot entirely consist of whitespaces").build();
        }

        User user = UsersFacade.findById(tokenUserId).get();

        group.setOwner(user);


        //no, this doesn't allow us to change other users passwords. That will just fail because it's not possible to persist non-managed entities without setting a cascadetype. I think.
        group.getUsers().add(user);
        GroupsFacade.add(group);

        ChatsFacade.add(new Chat(group, null, null, "text-chat"));

        return Response.status(201).entity(new JSONObject().put("id", group.getId()).toString()).build();
    }

    @POST
    @Path("/{groupId}/chats/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateChat(@PathParam("groupId") long groupId, Chat inputChat, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }
        token = token.split(" ")[1];

        long tokenUserId;
        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
        }

        if(inputChat.getName() == null){
            return Response.status(400).entity("Chat must have name").build();
        }

        //bit of sanitization (and some more validation)
        //this pattern just strips whitespaces at the start/end of string
        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);

        Matcher m = p.matcher(inputChat.getName());

        if(m.find()){
            inputChat.setName(m.group(0));
        }
        else{
            return Response.status(400).entity("Name cannot entirely consist of whitespaces").build();
        }

        Optional<Group> group = GroupsFacade.findById(groupId);
        if(group.isEmpty()){
            return Response.status(404).build();
        }
        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), group.get()).isEmpty() && group.get().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        //just make a group chat anyways
        inputChat.setGroup(group.get());
        inputChat.setUser1(null);
        inputChat.setUser2(null);
        ChatsFacade.add(inputChat);

        return Response.status(201).build();
    }
}
