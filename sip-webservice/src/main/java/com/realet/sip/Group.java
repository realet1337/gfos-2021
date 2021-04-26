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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Die {@link Group} Klasse modelliert eine Gruppe. Eine Gruppe ist eine Sammlung von {@link Role Roles}, {@link User Users} und {@link Chat Chats}. 
 * {@link Chat Chats}, die zu einer {@link Group Group} gehören, können Namen haben.
 * Definiert eine NamedQuery "Group.findShared", welche alle gemeinsamen Gruppen zweier {@link User} findet. Akzeptiert zwei Parameter: "user1", "user2".
 * Ein JPA-Entity für eine Tabelle names "Groups".
 */
@Entity
@Table(name="`Groups`")
@NamedQueries({
    @NamedQuery(name = "Group.findShared", query = "SELECT g FROM Group g JOIN g.users user1 JOIN g.users user2 WHERE user1 = :user1 AND user2 = :user2")
})
public class Group{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "group_name", nullable = false)
    private String name;

    @Column(name = "group_description")
    private String description;

    /**
     * Dateiname des Bilds ohne Pfad oder Endung.
     */
    private String picture;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "GroupMembership",
    		   joinColumns=@JoinColumn(name="group_id"),
    		   inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> users = new HashSet<>();

    @Cascade(CascadeType.DELETE)
    @OneToMany(mappedBy = "group")
    private List<Role> roles = new ArrayList<>();

    @Cascade(CascadeType.DELETE)
    @OneToMany(mappedBy = "group")
    private List<Chat> chats = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Group(String name, String description, String picture, User owner) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.owner = owner;
    }

    public Group() {
        super();
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    /**
     * Diese Methode vergleicht nur die {@link Group#id} beider Objekte.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
}
