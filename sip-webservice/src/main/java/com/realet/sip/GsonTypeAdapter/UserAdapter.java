package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.User;

/**
 * Ein Serialisierungs-Gson-TypeAdapter für die {@link User} Klasse.
 */
public class UserAdapter extends TypeAdapter<User>{

    /**
     * Serialisierungs-Methode. Schreibt folgendes in den "out" Parameter: 
     * {@link User#id}, 
     * {@link User#username}, 
     * {@link User#info}, falls existent, 
     * {@link User#profilePicture}, falls existent, 
     * {@link User#isOnline}, 
     * {@link User#status}, falls existent, 
     * {@link User#lastSeen}, falls existent.
     * @param out JsonWriter Objekt, welches das serialisierte {@link User} Objekt erhält.
     * @param value Zu serialisierendes {@link User} Objekt.
     * @throws IOException
     */
    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("username");
        out.value(value.getUsername());
        out.name("info");
        out.value(value.getInfo());
        out.name("profilePicture");
        out.value(value.getProfilePicture());
        out.name("isOnline");
        out.value(value.isOnline());
        out.name("status");
        out.value(value.getStatus());
        if(value.getLastSeen() != null){
            out.name("lastSeen");
            out.value(value.getLastSeen().toInstant().toString());
        }
        out.endObject();
        
    }

    /**
     * Nicht funktionelle Entserialisierungs-Methode.
     * @param in
     * @return null
     * @throws IOException
     */
    @Override
    public User read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
