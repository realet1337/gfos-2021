package com.realet.sip;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Path("/auth")
public class AuthResource {

    @POST
    @Path("/login")    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("eMail") String eMail, @FormParam("password") String password) {

        Optional<User> user = UsersFacade.findByEMail(eMail);

        if(!user.isEmpty() && BCrypt.verifyer().verify(password.getBytes(), user.get().getPass().getBytes()).verified ){
        
            //Token generation
            //who cares if we use md5, am i right?      
            String token = "";  
            try {
                do{
                    byte[] tokenBytes = MessageDigest.getInstance("MD5").digest((
                        user.get().getUsername() + String.valueOf(System.nanoTime())
                    ).getBytes(StandardCharsets.UTF_8));

                    token = String.format("%0" + (tokenBytes.length << 1) + "x", new BigInteger(1, tokenBytes)); //no clue why this works

                }while(SessionsFacade.findByToken(token).isPresent()); //Just keep going until we get one.

            } catch (NoSuchAlgorithmException e) {
                // this wont ever happen
                e.printStackTrace();
            }

            

            //persist session

            SessionsFacade.add(new Session(token, user.get(), Date.from(new Date().toInstant().plus(1, ChronoUnit.DAYS))));

            return Response.ok(new JSONObject().put("token", token).toString(), MediaType.APPLICATION_JSON).build();
        
        }else{

            return Response.status(403).build();

        }
        
    }

    @POST
    @Path("/logout")    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("token") String token) {

        Optional<Session> session = SessionsFacade.findByToken(token);

        if(session.isPresent()){
            SessionsFacade.remove(session.get());
            return Response.ok().build();
        }
        else{
            return Response.status(404).build();
        }

    }

}
