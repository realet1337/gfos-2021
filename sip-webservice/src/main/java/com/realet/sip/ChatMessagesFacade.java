package com.realet.sip;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

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

    public static List<ChatMessage> find(Chat chat, int count, long before, long after){

        //this is very annoying but duplicate class names so yea
        org.hibernate.Session session = emf.unwrap(SessionFactory.class).openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChatMessage> criteriaQuery = criteriaBuilder.createQuery(ChatMessage.class);
        ArrayList<Predicate> predicates = new ArrayList<>();

        Root<ChatMessage> root = criteriaQuery.from(ChatMessage.class);
        criteriaQuery.select(root);
        predicates.add(criteriaBuilder.equal(root.get("chat"), chat));
        
        //do not select expired messages
        predicates.add(criteriaBuilder.or(
            criteriaBuilder.greaterThan(root.get("expires"), new Date()),
            criteriaBuilder.isNull(root.get("expires"))
        ));
        
        if(before != 0){
            predicates.add(criteriaBuilder.lessThan(root.get("id"), before));
        }

        if(after != 0){
            predicates.add(criteriaBuilder.greaterThan(root.get("id"), after));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

        if(after == 0){
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        }
        else{
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
        }

        Query<ChatMessage> query = session.createQuery(criteriaQuery);
        query.setMaxResults(count);
        List<ChatMessage> result = query.getResultList();

        //small IDs at start
        if(after == 0){
            Collections.reverse(result);
        }

        return result;

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

    public static ChatMessage updateContent(ChatMessage chatMessage){
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        ChatMessage currentMessage = em.find(ChatMessage.class, chatMessage.getId());
        currentMessage.setContent(chatMessage.getContent());
        em.getTransaction().commit();

        return currentMessage;
    }

    public static void remove(ChatMessage chatMessage){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        chatMessage = em.merge(chatMessage);
        em.remove(chatMessage);
        em.getTransaction().commit();

    }

    
}
