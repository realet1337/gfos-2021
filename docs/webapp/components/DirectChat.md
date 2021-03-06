# DirectChat

Diese Komponente zeigt eine Direkt-Unterhaltung an und erlaubt Interaktion. Auf der linken Seite der Anwendung befindet sich ein Navigation Drawer, der es erlaubt, zwischen Chats zu wechseln. Die App-Leiste zeigt Informationen wie den Online-Status, sowie, falls nicht online, das Datum, an dem der Nutzer zuletzt online war, an. Registriert einen Event-Listener für den globalen Event-Hub, der auf neue Nachrichten reagiert. Der Navigation drawer verschwindet und lässt sich optional öffnen, sollte der Bildschirm weniger als 600px breit sein.

## Methods

<!-- @vuese:DirectChat:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|showUser|Zeigt den UserProfileDialog mit dem entsprechenden User.|user|
|openChat|Öffnet eine beliebige Art von Chat, überprüft ob Chat ein Gruppen-/Direkt-Chat ist und routet die Anwendung entweder zu entsprechenden URL oder ruft "openDirectChat" auf.|chat|
|openDirectChat|Passt die Komponente an um einen anderen Chat anzuzeigen.|chat|
|openGroup|Routet die Anwendung zur "Group"-Komponente mit der entsprechenden Id.|group|
|findNotSelf|Findet den  User eines Chats, der nicht mit der im Store gespeicherten userId übereinstimmt und fügt ihn dem Chat als "notSelf" hinzu.|chat|
|onNewMessage|Verschiebt den Chat einer neuen Nachricht in der Liste nach oben.|message|
|showUserFinder|Zeigt den UserFinderDialog.|-|
|fetchChats|Sendet eine Server-Abfrage um alle Chats zu erhalten und ändert die Komponente um diese anzuzeigen.|-|

<!-- @vuese:DirectChat:methods:end -->


