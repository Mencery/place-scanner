Kafka
https://www.youtube.com/watch?v=TkhU8d-uao8&ab_channel=JavaGuides
https://www.baeldung.com/spring-boot-kafka-testing
https://www.geeksforgeeks.org/how-to-install-and-run-apache-kafka-on-windows/#
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
.\bin\windows\kafka-server-start.bat .\config\server.properties

Crime rate - https://developer.precisely.com/software-apis/dashboard
Pollution - IQAir (v2/nearest_city?lat={{LATITUDE}}&lon={{LONGITUDE}}&key={{YOUR_API_KEY}})
Demographic https://developer.precisely.com/software-apis/dashboard
zip code to https://api.promaptools.com/service/us/zip-lat-lng/get/?zip=60090&key=17o8dysaCDrgvlc
Get all population https://api.census.gov/data/2021/pep/natmonthly?get=POP,NAME,MONTHLY&for=us:*&MONTHLY=4
Get all population in realtime = https://www.census.gov/popclock/data/population.php/us?_=1685803553787
https://api.census.gov/data/2021/pep/population?get=DENSITY_2021,POP_2021,NAME,STATE&for=region:*

https://api.census.gov/data/2019/pep/population?get=COUNTY,DATE_CODE,DATE_DESC,DENSITY,POP,NAME,STATE&for=place:*&in=state:17
https://api.census.gov/data/2018/pep/population?get=COUNTY,DATE_CODE,DATE_DESC,DENSITY,POP,GEONAME,STATE&for=county:031&in=state:17


https://api.census.gov/data/2017/acs/acs5?get=NAME,STATE,group(B19013)&for=zip%20code%20tabulation%20area:60090&in=state:17


get polygon https://nominatim.openstreetmap.org/search.php?q=Wheeling&polygon_geojson=1&format=jsonv2
https://nominatim.openstreetmap.org/search.php?q=Bloomington%2C%20IL&polygon_geojson=1&format=jsonv2

MongoDB
To start go to scripts folder from the terminal and run
`docker-compose up`

Mongo will start on localhost:27017
And UI version will be available  on localhost:8081


Test request:
http://localhost:8080/place-info?placeName=Chicago&state=Illinois&zipCode=60090
