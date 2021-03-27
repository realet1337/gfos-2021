package com.realet.sip;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class ChatMessagesFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        ChatMessagesFacade.em = em;

    }

    public static Optional<ChatMessage> findById(long id){

        ChatMessage chatMessage = em.find(ChatMessage.class, id);
        return chatMessage != null ? Optional.of(chatMessage) : Optional.empty();

    }

    public static Optional<ChatMessage> findMostRecentByChat(Chat chat){

        List<ChatMessage> chatMessages = em.createNamedQuery("ChatMessage.findMostRecentByChat", ChatMessage.class)
        .setParameter("chat", chat).getResultList();
        return chatMessages.isEmpty() ? Optional.empty() : Optional.of(chatMessages.get(chatMessages.size()-1));

        

    }

    public static void add(ChatMessage chatMessage){

        em.getTransaction().begin();
        em.persist(chatMessage);
        em.getTransaction().commit();

    }

    public static void update(ChatMessage chatMessage){

        em.getTransaction().begin();
        em.merge(chatMessage);
        em.getTransaction().commit();

    }

    public static void remove(ChatMessage chatMessage){

        em.getTransaction().begin();
        em.remove(chatMessage);
        em.getTransaction().commit();

    }

    
}
