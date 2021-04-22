package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Permission;
import com.realet.sip.Role;

public class PermissionAdapter extends TypeAdapter<Permission>{

    @Override
    public void write(JsonWriter out, Permission value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("canRead");
        out.value(value.isCanRead());
        out.name("canWrite");
        out.value(value.isCanWrite());
        if(value.getRole() != null){
            out.name("role");
            out.jsonValue(
                new GsonBuilder().registerTypeAdapter(Role.class, new RoleAdapter(0)).create()
                .toJson(value.getRole())
            );
        }
        out.endObject();
        
    }

    @Override
    public Permission read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
