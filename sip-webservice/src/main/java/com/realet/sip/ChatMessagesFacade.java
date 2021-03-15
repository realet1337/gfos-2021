package com.realet.sip;


import java.util.Optional;

import javax.persistence.EntityManager;

public class ChatMessagesFacade {

    static EntityManager em;

    public static void initialize(EntityManager em){

        ChatMessagesFacade.em = em;

    }

    public static Optional<ChatMessage> findById(long id){

        ChatMessage chatMessage = em.find(ChatMessage.class, id);
        return chatMessage != null ? Optional.of(chatMessage) : Optional.empty();

    }

    public static void add(ChatMessage chatMessage){

        em.getTransaction().begin();
        em.persist(chatMessage);
        em.getTransaction().commit();

    }

    public static void remove(ChatMessage chatMessage){

        em.getTransaction().begin();
        em.remove(chatMessage);
        em.getTransaction().commit();

    }

    
}
