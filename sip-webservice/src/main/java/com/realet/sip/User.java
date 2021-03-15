package com.realet.sip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
@NamedQueries({

    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.username = :name")

})

public class User{
    
    //directly matching database column names
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    
    @Column(nullable = false)
    private String username;

    private String info;
    private String profile_picture;
    
    @ManyToMany(mappedBy = "users")
    private Set<Group> groups = new HashSet<>();
    
    @ManyToMany(mappedBy = "users")
    private Set<Role> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "owner")
    private List<Group> ownedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user1")
    private List<Chat> directChats1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2")
    private List<Chat> directChats2 = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<ChatMessage> chatMessages = new ArrayList<>();

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

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public User(String username, String info, String profile_picture) {
        this.username = username;
        this.info = info;
        this.profile_picture = profile_picture;
    }

    //FIXME: escape this, add quotes around attributes in final string, try having this parsed as JSON
    @Override
    public String toString() {
        return "User{id=" + id + ", info=" + info + ", profile_picture=" + profile_picture + ", username=" + username
                + "}";
    }

    public User() {
        super();
    }

    
    

}