package com.realet.sip;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Diese Klasse repräsentiert eine einzelne {@link ChatMessage}. Sie enthält Text und gehört zu einem {@link Chat}. 
 * Eine {@link ChatMessage} hat einen {@link ChatMessage#author} und enthält Informationen darüber, wann sie geschrieben, und das letzte Mal bearbeitet wurde. 
 * Ein JPA-Entity für eine Tabelle names "ChatMessages".
 */

@Entity
@Table(name = "ChatMessages")
public class ChatMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String content;

    /**
     * Nicht implementiert.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date expires;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date sent;
    
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    private Date edited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public ChatMessage(String content, Date expires, Date sent, Chat chat, User author, Date edited) {
        this.content = content;
        this.expires = expires;
        this.sent = sent;
        this.chat = chat;
        this.author = author;
        this.edited = edited;
    }


    public ChatMessage() {
        super();
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    /**
     * Diese Methode vergleicht nur die {@link ChatMessage#id} beider Objekte.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChatMessage other = (ChatMessage) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
}
