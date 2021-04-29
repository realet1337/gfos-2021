package com.realet.sip;

import java.util.ArrayList;
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
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatAdapter;
import com.realet.sip.GsonTypeAdapter.GroupAdapter;
import com.realet.sip.GsonTypeAdapter.RoleAdapter;
import com.realet.sip.GsonTypeAdapter.UserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * RestEasy Resource-Klasse für {@link Group Groups}.
 */
@Path("/groups")
public class GroupsResource {

    /**
     * Findet eine {@link Group}.
     * @param groupId
     * @param token
     * @return Status Code 200 mit {@link Group}, serialisiert durch {@link GroupAdapter}, 
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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
    
    /**
     * Findet alle {@link Chat Chats} einer {@link Group}.
     * @param groupId
     * @param token
     * @return Status Code 200 mit {@link Chat Chats}, serialisiert durch {@link ChatAdapter}, 
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Findet alle {@link User} ohne {@link Role} in einer {@link Group}.
     * @param groupId
     * @param token
     * @return Status Code 200 mit {@link User Usern}, serialisiert durch {@link UserAdapter},
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @GET
    @Path("/{groupId}/basic-users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBasicUsers(@PathParam("groupId") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

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

    /**
     * Findet alle {@link User} einer {@link Group}.
     * @param groupId
     * @param token
     * @return Status Code 200 mit {@link User Usern}, serialisiert durch {@link UserAdapter},
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @GET
    @Path("/{groupId}/users")
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
            .toJson(group.get().getUsers())
        ).build();
    }

    /**
     * Findet alle {@link Role Roles} einer {@link Group}.
     * @param groupId
     * @param token
     * @return Status Code 200 mit {@link Role Roles}, serialisiert durch {@link RoleAdapter}mit einer {@link RoleAdapter#verbosity} von 1,
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Erstellt eine {@link Group}. Erstellt automatisch einen {@link Chat} und eine {@link Permission} mit {@link Permission#canRead} == true und {@link Permission#canWrite} == true.
     * Entfernt Leerzeichen am Anfang und Ende des {@link Group#name}. 
     * @param group
     * @param token
     * @return Status Code 201,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link Group#name} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
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
        //hibernate actually doesn't even attempt it. Found out through testing.
        group.getUsers().add(user);
        GroupsFacade.add(group);

        Chat defaultChat = new Chat(group, null, null, "text-chat");

        ChatsFacade.add(defaultChat);

        PermissionsFacade.add(new Permission(null, defaultChat, true, true));

        return Response.status(201).entity(new JSONObject().put("id", group.getId()).toString()).build();
    }

    /**
     * Erstellt einen {@link Chat} und fügt ihn zu einer {@link Group} hinzu. Erstellt automatisch eine {@link Permission} mit {@link Permission#canRead} == true und {@link Permission#canWrite} == true.
     * Entfernt Leerzeichen am Anfang und Ende des {@link Chat#name}. 
     * @param groupId
     * @param inputChat
     * @param token
     * @return Status Code 201,
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link Chat#name} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
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
        
        PermissionsFacade.add(new Permission(null, inputChat, true, true));

        return Response.status(201).build();
    }

    /**
     * Aktualisiert eine {@link Group}.
     * {@link Group#owner} wird in jedem Fall beibehalten.
     * Entfernt Leerzeichen am Anfang und Ende des {@link Group#name}. 
     * @param group
     * @param token
     * @return Status Code 201,
     * Status Code 404, falls keine {@link Group} mit derselben {@link Group#id} gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link Group#name} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGroup(Group group, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Group> oldGroup = GroupsFacade.findById(group.getId());
        if(oldGroup.isEmpty()){
            return Response.status(404).build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), oldGroup.get()).isEmpty() && oldGroup.get().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        group.setOwner(oldGroup.get().getOwner());

        //need to update because group owns the relationship
        group.setUsers(oldGroup.get().getUsers());

        GroupsFacade.update(group);

        return Response.ok().build();
    }

    /**
     * Fügt einen {@link User} zu einer {@link Group} hinzu. Diese Methode verändert den in der Datenbank gespeicherten {@link User} nicht.
     * @param groupId
     * @param inputUser
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls der {@link User} oder die {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link User} bereits Teil dieser {@link Group} ist.
     */
    @POST
    @Path("/{groupId}/users")
    public Response addUser(@PathParam("groupId") long groupId, User inputUser, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<User> user = UsersFacade.findById(inputUser.getId());
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        if(group.get().getUsers().contains(user.get())){
            return Response.status(400).entity("User is already part of group").build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), group.get()).isEmpty() && group.get().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        group.get().getUsers().add(user.get());

        GroupsFacade.update(group.get());

        return Response.ok().build();
        
    }

    /**
     * Entfernt einen {@link User} aus einer {@link Group}. Der {@link User} existiert weiterhin.
     * @param groupId
     * @param userId
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls die {@link Group} oder der {@link User} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link User} nicht Teil dieser {@link Group} oder der {@link Group#owner} ist.
     */
    @DELETE
    @Path("/{groupId}/users/{userId}")
    public Response removeUser(@PathParam("groupId") long groupId, @PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        if(group.get().getOwner().getId() == userId){
            return Response.status(400).entity("Cannot remove group owner from group").build();
        }

        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        if(!group.get().getUsers().contains(user.get())){
            return Response.status(400).entity("User is not part of group").build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), group.get()).isEmpty() && group.get().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        group.get().getUsers().remove(user.get());

        GroupsFacade.update(group.get());

        return Response.status(200).build();
    }

    /**
     * Erstellt eine {@link Role} und fügt sie zu einer {@link Group} hinzu.
     * @param groupId
     * @param role
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls die {@link Role#color} nicht vorhanden oder kein gültiger Hex-Code ist,
     *  oder  der {@link Role#name} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
    @POST
    @Path("/{groupId}/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRole(@PathParam("groupId") long groupId, Role role, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        if(role.getName() == null){
            return Response.status(400).entity("Role must have a name").build();
        }

        if(role.getColor() == null){
            return Response.status(400).entity("Role must have a color").build();
        }

        //check for valid hex code
        if(!Pattern.compile("^#[0-9a-fA-F]{6}$", Pattern.CASE_INSENSITIVE)
        .matcher(role.getColor()).matches()){
            return Response.status(400).entity("Invalid color tag. Use: #<hex color code>").build();
        }

        //bit of sanitization (and some more validation)
        //this pattern just strips whitespaces at the start/end of string
        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);

        Matcher m = p.matcher(role.getName());

        if(m.find()){
            role.setName(m.group(0));
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

        List<Role> roles = RolesFacade.findGroupRolesOrderedByPriority(group.get());

        //lowest priority
        if(roles.size() == 0){
            role.setPriority(0);
        }
        else {
            role.setPriority(roles.get(roles.size()-1).getPriority() + 1);
        }

        role.setGroup(group.get());
        
        RolesFacade.add(role);

        return Response.ok().build();
    }

    /**
     * Verändert {@link Role#priority} aller {@link Role Roles} in einer {@link Group}.
     * Akzeptiert einen JSON-Array mit den {@link Role#id} ALLER {@link Group#roles} sortiert nach Priorität (wichtigste Rollen zuerst).
     * @param groupId
     * @param requestBody
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls die {@link Group} oder eine der {@link Role Roles} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls Array-Elemente doppelt erscheinen, nicht als Long zu parsen sind, 
     *  oder die zu ihnen gehörenden {@link Role Roles} nicht Teil der {@link Group} sind.
     */
    @PUT
    @Path("/{groupId}/roles/priorities")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePriorities(@PathParam("groupId") long groupId, String requestBody, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Group> group = GroupsFacade.findById(groupId);
        if(group.isEmpty()){
            return Response.status(404).build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), group.get()).isEmpty() && group.get().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        // This is frank    =>     :)      <=     Say hi to frank

        JSONArray array = new JSONArray(requestBody);
        List<Long> ids = new ArrayList<Long>();

        for(int i = 0; i < array.length(); i++){
            try{
                if(ids.contains(Long.valueOf(array.getLong(i)))){
                    return Response.status(400).entity("Array elements must be unique").build();
                }
                ids.add(Long.valueOf(array.getLong(i)));
            }
            catch(JSONException e){
                return Response.status(400).entity("Array elements must be Longs").build();
            }
        }

        //the reason we are doing this next part in 2 steps is so the process doesn't fail midway through, possibly leaving us with non-unique 
        //priorities
        List<Role> roles = new ArrayList<>();
        for(Long id: ids){
            Optional<Role> role = RolesFacade.findById(id.longValue());
            if(role.isEmpty()){
                return Response.status(404).build();
            }
            if(role.get().getGroup().getId() != groupId){
                return Response.status(400).entity("Role is not part of group").build();
            }
            roles.add(role.get());
        }

        for(int i = 0; i < roles.size(); i++){
            roles.get(i).setPriority(i);
            RolesFacade.update(roles.get(i));
        }

        return Response.ok().build();
    }

    /**
     * Löscht eine {@link Group} und alle dazugehörigen {@link Chat Chats} und {@link Role Roles}.
     * @param groupId
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls die {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @DELETE
    @Path("/{groupId}")
    public Response deleteGroup(@PathParam("groupId") long groupId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Group> group = GroupsFacade.findById(groupId);
        if(group.isEmpty()){
            return Response.status(404).build();
        }

        if(group.get().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        GroupsFacade.remove(group.get());

        return Response.ok().build();
    }

}
