package com.realet.sip;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Beantworte all OPTIONS-Anfragen mit den korrekten HTTP-Headers und einer 200 OK Antwort, um CORS zu erlauben.
 */
@Path("/")
public class CORSPreflightHandler {

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");

        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }
        
}
