package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 * Manager-Klasse für {@link Chat Chats}.
 */
public class ChatsFacade {

    /**
     * Die EntityManagerFactory, die für alle Operationen von {@link ChatsFacade} verwendet wird.
     */
    static EntityManagerFactory emf;

    /**
     * Setzt {@link ChatsFacade#emf}
     */
    public static void initialize(EntityManagerFactory emf){

        ChatsFacade.emf = emf;

    }

    public static Optional<Chat> findById(long id){

        EntityManager em = emf.createEntityManager();

        Chat chat = em.find(Chat.class, id);
        return chat != null ? Optional.of(chat) : Optional.empty();

    }

    /**
     * Findet Direkt-Unterhaltung zweier {@link User}.
     */
    public static Optional<Chat> findByUsers(User user1, User user2){

        EntityManager em = emf.createEntityManager();

        try{
            Chat chat = em.createNamedQuery("Chat.findByUsers", Chat.class).setParameter("user1", user1).setParameter("user2", user2).getSingleResult();
            return Optional.of(chat);
        }catch(NoResultException e){
            return Optional.empty();
        }
        


    }

    /**
     * Findet alle Direkt-Unterhaltungen eines {@link User Users} und sortiert sie nach dem Sendedatum der zuletzt gesendeten {@link ChatMessage}.
     */
    public static List<Chat> findDirectChatsByUser(User user){

        EntityManager em = emf.createEntityManager();

        List<Chat> chats = em.createNamedQuery("Chat.findDirectChatsByUser", Chat.class).setParameter(1, user.getId()).setParameter(2, user.getId()).getResultList();
        return chats;

    }

    public static List<Chat> findByGroup(Group group){
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("Chat.findByGroup", Chat.class).setParameter("group", group).getResultList();
    }

    public static void add(Chat chat){

        EntityManager em = emf.createEntityManager();

        if(chat.getGroup() == null){
            if(ChatsFacade.findByUsers(chat.getUser1(), chat.getUser2()).isPresent()){

                throw new IllegalArgumentException("This chat already exists.");

            }
        }

        em.getTransaction().begin();
        em.persist(chat);
        em.getTransaction().commit();

    }

    public static void update(Chat chat){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(chat);
        em.getTransaction().commit();

    }

    public static void remove(Chat chat){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        chat = em.merge(chat);
        em.remove(chat);
        em.getTransaction().commit();

    }

    
}
