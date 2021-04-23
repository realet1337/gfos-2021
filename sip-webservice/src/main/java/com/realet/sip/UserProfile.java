package com.realet.sip;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UserProfiles")
public class UserProfile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //OH? You're saying this should have been a One-To-One relationship? Sorry, I can't hear you over the surprisingly loud sound of not having to deal with Hibernates bullshit bi-directional consistency problems.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "reverse_blocking", nullable = false)
    private boolean reverseBlocking;

    @Column(name = "max_loaded_messages", nullable = false)
    private int maxLoadedMessages;

    @Column(name = "message_chunk_size", nullable = false)
    private int messageChunkSize;

    public boolean isReverseBlocking() {
        return reverseBlocking;
    }

    public void setReverseBlocking(boolean reverseBlocking) {
        this.reverseBlocking = reverseBlocking;
    }

    public int getMaxLoadedMessages() {
        return maxLoadedMessages;
    }

    public void setMaxLoadedMessages(int maxLoadedMessages) {
        this.maxLoadedMessages = maxLoadedMessages;
    }

    public int getMessageChunkSize() {
        return messageChunkSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile(User user, boolean reverseBlocking, int maxLoadedMessages, int messageChunkSize) {
        this.user = user;
        this.reverseBlocking = reverseBlocking;
        this.maxLoadedMessages = maxLoadedMessages;
        this.messageChunkSize = messageChunkSize;
    }

    public void setMessageChunkSize(int messageChunkSize) {
        this.messageChunkSize = messageChunkSize;
    }

    public UserProfile() {
    }
}
