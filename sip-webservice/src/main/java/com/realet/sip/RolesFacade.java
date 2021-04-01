package com.realet.sip;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RolesFacade {

    static EntityManagerFactory emf;

    public static void initialize(EntityManagerFactory emf){

        RolesFacade.emf = emf;

    }

    public static Optional<Role> findById(long id){

        EntityManager em = emf.createEntityManager();

        Role role = em.find(Role.class, id);
        return role != null ? Optional.of(role) : Optional.empty();

    }

    public static void add(Role role){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();

    }

    public static void update(Role role){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(role);
        em.getTransaction().commit();

    }

    public static void remove(Role role){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        role = em.merge(role);
        em.remove(role);
        em.getTransaction().commit();

    }

    
}
