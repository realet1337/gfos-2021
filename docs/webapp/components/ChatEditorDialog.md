# ChatEditorDialog

Erlaubt das Bearbeiten von Gruppenchats.

## Events

<!-- @vuese:ChatEditorDialog:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|chat-updated|Emittiert den aktualisierten Chat|chat|
|chat-created|Emittiert den erstellten Chat|chat|

<!-- @vuese:ChatEditorDialog:events:end -->


## Methods

<!-- @vuese:ChatEditorDialog:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|show|Öffnet den Dialog. Falls ein "chat"-Paramter angegeben ist, wird der Dialog im Bearbeitungsmodus geöffnet, andernfalls wird er der Erstellungsmodus gewählt.|chat|
|submit|Aktualisiert/erstellt, abhängig vom Modus, beim Server einen Chat.|-|
|close|Schließt den Dialog|-|

<!-- @vuese:ChatEditorDialog:methods:end -->


