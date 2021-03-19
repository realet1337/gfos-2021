package com.realet.sip;

import java.util.Optional;

import javax.persistence.EntityManager;

public class GroupsFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        GroupsFacade.em = em;

    }

    public static Optional<Group> findById(long id){

        Group group = em.find(Group.class, id);
        return group != null ? Optional.of(group) : Optional.empty();

    }

    public static void add(Group group){

        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();

    }

    public static void update(Group group){

        em.getTransaction().begin();
        em.merge(group);
        em.getTransaction().commit();

    }

    public static void remove(Group group){

        em.getTransaction().begin();
        em.remove(group);
        em.getTransaction().commit();

    }

    
}
