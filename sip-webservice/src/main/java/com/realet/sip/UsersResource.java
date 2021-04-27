package com.realet.sip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;
import com.realet.sip.GsonTypeAdapter.GroupAdapter;
import com.realet.sip.GsonTypeAdapter.RoleAdapter;
import com.realet.sip.GsonTypeAdapter.UserAdapter;
import com.realet.sip.GsonTypeAdapter.UserProfileAdapter;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * RestEasy Resource-Klasse für {@link User Users}.
 */
@Path("/users")
public class UsersResource {

    /***
     * Findet einen {@link User}
     * @param id
     * @return Status Code 200 mit {@link User}, serialisiert durch {@link UserAdapter}, 
     * Status Code 404, falls der {@link User} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @GET
    @Path("/{id}")    
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@PathParam("id") long id) {
        Optional<User> user = UsersFacade.findById(id);
        if (user.isPresent()){

            return Response.ok( 
                new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
                .toJson(user.get())
            ).build();
            
        }
        else{

            return Response.status(404).build();

        }
    }
    
    /**
     * Findet alle Direkt-Unterhaltungen eines {@link User Users}.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link Chat Chats}, serialisiert durch {@link ChatAdapter}, 
     * Status Code 404, falls der {@link User} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Findet alle {@link Group Groups} eines {@link User Users}.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link Group Groups}, serialisiert durch {@link GroupAdapter}, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Findet alle gemeinsamen {@link Group Groups} zweier {@link User}.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link Group Groups}, serialisiert durch {@link GroupAdapter}, 
     * Status Code 404, falls einer der beiden {@link User} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls beide {@link User#id} identisch sind.
     */
    @GET
    @Path("/{user1Id}/shared-groups/{user2Id}")
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

    /**
     * Findet alle {@link User}, die ein {@link User} blockiert hat.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link User Usern}, serialisiert durch {@link UserAdapter}, 
     * Status Code 404, falls der {@link User} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Fügt einen {@link User} zu {@link User#blockedUsers} eines anderen {@link User Users} hinzu.
     * Informiert alle WebSockets, die für diesen {@link User} registriert sind.
     * @param userId
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls einer (oder beide) der {@link User} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Entfernt einen {@link User} von {@link User#blockedUsers} eines anderen {@link User Users} hinzu.
     * Informiert alle WebSockets, die für diesen {@link User} registriert sind.
     * @param userId
     * @param token
     * @return Status Code 200, 
     * Status Code 404, falls einer (oder beide) der {@link User} nicht gefunden werden konnte
     *  oder der {@link User} nicht blockiert ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Findet alle {@link User}, von denen ein {@link User} blockiert ist.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link User Usern}, serialisiert durch {@link UserAdapter}, 
     * Status Code 404, falls der {@link User} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Findet alle {@link Role Roles} eines {@link User Users} in einer {@link Group}.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link Role Role}, serialisiert durch {@link RoleAdapeter} mit einer {@link RoleAdapter#verbosity} von 0, 
     * Status Code 404, falls die {@link Group} oder der {@link User} nicht gefunden werden konnten, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @GET
    @Path("/{userId}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(@PathParam("userId") long userId, @QueryParam("group") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<User> requestIssuer = UsersFacade.findById(tokenUserId);

        if(!group.get().getUsers().contains(requestIssuer.get())){
            return Response.status(403).entity("Unauthorized").build();
        }
        
        Optional<User> user = UsersFacade.findById(userId);

        if(user.isEmpty()){
            return Response.status(404).build();
        }

        List<Role> roles = RolesFacade.findUserGroupRoles(group.get(), user.get());

        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Role.class, new RoleAdapter(0)).create()
            .toJson(roles)
        ).build();

    }

    /**
     * Erstellt einen {@link User}. Kann optional außerdem ein {@link UserProfile} erstellen.
     * @param user
     * @return Status Code 201,
     * Status Code 400, falls {@link User#username} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht,
     *  ein {@link UserProfile} gegeben ist, bei dem {@link UserProfile#maxLoadedMessages} kleiner ist als {@link UserProfile#messageChunkSize}
     *  oder ein anderer {@link User} diese {@link User#email} bereits verwendet.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user){
        user.setOnline(false);
        user.setPass(BCrypt.withDefaults().hashToString(10, user.getPass().toCharArray()));
        UserProfile userProfile;

        if(user.getUsername() == null){
            return Response.status(400).build();
        }

        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);

        Matcher m = p.matcher(user.getUsername());

        if(m.find()){
            user.setUsername(m.group(0));
        }
        else{
            return Response.status(400).entity("Name cannot entirely consist of whitespaces").build();
        }

        if(user.getUserProfiles() != null){
            userProfile = user.getUserProfiles().get(0);
            if(userProfile.getMaxLoadedMessages() < userProfile.getMessageChunkSize()){
                return Response.status(400).entity("maxLoadedMessages must be larger than messageChunkSize").build();
            }
            userProfile.setUser(user);
        }
        else{
            userProfile = new UserProfile(user, false, 100, 50);
        }
        try{
            UsersFacade.add(user);
            UserProfilesFacade.add(userProfile);
            return Response.status(201).build();
        }
        catch(PersistenceException e){
            e.printStackTrace();

            //find column
            String errorMessage = e.getMessage().split(" ")[1];
            errorMessage = errorMessage.substring(1, errorMessage.length()-1);

            if(errorMessage.equals("e_mail")){
                return Response.status(400).entity("Email is already in use.").build();
            }
            else{
                return Response.status(400).build();
            }
        }
    }

    /**
     * Findet das {@link UserProfile} eines {@link User Users}.
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link UserProfile}, serialisiert durch {@link UserProfileAdapter}, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{userId}/profile")
    public Response getProfile(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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
        return Response.ok(
            new GsonBuilder().registerTypeAdapter(UserProfile.class, new UserProfileAdapter()).create()
            .toJson(UsersFacade.findById(userId).get().getUserProfiles().get(0))
        ).build();
    }

    /**
     * Aktualisiert einen {@link User}.
     * {@link User#email}, {@link User#lastSeen} und {@link User#isOnline} werden in jedem Fall beibehalten.
     * {@link User#pass} wird beibehalten, falls kein neues Passwort angegeben ist.
     * @param user
     * @return Status Code 200, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls {@link User#username} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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
        if(user.getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);

        Matcher m = p.matcher(user.getUsername());

        if(m.find()){
            user.setUsername(m.group(0));
        }
        else{
            return Response.status(400).entity("Name cannot entirely consist of whitespaces").build();
        }
        
        Optional<User> oldUser = UsersFacade.findById(user.getId());

        //these shoudln't be set by the user
        user.setEmail(oldUser.get().getEmail());
        user.setLastSeen(oldUser.get().getLastSeen());
        user.setOnline(oldUser.get().isOnline());
        if(user.getPass() != null){
            user.setPass(BCrypt.withDefaults().hashToString(10, user.getPass().toCharArray()));

            //log out all other users
            SessionsFacade.removeAllUserSessionsExcept(token, oldUser.get());
        }
        else{
            user.setPass(oldUser.get().getPass());
        }
        
        UsersFacade.update(user);

        return Response.ok().build();
    }

    /**
     * Löscht einen {@link User} und alle {@link Group Groups} von denen er der {@link Group#owner} ist, sowie seine {@link Session Sessions}.
     * @param userId
     * @param token
     * @return Status Code 200, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @DELETE
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<User> user = UsersFacade.findById(tokenUserId);
        UsersFacade.remove(user.get());

        return Response.ok().build();
    }
}
