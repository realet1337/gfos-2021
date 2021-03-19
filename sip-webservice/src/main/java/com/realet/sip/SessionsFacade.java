package com.realet.sip;

import java.util.Optional;

import javax.persistence.EntityManager;

public class SessionsFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        SessionsFacade.em = em;

    }

    public static Optional<Session> findByToken(String token){

        Session session = em.find(Session.class, token);
        return session != null ? Optional.of(session) : Optional.empty();

    }

    public static void add(Session session){

        em.getTransaction().begin();
        em.persist(session);
        em.getTransaction().commit();

    }

    //!! doesn't need update method

    public static void remove(Session session){

        em.getTransaction().begin();
        em.remove(session);
        em.getTransaction().commit();

    }

    
}
