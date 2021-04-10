package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Chat;
import com.realet.sip.ChatMessage;
import com.realet.sip.User;

public class ChatMessageAdapter extends TypeAdapter<ChatMessage> {

    int verbosity;
    
    //0: all IDs
    //1: above except author as obj
    //2: above except chat as obj

    @Override
    public void write(JsonWriter out, ChatMessage value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("content");
        out.value(value.getContent());
        out.name("sent");
        out.value(value.getSent().toInstant().toString());
        out.name("expires");
        out.value(value.getExpires() != null ? value.getExpires().toInstant().toString() : null);
        if(verbosity >= 2){
            out.name("chat");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create().toJson(value.getChat()));
        }
        else{
            out.name("chatId");
            out.value(value.getChat().getId());
        }
        if(verbosity >= 1){
            out.name("author");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create().toJson(value.getAuthor()));
        }
        else{
            out.name("authorId");
            out.value(value.getAuthor().getId());
        }
        out.name("edited");
        out.value(value.getEdited() != null ? value.getEdited().toInstant().toString() : null);
        out.endObject();
        
    }

    @Override
    public ChatMessage read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public ChatMessageAdapter(int verbosity) {
        this.verbosity = verbosity;
    }
    
}
