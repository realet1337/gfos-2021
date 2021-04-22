package com.realet.sip;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class ServiceApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() { 

        Set<Class<?>> set = new HashSet<>();
        set.add( CORSPreflightHandler.class );
        set.add( UsersResource.class );
        set.add( AuthResource.class );
        set.add( ChatsResource.class );
        set.add( ChatMessagesResource.class );
        set.add( GroupsResource.class );
        set.add( ChatWebsocketResource.class );
        set.add( ImagesResource.class );
        set.add( RolesResource.class );
        set.add( PermissionsResource.class );
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
        UserProfilesFacade.initialize(emf);

        UsersFacade.add(new User("Gerd","gerd@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin gerd", "this is my status", null, false, null));
        UsersFacade.add(new User("Peter","pete@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin peter", "this is my status", null, false, null));
        UsersFacade.remove(UsersFacade.findById(2).get());
        UsersFacade.add(new User("Frederik","frederik@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin Frederik", "this is my status", null, false, "7053bf6dc83b9b50d95c6e19eb23b850"));
        UsersFacade.add(new User("Peter","pete@a.com","$2y$10$Tm0jjN0GX5bU5WCiCVhX7ekmSBDAbZ35mQSbm4mQ.ByhgY90nzU3i","hallo ich bin peter", "this is my status", null, false, null));

        UserProfilesFacade.add(new UserProfile(UsersFacade.findById(1).get(), false, 100, 50));
        UserProfilesFacade.add(new UserProfile(UsersFacade.findById(3).get(), false, 100, 50));
        UserProfilesFacade.add(new UserProfile(UsersFacade.findById(4).get(), false, 100, 50));

        Set<User> group_users = new HashSet<User>(UsersFacade.findAll());

        Group group = new Group("coole gruppe", "beschreibung", "7053bf6dc83b9b50d95c6e19eb23b850", UsersFacade.findById(1).get());

        group.setUsers(group_users);

        GroupsFacade.add(group);

        Set<User> role_users = new HashSet<User>();
        role_users.add(UsersFacade.findById(1).get());
        role_users.add(UsersFacade.findById(4).get());

        Role role = new Role("cool role 1","#FFFFFF", GroupsFacade.findById(1).get(), true, 0);
        role.setUsers(role_users);

        RolesFacade.add(role);

        role_users = new HashSet<User>();
        role_users.add(UsersFacade.findById(3).get());
        role_users.add(UsersFacade.findById(4).get());

        role = new Role("cool role 2","#FF0000",GroupsFacade.findById(1).get(), true, 1);

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

        PermissionsFacade.add(new Permission(null, ChatsFacade.findById(1).get(), false, false));
        PermissionsFacade.add(new Permission(null, ChatsFacade.findById(3).get(), false, false));
        PermissionsFacade.add(new Permission(null, ChatsFacade.findById(4).get(), false, false));
        PermissionsFacade.add(new Permission(null, ChatsFacade.findById(5).get(), true, false));

        PermissionsFacade.add(new Permission(RolesFacade.findById(1).get(), ChatsFacade.findById(1).get(), true, true));
        PermissionsFacade.add(new Permission(RolesFacade.findById(1).get(), ChatsFacade.findById(4).get(), false, true));
        PermissionsFacade.add(new Permission(RolesFacade.findById(2).get(), ChatsFacade.findById(4).get(), true, false));

        PermissionsFacade.add(new Permission(RolesFacade.findById(1).get(), ChatsFacade.findById(5).get(), true, false));
        PermissionsFacade.add(new Permission(RolesFacade.findById(2).get(), ChatsFacade.findById(5).get(), true, false));
        
        
    }

}