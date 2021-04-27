package com.realet.sip;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Die HauptKlasse der Anwendung. Registriert alle RESTEasy Resource-Klassen und initialisiert alle Facade-Klassen.
 */
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
        set.add( UserProfilesResource.class );
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
    }

}