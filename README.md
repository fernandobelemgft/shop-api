# Shop API



Description
-
RESTful API created using Spring boot framework to attend POST/GET requests, produce JSON data as response and perform crud operations with memory database.

Running with Gradle
-
Go to directory where build.gradle file are located and run ```gradle build```.
After build is complete run ```gradle bootRun```

Running with Maven
-
Go to directory of pom.xml file and use ```mvn install``` command.
The “target” folder will be generated, move to that folder and run ```java –jar shop-api-0.0.1-SNAPSHOT.jar```

Testing units
-
During the gradle or maven build the test classes will be executed testing its functionalities. 
There are tests for each layer of this application

You can check how the unit tests was developed by browsing the test folders.

Functionalities:
-
<b>INSERT/EDIT REGISTERS</b>

Provide management tool for Retail Manager to add/edit registers of Shops through a "POST" which consumes a JSON data. 

<b>For example:</b> 

You can "POST" for "/shops" end-point passing as body parameter the json data below: 

```json
{
	"shopName": "Birmingham Shop",
		"shopAddress":{
			"number": "50",
			"postCode": "B1 1BA"
		}	
}
```
<b>TRACKING SHOPS</b>

Retail Manager can keep track of all registered Shops through a "GET" in end-point "/findAllShops". 
The API will provide response as JSON data for each Shop in database.


<b> FIND NEAREST SHOP </b>

Customers can find the nearest shop by informing their geolocation in end-point "/findNearest" which receives latitude and longitude as parameters. 

<b>For example:</b>

Do a "GET" to ```"/findNearest?latitude=YOUR_LATITUDE&longitude=YOUR_LONGITUDE```
The API will provide response as JSON data for the nearest Shop in database.


Key Technologies:
-
- Springboot 1.5.3
- Hibernate and JPA for entity management and crud operation
- Jackson API for json manipulation
- Gradle
- Maven
- Featuring Google Geocode/DistanceMatrix APIs
