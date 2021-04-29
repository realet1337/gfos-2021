package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;


import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Group;
import com.realet.sip.User;

/**
 * Ein Serialisierungs-Gson-TypeAdapter für die {@link Group} Klasse.
 */
public class GroupAdapter extends TypeAdapter<Group>{
    /**
     * Serialisierungs-Methode. Schreibt folgendes in den "out" Parameter: 
     * {@link Group#id}, 
     * {@link Group#description}, falls existent, 
     * {@link Group#name}, 
     * {@link Group#picture}, falls existent, 
     * {@link Group#owner}, als Objekt.
     * @param out JsonWriter Objekt, welches das serialisierte {@link Group} Objekt erhält.
     * @param value Zu serialisierendes {@link Group} Objekt.
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
     * Nicht funktionelle Entserialisierungs-Methode.
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
