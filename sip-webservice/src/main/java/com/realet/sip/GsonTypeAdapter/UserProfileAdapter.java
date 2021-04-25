package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.UserProfile;

/**
 * A serialization Gson-TypeAdapter for the {@link UserProfile} class.
 */
public class UserProfileAdapter extends TypeAdapter<UserProfile>{

    /**
     * Serialization method. Will write the following to "out" parameter: 
     * {@link UserProfile#id}, 
     * {@link UserProfile#reverseBlocking}, 
     * {@link UserProfile#maxLoadedMessages}, 
     * {@link UserProfile#messageChunkSize}, 
     * @param out JsonWriter object that will receive the serialized {@link UserProfile} Object.
     * @param value {@link UserProfile} object that will be serialized.
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
     * Non-functional deserialization method
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
