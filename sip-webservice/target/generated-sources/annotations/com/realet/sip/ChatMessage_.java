package com.realet.sip;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ChatMessage.class)
public abstract class ChatMessage_ {

	public static volatile SingularAttribute<ChatMessage, Date> expires;
	public static volatile SingularAttribute<ChatMessage, Date> edited;
	public static volatile SingularAttribute<ChatMessage, Chat> chat;
	public static volatile SingularAttribute<ChatMessage, User> author;
	public static volatile SingularAttribute<ChatMessage, Long> id;
	public static volatile SingularAttribute<ChatMessage, Date> sent;
	public static volatile SingularAttribute<ChatMessage, String> content;

	public static final String EXPIRES = "expires";
	public static final String EDITED = "edited";
	public static final String CHAT = "chat";
	public static final String AUTHOR = "author";
	public static final String ID = "id";
	public static final String SENT = "sent";
	public static final String CONTENT = "content";

}

