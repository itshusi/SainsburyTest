Test for Sainsbury Java Position <br>

1. git clone https://github.com/itshusi/SainsburyTest.git <br>
2. mvn clean install <br>
3. mvn exec:java to view JSON in logs <br>

Issues with RESTful functionality (below steps) caused by conflict with HTTP servlet use from WireMock. Working when test class and WireMock are commented out. 
4. mvn tomcat:run for REST API accessed via <a href=http://localhost:8080/SainsburysTest/api/items/all/>http://localhost:8080/SainsburysTest/api/items/all/</a> when run