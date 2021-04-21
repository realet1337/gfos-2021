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

@Path("/roles")
public class RolesResource {

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

        return Response.status(200).build();
    }
    
}
