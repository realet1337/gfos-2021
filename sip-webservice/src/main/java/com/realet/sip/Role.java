package com.realet.sip;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    private int id;

    @Column(name = "role_name")
    private String name;

    @Column(name = "role_color")
    private String color;
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

}
