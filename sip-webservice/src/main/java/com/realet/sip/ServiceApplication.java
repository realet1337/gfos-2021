package com.realet.sip;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.realet.sip.UsersResource;

@ApplicationPath("/")
public class ServiceApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> set = new HashSet<>();
        set.add( UsersResource.class );
        return set;
    }

    public ServiceApplication() {

        EntityManager em = Persistence.createEntityManagerFactory("sip-webservicePersistenceUnit").createEntityManager();

        UsersFacade.initialize(em);
        GroupsFacade.initialize(em);
        RolesFacade.initialize(em);
        ChatsFacade.initialize(em);
        ChatMessagesFacade.initialize(em);

        UsersFacade.add(new User("Gerd","hallo ich bin gerd",null));
        UsersFacade.add(new User("Peter","hallo ich bin peter",null));
        UsersFacade.add(new User("Frederik","hallo ich bin Frederik",null));

        Set<User> group_users = new HashSet<User>(UsersFacade.findAll());

        GroupsFacade.add(new Group("coole gruppe", "beschreibung", null, UsersFacade.findById(1).get(), group_users));

        Set<User> role_users = new HashSet<User>();
        role_users.add(UsersFacade.findById(1).get());
        role_users.add(UsersFacade.findById(2).get());

        RolesFacade.add(new Role("cool role 1","#FFFFFF",GroupsFacade.findById(1).get(),0,role_users));

        role_users = new HashSet<User>();
        role_users.add(UsersFacade.findById(3).get());

        RolesFacade.add(new Role("cool role 1","#FF0000",GroupsFacade.findById(1).get(),0,role_users));

        ChatsFacade.add(new Chat(GroupsFacade.findById(1).get(),null,null));

        ChatsFacade.add(new Chat(null, UsersFacade.findById(2).get(), UsersFacade.findById(3).get()));

        ChatMessagesFacade.add(new ChatMessage("hi there", null, null, ChatsFacade.findById(2).get(), UsersFacade.findById(2).get()));

        ChatMessagesFacade.add(new ChatMessage("general kenobi", null, null, ChatsFacade.findById(2).get(), UsersFacade.findById(3).get()));

        ChatMessagesFacade.add(new ChatMessage("hi there", null, null, ChatsFacade.findById(1).get(), UsersFacade.findById(1).get()));
        ChatMessagesFacade.add(new ChatMessage("hi there", null, null, ChatsFacade.findById(1).get(), UsersFacade.findById(2).get()));
        ChatMessagesFacade.add(new ChatMessage("hi there", null, null, ChatsFacade.findById(1).get(), UsersFacade.findById(3).get()));




        

    }

    

}