package com.realet.sip;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Die {@link Session}-Klasse modelliert eine Sitzung.
 * <br>
 * Definiert eine NamedQuery "Session.getAllUserSessionsExcept", welche alle {@link Session Sessions} eines User, die nicht ein bestimmtes token haben, findet. 
 * Akzeptiert zwei Parameter: "user", "token".
 * <br>
 * Ein JPA-Entity für eine Tabelle names "Sessions".
 */
@Entity
@Table(name = "Sessions")
@NamedQueries({
    @NamedQuery(name = "Session.getAllUserSessionsExcept", query = "SELECT s FROM Session s WHERE s.user = :user AND s.token != :token")
})
public class Session{

    /**
     * MD5-Hash, um Einzigartigkeit zu garantieren und das Erraten eines Tokens praktisch unmöglich zu machen.
     */
    @Id
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expires;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Session() {
        super();
    }

    public Session(String token, User user, Date expires) {
        this.user = user;
        this.expires = expires;
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    /**
     * Diese Methode vergleicht nur das {@link Session#token} beider Objekte.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Session other = (Session) obj;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        return true;
    }

}
