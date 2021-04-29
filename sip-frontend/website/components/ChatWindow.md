# ChatWindow

Sobald erstellt, lädt diese Komponente die Berichtigungen zu einem Chat sowie die neuesten Nachrichten, die Anzahl ist definiert durch userProfile.messageChunkSize im store. Falls der Nutzer nicht lesen oder schreiben darf, werden jeweils schriftzüge angezeigt. Gegebenenfalls verschwindet das Eingabefeld. Nachrichten werden gruppiert, falls sie vom selben Nutzer stammen, am selben Tag geschrieben

## Events

<!-- @vuese:ChatWindow:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|show-user|Wenn das Profilbild oder der Name eines Nutzers angeklickt wird.|user|

<!-- @vuese:ChatWindow:events:end -->


## Methods

<!-- @vuese:ChatWindow:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|updateMessages|Lädt neue Nachrichten und setzt hasOldest und hasNewest entsprechend. Ruft "addMessages" mit der Serverantwort auf.|-|
|sendMessage|Sendet eine Nachricht an den Server, falls "editing" false ist und sie nicht ausschliesslich ausschließlich aus Leerzeichen besteht. Aktualisiert eine Nachricht beim Server, falls "editing" true ist und sie nicht ausschliesslich ausschließlich aus Leerzeichen besteht.|-|
|scrollDownMessages|Scrollt den Nachrichtencontainer nach unten.|-|
|addMessages|Fügt Nachrichten lokal hinzu. Nimmt Rücksicht auf userProfile.maxLoadedMessages im store und behält Scroll-Position bei. Achtung: nimmt an, dass sich der gesamte Array entweder vor oder hinter den geladenen Nachrichten befindet.|-|
|copyToClipboard|Kopiert einen Text zur Zwischenablage.|text|
|deleteMessage|Löscht eine Nachricht beim Server.|chatMessage|
|removeMessage|Entfernt eine Nachricht lokal.|chatMessage|
|editMessage|Leert das Eingabefeld und aktiviert Bearbeitungsmodus.|chatMessage|
|updateMessage|Aktualisiert eine Nachricht lokal.|chatMessage|
|createWatcher|Baut websocket Verbindung auf, um den Chat zu beobachten. Registriert callback für neue Nachrichten.|-|
|onNewMessage|Callback für den globalen eventHub bei Event "new-message".|chatMessage|
|getPermissions|Lädt Permissions.|-|

<!-- @vuese:ChatWindow:methods:end -->


