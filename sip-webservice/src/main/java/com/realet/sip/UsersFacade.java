package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class UsersFacade {

    static EntityManagerFactory emf;

    public static void initialize(EntityManagerFactory emf){

        UsersFacade.emf = emf;

    }

    public static Optional<User> findById(long id){

        EntityManager em = emf.createEntityManager();

        User user = em.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();

    }

    public static List<User> findAll(){

        EntityManager em = emf.createEntityManager();

        return em.createQuery("from User", User.class).getResultList();

    }

    public static List<User> findByName(String name){

        EntityManager em = emf.createEntityManager();

        return em.createNamedQuery("User.findByName", User.class).setParameter("name", name).getResultList();

    }

    public static Optional<User> findByEmail(String email){

        EntityManager em = emf.createEntityManager();

        try{
            User user = em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getSingleResult();
            return Optional.of(user);
        }catch(NoResultException e){

            return Optional.empty();
            
        }

    }

    public static void add(User user){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

    }

    public static void update(User user){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();

    }

    public static void remove(User user){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        //this needs to be done to mark the user as "managed"
        user = em.merge(user);
        em.remove(user);
        em.getTransaction().commit();

    }

    
}
