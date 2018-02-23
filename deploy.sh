rm -rf target/Java*
rm -rf target/classes
mvn liberty:stop-server
rm -rf target/liberty/wlp/usr/servers/defaultServer/logs/*.log
mvn compile install package

cp resources/jvm.options target/liberty/wlp/usr/servers/defaultServer/
mvn liberty:start-server

