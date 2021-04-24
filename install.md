## Installation/Deployment
*(Tested on WildFly 23.0.1, MySql 8.0.24, openjdk 11.0.10, Ubuntu 20.04.2)*

To install, simply deploy the `.ear` archive found in the `dist` directory.
The app needs a few things to work:

 1. A system property called `com.realet.sip.uploadDir`. This tells the
    application where it should store profile pictures. Make sure this
    directory is writable. If you use WildFly, you can add this tag behind the `<extensions>` tag in WildFly's standalone.xml:
    
    ```
    <system-properties>
	    <property  name="com.realet.sip.uploadDir"  value="<Upload Directory here>"/>
    </system-properties>
    ```
 2. A MySql datasource called "MySqlDS". Here is a great walkthrough: https://medium.com/@hasnat.saeed/install-and-configure-mysql-jdbc-driver-on-jboss-wildfly-e751a3be60d3. Make sure to download the newest version of Connector/J and configure the module with version 1.9 (`xmlns="urn:jboss:module:1.9"`).
 
 3. A file handler for static content for URL `/upload`. This should serve the same directory specified by `com.realet.sip.uploadDir`. If you're on WildFly, you can do this by adding
 
	 `<location  name="/upload"  handler="uploads"/>` 
	
	`<file  name="uploads"  path="<Upload Directory here>"/>`    
	
	next to the respective tags for the URL `/` which is being served from `${jboss.home.dir}/welcome-content`.

4. The necessary databases schema. To generate these tables, simply run `create.sql` from the `dist` directory. `dist` also holds `drop.sql` which drops all of those tables.

(*Note: The App will attempt to register for web-context `/`. Most Application servers occupy this already, meaning that it won't be possible to open the App. To avoid this on WildFly, either rename the `welcome-content` folder or remove the handler altogether.)* 

And that's it! You should be good to go. (╹ڡ╹ )

It is also possible to deploy the Web-App as a static build with the Webservice on a different server. To achieve that, simply change the `$apiHost` and `$uploadHost` attributes in `sip-frontend/src/main.js` to point to your Webservice and run `npm run build` in the `sip-frontend` directory. This will generate static files that can be deployed separately from the Webservice. The Webservice can be built by running `mvn clean install` in the `sip-webservice directory.` Don't forget to account for CORS though!
