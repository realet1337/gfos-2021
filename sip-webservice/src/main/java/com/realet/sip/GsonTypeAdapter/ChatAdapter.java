package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Chat;
import com.realet.sip.Group;
import com.realet.sip.User;

/**
 * Ein Serialisierungs-Gson-TypeAdapter für die {@link Chat} Klasse.
 */

public class ChatAdapter extends TypeAdapter<Chat>{

    /**
     * Serialisierungs-Methode. Schreibt folgendes in den "out" Parameter: 
     * {@link Chat#id}, 
     * {@link Chat#group} als Objekt, falls existent, 
     * {@link Chat#user1} als Objekt, falls existent, 
     * {@link Chat#user2} als Objekt, falls existent, 
     * {@link Chat#name}, falls existent.
     * @param out JsonWriter Objekt, welches das serialisierte {@link Chat} Objekt erhält.
     * @param value Zu serialisierendes {@link Chat} Objekt.
     * @throws IOException
     */
    @Override
    public void write(JsonWriter out, Chat value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        if(value.getGroup() != null){
            out.name("group");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(Group.class, new GroupAdapter()).create()
            .toJson(value.getGroup()));
        }
        if(value.getUser1() != null){
            out.name("user1");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(value.getUser1()));
        }
        if(value.getUser2() != null){
            out.name("user2");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(value.getUser2()));
        }
        out.name("name");
        out.value(value.getName());
        out.endObject();
    }

    /**
     * Nicht funktionelle Entserialisierungs-Methode.
     * @param in
     * @return null
     * @throws IOException
     */

    @Override
    public Chat read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
