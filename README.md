# Sample App

This project contains a simple servlet application. Use at your own risk.

## Running the application using the command-line

This project can be built with [Apache Maven](http://maven.apache.org/). The project uses [Liberty Maven Plug-in][] to automatically download and install Liberty from the [Liberty repository](https://developer.ibm.com/wasdev/downloads/). Liberty Maven Plug-in is also used to create, configure, and run the application on the Liberty server. 

Use the following steps to run the application locally:

1. Execute full Maven build to create the `target/JavaHelloWorldApp.war` file:
    ```bash
    $ mvn install package
    ```
2. Execute resources/create_db.sql and resources/insert_db.sql

3. Update jvm.options.templates template with proper database credentials and other properties

4. Create server
   ```bash
    $ mvn liberty:create-server
    ```
4. Rename jvm.options.templates to jvm.options and copy it to target/liberty/usr/servers/defaultServer

5. Start the server

    ```bash
    $ mvn liberty:install-feature
    $ mvn liberty:start-server
    ```

    Once the server is running, the application will be available under [http://localhost:9080/](http://localhost:9080).
 
