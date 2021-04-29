package com.realet.sip;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Manager-Klasse für {@link UserProfile UserProfiles}.
 */
public class UserProfilesFacade {

    /**
     * Die EntityManagerFactory, die für alle Operationen von {@link UserProfilesFacade} verwendet wird.
     */
    static EntityManagerFactory emf;

    /**
     * Setzt {@link UserProfilesFacade#emf}.
     */
    public static void initialize(EntityManagerFactory emf){

        UserProfilesFacade.emf = emf;

    }

    public static Optional<UserProfile> findById(long id){

        EntityManager em = emf.createEntityManager();

        UserProfile userProfile = em.find(UserProfile.class, id);
        return userProfile != null ? Optional.of(userProfile) : Optional.empty();

    }


    public static void add(UserProfile userProfile){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(userProfile);
        em.getTransaction().commit();

    }

    public static void update(UserProfile userProfile){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(userProfile);
        em.getTransaction().commit();

    }

    public static void remove(UserProfile userProfile){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        //this needs to be done to mark the UserProfile as "managed"
        userProfile = em.merge(userProfile);
        em.remove(userProfile);
        em.getTransaction().commit();

    }

    
}
