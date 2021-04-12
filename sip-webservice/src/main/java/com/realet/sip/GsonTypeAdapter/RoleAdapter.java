package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import javax.xml.registry.infomodel.User;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Group;
import com.realet.sip.Role;

public class RoleAdapter extends TypeAdapter<Role> {

    @Override
    public void write(JsonWriter out, Role value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("name");
        out.value(value.getName());
        out.name("color");
        out.value(value.getColor());
        if(value.getGroup() != null){
            out.name("group");
            out.jsonValue(
                new GsonBuilder().registerTypeAdapter(Group.class, new GroupAdapter()).create()
                .toJson(value.getGroup())
            );
        }
        out.name("users");
        out.jsonValue(
            new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
            .toJson(value.getUsers())
        );
    }

    @Override
    public Role read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
