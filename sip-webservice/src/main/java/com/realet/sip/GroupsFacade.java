package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Manager-Klasse für {@link Group Groups}.
 */
public class GroupsFacade {

    /**
     * Die EntityManagerFactory, die für alle Operationen von {@link GroupsFacade} verwendet wird.
     */
    static EntityManagerFactory emf;

    /**
     * Setzt {@link GroupsFacade#emf}.
     */
    public static void initialize(EntityManagerFactory emf){

        GroupsFacade.emf = emf;

    }

    public static Optional<Group> findById(long id){

        EntityManager em = emf.createEntityManager();

        Group group = em.find(Group.class, id);
        return group != null ? Optional.of(group) : Optional.empty();

    }

    /**
     * Findet alle gemeinsamen {@link Group Groups} zweier {@link User}.
     */
    public static List<Group> findShared(User user1, User user2){

        EntityManager em = emf.createEntityManager();

        return em.createNamedQuery("Group.findShared", Group.class).setParameter("user1", user1).setParameter("user2", user2).getResultList();

    }

    public static void add(Group group){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();

    }

    public static void update(Group group){
        
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(group);
        em.getTransaction().commit();

    }

    public static void remove(Group group){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        group = em.merge(group);
        em.remove(group);
        em.getTransaction().commit();

    }

    
}
