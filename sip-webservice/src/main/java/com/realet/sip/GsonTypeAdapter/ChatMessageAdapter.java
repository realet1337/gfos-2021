package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.print.attribute.standard.MediaSize.Other;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.ChatMessage;

public class ChatMessageAdapter extends TypeAdapter<ChatMessage> {

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
        out.name("chatId");
        out.value(value.getChat().getId());
        out.name("authorId");
        out.value(value.getAuthor().getId());
        out.endObject();
        
    }

    @Override
    public ChatMessage read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
