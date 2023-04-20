# search-academy-project
## Summary
This project provides an api that allows you to index titles and names from the imdb files (https://www.imdb.com/interfaces/).
Also you can search using diferent end point, witch are documented using swagger
## Installation and setup
Download
````
git clone https://github.com/juanma6245/search-academy-project.git
````
Then, use the docker-compose to execute the project
````
docker-compose up --build
````
## Documentation
You can look at the API documentation
````
http://localhost:8080/swagger-ui/index.html
````
Also, the JobRunr dashboard is accesible for Job control
````
http://localhost:8000/dashboard/overview
````
## Important
For index operations, please remove the header line from the TSV files
## Docker
You can find a docker image with all the data needed already indexed
````
docker pull juanma6245/elastic
````
