package com.realet.sip;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Permissions")
@NamedNativeQueries({
    //PARAMETERS:
    //?1 = userid
    //?2 = chatid
    //?3 = groupid
    @NamedNativeQuery(name = "Permission.findGroupChatPermissions", 
    query = "SELECT Permissions.* FROM Permissions JOIN Roles ON Roles.id = Permissions.role_id " 
    + "JOIN RoleMembership ON Roles.id = RoleMembership.role_id JOIN Users ON Users.id = RoleMembership.user_id " 
    + "WHERE Users.id = ?1 AND (Permissions.chat_id = ?2 or Permissions.chat_id IS NULL) AND Roles.group_id = ?3 " 
    + "ORDER BY Roles.priority ASC, Permissions.chat_id DESC LIMIT 1", 
    resultClass = Permission.class)
})
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private boolean canRead;

    private boolean canWrite;

    public Permission(Role role, Chat chat, boolean canRead, boolean canWrite) {
        this.role = role;
        this.chat = chat;
        this.canRead = canRead;
        this.canWrite = canWrite;
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
        Permission other = (Permission) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public Permission() {
    }
}
