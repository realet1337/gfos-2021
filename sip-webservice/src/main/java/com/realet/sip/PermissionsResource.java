package com.realet.sip;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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

        if(RolesFacade.findAdminRolesByUserAndGroup(UsersFacade.findById(tokenUserId).get(), oldPermission.get().getRole().getGroup()).isEmpty() && oldPermission.get().getRole().getGroup().getOwner().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        permission.setRole(oldPermission.get().getRole());
        permission.setChat(oldPermission.get().getChat());

        PermissionsFacade.update(permission);

        return Response.ok(200).build();

    }

}
