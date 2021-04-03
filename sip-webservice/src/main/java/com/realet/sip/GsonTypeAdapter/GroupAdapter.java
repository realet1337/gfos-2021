package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Group;

public class GroupAdapter extends TypeAdapter<Group>{

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
        out.name("ownerId");
        out.value(value.getOwner().getId());
        out.endObject();
        
    }

    @Override
    public Group read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}