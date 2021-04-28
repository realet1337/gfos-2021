# Group

Diese Komponente zeigt eine Gruppe an und erlaubt Interaktion. Auf der linken Seite der Anwendung befindet sich ein Navigation Drawer, der es erlaubt, zwischen Chats und Gruppen zu wechseln. Falls der Nutzer administrative Rechte hat, kann er hier auch Chats hinzufügen, bearbeiten und entfernen. Auf der rechten Seite der Anwendung befindet sich ein Navigation Drawer, der alle GruppenMitglieder, nach Rollen sortiert, anzeigt. Die App-Leiste zeigt den Namen des aktuellen Chats, sowie ein Icon für den zweiten Navigation Drawer und, falls der User adminstrative Rechte hat, ein Icon als Link zu den Gruppeneinstellungen an. Die Navigation Drawers verschwindet und lassen sich optional öffnen, sollte der Bildschirm weniger als 600px breit sein.

## Methods

<!-- @vuese:Group:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|toggleUserDrawer|Zeigt den rechten Navigation Drawer|-|
|showUser|Zeigt den UserProfileDialog für einen User|-|
|openDirectChat|Routet die Anwendung zum entsprechenden Chat|-|
|openGroup|Passt die Komponente an um eine gewisse Gruppe zu zeigen|-|
|openChat|Öffnet eine beliebige Art von Chat, überprüft ob Chat ein Gruppen-/Direkt-Chat ist und routet die Anwendung entweder zu entsprechenden URL oder öffnet einen Gruppen-Chat auf.|-|
|initGroup|Lädt alle Chats, Rollen und Nutzer ohne Rolle einer Gruppe.|-|
|resetView|Setzt die Komponente zurück.|-|
|getGroups|Lädt alle Gruppen eines Nutzers|-|
|openGroupId|Öffnet eine Gruppe anhand ihrer ID|-|
|showGroupCreator|Zeigt den "GroupCreatorDialog"|-|
|createChat|Zeigt den ChatEditorDialog im Erstellungs-Modus|-|
|editChat|Zeigt den ChatEditorDialog im Bearbeitungs-Modus mit dem "chat" Parameter.|-|
|deleteChat|Löscht einen Chat beim Server|-|

<!-- @vuese:Group:methods:end -->


## Computed

<!-- @vuese:Group:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|isAdmin|-|Bestimmt anhand der geladenen Rollen und der geladenen Gruppe ob der User administrative Rechte hat|No|
|rolesWithMembers|-|Findet alle Rollen die Mitglieder haben|No|

<!-- @vuese:Group:computed:end -->


