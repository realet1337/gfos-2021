package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.UserProfile;

/**
 * Ein Serialisierungs-Gson-TypeAdapter für die {@link UserProfile} Klasse.
 */
public class UserProfileAdapter extends TypeAdapter<UserProfile>{

    /**
     * Serialisierungs-Methode. Schreibt folgendes in den "out" Parameter: 
     * {@link UserProfile#id}, 
     * {@link UserProfile#reverseBlocking}, 
     * {@link UserProfile#maxLoadedMessages}, 
     * {@link UserProfile#messageChunkSize}, 
     * @param out JsonWriter Objekt, welches das serialisierte {@link UserProfile} Objekt erhält.
     * @param value Zu serialisierendes {@link UserProfile} Objekt.
     * @throws IOException
     */
    @Override
    public void write(JsonWriter out, UserProfile value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("reverseBlocking");
        out.value(value.isReverseBlocking());
        out.name("maxLoadedMessages");
        out.value(value.getMaxLoadedMessages());
        out.name("messageChunkSize");
        out.value(value.getMessageChunkSize());
        
        //If you prefer consistency over speed
        //
        // out.name("user");
        // out.jsonValue(
        //     new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
        //     .toJson(value.getUser())
        // );

        out.endObject();
    }

    /**
     * Nicht funktionelle Entserialisierungs-Methode.
     * @param in
     * @return null
     * @throws IOException
     */
    @Override
    public UserProfile read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
