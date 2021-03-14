package com.realet.sip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Groups")
public class Group {

    @Id
    @GeneratedValue
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
    
}
