package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Group;
import com.realet.sip.Role;
import com.realet.sip.User;

public class RoleAdapter extends TypeAdapter<Role> {

    //0: no arrays
    //1: as above except users are added

    int verbosity;

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
        out.name("priority");
        out.value(value.getPriority());
        if(verbosity >= 1){
        out.name("users");
            out.jsonValue(
                new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create()
                .toJson(value.getUsers())
            );
        }
        out.name("isAdmin");
        out.value(value.isAdmin());
        out.endObject();
    }

    @Override
    public Role read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    public RoleAdapter(int verbosity) {
        this.verbosity = verbosity;
    }
    
}
