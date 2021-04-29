# RemoveUserConfirmDialog

Bittet den Nutzer um Bestätigung, dass er einen Nutzer aus einer Gruppe/Rolle entfernen will.

## Events

<!-- @vuese:RemoveUserConfirmDialog:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|removed|Wenn der Nutzer erfolgreich entfernt wurde|user|

<!-- @vuese:RemoveUserConfirmDialog:events:end -->


## Methods

<!-- @vuese:RemoveUserConfirmDialog:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|show|Zeigt den Dialog. Wird dies als show(user) aufgerufen, fragt die Komponente, ob der Nutzer aus einer Gruppe entfernt werden soll. Wird dies als show(user, role) aufgerufen, fragt die Komponente, ob der Nutzer aus einer Rolle entfernt werden soll.|user role|
|close|Schließt den Dialog.|-|
|remove|Entfernt den Nutzer, abhängig davon, wie "show" aufgerufen wurde, beim Server entweder aus einer Gruppe oder aus einer Rolle.|-|

<!-- @vuese:RemoveUserConfirmDialog:methods:end -->


