package com.realet.sip;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PermissionsFacade {

    static EntityManagerFactory emf;

    public static void initialize(EntityManagerFactory emf){

        PermissionsFacade.emf = emf;

    }

    public static Optional<Permission> findById(long id){

        EntityManager em = emf.createEntityManager();

        Permission permission = em.find(Permission.class, id);
        return permission != null ? Optional.of(permission) : Optional.empty();

    }

    public static void add(Permission permission){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(permission);
        em.getTransaction().commit();

    }

    public static void update(Permission permission){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(permission);
        em.getTransaction().commit();

    }

    public static void remove(Permission permission){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        permission = em.merge(permission);
        em.remove(permission);
        em.getTransaction().commit();

    }

    
}
