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

@Entity
@Table(name = "Chats")
@NamedQueries({

    @NamedQuery(name = "Chat.findByUsers", query = "SELECT c FROM Chat c WHERE c.user1 = :user1 AND c.user2 = :user2 OR c.user1 = :user2 AND c.user2 = :user1"),
    @NamedQuery(name = "Chat.findByGroup", query = "SELECT c FROM Chat c WHERE c.group = :group")
})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Chat.findDirectChatsByUser", query = "SELECT * from Chats WHERE user1_id = ? OR user2_id = ? ORDER BY (SELECT MAX(sent) FROM ChatMessages WHERE chat_id = Chats.id) DESC", resultClass = Chat.class)
})

/**
 * The chat class is a container for {@link ChatMessage ChatMessages}. It can either belong to a {@link Group}, in which case it has a {@link Chat#name name}, or have two {@link Users Users}, modelling a direct chat. 
 */
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
     * This is only set if the chat belongs to a {@link Group}, meaning that {@link Chat#group} is set.
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
     * This method only compares the {@link Chat#id} of both objects.
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
