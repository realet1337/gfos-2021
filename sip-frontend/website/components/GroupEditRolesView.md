# GroupEditRolesView

Erlaubt, Rollen zu kreieren, zu bearbeiten und zu löschen.

## Methods

<!-- @vuese:GroupEditRolesView:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|fetchRoles|Lädt alle Rollen der Gruppe.|-|
|submit|Aktualisiert eine Rolle beim Server, falls sie eine ID hat, falls nicht, muss die Rolle neu sein und wird neu hinzugefügt.|-|
|createRole|Erstellt lokal eine neue Rolle ohne ID.|-|
|deleteRole|Löscht eine Rolle beim Server.|-|
|removeUser|Zeigt den "RemoveUserConfirmDialog" mit einem "user" Parameter.|-|
|addUser|Zeigt den "AddUserConfirmDialog" mit einem "user" Parameter.|-|
|showUserFinder|Zeigt einen Dialog mit allen Gruppenmitgliedern, die nicht Teil der Rolle sind. Dieser erlaubt es, Nutzer zur Rolle hinzuzufügen.|-|
|localRemoveUserFromRole|Entfernt einen Nutzer aus dem "user"-Parameter lokal aus einer Rolle.|-|
|localAddUserToRole|Fügt einen Nutzer aus dem "user"-Parameter lokal zu einer Rolle hinzu.|-|
|fetchUsers|Lädt alle Mitglieder einer Gruppe.|-|
|swapRolesUpwards|Verschiebt eine Rolle mit index von Parameter "idx" nach oben. Überprüft nicht, ob dies möglich ist.|-|
|swapRolesDownwards|Verschiebt eine Rolle mit index von Parameter "idx" nach unten. Überprüft nicht, ob dies möglich ist.|-|
|showPriorityEditor|Zeigt einen Dialog mit einer Liste aller Rollen. Dieser erlaubt, die Reihenfolge dieser Rollen zu ändern.|-|
|updateRolePriorities|Aktualisiert die Prioritäten der Rollen beim Server.|-|

<!-- @vuese:GroupEditRolesView:methods:end -->


## Computed

<!-- @vuese:GroupEditRolesView:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|notMembers|-|Findet alle Nutzer, die nicht Teil der aktuellen Rolle sind.|No|

<!-- @vuese:GroupEditRolesView:computed:end -->


## Watch

<!-- @vuese:GroupEditRolesView:watch:start -->
|Name|Description|Parameters|
|---|---|---|
|roleId|Wenn eine neue Rolle ausgewählt wird, wird diese auch zur Bearbeitung bereitgestellt.|-|

<!-- @vuese:GroupEditRolesView:watch:end -->


