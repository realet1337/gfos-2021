package com.realet.sip;

import java.util.Optional;

import javax.persistence.EntityManager;

public class RolesFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        RolesFacade.em = em;

    }

    public static Optional<Role> findById(long id){

        Role role = em.find(Role.class, id);
        return role != null ? Optional.of(role) : Optional.empty();

    }

    public static void add(Role role){

        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();

    }

    public static void remove(Role role){

        em.getTransaction().begin();
        em.remove(role);
        em.getTransaction().commit();

    }

    
}
