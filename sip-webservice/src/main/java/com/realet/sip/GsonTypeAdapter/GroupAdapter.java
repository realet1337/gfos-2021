package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;


import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Group;
import com.realet.sip.User;

/**
 * A serialization Gson-TypeAdapter for the {@link Group} class.
 */
public class GroupAdapter extends TypeAdapter<Group>{
    /**
     * Serialization method. Will write the following to "out" parameter: 
     * {@link Group#id}, 
     * {@link Group#description} if existent, 
     * {@link Group#name}, 
     * {@link Group#picture} if existent, 
     * {@link Group#owner} as an object, 
     * @param out JsonWriter object that will receive the serialized {@link Group} Object.
     * @param value {@link Group} object that will be serialized.
     * @throws IOException
     */
    @Override
    public void write(JsonWriter out, Group value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("description");
        out.value(value.getDescription());
        out.name("name");
        out.value(value.getName());
        out.name("picture");
        out.value(value.getPicture());
        out.name("owner");
        out.jsonValue(
            new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(value.getOwner())
        );
        out.endObject();
        
    }

    /**
     * Non-functional deserialization method
     * @param in
     * @return null
     * @throws IOException
     */
    @Override
    public Group read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
