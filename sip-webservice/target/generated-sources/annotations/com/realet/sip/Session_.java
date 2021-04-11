package com.realet.sip;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Session.class)
public abstract class Session_ {

	public static volatile SingularAttribute<Session, Date> expires;
	public static volatile SingularAttribute<Session, User> user;
	public static volatile SingularAttribute<Session, String> token;

	public static final String EXPIRES = "expires";
	public static final String USER = "user";
	public static final String TOKEN = "token";

}

