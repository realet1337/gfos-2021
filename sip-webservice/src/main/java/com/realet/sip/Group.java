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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Groups")

public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "group_name", nullable = false)
    private String name;

    @Column(name = "group_description")
    private String description;

    private String image;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(mappedBy = "groups")
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Group(String name, String description, String image, User owner) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.owner = owner;
    }

    //FIXME: escape this, add quotes around attributes in final string, try having this parsed as JSON
    @Override
    public String toString() {
        return "Group{description=" + description + ", id=" + id + ", image=" + image + ", name=" + name + ", owner="
                + owner + "}";
    }

    public Group() {
        super();
    }
    
}
