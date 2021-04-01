package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Chat;

public class ChatAdapter extends TypeAdapter<Chat>{

    @Override
    public void write(JsonWriter out, Chat value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("groupId");
        out.value(value.getGroup() != null ? value.getGroup().getId() : null);
        out.name("user1Id");
        out.value(value.getUser1() != null ? value.getUser1().getId() : null);
        out.name("user2Id");
        out.value(value.getUser2() != null ? value.getUser2().getId() : null);
        out.endObject();

        
    }

    @Override
    public Chat read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
