package com.realet.sip;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

@Path("/permissions")
public class PermissionsResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePermission(Permission permission, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        
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

        Optional<Permission> oldPermission = PermissionsFacade.findById(permission.getId());
        if(oldPermission.isEmpty()){
            return Response.status(404).build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), oldPermission.get().getChat().getGroup()).isEmpty() && oldPermission.get().getChat().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        permission.setRole(oldPermission.get().getRole());
        permission.setChat(oldPermission.get().getChat());

        PermissionsFacade.update(permission);

        return Response.ok(200).build();

    }

    @DELETE
    @Path("/{permissionId}")
    public Response deletePermission(@PathParam("permissionId") long permissionId, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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

        Optional<Permission> permission = PermissionsFacade.findById(permissionId);
        if(permission.isEmpty()){
            return Response.status(404).build();
        }

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), permission.get().getChat().getGroup()).isEmpty() && permission.get().getChat().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        if(permission.get().getRole() == null){
            return Response.status(400).entity("Can't delete base rule").build();
        }

        PermissionsFacade.remove(permission.get());

        return Response.status(200).build();
    }
    

}
