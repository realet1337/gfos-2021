# EditProfile

Erlaubt, das Profil des eigenen Nutzers zu bearbeiten, das Passwort zu ändern oder den Account zu löschen.

## Methods

<!-- @vuese:EditProfile:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|updateFile|ändert das imgFile und sendet ein post-request an "/api/images/users/pictures" um einen neuen Picture-Code zu erhalten.|-|
|updateUser|Aktualisiert den User beim Server|-|
|changePass|Aktualisiert das Passwort beim Server|-|
|openDeleteDialog|Öffnet einen Dialog, der das Löschen des Nutzers bestätigen lässt.|-|
|deleteUser|Löscht den User beim Server|-|

<!-- @vuese:EditProfile:methods:end -->


## Computed

<!-- @vuese:EditProfile:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|avatarUrl|-|Generiert eine Object-Url anhand des imgFile.|No|

<!-- @vuese:EditProfile:computed:end -->


