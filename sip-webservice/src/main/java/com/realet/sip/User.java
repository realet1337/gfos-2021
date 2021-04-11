package com.realet.sip;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Users")
@NamedQueries({

    //FIXME: make this "like"
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.username = :name"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")

})

public class User{
    
    //directly matching database column names
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    
    @Column(nullable = false)
    private String username;

    @Column(name = "e_mail", nullable = false, unique = true)
    private String email;

    private String pass;

    private String info;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_seen")
    private Date lastSeen;

    @Column(name = "is_online")
    private boolean isOnline;

    @Column(name = "profile_picture")
    private String profilePicture;

    @ManyToMany
    @JoinTable(name = "BlockedUsers",
    		   joinColumns=@JoinColumn(name="user1_id"),
    		   inverseJoinColumns=@JoinColumn(name="user2_id"))
    private Set<User> blockedUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "BlockedUsers",
    		   joinColumns=@JoinColumn(name="user2_id"),
    		   inverseJoinColumns=@JoinColumn(name="user1_id"))
    private Set<User> blockedBy = new HashSet<>();
    
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

    @OneToMany(mappedBy = "user")
    private List<Session> sessions = new ArrayList<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(List<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public List<Chat> getDirectChats1() {
        return directChats1;
    }

    public void setDirectChats1(List<Chat> directChats1) {
        this.directChats1 = directChats1;
    }

    public List<Chat> getDirectChats2() {
        return directChats2;
    }

    public void setDirectChats2(List<Chat> directChats2) {
        this.directChats2 = directChats2;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public User(String username, String email, String pass, String info, String status, Date lastSeen,
            boolean isOnline, String profilePicture) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.info = info;
        this.status = status;
        this.lastSeen = lastSeen;
        this.isOnline = isOnline;
        this.profilePicture = profilePicture;
    }

    public Set<User> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(Set<User> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public Set<User> getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(Set<User> blockedBy) {
        this.blockedBy = blockedBy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

}