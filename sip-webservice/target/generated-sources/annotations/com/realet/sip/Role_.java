package com.realet.sip;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ {

	public static volatile SingularAttribute<Role, Boolean> administrator;
	public static volatile SingularAttribute<Role, String> color;
	public static volatile ListAttribute<Role, Permission> permissions;
	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, Long> id;
	public static volatile SetAttribute<Role, User> users;
	public static volatile SingularAttribute<Role, Group> group;

	public static final String ADMINISTRATOR = "administrator";
	public static final String COLOR = "color";
	public static final String PERMISSIONS = "permissions";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String USERS = "users";
	public static final String GROUP = "group";

}

