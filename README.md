Run Application
---------------
- mvn clean install
- java -jar target/student-information-system-0.0.1-SNAPSHOT.jar 
- http://localhost:8080/student-information-system/

If you want to use the production settings:
java -jar -Dspring.profiles.active=production target/student-information-system-0.0.1-SNAPSHOT.jar 
If you want to use the development settings:
java -jar -Dspring.profiles.active=development target/student-information-system-0.0.1-SNAPSHOT.jar 

Live System
-----------
- https://student-information-system.herokuapp.com

Database Access
---------------
Development: To view the content of the H2 database, go to localhost:8080/console, enter "jdbc:h2:mem:AZ" for the JDBC url

Model
-----
Check the link to see the relationship between the models.
http://www.gliffy.com/go/publish/7984495