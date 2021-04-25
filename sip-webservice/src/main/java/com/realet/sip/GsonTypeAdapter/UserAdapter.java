package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.User;

/**
 * A serialization Gson-TypeAdapter for the {@link User} class.
 */
public class UserAdapter extends TypeAdapter<User>{

    /**
     * Serialization method. Will write the following to "out" parameter: 
     * {@link User#id}, 
     * {@link User#username}, 
     * {@link User#info} if existent, 
     * {@link User#profilePicture} if existent, 
     * {@link User#isOnline}, 
     * {@link User#status} if existent, 
     * {@link User#lastSeen} if existent, 
     * @param out JsonWriter object that will receive the serialized {@link User} Object.
     * @param value {@link User} object that will be serialized.
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
     * Non-functional deserialization method
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
