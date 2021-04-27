package com.realet.sip;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Die {@link Chat} Klasse ist ein Behälter für {@link ChatMessage ChatMessages}. Ein {@link Chat} kann entweder zu einer {@link Group} gehören, in diesem Fall hat er einen {@link Chat#name name}, oder zwei {@link User Users} haben, wodurch er eine Direkte-Unterhaltung modelliert. 
 * Definiert eine NamedQuery "Chat.findByUsers", welchen eine Direkt-Unterhaltung zweier {@link User} findet. Akzeptiert zwei Parameter: "user1", "user2".
 * Definiert eine NamedQuery "Chat.findByGroup", welche alle {@link Chat Chats} einer {@link Group} findet. Akzeptiert einen Parameter: "group".
 * Definiert eine NamedNativeQuery "Chat.findetDirectChatsByUser", welche alle Direkt-Unterhaltungen eines {@link User Users} findet und diese nach {@link ChatMessage#sent} der zuletzt gesendeten {@link ChatMessage} sortiert. Akzeptiert einen Parameter: 1: {@link User#id}.
 * Ein JPA-Entity für eine Tabelle names "Chats".
 */
@Entity
@Table(name = "Chats")
@NamedQueries({

    @NamedQuery(name = "Chat.findByUsers", query = "SELECT c FROM Chat c WHERE c.user1 = :user1 AND c.user2 = :user2 OR c.user1 = :user2 AND c.user2 = :user1"),
    @NamedQuery(name = "Chat.findByGroup", query = "SELECT c FROM Chat c WHERE c.group = :group")
})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Chat.findDirectChatsByUser", query = "SELECT * from Chats WHERE user1_id = ? OR user2_id = ? ORDER BY (SELECT MAX(sent) FROM ChatMessages WHERE chat_id = Chats.id) DESC", resultClass = Chat.class)
})
public class Chat{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    /**
     * Dieses Attribut existiert nur, wenn der {@link Chat} zu einer {@link Group} gehört ({@link Chat#group} ist gesetzt).
     */
    private String name;

    @Cascade(CascadeType.DELETE)
    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages = new ArrayList<>();

    @Cascade(CascadeType.DELETE)
    @OneToMany(mappedBy = "chat")
    private List<Permission> permissions = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Chat() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    /**
     * Diese Methode vergleicht nur die {@link Chat#id} beider Objekte.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Chat other = (Chat) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public Chat(Group group, User user1, User user2, String name) {
        this.group = group;
        this.user1 = user1;
        this.user2 = user2;
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    

}
