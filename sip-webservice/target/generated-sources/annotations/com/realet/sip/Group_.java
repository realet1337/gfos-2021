package com.realet.sip;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Group.class)
public abstract class Group_ {

	public static volatile SingularAttribute<Group, User> owner;
	public static volatile ListAttribute<Group, Role> roles;
	public static volatile SingularAttribute<Group, String> name;
	public static volatile ListAttribute<Group, Chat> chats;
	public static volatile SingularAttribute<Group, String> description;
	public static volatile SingularAttribute<Group, Long> id;
	public static volatile SingularAttribute<Group, String> picture;
	public static volatile SetAttribute<Group, User> users;

	public static final String OWNER = "owner";
	public static final String ROLES = "roles";
	public static final String NAME = "name";
	public static final String CHATS = "chats";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String PICTURE = "picture";
	public static final String USERS = "users";

}

