package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Permission;
import com.realet.sip.Role;

/**
 * A serialization Gson-TypeAdapter for the {@link Permission} class.
 */
public class PermissionAdapter extends TypeAdapter<Permission>{

    /**
     * Serialization method. Will write the following to "out" parameter: 
     * {@link Permission#id}, 
     * {@link Permission#canRead}, 
     * {@link Permission#canWrite}, 
     * {@link Permission#role} as an object, without attributes as objects, if existent, 
     * @param out JsonWriter object that will receive the serialized {@link Permission} Object.
     * @param value {@link Permission} object that will be serialized.
     * @throws IOException
     */
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

    /**
     * Non-functional deserialization method
     * @param in
     * @return null
     * @throws IOException
     */
    @Override
    public Permission read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
