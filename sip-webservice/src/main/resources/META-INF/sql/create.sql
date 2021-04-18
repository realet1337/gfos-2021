CREATE TABLE Users (id INT UNSIGNED AUTO_INCREMENT NOT NULL, username VARCHAR(64) NOT NULL, e_mail VARCHAR(320) NOT NULL UNIQUE, pass CHAR(60) NOT NULL, info VARCHAR(200), profile_picture VARCHAR(100), status VARCHAR(200), last_seen DATETIME, is_online BOOLEAN NOT NULL, PRIMARY KEY (id));

CREATE TABLE UserProfiles(id INT UNSIGNED AUTO_INCREMENT NOT NULL, user_id INT UNSIGNED NOT NULL, reverse_blocking BOOLEAN NOT NULL, message_chunk_size INT NOT NULL, max_loaded_messages INT NOT NULL, FOREIGN KEY(user_id) REFERENCES Users(id), PRIMARY KEY(id));

CREATE TABLE `Groups` (id INT UNSIGNED AUTO_INCREMENT NOT NULL, group_name VARCHAR(64) NOT NULL, group_description VARCHAR(200), picture VARCHAR(100), owner_id INT UNSIGNED NOT NULL, FOREIGN KEY (owner_id) REFERENCES Users(id), PRIMARY KEY (id));

CREATE TABLE Roles (id INT UNSIGNED AUTO_INCREMENT NOT NULL, group_id INT UNSIGNED NOT NULL, role_name VARCHAR(64) NOT NULL, role_color VARCHAR(7) NOT NULL, is_admin BOOLEAN NOT NULL, priority INT UNSIGNED NOT NULL, FOREIGN KEY (group_id) REFERENCES `Groups`(id), PRIMARY KEY (id));

CREATE TABLE GroupMembership (user_id INT UNSIGNED NOT NULL, group_id INT UNSIGNED NOT NULL, FOREIGN KEY (user_id) REFERENCES Users(id), FOREIGN KEY (group_id) REFERENCES `Groups`(id), PRIMARY KEY (user_id, group_id));

CREATE TABLE RoleMembership (user_id INT UNSIGNED NOT NULL, role_id INT UNSIGNED NOT NULL, FOREIGN KEY (user_id) REFERENCES Users(id), FOREIGN KEY (role_id) REFERENCES Roles(id), PRIMARY KEY (user_id, role_id));

CREATE TABLE Chats (id INT UNSIGNED AUTO_INCREMENT NOT NULL, group_id INT UNSIGNED, user1_id INT UNSIGNED, user2_id INT UNSIGNED, name VARCHAR(200), FOREIGN KEY (user1_id) REFERENCES Users(id), FOREIGN KEY (user2_id) REFERENCES Users(id), FOREIGN KEY (group_id) REFERENCES `Groups`(id), PRIMARY KEY (id));

CREATE TABLE ChatMessages (id INT UNSIGNED AUTO_INCREMENT NOT NULL, author_id INT UNSIGNED NOT NULL, chat_id INT UNSIGNED NOT NULL, content TEXT NOT NULL, expires DATETIME, sent DATETIME, edited DATETIME, FOREIGN KEY (author_id) REFERENCES Users(id), FOREIGN KEY (chat_id) REFERENCES Chats(id), PRIMARY KEY (id));

CREATE TABLE BlockedUsers(user1_id INT UNSIGNED NOT NULL, user2_id INT UNSIGNED NOT NULL, FOREIGN KEY (user1_id) REFERENCES Users(id), FOREIGN KEY (user2_id) REFERENCES Users(id), PRIMARY KEY(user1_id, user2_id));

CREATE TABLE Permissions(id INT UNSIGNED AUTO_INCREMENT NOT NULL, role_id INT UNSIGNED NOT NULL, chat_id INT UNSIGNED, canRead BOOLEAN NOT NULL, canWrite BOOLEAN NOT NULL, FOREIGN KEY (role_id) REFERENCES Roles(id), FOREIGN KEY (chat_id) REFERENCES Chats(id), PRIMARY KEY(id));

CREATE TABLE Sessions(token VARCHAR(32) NOT NULL, user_id INT UNSIGNED NOT NULL, expires DATETIME, FOREIGN KEY (user_id) REFERENCES Users(id), PRIMARY KEY(token));