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

/**
 * RestEasy Resource-Klasse, die für Authentifizierung zuständig ist.
 */
@Path("/auth")
public class AuthResource {

    /**
     * Erstellt eine {@link Session} und speichert sie in der Datenbank, falls die Authentifizierung erfolgreich verläuft.
     * @param email
     * @param password
     * @return Status Code 200 mit JSON-Objekt {"token":&lt;{@link Session#token}&gt;,"userId":&lt;{@link User#id}&gt;}
     * Status Code 403, falls die Authentifizierung fehlschlägt
     */
    @POST
    @Path("/login")    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email, @FormParam("password") String password) {

        Optional<User> user = UsersFacade.findByEmail(email);

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

            return Response.ok(new JSONObject().put("token", token).put("userId", user.get().getId()).toString(), MediaType.APPLICATION_JSON).build();
        
        }else{

            return Response.status(403).build();

        }
        
    }

    /**
     * Findet eine {@link Session} anhand des "token"-Parameters und entfernt sie aus der Datenbank.
     * @param token
     * @return Status Code 200
     * Status Code 403, falls keine Session mit diesem {@link Session#token} gefunden werden konnte.
     */
    @POST
    @Path("/logout")    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("token") String token) {

        if(token == null){
            return Response.status(404).build();
        }

        Optional<Session> session = SessionsFacade.findByToken(token);

        if(session.isPresent()){
            SessionsFacade.remove(session.get());
            return Response.ok().build();
        }
        else{
            return Response.status(404).build();
        }

    }

    /**
     * Überprüft ob eine Session existiert
     * @param token
     * @return Status Code 200 mit {"userId":&lt;{@link User#id}&gt;}
     * Status Code 403, falls keine {@link Session} mit diesem {@link Session#token} existiert.
     */
    @POST
    @Path("/validate")    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response validate(@FormParam("token") String token) {

        if(token == null){
            return Response.status(403).build();
        }

        Optional<Session> session = SessionsFacade.findByToken(token);

        if(session.isPresent()){
            return Response.ok(new JSONObject().put("userId", session.get().getUser().getId()).toString()).build();
        }
        else{
            return Response.status(403).build();
        }

    }

}
