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
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * Manager-Klasse für {@link ChatMessage ChatMessages}.
 */
public class ChatMessagesFacade {

    /**
     * Die EntityManagerFactory, die für alle Operationen von {@link ChatMessagesFacade} verwendet wird.
     */
    static EntityManagerFactory emf;

    /**
     * Setzt {@link ChatMessagesFacade#emf}
     */
    public static void initialize(EntityManagerFactory emf){

        ChatMessagesFacade.emf = emf;

    }

    public static Optional<ChatMessage> findById(long id){

        EntityManager em = emf.createEntityManager();

        ChatMessage chatMessage = em.find(ChatMessage.class, id);
        return chatMessage != null ? Optional.of(chatMessage) : Optional.empty();

    }

    /**
     * Benutzt die CriteriaQuery API um dynamisch {@link ChatMessage ChatMessages} nach bestimmten Kriterien zu finden. Diese werden aufsteigend nach {@link ChatMessage#id} sortiert.
     * @param chat In welchem {@link Chat} gesucht werden soll
     * @param count Wie viele {@link ChatMessage ChatMessages} gesammelt werden sollen.
     * @param before ID der {@link ChatMessage}, vor deren Sendedatum die gesuchten {@link ChatMessage ChatMessages} gesendet werden sein sollen. Wird ignoriert wenn der gegebene Wert 0 ist.
     * @param after ID der {@link ChatMessage}, nach deren Sendedatum die gesuchten {@link ChatMessage ChatMessages} gesendet werden sein sollen. Wird ignoriert wenn der gegebene Wert 0 ist.
     * @param unblockedById Spezifiziert die ID des Nutzers der die Abfrage stellt und schließt alle Nachrichten, deren Autor von dem ihm blockiert ist, aus.
     * @param reverseBlocking Legt fest, ob Nachrichten, deren Autor den Nutzer der die Abfrage stellt blockiert haben, eingschlossen werden sollen.
     * @return Gefundene {@link ChatMessage ChatMessages}
     */
    public static List<ChatMessage> find(Chat chat, int count, long before, long after, long unblockedById, long reverseBlocking){

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

        if(unblockedById  != 0){

            Subquery<Long> sub = criteriaQuery.subquery(Long.class);
            Root<User> subRoot = sub.from(User.class);
            SetJoin<User, User> subBlockedUsers = subRoot.join(User_.blockedUsers);
            sub.select(criteriaBuilder.count(subRoot.get(User_.id)));
            sub.where(
                criteriaBuilder.and(
                    criteriaBuilder.equal(subBlockedUsers.get(User_.id), root.get(ChatMessage_.author).get(User_.id)),
                    criteriaBuilder.equal(subRoot.get(User_.id), Long.valueOf(unblockedById))
                )
            );

            predicates.add(criteriaBuilder.lessThan(sub, 1L));

            if(reverseBlocking != 0){
                sub = criteriaQuery.subquery(Long.class);
                subRoot = sub.from(User.class);
                SetJoin<User, User> subBlockedBy = subRoot.join(User_.blockedBy);
                sub.select(criteriaBuilder.count(subRoot.get(User_.id)));
                
                sub.where(
                    criteriaBuilder.and(
                        criteriaBuilder.equal(subBlockedBy.get(User_.id), root.get(ChatMessage_.author).get(User_.id)),
                        criteriaBuilder.equal(subRoot.get(User_.id), Long.valueOf(unblockedById))
                    )
                );

                predicates.add(criteriaBuilder.lessThan(sub, 1L));
            }
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

    /**
     * Aktualisiert den {@link ChatMessage#content} einer {@link ChatMessage} und setzt den {@link ChatMessage#edited} Timestamp auf die aktuelle Zeit.
     * @return
     */
    public static ChatMessage updateContent(ChatMessage chatMessage){
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        ChatMessage currentMessage = em.find(ChatMessage.class, chatMessage.getId());
        currentMessage.setContent(chatMessage.getContent());
        currentMessage.setEdited(new Date());
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
