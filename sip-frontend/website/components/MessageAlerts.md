# MessageAlerts

Eine Snackbar, die die neueste erhaltene Nachricht anzeigt. Falls eine neue Nachricht ankommt, wird der Timer zurückgesetzt. Keine neue Snackbar wird angezeigt. Registriert einen Event-Listener für den globalen Event-Hub.

## Events

<!-- @vuese:MessageAlerts:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|open-chat|Wenn der entsprechende Knopf geklickt wird.|chat|

<!-- @vuese:MessageAlerts:events:end -->


## Methods

<!-- @vuese:MessageAlerts:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|onNewMessage|Zeigt die Snackbar bzw. setzt das Timeout zurück und zeigt die neue Nachricht an.|chatMessage|
|openChat|Schließt den Dialog und emittiert ein "open-chat" event.|-|

<!-- @vuese:MessageAlerts:methods:end -->


