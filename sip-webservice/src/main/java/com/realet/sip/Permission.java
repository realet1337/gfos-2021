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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Die {@link Permission}-Klasse ist ein Konfigurations-Objekt für Lese-/Schreibzugriff auf einen {@link Chat}. 
 * Es beschreibt entweder die grundlegenden Zugangsberechtigungen zu einem {@link Chat Chat} (falls {@link Permission#role} nicht gesetzt ist),
 * oder andernfalls die Zugangsberechtigungen einer {@link Role} zu einem {@link Chat Chat}. 
 * {@link Permission Permissions} funktionieren nach OR-Prinzip. 
 * Wenn also eine {@link Permission} Zugriff erlaubt, ist es irrelevant, wie viele {@link Permission Permissions} dies nicht tun.
 * Definiert eine NamedQuery "Permission.findByRoleAndChat", welche eine {@link Permission} einer {@link Role} zu einem {@link Chat} findet. Akzeptiert zwei Parameter: "role", "chat".
 * Definiert eine NamedNativeQuery "Permission.findGroupChatPermissions", welche 1-3 relevante {@link Permission Permissions} eines {@link User Users} zu einem {@link Chat} findet; 
 *  Einen Eintrag mit {@link Permission#canRead} == true, falls vorhanden, 
 *  einen Eintrag mit {@link Permission#canWrite} == true, falls vorhanden 
 *  und die Grundberechtigungen zu einem {@link Chat},
 *  was alle notwendingen Informationen bereitstellt um die Berechtigungen eines {@link User Users} zu erfahren.
 *  Akzeptiert einen Parameter: 1: {@link User#id}, 2: {@link Chat#id}.
 * Ein JPA-Entity für eine Tabelle names "Permissions".
 */
@Entity
@Table(name = "Permissions")
@NamedQueries({
    @NamedQuery(name = "Permission.findByRoleAndChat", query = "SELECT p FROM Permission p WHERE p.role = :role AND p.chat = :chat")
})
@NamedNativeQueries({
    //PARAMETERS:
    //?1 = userid
    //?2 = chatid

    //This spits out 1-3 permissions in a way that (inclusive) OR-ing all of the canRead/canWrite values yields the permissions.

    @NamedNativeQuery(name = "Permission.findGroupChatPermissions", 
    query = "(SELECT Permissions.* "
    + " FROM   Permissions "
    + "        LEFT OUTER JOIN Roles "
    + "                     ON Roles.id = Permissions.role_id "
    + "        LEFT OUTER JOIN RoleMembership "
    + "                     ON Roles.id = RoleMembership.role_id "
    + "        LEFT OUTER JOIN Users "
    + "                     ON RoleMembership.user_id = Users.id "
    + " WHERE  Permissions.chat_id = ?2 "
    + "        AND Users.id = ?1 "
    + "        AND Permissions.canWrite "
    + " ORDER  BY Roles.priority ASC "
    + " LIMIT  1) "
    + "UNION "
    + "(SELECT Permissions.* "
    + " FROM   Permissions "
    + "        LEFT OUTER JOIN Roles "
    + "                     ON Roles.id = Permissions.role_id "
    + "        LEFT OUTER JOIN RoleMembership "
    + "                     ON Roles.id = RoleMembership.role_id "
    + "        LEFT OUTER JOIN Users "
    + "                     ON RoleMembership.user_id = Users.id "
    + " WHERE  Permissions.chat_id = ?2 "
    + "        AND Users.id = ?1 "
    + "        AND Permissions.canRead "
    + " ORDER  BY Roles.priority ASC "
    + " LIMIT  1) "
    + "UNION "
    + "(SELECT * "
    + " FROM   Permissions "
    + " WHERE  Permissions.role_id IS NULL "
    + "        AND Permissions.chat_id = ?2);", 
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
    @JoinColumn(name = "chat_id", nullable = false)
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

    /**
     * Diese Methode vergleicht nur die {@link Permission#id} beider Objekte.
     */
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

    public void setId(long id) {
        this.id = id;
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
