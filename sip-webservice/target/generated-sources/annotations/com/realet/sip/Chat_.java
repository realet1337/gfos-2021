package com.realet.sip;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Chat.class)
public abstract class Chat_ {

	public static volatile SingularAttribute<Chat, User> user1;
	public static volatile SingularAttribute<Chat, User> user2;
	public static volatile ListAttribute<Chat, ChatMessage> messages;
	public static volatile SingularAttribute<Chat, Long> id;
	public static volatile SingularAttribute<Chat, Group> group;

	public static final String USER1 = "user1";
	public static final String USER2 = "user2";
	public static final String MESSAGES = "messages";
	public static final String ID = "id";
	public static final String GROUP = "group";

}

