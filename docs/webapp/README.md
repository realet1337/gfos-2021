# SIP

Dies ist dies Dokumentation der Sip-Webapp. Alle Komponenten sind in der Seitenleiste zu finden, Dokumentation zu allen Skripts hier.

- [SIP](#sip)
  - [src/main.js](#srcmainjs)
  - [src/store/index.js](#srcstoreindexjs)
  - [src/router/index.js](#srcrouterindexjs)

## src/main.js
Das Skript main.js etabliert einige Konstanten, die durch den Vue prototype für alle Komponenten erreichbar sind.
```js
Vue.prototype.$notificationTimeout = 5000;
Vue.prototype.$apiHost = ""
Vue.prototype.$uploadHost =""
```
Ein Event-Hub wird erstellt:
```js
//global event bus
Vue.prototype.$eventHub = new Vue();
```
Außerdem definiert es einige Methoden, ebenfalls über den Vue.prototype:

|Method|Description|Parameters|
|---|---|---|
|$getApiUrl|Findet eine URL der API mit Protokoll.|protocol|
|$getUploadUrl|Findet eine URL zum Upload-Directory mit Protokoll.|protocol|
|$getAvatarUrl|Findet die URL eines Avatars anhand eines Object und seines Typs: "user" oder "group".|type, obj|
|$initApp|Holt funktionsrelevante Daten (user, userProfile, blockedUsers, blockedBy) vom Server und markiert die App im store als intialisiert, sobald alle Daten angekommen sind.|-|

Eine Hilfsmethode wird lokal definiert:

|Method|Description|Parameters|
|---|---|---|
|createUserWatcher|Aktiviert eine global WebSockets Verbindung nach `/users/{userId}/websockets` und registriert callbacks für Fehler und Nachrichten. Wenn eine neue chatMesssage ankommt, wird sie durch den globalen Event-Hub geschickt.|protocol|

## src/store/index.js

Definiert den Store und Setter-Mutations für alle Attribute.

|Store Element|Bedeutung|
|---|---|
|token|Das Token der aktuelle Session|
|userId|Die eigene userId|
|user|Der eigene User|
|ws|Das WebSocket Objekt, welches den User beobachtet|
|initialized|Ob alle funktionsnotwendigen Informationen geladen sind|
|blockedUsers|List aller vom eigenen Nutzer blockierten Nutzer|
|blockedBy|List aller Nutzer, die den eigenen Nutzer blockiert haben|
|userProfile|UserProfile Objekt des eigenen Nutzers|

## src/router/index.js

Definiert alle Routen:

```js
const routes = [
	{
		path: '/login',
		name: 'Login',
		component: Login
	},
	{
		path: '/register',
		name: 'Register',
		component: Register
	},
	{
		path: '/home',
		component: Home,
		children: [
			{
				path:'',
				redirect:'direct-chats',
				name: 'Home',
			},
			{
				path: 'direct-chats',
				component: HomeDirectChats,
				name: "HomeDirectChats",
			},
			{
				path: 'groups',
				component: HomeGroups,
				name: "HomeGroups",
			}
		]
	},
	{
		path: '/chat/:chatId',
		component: DirectChat,
		name: 'DirectChat'
	},
	{
		path: '/group/:groupId/chat/:chatId',
		component: Group,
		name: 'GroupChat'
	},
	{
		path: '/group/:groupId',
		component: Group,
		name: 'Group'
	},
	{
		path: '/group/:groupId/edit',
		component: GroupEdit,
		children: [
			{
				path:'overview',
				component: GroupEditOverview,
				name: 'GroupEditOverview',
			},
			{
				path: 'roles',
				component: GroupEditRolesView,
				name: "GroupEditRolesView",
			},
			{
				path: 'permissions',
				component: GroupEditPermissionsView,
				name: "GroupEditPermissionsView",
			},
			{
				path: 'users',
				component: GroupEditUsersView,
				name: "GroupEditUsersView",
			}
		]
	},
	{
		path: '/edit',
		component: Edit,
		children: [
			{
				path: 'profile',
				component: EditProfile,
				name: 'EditProfile'
			},
			{
				path: 'settings',
				component: EditSettings,
				name: 'EditSettings'
			},
		]
	},
	{
		path: '',
		name: 'FrontPage',
		component: FrontPage,
	}
]
```

Definiert außerdem einen globalen forEach Navigation-Guard. Dieser überprüft, falls die Navigation zu

`/home...`

`/chat...`

`/group...`

`/edit...`

führt, ob ein Token im Store vorhanden ist. Falls nein, wird im Cookie gesucht. Wird dort eins gefunden, wird es beim Server überprüft. Ist dies erfolgreich, wird die App intialisiert.

Falls die Anwendung intialisiert ist, die Navigation aber zu einer anderen Route führt, wird die Anwendung terminiert.