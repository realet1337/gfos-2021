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

/**
 * RestEasy Resource-Klasse für {@link Permission Permissions}.
 */
@Path("/permissions")
public class PermissionsResource {

    /**
     * Aktualisiert eine {@link Permission}.
     * {@link Permission#chat} und {@link Permission#role} werden in jedem Fall beibehalten.
     * @param permission
     * @param token
     * @return Status Code 200
     * Status Code 404, falls keine {@link Permission} mit derselben {@link Permission#id} gefunden werden konnte,
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt.
     */
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

    /**
     * Entfernt eine {@link Permission} anhand ihrer {@link Permission#id}. 
     * @param permissionId
     * @param token
     * @return Status Code 200, 
     * Status Code 404, falls keine {@link Permission} mit dieser {@link Permission#id} existiert, 
     * Status Code 403, falls das token ungültig ist oder keinen Zugriff auf diese Ressource erlaubt, 
     * Status Code 400, falls die {@link Permission} keine {@link Permission#role} hat. 
     *  In diesem Fall ist die {@link Permission} die Grundregel für diesen Chat und kann nicht gelöscht werden. 
     */
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
