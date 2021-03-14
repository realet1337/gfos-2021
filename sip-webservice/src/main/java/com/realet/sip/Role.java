package com.realet.sip;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private long id;

    @Column(name = "role_name", nullable = false)
    private String name;

    @Column(name = "role_color", nullable = false)
    private String color;
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private long permissions;

}
