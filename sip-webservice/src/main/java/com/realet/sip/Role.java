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

@Entity
@Table(name = "Roles")
@NamedQueries({
    @NamedQuery(name = "Role.findGroupRolesOrderedByPriority", query = "SELECT r FROM Role r WHERE r.group = :group ORDER BY r.priority ASC"),
    @NamedQuery(name = "Role.findUserGroupRoles", query = "SELECT r FROM Role r JOIN r.users u WHERE u = :user AND r.group = :group")
})
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "role_name", nullable = false)
    private String name;

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


    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    private long priority;

    @OneToMany(mappedBy = "role")
    private List<Permission> permissions = new ArrayList<>();

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

    public Set<User> getUsers(){

        return users;

    }

    public Role() {
        super();
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
        Role other = (Role) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }


    public Role(String name, String color, Group group, boolean isAdmin, long priority) {
        this.name = name;
        this.color = color;
        this.group = group;
        this.isAdmin = isAdmin;
        this.priority = priority;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

}
