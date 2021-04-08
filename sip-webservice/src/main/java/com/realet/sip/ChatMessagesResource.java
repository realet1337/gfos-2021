package com.realet.sip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.realet.sip.GsonTypeAdapter.ChatMessageAdapter;



@Path("/chat-messages")
public class ChatMessagesResource {

    @DELETE
    @Path("/{id}")
    public Response removeMessage(@PathParam("id") long id, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){
        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        long tokenUserId = 0;

        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();  
        }

        Optional<ChatMessage> chatMessage = ChatMessagesFacade.findById(id);
        
        if(chatMessage.isEmpty()){
            return Response.status(404).entity("Chat not found").build();
        }

        if(chatMessage.get().getAuthor().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        String chatMessageJSON = new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter()).create()
        .toJson(chatMessage.get());

        long chatId = chatMessage.get().getChat().getId();

        ChatMessagesFacade.remove(chatMessage.get());

        ArrayList<javax.websocket.Session> list = ChatWatcherManagement.getSessions(chatId);

        if(!(list == null || list.isEmpty())){
            for(javax.websocket.Session s: list){
                try {
                    s.getBasicRemote().sendText("removed: " + chatMessageJSON );
                } catch (IOException e) {
                    ChatWatcherManagement.removeSession(chatId, s);
                }
            }
            
        }

        return Response.ok().build();

    }

    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addChatMessage(ChatMessage chatMessage, @HeaderParam(HttpHeaders.AUTHORIZATION) String token){

        if(chatMessage.getContent().equals("")){
            return Response.status(400).build();
        }

        if(token == null){
            return Response.status(403).entity("Unauthenticated").build();
        }

        token = token.split(" ")[1];

        long tokenUserId = 0;

        try {
            tokenUserId = SessionsFacade.findUserIdByToken(token);
        } catch (IllegalAccessException e) {
            return Response.status(403).entity("Unauthenticated").build();
           
        }

        Optional<ChatMessage> oldMessage = ChatMessagesFacade.findById(chatMessage.getId());
        if(oldMessage.isEmpty()){
            return Response.status(404).build();
        }

        if(oldMessage.get().getAuthor().getId() != tokenUserId){
            return Response.status(403).entity("Unauthorized").build();
        }

        if(chatMessage.getContent() == null){
            return Response.status(400).entity("Message must contain text").build();
        }

        //bit of sanitization (and some more validation)
        //this pattern just strips whitespaces at the start/end of string
        Pattern p = Pattern.compile("\\S(.*\\S)?", Pattern.DOTALL);
        Matcher m = p.matcher(chatMessage.getContent());

        if(m.find()){
            chatMessage.setContent(m.group(0));
        }
        else{
            return Response.status(400).entity("Message cannot entirely consist of whitespaces").build();
        }

        chatMessage.setAuthor(null);

        chatMessage.setChat(null);
        
        chatMessage.setSent(null);

        chatMessage = ChatMessagesFacade.updateContent(chatMessage);

        long chatId = oldMessage.get().getChat().getId();

        ArrayList<javax.websocket.Session> list = ChatWatcherManagement.getSessions(chatId);

        if(!(list == null || list.isEmpty())){
            for(javax.websocket.Session s: list){
                try {
                    s.getBasicRemote().sendText("updated: " + 
                        new GsonBuilder().registerTypeAdapter(ChatMessage.class, new ChatMessageAdapter()).create()
                        .toJson(chatMessage)
                    );
                } catch (IOException e) {
                    ChatWatcherManagement.removeSession(chatId, s);
                }
            }
            
        }
        
        return Response.ok().build();

    }
}
