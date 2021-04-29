# GroupEditOverview

Erlaubt das Bearbeiten grundlegender Eigenschaften einer Gruppe wie z.B. ihren Namen und das Löschen einer Gruppe.

## Methods

<!-- @vuese:GroupEditOverview:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|updateFile|Ändert das imgFile und sendet ein post-request an "/api/images/groups/pictures" um einen neuen Picture-Code zu erhalten.|file|
|submit|Aktualisiert die Gruppe beim Server.|-|
|openDeleteDialog|Öffnet einen Dialog, der das Löschen der Gruppe bestätigen lässt.|-|
|deleteGroup|Löscht die Gruppe beim Server.|-|

<!-- @vuese:GroupEditOverview:methods:end -->


## Computed

<!-- @vuese:GroupEditOverview:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|avatarUrl|-|Generiert eine Object-Url anhand des imgFile.|No|

<!-- @vuese:GroupEditOverview:computed:end -->


