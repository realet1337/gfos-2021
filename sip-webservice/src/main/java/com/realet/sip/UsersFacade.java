package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

public class UsersFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        UsersFacade.em = em;

    }

    public static Optional<User> findById(long id){

        User user = em.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();

    }

    public static List<User> findAll(){

        return em.createQuery("from User").getResultList();

    }

    public static List<User> findByName(String name){

        return em.createNamedQuery("User.findByName", User.class).setParameter("name", name).getResultList();

    }

    public static void add(User user){

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

    }

    public static void remove(User user){

        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();

    }

    
}
