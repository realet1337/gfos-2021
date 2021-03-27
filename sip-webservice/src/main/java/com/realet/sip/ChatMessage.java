package com.realet.sip;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "ChatMessages")
@NamedQueries({
    @NamedQuery(name = "ChatMessage.findMostRecentByChat", query = "select cm from ChatMessage cm where cm.sent = (select max(cm1.sent) from ChatMessage cm1 where cm1.chat = :chat) and cm.chat = :chat")
})
public class ChatMessage implements Serializable{

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Expose
    @Column(nullable = false)
    private String content;

    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    private Date expires;

    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;
    
    @Expose
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Expose
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

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

    public ChatMessage(String content, Date expires, Date sent, Chat chat, User author) {
        this.content = content;
        this.expires = expires;
        this.sent = sent;
        this.chat = chat;
        this.author = author;
    }


    public ChatMessage() {
        super();
    }


    
}
