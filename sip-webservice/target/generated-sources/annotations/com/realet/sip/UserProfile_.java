package com.realet.sip;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserProfile.class)
public abstract class UserProfile_ {

	public static volatile SingularAttribute<UserProfile, Boolean> reverseBlocking;
	public static volatile SingularAttribute<UserProfile, Integer> messageChunkSize;
	public static volatile SingularAttribute<UserProfile, Integer> maxLoadedMessages;
	public static volatile SingularAttribute<UserProfile, Long> id;
	public static volatile SingularAttribute<UserProfile, User> user;

	public static final String REVERSE_BLOCKING = "reverseBlocking";
	public static final String MESSAGE_CHUNK_SIZE = "messageChunkSize";
	public static final String MAX_LOADED_MESSAGES = "maxLoadedMessages";
	public static final String ID = "id";
	public static final String USER = "user";

}

