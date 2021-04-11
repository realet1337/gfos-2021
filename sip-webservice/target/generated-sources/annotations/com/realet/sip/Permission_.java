package com.realet.sip;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Permission.class)
public abstract class Permission_ {

	public static volatile SingularAttribute<Permission, Role> role;
	public static volatile SingularAttribute<Permission, Boolean> canRead;
	public static volatile SingularAttribute<Permission, Chat> chat;
	public static volatile SingularAttribute<Permission, Boolean> canWrite;
	public static volatile SingularAttribute<Permission, Long> id;

	public static final String ROLE = "role";
	public static final String CAN_READ = "canRead";
	public static final String CHAT = "chat";
	public static final String CAN_WRITE = "canWrite";
	public static final String ID = "id";

}

