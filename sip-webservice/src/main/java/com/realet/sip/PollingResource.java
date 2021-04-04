package com.realet.sip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/polling")
public class PollingResource {

    private static HashMap<Long, ArrayList<AsyncResponse>> responseLists = new HashMap<>();

    @GET
    @Path("/chat/{chatId}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getChatUpdate(@PathParam("chatId") long chatId, @Suspended AsyncResponse response, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(token == null){
            response.resume(Response.status(403).build());
            return;
        }
        token = token.split(" ")[1];

        long tokenUserId = 0;

        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            response.resume(Response.status(403).build());
            return;
        }

        Optional<Chat> chat = ChatsFacade.findById(chatId);
        
        if(chat.isEmpty()){
            response.resume(Response.status(404).build());
            return;
        }

        
        if(chat.get().getGroup() ==  null){
            if(chat.get().getUser1().getId() != tokenUserId && chat.get().getUser2().getId() != tokenUserId){
                response.resume(Response.status(403).build());
                return;                
            }
        }
        //This must be a group chat, now check membership
        //FIXME: IMPLEMENT ROLE PERMISSION CHECKS                           ret: optional (wont be empty because token validation succeeded)
        else{
            if(!chat.get().getGroup().getUsers().contains(UsersFacade.findById(tokenUserId).get())){
                response.resume(Response.status(403).build());
                return;                
            }
        }

        //access to chat validated
        PollingResource.addToResponses(chatId, response);

    }

    public static ArrayList<AsyncResponse> getResponses(long chatId){
        return responseLists.get(Long.valueOf(chatId));
    }

    public static void addToResponses(long chatId, AsyncResponse response){
        ArrayList<AsyncResponse> responses = responseLists.get(Long.valueOf(chatId));
        if(responses != null){
            responses.add(response);
        }
        else{
            responses = new ArrayList<>();
            responses.add(response);
            responseLists.put(Long.valueOf(chatId), responses);
        }
    }
    
}
