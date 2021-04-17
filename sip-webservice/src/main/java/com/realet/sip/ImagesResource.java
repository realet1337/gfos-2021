package com.realet.sip;

import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/images")
public class ImagesResource {

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUser(@PathParam("id") long id) {
        Optional<User> user = UsersFacade.findById(id);
        if (user.isPresent()){

            return Response.ok( new Gson().toJson(user.get()) ).build();
            
        }
        else{

            return Response.status(404).build();

        }
    }

}
