package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Group;
import com.realet.sip.Role;
import com.realet.sip.User;

/**
 * A serialization Gson-TypeAdapter for the {@link Role} class.
 */

public class RoleAdapter extends TypeAdapter<Role> {

    /**
     * <ul>
     * <li>0: Don't serialize {@link Role#users}.</li> 
     * <li>1: Serialize {@link Role#users} as array of objects.</li>
     * </ul>
     */
    int verbosity;

    /**
     * Serialization method. Will write the following to "out" parameter: 
     * {@link Role#id}, 
     * {@link Role#name}, 
     * {@link Role#color}, 
     * {@link Role#group} as an object if existent, 
     * {@link Role#priority}, 
     * {@link Role#users} depending on {@link RoleAdapter#verbosity},
     * {@link Role#admin}. 
     * @param out JsonWriter object that will receive the serialized {@link Role} Object.
     * @param value {@link Role} object that will be serialized.
     * @throws IOException
     */
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
        out.name("admin");
        out.value(value.isAdmin());
        out.endObject();
    }

    /**
     * Non-functional deserialization method
     * @param in
     * @return null
     * @throws IOException
     */

    @Override
    public Role read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Constructor for {@link RoleAdapter}
     * @param verbosity Value for {@link RoleAdapter#verbosity}
     */
    public RoleAdapter(int verbosity) {
        this.verbosity = verbosity;
    }
    
}
