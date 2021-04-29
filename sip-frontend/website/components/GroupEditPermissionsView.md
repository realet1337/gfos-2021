# GroupEditPermissionsView

Erlaubt das Hinzufügen, Bearbeiten und Entfernen von Berechtigungen.

## Methods

<!-- @vuese:GroupEditPermissionsView:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|fetchRoles|Lädt alle Rollen einer Gruppe.|-|
|fetchChats|Lädt alle Chats einer Gruppe.|-|
|fetchPermissions|Lädt alle Permissions eines Chats und findet die Permission ohne Role, die "rule". Alle anderen bilden die "exceptions".|-|
|showRoleFinder|Zeigt einen Dialog mit einer Gruppe.|-|
|addException|Fügt einen neue Permission beim Server hinzu.|role|
|updatePermission|Aktualisiert eine Permission beim Server.|permission|
|deleteException|Löscht eine Permission beim Server.|permission|

<!-- @vuese:GroupEditPermissionsView:methods:end -->


## Computed

<!-- @vuese:GroupEditPermissionsView:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|rolesWithoutExceptions|-|Findet alle Rollen, die keine Berechtigung für den aktuellen Chat haben.|No|

<!-- @vuese:GroupEditPermissionsView:computed:end -->


## Watch

<!-- @vuese:GroupEditPermissionsView:watch:start -->
|Name|Description|Parameters|
|---|---|---|
|chatId|Wenn ein neuer chat ausgewählt wird, werden die Berechtigungen neu geladen.|-|

<!-- @vuese:GroupEditPermissionsView:watch:end -->


