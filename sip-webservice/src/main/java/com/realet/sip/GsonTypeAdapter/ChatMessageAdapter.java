package com.realet.sip.GsonTypeAdapter;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.realet.sip.Chat;
import com.realet.sip.ChatMessage;
import com.realet.sip.User;


/**
 * A serialization Gson-TypeAdapter for the {@link ChatMessage} class.
 */
public class ChatMessageAdapter extends TypeAdapter<ChatMessage> {

    /**
     * <ul>
     * <li>0: Don't serialize any attributes as objects, instead just provide the ID of that object.</li> 
     * <li>1: Serialize {@link ChatMessage#author} as object, all other object attributes as IDs.</li>
     * <li>2: Serialize {@link ChatMessage#author}, {@link ChatMessage#chat} as objects.</li>
     * </ul>
     */
    int verbosity;

    /**
     * Serialization method. Will write the following to "out" parameter: 
     * {@link ChatMessage#id}, 
     * {@link ChatMessage#content}, 
     * {@link ChatMessage#sent}, 
     * {@link ChatMessage#expires} if existent, 
     * {@link ChatMessage#chat} depending on {@link ChatMessageAdapter#verbosity}, 
     * {@link ChatMessage#author} depending on {@link ChatMessageAdapter#verbosity},
     * {@link ChatMessage#edited} if existent. 
     * @param out JsonWriter object that will receive the serialized {@link ChatMessage} Object.
     * @param value {@link ChatMessage} object that will be serialized.
     * @throws IOException
     */

    @Override
    public void write(JsonWriter out, ChatMessage value) throws IOException {
        out.beginObject();
        out.name("id");
        out.value(value.getId());
        out.name("content");
        out.value(value.getContent());
        out.name("sent");
        out.value(value.getSent().toInstant().toString());
        out.name("expires");
        out.value(value.getExpires() != null ? value.getExpires().toInstant().toString() : null);
        if(verbosity >= 2){
            out.name("chat");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(Chat.class, new ChatAdapter()).create().toJson(value.getChat()));
        }
        else{
            out.name("chatId");
            out.value(value.getChat().getId());
        }
        if(verbosity >= 1){
            out.name("author");
            out.jsonValue(new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create().toJson(value.getAuthor()));
        }
        else{
            out.name("authorId");
            out.value(value.getAuthor().getId());
        }
        out.name("edited");
        out.value(value.getEdited() != null ? value.getEdited().toInstant().toString() : null);
        out.endObject();
        
    }

    /**
     * Non-functional deserialization method
     * @param in
     * @return null
     * @throws IOException
     */

    @Override
    public ChatMessage read(JsonReader in) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Constructor for {@link ChatMessageAdapter}
     * @param verbosity Value for {@link ChatMessageAdapter#verbosity}
     */
    public ChatMessageAdapter(int verbosity) {
        this.verbosity = verbosity;
    }
    
}
