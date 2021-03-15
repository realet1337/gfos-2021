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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="`Groups`")

public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "group_name", nullable = false)
    private String name;

    @Column(name = "group_description")
    private String description;

    private String picture;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "GroupMembership",
    		   joinColumns=@JoinColumn(name="group_id"),
    		   inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    private List<Chat> chat = new ArrayList<>();

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

    public void setPicture(String image) {
        this.picture = picture;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addUser(User user){

        users.add(user);

    }

    public void removeUser(User user){

        users.remove(user);

    }

    public Set<User> getUsers(){

        return users;

    }

    public Group(String name, String description, String picture, User owner, Set<User> users) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.owner = owner;
        this.users = users;
    }

    //FIXME: escape this, add quotes around attributes in final string, try having this parsed as JSON
    @Override
    public String toString() {
        return "Group{description=" + description + ", id=" + id + ", picture=" + picture + ", name=" + name + ", owner="
                + owner + "}";
    }

    public Group() {
        super();
    }
    
}
