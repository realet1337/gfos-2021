package com.realet.sip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
@NamedQueries({

    //FIXME: make this "like"
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.username = :name"),
    @NamedQuery(name = "User.findByEMail", query = "SELECT u FROM User u WHERE u.eMail = :eMail")

})

public class User implements Serializable{
    
    //directly matching database column names
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    
    @Column(nullable = false)
    private String username;

    @Column(name = "e_mail", nullable = false)
    private transient String eMail;

    private transient String pass;

    private String info;

    @Column(name = "profile_picture")
    private String profilePicture;
    
    @ManyToMany(mappedBy = "users")
    private transient  Set<Group> groups = new HashSet<>();
    
    @ManyToMany(mappedBy = "users")
    private transient Set<Role> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "owner")
    private transient List<Group> ownedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user1")
    private transient List<Chat> directChats1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2")
    private transient List<Chat> directChats2 = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private transient List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private transient List<Session> sessions = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfile_picture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public User(String username, String eMail, String pass, String info, String profilePicture) {
        this.username = username;
        this.eMail = eMail;
        this.info = info;
        this.profilePicture = profilePicture;
        this.pass = pass;
    }

    public User() {
        super();
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    
    

}