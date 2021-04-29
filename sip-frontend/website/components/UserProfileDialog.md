# UserProfileDialog

Zeigt das Profil eines Nutzers in einem Dialog an. Erlaubt, den Nutzer zu blockieren und einen Chat mit ihm zu öffnen.

## Events

<!-- @vuese:UserProfileDialog:events:start -->
|Event Name|Description|Parameters|
|---|---|---|
|open-direct-chat|Wenn der Nutzer auf "MESSAGE" klickt um den Direct-Chat zu öffnen.|chat|
|open-group|Wenn der Nutzer eine Gruppe anklickt.|group|

<!-- @vuese:UserProfileDialog:events:end -->


## Methods

<!-- @vuese:UserProfileDialog:methods:start -->
|Method|Description|Parameters|
|---|---|---|
|openDirectChat|Lädt einen Direct-Chat zwischen dem gezeigten Nutzer  und dem eingeloggten Nutzer vom Server und emmitiert 'open-direct-chat' mit ihm.|-|
|openGroup|Emittiert 'open-group' mit einem Parameter group und schließt den Dialog.|group|
|show|Setzt die Komponente zurück und zeigt den Dialog.|-|
|blockUser|Blockiert einen Nutzer beim Server.|-|
|unblockUser|Hebt eine Blockierung beim Server auf.|-|

<!-- @vuese:UserProfileDialog:methods:end -->


## Computed

<!-- @vuese:UserProfileDialog:computed:start -->
|Computed|Type|Description|From Store|
|---|---|---|---|
|userIsBlocked|-|Ob der gezeigt Nutzer blockiert ist.|No|

<!-- @vuese:UserProfileDialog:computed:end -->


