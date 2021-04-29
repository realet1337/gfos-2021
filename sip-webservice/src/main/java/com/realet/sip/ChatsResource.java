package com.realet.sip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;
import com.realet.sip.GsonTypeAdapter.ChatMessageAdapter;
import com.realet.sip.GsonTypeAdapter.PermissionAdapter;

/**
 * RestEasy Resource-Klasse für {@link Chat Chats}.
 */
@Path("/chats")
public class ChatsResource {

    // @GET
    // @Path("/{id}")    
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getChat(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token) {
    //     //(LATER) REMOVE THIS METHOD IT ISN'T NECESSARY

    //     if(token == null){
    //         return Response.status(403).build();
    //     }

    //     token = token.split(" ")[1];

    //     Optional<Chat> chat = ChatsFacade.findById(id);
    //     if(chat.isPresent()){

    //         //validate access
    //         try {
    //             long user_id = SessionsFacade.findUserIdByToken(token);
    //             if(user_id == chat.get().getUser1().getId() || 
    //                 user_id == chat.get().getUser2().getId()){
    //                 return Response.ok(new Gson().toJson(chat.get())).build();
    //             }else{

    //                 return Response.status(403).build();

    //             }
    //         } catch (IllegalAccessException e) {

    //             return Response.status(403).build();
    //         }

    //     }

    //     return Response.status(404).build();
        
    // }

    /**
     * Findet eine Direkt-Unterhaltung zweier {@link User}.
     * @param user1Id
     * @param user2Id
     * @param token
     * @return Status Code 200 mit {@link Chat}, serialisiert durch {@link ChatAdapter}, 
     * Status Code 404, falls ein {@link User} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * 
     */
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

        Chat newChat = new Chat(null, user1.get(), user2.get(), null);
        ChatsFacade.add(newChat);


        //just act like it was always there
        return Response.ok(
            new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create().toJson(newChat)
        ).build();


    }

    /**
     * Findet {@link ChatMessage ChatMessages} eines {@link Chat Chats} mittels {@link ChatMessagesFacade#find}.
     * @param chatId
     * @param count
     * @param beforeId
     * @param afterId
     * @param userId
     * @param reverseBlocking
     * @param token
     * @return Status Code 200 mit {@link ChatMessage ChatMessages}, serialisiert durch {@link ChatMessageAdapter} mit einer {@link ChatMessageAdapter#verbosity} von 1, 
     * Status Code 404, falls der {@link Chat} oder eine angegebene einschränkende {@link ChatMessage} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls "count" nicht zwischen 1 und 1000 liegt oder sowohl "beforeId" und "afterId" ungleich 0 sind.
     */
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
            else{
                if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(user_id).get())){
                    return Response.status(403).entity("Unauthorized").build();
                }

                List<Permission> permissions = PermissionsFacade.findGroupChatPermissions(chatId, user_id);

                boolean canRead = false;

                for(Permission p: permissions){
                    canRead = canRead || p.isCanRead();
                }


                if(!canRead){
                    return Response.status(403).entity("Insufficient permissions").build();
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

    /**
     * Fügt eine {@link ChatMessage} zu einem {@link Chat} hinzu.
     * Sendet diese {@link ChatMessage} an alle WebSockets, die für {@link User} registriert sind, welche diesen {@link Chat} lesen können.
     * Entfernt Leerzeichen am Anfang und Ende des {@link ChatMessage#content}.
     * @param chatMessage
     * @param chatId
     * @param token
     * @return Status Code 201, 
     * Status Code 404, falls der {@link Chat} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls der {@link ChatMessage#content} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
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
        else{
            if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(tokenUserId).get())){
                return Response.status(403).entity("Unauthorized").build();               
            }

            List<Permission> permissions = PermissionsFacade.findGroupChatPermissions(chatId, tokenUserId);

                boolean canWrite = false;

                for(Permission p: permissions){
                    canWrite = canWrite || p.isCanWrite();
                }


                if(!canWrite){
                return Response.status(403).entity("Insufficient permissions").build();
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
                        s.getBasicRemote().sendText("new-message: " + 
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
                        s.getBasicRemote().sendText("new-message: " + 
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
            for(User user: UsersFacade.findGroupChatReaders(chat.get().getId(), chat.get().getGroup().getId())){
                ArrayList<javax.websocket.Session> list = UserWebsocketManagement.getSessions(user.getId());
                if(list != null){
                    for(javax.websocket.Session s: list){
                        try {
                            s.getBasicRemote().sendText("new-message: " + 
                                new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter(2)).create()
                                .toJson(chatMessage)
                            );
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }      
        
        return Response.status(201).build();

    }

    /**
     * Aktualisiert einen {@link Chat}.
     * Entfernt Leerzeichen am Anfang und Ende des {@link Chat#name}.
     * @param inputChat
     * @param token
     * @return Status Code 200, 
     * Status Code 404, falls der {@link Chat} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls der {@link ChatMessage#content} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht, oder der {@link Chat} eine Direkt-Unterhaltung ist.
     */
    @PUT
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateChat(Chat inputChat, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Chat> chat = ChatsFacade.findById(inputChat.getId());
        if(chat.isEmpty()){
            return Response.status(404).build();
        }
        if(chat.get().getGroup() == null){
            return Response.status(400).entity("Can't modify direct chats.").build();
        }
        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), chat.get().getGroup()).isEmpty() && chat.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }
        inputChat.setGroup(chat.get().getGroup());
        inputChat.setUser1(null);
        inputChat.setUser2(null);
        ChatsFacade.update(inputChat);

        return Response.status(200).build();
    }

    /**
     * Entfernt einen {@link Chat} und alle seine  {@link ChatMessage ChatMessages}.
     * @param chatId
     * @param token
     * @return Status Code 200, 
     * Status Code 404, falls der {@link Chat} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls der {@link Chat} eine Direkt-Unterhaltung ist.
     */
    @DELETE
    @Path("/{chatId}")
    public Response deleteChat(@PathParam("chatId") long chatId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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
        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            return Response.status(404).build();
        }
        if(chat.get().getGroup() == null){
            return Response.status(400).entity("Can't delete direct chats.").build();
        }
        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), chat.get().getGroup()).isEmpty() && chat.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }
        if(chat.get().getGroup().getChats().size() == 1){
            return Response.status(400).entity("Can't delete only chat of group.").build();
        }
        ChatsFacade.remove(chat.get());

        return Response.status(200).build();
    }

    /**
     * Findet alle {@link Permission Permissions} eines {@link Chat Chats}.
     * @param chatId
     * @param userId
     * @param token
     * @return Status Code 200 mit {@link Permission Permissions}, serialisiert durch {@link PermissionAdapter}, 
     * Status Code 404, falls der {@link Chat} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls der {@link Chat} eine Direkt-Unterhaltung ist.
     */
    @GET
    @Path("/{chatId}/permissions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatPermissions(@PathParam("chatId") long chatId, @QueryParam("user") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

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

        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            return Response.status(404).build();
        }

        if(chat.get().getGroup() == null){
            return Response.status(400).entity("Only group chats have permissions.").build();
        }
        
        if(userId != 0){
            //is requesting own permissions
            if(userId != tokenUserId){
                return Response.status(403).entity("Unauthorized").build();
            }

            List<Permission> permissions = PermissionsFacade.findGroupChatPermissions(chatId, userId);

            return Response.ok(
                new GsonBuilder().registerTypeAdapter(Permission.class, new PermissionAdapter()).create()
                .toJson(permissions)
            ).build();
        }
        else{
            //is requesting all chat permissions (must be admin)
            if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), chat.get().getGroup()).isEmpty() && chat.get().getGroup().getOwner().getId() != tokenUserId){
                return Response.status(403).entity("Unauthorized").build();
            }
    
            return Response.ok(
                new GsonBuilder().registerTypeAdapter(Permission.class, new PermissionAdapter()).create()
                .toJson(chat.get().getPermissions())
            ).build();
        }

    }

    /**
     * Fügt eine {@link Permission} für einen Chat hinzu.
     * @param chatId
     * @param permission
     * @param token
     * @return Status Code 200, 
     * Status Code 404, falls der {@link Chat} nicht vorhanden ist, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls die {@link Permission} keine {@link Permission#role} hat,
     *  die {@link Permission#role} nicht zur {@link Chat#group} gehört
     *  oder bereits eine {@link Permission} für die {@link Permission#role} und den {@link Chat} existiert.
     */    
    @POST
    @Path("/{chatId}/permissions")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPermission(@PathParam("chatId") long chatId, Permission permission, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Chat> chat = ChatsFacade.findById(chatId);
        if(chat.isEmpty()){
            return Response.status(404).build();
        }
        
        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), chat.get().getGroup()).isEmpty() && chat.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        if(permission.getRole() == null){
            return Response.status(400).entity("Permission must have a role").build();
        }

        Optional<Role> role = RolesFacade.findById(permission.getRole().getId());
        if(role.isEmpty()){
            return Response.status(404).build();
        }

        if(!chat.get().getGroup().getRoles().contains(role.get())){
            return Response.status(400).entity("Role doesn't belong to group").build();
        }

        if(PermissionsFacade.findByRoleAndChat(role.get(), chat.get()).isPresent()){
            return Response.status(400).entity("A permission for this role and chat already exists.").build();
        }

        //this is unnecessary but i feel safer doing it leave me alone
        permission.setRole(role.get());
        permission.setChat(chat.get());

        PermissionsFacade.add(permission);

        return Response.status(201).build();
    }

}
