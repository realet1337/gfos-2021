package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Permission;
import com.realet.sip.Role;

/**
 * Ein Serialisierungs-Gson-TypeAdapter für die {@link Permission} Klasse.
 */
public class PermissionAdapter extends TypeAdapter<Permission>{

    /**
     * Serialisierungs-Methode. Schreibt folgendes in den "out" Parameter: 
     * {@link Permission#id}, 
     * {@link Permission#canRead}, 
     * {@link Permission#canWrite}, 
     * {@link Permission#role}, als Objekt, mit Attributen als IDs, falls existent, 
     * @param out JsonWriter Objekt, welches das serialisierte {@link Permission} Objekt erhält.
     * @param value Zu serialisierendes {@link Permission} Objekt.
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
     * Nicht funktionelle Entserialisierungs-Methode.
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
