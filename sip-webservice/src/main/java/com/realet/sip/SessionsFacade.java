package com.realet.sip;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Manager-Klasse für {@link Session Sessions}
 */
public class SessionsFacade {

    /**
     * Die EntityManagerFactory die für alle Operationen von {@link SessionsFacade} verwendet wird.
     */
    static EntityManagerFactory emf;

    /**
     * Setzt {@link SessionsFacade#emf}.
     */
    public static void initialize(EntityManagerFactory emf){

        SessionsFacade.emf = emf;

    }

    public static Optional<Session> findByToken(String token){

        EntityManager em = emf.createEntityManager();

        Session session = em.find(Session.class, token);
        return session != null ? Optional.of(session) : Optional.empty();

    }

    /**
     * Findet eine {@link User#id} eines {@link Session#user} einer {@link Session} mit einem bestimmten {@link Session#token}.
     * @throws IllegalAccessException Falls keine Session mit dem gegebenen Token gefunden werden kann.
     */

    public static long findUserIdByToken(String token) throws IllegalAccessException{

        EntityManager em = emf.createEntityManager();

        Session session = em.find(Session.class, token);
        if(session == null || session.getExpires().compareTo(new Date()) < 0){
            throw new IllegalAccessException("No session was found");
        }

        em.getTransaction().begin();
        session.getUser().setLastSeen(new Date());
        em.getTransaction().commit();
        
        return session.getUser().getId();

    }

    /**
     * Entfernt alle {@link Session Sessions} mit einem bestimmten {@link Session#user}, außer eine {@link Session} mit einem bestimmten {@link Session#token}.
     */
    public static void removeAllUserSessionsExcept(String token, User user){

        EntityManager em = emf.createEntityManager();

        List<Session> sessions = em.createNamedQuery("Session.getAllUserSessionsExcept", Session.class)
        .setParameter("user", user)
        .setParameter("token", token)
        .getResultList();

        em.getTransaction().begin();

        for(Session s: sessions){
            em.remove(s);
        }

        em.getTransaction().commit();
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
