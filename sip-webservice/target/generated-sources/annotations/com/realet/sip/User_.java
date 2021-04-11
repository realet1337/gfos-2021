package com.realet.sip;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile ListAttribute<User, Session> sessions;
	public static volatile ListAttribute<User, Group> ownedGroups;
	public static volatile SingularAttribute<User, String> pass;
	public static volatile ListAttribute<User, ChatMessage> chatMessages;
	public static volatile SetAttribute<User, Role> roles;
	public static volatile SetAttribute<User, Group> groups;
	public static volatile ListAttribute<User, Chat> directChats2;
	public static volatile SingularAttribute<User, Boolean> isOnline;
	public static volatile SetAttribute<User, User> blockedBy;
	public static volatile SingularAttribute<User, String> profilePicture;
	public static volatile SingularAttribute<User, Date> lastSeen;
	public static volatile ListAttribute<User, Chat> directChats1;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SetAttribute<User, User> blockedUsers;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> info;
	public static volatile SingularAttribute<User, String> status;

	public static final String SESSIONS = "sessions";
	public static final String OWNED_GROUPS = "ownedGroups";
	public static final String PASS = "pass";
	public static final String CHAT_MESSAGES = "chatMessages";
	public static final String ROLES = "roles";
	public static final String GROUPS = "groups";
	public static final String DIRECT_CHATS2 = "directChats2";
	public static final String IS_ONLINE = "isOnline";
	public static final String BLOCKED_BY = "blockedBy";
	public static final String PROFILE_PICTURE = "profilePicture";
	public static final String LAST_SEEN = "lastSeen";
	public static final String DIRECT_CHATS1 = "directChats1";
	public static final String ID = "id";
	public static final String BLOCKED_USERS = "blockedUsers";
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";
	public static final String INFO = "info";
	public static final String STATUS = "status";

}

