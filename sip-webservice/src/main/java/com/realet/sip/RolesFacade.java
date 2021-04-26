package com.realet.sip;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Manager-Klasse für {@link Role Roles}
 */
public class RolesFacade {

    /**
     * Die EntityManagerFactory die für alle Operationen von {@link RolesFacade} verwendet wird.
     */
    static EntityManagerFactory emf;

    /**
     * Setzt {@link RolesFacade#emf}.
     */
    public static void initialize(EntityManagerFactory emf){

        RolesFacade.emf = emf;

    }

    public static Optional<Role> findById(long id){

        EntityManager em = emf.createEntityManager();

        Role role = em.find(Role.class, id);
        return role != null ? Optional.of(role) : Optional.empty();

    }

    public static List<Role> findGroupRolesOrderedByPriority(Group group){
        
        EntityManager em = emf.createEntityManager();

        return em.createNamedQuery("Role.findGroupRolesOrderedByPriority", Role.class).setParameter("group", group).getResultList();
    }

    public static List<Role> findUserGroupRoles(Group group, User user){
        
        EntityManager em = emf.createEntityManager();

        return em.createNamedQuery("Role.findUserGroupRoles", Role.class).setParameter("group", group).setParameter("user", user).getResultList();
    }

    public static List<Role> findAdminRolesByUserAndGroup(User user, Group group){

        EntityManager em = emf.createEntityManager();

        return em.createNamedQuery("Role.findAdminRolesByUserAndGroup", Role.class).setParameter("group", group).setParameter("user", user).getResultList();
    }

    public static void add(Role role){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();

    }

    public static void update(Role role){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(role);
        em.getTransaction().commit();

    }

    public static void remove(Role role){

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        role = em.merge(role);
        em.remove(role);
        em.getTransaction().commit();

    }

    
}
