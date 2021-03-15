package com.realet.sip;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

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
    }

    

}