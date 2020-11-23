## Player Market

REST API for Teams and Football Players Market.

## Tech Stack

- Java 11
- Spring Boot
- H2 (SQL)
- Liquibase
- Lombok
- MapStruct
- Swagger
- Postman (see Player Market.postman_collection.json)

## API

    # Actuator
    # http://localhost:8080/actuator
    
    # Swagger
    # http://localhost:8080/swagger-ui/
    
## Next Steps

- Apply Microservice Architecture 
    - Move Player and Team controllers to one microservice, Market controller to another
    - Produce TransferEvent request in one microservice and consume it in another
    - Add Security
    - Enable logging
    - Pack everything in Docker containers
    - Add service discovery
- Connect real DB
- Deploy in the Cloud
- Install monitoring tools
