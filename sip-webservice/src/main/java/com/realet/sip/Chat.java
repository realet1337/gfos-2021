package com.realet.sip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "Chats")
@NamedQueries({

    @NamedQuery(name = "Chat.findByUsers", query = "SELECT c FROM Chat c WHERE c.user1 = :user1 AND c.user2 = :user2 OR c.user1 = :user2 AND c.user2 = :user1"),
    @NamedQuery(name = "Chat.Chat.findDirectChatByUser", query = "SELECT c FROM Chat c WHERE c.user1 = :user OR c.user2 = :user")

})

public class Chat implements Serializable{
    
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Expose
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Expose
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @Expose
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages = new ArrayList<>();

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

    public Chat(Group group, User user1, User user2) {
        this.group = group;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Chat() {
        super();
    }
    

}
