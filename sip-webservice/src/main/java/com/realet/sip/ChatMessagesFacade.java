package com.realet.sip;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class ChatMessagesFacade {

    static EntityManagerFactory emf;

    public static void initialize(EntityManagerFactory emf){

        ChatMessagesFacade.emf = emf;

    }

    public static Optional<ChatMessage> findById(long id){

        EntityManager em = emf.createEntityManager();

        ChatMessage chatMessage = em.find(ChatMessage.class, id);
        return chatMessage != null ? Optional.of(chatMessage) : Optional.empty();

    }

    public static Optional<ChatMessage> findMostRecentByChat(Chat chat){

        EntityManager em = emf.createEntityManager();

        List<ChatMessage> chatMessages = em.createNamedQuery("ChatMessage.findMostRecentByChat", ChatMessage.class)
        .setParameter("chat", chat).getResultList();
        return chatMessages.isEmpty() ? Optional.empty() : Optional.of(chatMessages.get(chatMessages.size()-1));

        

    }

    public static void add(ChatMessage chatMessage){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(chatMessage);
        em.getTransaction().commit();

    }

    public static void update(ChatMessage chatMessage){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(chatMessage);
        em.getTransaction().commit();

    }

    public static void remove(ChatMessage chatMessage){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        chatMessage = em.merge(chatMessage);
        em.remove(chatMessage);
        em.getTransaction().commit();

    }

    
}
