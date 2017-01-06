IHasTheToken
============
It's a simple application to manage a token queue. It's based on the Clarity Design System and Spring Boot.

Quick Links
-----------
* [Git repository](https://github.com/jelohazi/ihasthetoken)
* [Clarity Website](http://clarity.vmware.com/)
* `clarity-ui` [NPM package](http://build-artifactory.eng.vmware.com/artifactory/api/npm/npm/clarity-ui)
* `clarity-angular` [NPM package](http://build-artifactory.eng.vmware.com/artifactory/api/npm/npm/clarity-angular)
* [Spring Boot](https://projects.spring.io/spring-boot/)
* [Maven](http://maven.apache.org/)
* [Java](http://java.oracle.com/)
* [PostgeSQL](https://www.postgresql.org/)
* [Docker](https://www.docker.com/) (If you want to run the application as a container)

Getting started
---------------

#### Pre-requisites
You'll need to have a postgres DB running. Check the ihasthetoken/resources/schema.sql file for database schema.
For database settings check the application.properties file in the same folder and adjust as needed.
You'll need JDKv8, Angular CLI, Postgres SQL Server, Maven

#### Installation
These are the steps to run the seed project:
```
git clone git@github.com:jelohazi/ihasthetoken.git
cd ihasthetoken
./make.sh
```