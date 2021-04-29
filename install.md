## Installation/Deployment
*(Getestet mit WildFly 23.0.1, MySql 8.0.24, openjdk 11.0.10, Ubuntu 20.04.2)*

Deployen Sie zur Installation das `.ear` Archiv, welches im `dist` Verzeichnis gefunden werden kann.
Die App braucht einige Dinge um zu funktionieren:

 1. Eine system property mit Namen `com.realet.sip.uploadDir`. Diese sagt der Anwendung,
    wo sie Profil-Bilder speichern soll. Dieses Verzeichnis muss
    beschreibbar sein. Falls sie WildFly verwenden, können sie diesen Tag hinter dem `<extensions>` Tag in WildFly's standalone.xml einfügen:
    
    ```
    <system-properties>
	    <property  name="com.realet.sip.uploadDir"  value="<Upload Directory here>"/>
    </system-properties>
    ```
    
 2. Eine MySql datasource names "MySqlDS". Hier ist eine gute Erklärung: https://medium.com/@hasnat.saeed/install-and-configure-mysql-jdbc-driver-on-jboss-wildfly-e751a3be60d3.     Laden sie die richtige Connector/J-Version für ihre MySql Installation (empf. 8.0.24) und konfigurieren sie das Modul mit Version 1.9 (`xmlns="urn:jboss:module:1.9"`).
 
 3. Einen File-Handler für statische Inhalte bei der URL `/upload`. Dieser sollte dasselbe Verzeichnis wie `com.realet.sip.uploadDir` bereitstellen. Falls sie WildFly benutzen, 
    können sie hierfür die folgenden Tags in WildFlys standalone.xml in der undertow-Sektion einfügen:
 
	 `<location  name="/upload"  handler="uploads"/>` 
	
	`<file  name="uploads"  path="<Upload Directory here>"/>`    
	
    Diese sollten neben dem Tag für die URL `/`, welche aus `${jboss.home.dir}/welcome-content` angeboten wird, Platz finden.

4. Das notwendige Datenbank-Schema. Um diese Tabellen zu generieren, führen sie einfach `create.sql` aus dem `dist` Verzeichnis aus. `dist` enthält außerdem `drop.sql`, was 
   diese Tabellen entfernt.

(*Note: Die App versucht, sich für den Web-Context `/` zu registrieren. Die meisten Application servers belegen diesen Pfad bereits, was bedeutet, dass es nicht möglich sein wird, die App zu öffnen. Um dies auf WildFly zu vermeiden, benennen sie entweder `welcome-content` um oder entfernen sie den Handler komplett.)* 

Und das wars! Die App sollte funktionieren. (╹ڡ╹ )

Es is auch möglich, die  Web-App als Statischen Build laufen zu lassen, mit dem Webservice auf einem anderen Server. Um diese zu erreichen, ändern sie einfach die `$apiHost` und `$uploadHost` Attribute in `sip-frontend/src/main.js` um auf ihren Webservice zu zeigen und führen sie `npm run build` im `sip-frontend` Verzeichnis aus. Dies generiert statische Dateien, die unabhängig vom Webservice deployed werden können. Ein Webservice-Build kann erstellt werden, indem `mvn clean install` im `sip-webservice` Verzeichnis ausgeführt wird. Vergessen sie CORS nicht!
