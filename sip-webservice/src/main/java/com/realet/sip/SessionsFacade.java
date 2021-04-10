package com.realet.sip;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SessionsFacade {

    static EntityManagerFactory emf;

    public static void initialize(EntityManagerFactory emf){

        SessionsFacade.emf = emf;

    }

    public static Optional<Session> findByToken(String token){

        EntityManager em = emf.createEntityManager();

        Session session = em.find(Session.class, token);
        return session != null ? Optional.of(session) : Optional.empty();

    }

    public static long findUserIdByToken(String token) throws IllegalAccessException{

        EntityManager em = emf.createEntityManager();

        Session session = em.find(Session.class, token);
        if(session != null){
            em.getTransaction().begin();
            session.getUser().setLastSeen(new Date());
            em.getTransaction().commit();
            
            return session.getUser().getId();
        }
        throw new IllegalAccessException("No session was found");

    }

    public static void add(Session session){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(session);
        em.getTransaction().commit();

    }

    //!! doesn't need update method

    public static void remove(Session session){
        
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        session = em.merge(session);
        em.remove(session);
        em.getTransaction().commit();

    }

    
}
