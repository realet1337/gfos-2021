# RegisterForm

Erlaubt Nutzerregistrierung  in 3 Schritten: <ol> <li>Name + Login-Informationen</li> <li>Profil</li> <li>Einstellungen</li> </ol>

## Methods

<!-- @vuese:RegisterForm:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|updateFile|Ändert das imgFile und sendet ein post-request an "/api/images/users/pictures" um einen neuen Picture-Code zu erhalten.|file|
|onSubmit|Erstellt den Nutzer beim Server. Falls dies gelingt, wird die Anwendung nach "/login" geroutet.|-|
|testEmail|Testet ob eine E-Mail gültig ist und gibt ggf. eine Fehlermeldung zurück.|v|

<!-- @vuese:RegisterForm:methods:end -->


## Computed

<!-- @vuese:RegisterForm:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|avatarUrl|-|Generiert eine Object-Url anhand des imgFile.|No|

<!-- @vuese:RegisterForm:computed:end -->


