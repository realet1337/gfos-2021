package com.realet.sip;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.realet.sip.UsersResource;

@ApplicationPath("/api")
public class ServiceApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add( UsersResource.class );
        return set;
    }

}