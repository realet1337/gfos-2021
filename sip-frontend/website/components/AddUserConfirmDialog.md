# AddUserConfirmDialog

Bittet den Nutzer um Bestätigung, dass er einen Nutzer zu einer Gruppe/Rolle hinzufügen will.

## Events

<!-- @vuese:AddUserConfirmDialog:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|added|-|-|

<!-- @vuese:AddUserConfirmDialog:events:end -->


## Methods

<!-- @vuese:AddUserConfirmDialog:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|show|Zeigt den Dialog. Wird dies als show(user) aufgerufen, fragt die Komponente, ob der Nutzer zu einer Gruppe hinzugefügt werden soll. Wird dies als show(user, role) aufgerufen, fragt die Komponente, ob der Nutzer zu einer Rolle hinzugefügt werden soll.|user role|
|close|Schließt den Dialog.|-|
|add|Fügt den Nutzer, abhängig davon, wie "show" aufgerufen wurde, beim Server entweder zu einer Gruppe oder einer Rolle hinzu.|-|

<!-- @vuese:AddUserConfirmDialog:methods:end -->


