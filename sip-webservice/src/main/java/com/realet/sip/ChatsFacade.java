package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

public class ChatsFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        ChatsFacade.em = em;

    }

    public static Optional<Chat> findById(long id){

        Chat chat = em.find(Chat.class, id);
        return chat != null ? Optional.of(chat) : Optional.empty();

    }

    public static Optional<Chat> findByUsers(User user1, User user2){

        Chat chat = em.createNamedQuery("Chat.findByUsers", Chat.class).setParameter("user1", user1).setParameter("user2", user2).getSingleResult();
        return chat != null ? Optional.of(chat) : Optional.empty();

    }

    public static List<Chat> findDirectChatByUser(User user){

        List<Chat> chats = em.createNamedQuery("Chat.findDirectChatByUser", Chat.class).setParameter("user", user).getResultList();
        return chats;

    }

    public static void add(Chat chat){

        if(ChatsFacade.findByUsers(chat.getUser1(), chat.getUser2()).isPresent()){

            throw new IllegalArgumentException("This chat already exists.");

        }

        em.getTransaction().begin();
        em.persist(chat);
        em.getTransaction().commit();

    }

    public static void remove(Chat chat){

        em.getTransaction().begin();
        em.remove(chat);
        em.getTransaction().commit();

    }

    
}
