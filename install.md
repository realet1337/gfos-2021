## Installation/Deployment
*(Tested on WildFly 23.0.1, MySql 8.0.24, openjdk 11.0.10, Ubuntu 20.04.2)*

To install, simply deploy the `.ear` archive found in the `dist` directory.
The app needs a few things to work:

 1. A system property called `com.realet.sip.uploadDir`. This tells the
    application where it should store profile pictures. Make sure this
    directory is writable. If you use WildFly, you can add this tag behind the `<extensions>` tag in WildFly's standalone.xml:
    
    ```
    <system-properties>
	    <property  name="com.realet.sip.uploadDir"  value="/var/www/sip/upload"/>
    </system-properties>
    ```
 2. A MySql datasource called "MySqlDS". Here is a great walkthrough: https://medium.com/@hasnat.saeed/install-and-configure-mysql-jdbc-driver-on-jboss-wildfly-e751a3be60d3. Make sure to download the newest version of Connector/J and configure the module with version 1.9 (`xmlns="urn:jboss:module:1.9"`).

3. The necessary databases schema. To generate these tables, simply run `create.sql` from the `dist` directory. `dist` also holds `drop.sql` which drops all of those tables.

And that's it! You should be good to go. (╹ڡ╹ )
