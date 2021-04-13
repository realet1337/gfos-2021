package com.realet.sip;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
 

//import com.realet.sip.UsersResource;

@ApplicationPath("/")
public class ServiceApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> set = new HashSet<>();
        set.add( UsersResource.class );
        set.add( AuthResource.class );
        set.add( ChatsResource.class );
        set.add( ChatMessagesResource.class );
        set.add( CORSPreflightHandler.class );
        set.add( GroupsResource.class );
        set.add( ChatWebsocketResource.class );
        return set;
    }

    public ServiceApplication() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sip-webservicePersistenceUnit");

        UsersFacade.initialize(emf);
        GroupsFacade.initialize(emf);
        RolesFacade.initialize(emf);
        ChatsFacade.initialize(emf);
        ChatMessagesFacade.initialize(emf);
        SessionsFacade.initialize(emf);
        PermissionsFacade.initialize(emf);

        UsersFacade.add(new User("Gerd","gerd@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin gerd", "this is my status", null, false, null));
        UsersFacade.add(new User("Peter","pete@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin peter", "this is my status", null, false, null));
        UsersFacade.remove(UsersFacade.findById(2).get());
        UsersFacade.add(new User("Frederik","frederik@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin Frederik", "this is my status", null, false, "7053bf6dc83b9b50d95c6e19eb23b850"));
        UsersFacade.add(new User("Peter","pete@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin peter", "this is my status", null, false, null));

        Set<User> group_users = new HashSet<User>(UsersFacade.findAll());

        Group group = new Group("coole gruppe", "beschreibung", "7053bf6dc83b9b50d95c6e19eb23b850", UsersFacade.findById(1).get());

        group.setUsers(group_users);

        GroupsFacade.add(group);

        Set<User> role_users = new HashSet<User>();
        role_users.add(UsersFacade.findById(1).get());
        role_users.add(UsersFacade.findById(4).get());

        Role role = new Role("cool role 1","#FFFFFF", GroupsFacade.findById(1).get(), false, 1);
        role.setUsers(role_users);

        RolesFacade.add(role);

        role_users = new HashSet<User>();
        role_users.add(UsersFacade.findById(3).get());
        role_users.add(UsersFacade.findById(4).get());

        role = new Role("cool role 2","#FF0000",GroupsFacade.findById(1).get(), false, 2);

        role.setUsers(role_users);

        RolesFacade.add(role);

        ChatsFacade.add(new Chat(GroupsFacade.findById(1).get(),null,null, "text-chat"));

        ChatsFacade.add(new Chat(null, UsersFacade.findById(4).get(), UsersFacade.findById(3).get(), null));

        ChatsFacade.add(new Chat(GroupsFacade.findById(1).get(),null,null, "text-chat2"));
        ChatsFacade.add(new Chat(GroupsFacade.findById(1).get(),null,null, "text-chat3"));
        ChatsFacade.add(new Chat(GroupsFacade.findById(1).get(),null,null, "text-chat4"));

        ChatMessagesFacade.add(new ChatMessage("hi there", null, Date.from(new Date().toInstant().plus(-1, ChronoUnit.DAYS)), ChatsFacade.findById(2).get(), UsersFacade.findById(4).get(), null));

        ChatMessagesFacade.add(new ChatMessage("hi there", null, new Date(), ChatsFacade.findById(2).get(), UsersFacade.findById(4).get(), null));

        ChatMessagesFacade.add(new ChatMessage("general kenobi", null, new Date(), ChatsFacade.findById(2).get(), UsersFacade.findById(3).get(), null));

        ChatMessagesFacade.add(new ChatMessage("hi there", null, new Date(), ChatsFacade.findById(1).get(), UsersFacade.findById(1).get(), null));
        ChatMessagesFacade.add(new ChatMessage("hi there", null, new Date(), ChatsFacade.findById(1).get(), UsersFacade.findById(4).get(), null));
        ChatMessagesFacade.add(new ChatMessage("hi there", null, new Date(), ChatsFacade.findById(1).get(), UsersFacade.findById(3).get(), null));

        SessionsFacade.add(new Session("aaaaaaa", UsersFacade.findById(4).get(), new Date()));

        ChatsFacade.add(new Chat(null, UsersFacade.findById(4).get(), UsersFacade.findById(1).get(), null));

        role = RolesFacade.findById(1).get();

        PermissionsFacade.add(new Permission(role, null, false, false));

        PermissionsFacade.add(new Permission(role, ChatsFacade.findById(4).get(), true, true));

        PermissionsFacade.add(new Permission(role, ChatsFacade.findById(1).get(), true, false));

        role = RolesFacade.findById(2).get();

        PermissionsFacade.add(new Permission(role, null, true, true));

        PermissionsFacade.add(new Permission(role, ChatsFacade.findById(4).get(), false, false));
        
    }

}