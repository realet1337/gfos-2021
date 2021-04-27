package com.realet.sip;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

/**
 * RestEasy Resource-Klasse für {@link Role Roles}.
 */
@Path("/roles")
public class RolesResource {

    /**
     * Aktualisiert eine {@link Role}.
     * {@link Role#priority} und {@link Role#group} werden in jedem Fall beibehalten.
     * Entfernt Leerzeichen am Anfang und Ende des {@link Role#name}. 
     * @param chatMessage
     * @param token
     * @return Status Code 200
     * Status Code 404, falls keine {@link Role} mit derselben {@link Role#id} gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls die {@link Role#color} nicht vorhanden oder kein gültiger Hex-Code ist,
     *  oder der {@link Role#name} nicht vorhanden oder leer ist, oder nur aus Leerzeichen besteht.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRole(Role role, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Role> oldRole = RolesFacade.findById(role.getId());

        if(oldRole.isEmpty()){
            return Response.status(404).build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), oldRole.get().getGroup()).isEmpty() && oldRole.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }


        //separate request for priority
        role.setPriority(oldRole.get().getPriority());

        role.setGroup(oldRole.get().getGroup());
        
        RolesFacade.update(role);

        return Response.ok().build();
    }

    /**
     * Entfernt eine {@link Role} anhand ihrer {@link Role#id}. 
     * @param id
     * @param token
     * @return Status Code 200, 
     * Status Code 404, falls keine {@link Role} mit dieser {@link Role#id} existiert, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
    @DELETE
    @Path("/{roleId}")
    public Response deleteRole(@PathParam("roleId") long roleId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Role> role = RolesFacade.findById(roleId);
        if(role.isEmpty()){
            return Response.status(404).build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), role.get().getGroup()).isEmpty() && role.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        RolesFacade.remove(role.get());

        return Response.status(200).build();
    }

    /**
     * Fügt einen {@link User} zu einer {@link Role} hinzu. Diese Methode verändert den in der Datenbank gespeicherten {@link User} nicht.
     * @param groupId
     * @param inputUser
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls der {@link User} oder die {@link Role} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link User} bereits Teil dieser {@link Role} oder nicht Teil der {@link Role#group} ist.
     */
    @POST
    @Path("/{roleId}/users")
    public Response addUser(@PathParam("roleId") long roleId, User inputUser, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Role> role = RolesFacade.findById(roleId);
        if(role.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> user = UsersFacade.findById(inputUser.getId());
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        if(role.get().getUsers().contains(user.get())){
            return Response.status(400).entity("User is already part of role").build();
        }

        if(!role.get().getGroup().getUsers().contains(user.get())){
            return Response.status(400).entity("User is not part of group").build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), role.get().getGroup()).isEmpty() && role.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        role.get().getUsers().add(user.get());

        RolesFacade.update(role.get());

        return Response.ok().build();
        
    }

    /**
     * Entfernt einen {@link User} aus einer {@link Group}. Der {@link User} existiert weiterhin.
     * @param groupId
     * @param userId
     * @param token
     * @return Status Code 200,
     * Status Code 404, falls die {@link Group} {@link Group} nicht gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status code 400, falls der {@link User} nicht Teil dieser {@link Role} ist.
     */
    @DELETE
    @Path("/{roleId}/users/{userId}")
    public Response removeUser(@PathParam("roleId") long roleId, @PathParam("userId") long userId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Role> role = RolesFacade.findById(roleId);
        if(role.isEmpty()){
            return Response.status(404).build();
        }

        Optional<User> user = UsersFacade.findById(userId);
        if(user.isEmpty()){
            return Response.status(404).build();
        }

        if(!role.get().getUsers().contains(user.get())){
            return Response.status(400).entity("User is not part of role").build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), role.get().getGroup()).isEmpty() && role.get().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        role.get().getUsers().remove(user.get());

        RolesFacade.update(role.get());

        return Response.ok().build();
    }
    
}
