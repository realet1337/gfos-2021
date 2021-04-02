package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.User;

public class UserAdapter extends TypeAdapter<User>{

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
        out.endObject();
        
    }

    @Override
    public User read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
