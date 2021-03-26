package com.realet.sip;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "Roles")
public class Role implements Serializable{

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Expose
    @Column(name = "role_name", nullable = false)
    private String name;

    @Expose
    @Column(name = "role_color", nullable = false)
    private String color;
    
    @ManyToMany
    @JoinTable(name = "RoleMembership",
    		   joinColumns=@JoinColumn(name="role_id"),
    		   inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private long permissions;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public long getPermissions() {
        return permissions;
    }

    public void setPermissions(long permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers(){

        return users;

    }

    public Role(String name, String color, Group group, long permissions, Set<User> users) {
        this.name = name;
        this.color = color;
        this.group = group;
        this.permissions = permissions;
        this.users = users;
    }

    public Role() {
        super();
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
