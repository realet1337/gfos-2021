package com.realet.sip;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

@Path("/user-profiles")
public class UserProfilesResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserProfile(UserProfile userProfile, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
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
        if(userProfile.getUser().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        Optional<User> oldUser = UsersFacade.findById(tokenUserId);

        if(oldUser.get().getUserProfiles().get(0).getId() != userProfile.getId()){
            return Response.status(400).entity("User can only have one profile.").build();
        }

        UserProfilesFacade.update(userProfile);

        return Response.ok().build();
    }
}
