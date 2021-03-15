package com.realet.sip;

import java.util.List;
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

    public static void add(Chat chat){

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
