# GroupCreatorDialog

Erlaubt das Erstellen von Gruppen samt Namen, Beschreibung und Bild.

## Events

<!-- @vuese:GroupCreatorDialog:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|open-group-id|Emittitet die der erstellten Gruppe.|group|

<!-- @vuese:GroupCreatorDialog:events:end -->


## Methods

<!-- @vuese:GroupCreatorDialog:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|updateFile|Ändert das imgFile und sendet ein post-request an "/api/images/groups/pictures" um einen neuen Picture-Code zu erhalten.|file|

<!-- @vuese:GroupCreatorDialog:methods:end -->


## Computed

<!-- @vuese:GroupCreatorDialog:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|avatarUrl|-|Generiert eine Object-Url anhand des imgFile.|No|

<!-- @vuese:GroupCreatorDialog:computed:end -->


