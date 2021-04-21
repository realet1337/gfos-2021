package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class PermissionsFacade {

    static EntityManagerFactory emf;

    public static void initialize(EntityManagerFactory emf){

        PermissionsFacade.emf = emf;

    }

    public static Optional<Permission> findById(long id){

        EntityManager em = emf.createEntityManager();

        Permission permission = em.find(Permission.class, id);
        return permission != null ? Optional.of(permission) : Optional.empty();

    }

    public static List<Permission> findGroupChatPermissions(long chatId, long userId) throws IllegalArgumentException{

        EntityManager em = emf.createEntityManager();

        return em.createNamedQuery("Permission.findGroupChatPermissions", Permission.class)
        .setParameter(1, userId)
        .setParameter(2, chatId)
        .getResultList();
    }

    public static Optional<Permission> findByRoleAndChat(Role role, Chat chat){
        
        EntityManager em = emf.createEntityManager();

        try{
            return Optional.of(em.createNamedQuery("Permission.findByRoleAndChat", Permission.class).setParameter("chat", chat).setParameter("role", role).getSingleResult());
        }
        catch(NoResultException e){
            return Optional.empty();
        }

    }

    

    public static void add(Permission permission){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(permission);
        em.getTransaction().commit();

    }

    public static void update(Permission permission){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(permission);
        em.getTransaction().commit();

    }

    public static void remove(Permission permission){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        permission = em.merge(permission);
        em.remove(permission);
        em.getTransaction().commit();

    }

    
}
