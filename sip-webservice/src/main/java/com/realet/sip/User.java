package com.realet.sip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User{
    
    //directly matching database column names
    @Id
    private Integer id;
    
    private String username;
    private String info;
    private String profile_picture;
    
    @ManyToMany
    @JoinTable(name = "GroupMembership",
    		   joinColumns=@JoinColumn(name="user_id"),
    		   inverseJoinColumns=@JoinColumn(name="group_id"))
    private Set<Group> groups = new HashSet<>();
    
    @ManyToMany
    @JoinTable(name = "RoleMembership",
    		   joinColumns=@JoinColumn(name="user_id"),
    		   inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "owner")
    private List<Group> ownedGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user1")
    private List<Chat> directChats1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2")
    private List<Chat> directChats2 = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<ChatMessage> chatMessages = new ArrayList<>();

}