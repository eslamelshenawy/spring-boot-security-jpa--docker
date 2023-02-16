# Spring Boot, Spring Security,  JPA, and Docker  

## Prerequisites:
* Docker 
* JDK 1.8 
* Maven 3.*


## Install and run the project 
1. download/clone the project 
2. Build the project using following maven command from project root folder where pom.xml file place.
  * `mvn clean package`
3. Create docker image from following command 
  * `docker build -t mysql .`
4. Run the docker-compose using the following command   
  * `docker-compose up -d` 
  
     
5. Let's authentication and get JWT token 

   ``` 
   curl -X POST http://localhost:8080/authenticate  -H 'Content-Type: application/json'  -d '{	"userName" : "ahmed",	"password" : "password"}' 
   ```
    
   You will get following format Json response. data field has the JWT token. It requires for authorization to call rest api.
   
    ```
        {
           "status": "SUCCESS",
           "message": "Authenticate successfully",
           "data": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhaG1lZCIsInJvbGUiOiJST0xFX0FETUlOIiwidXNlck5hbWUiOiJhaG1lZCIsImV4cCI6MTU3OTgzMTk1NywiaWF0IjoxNTc5NzQ1NTU3fQ.wFc6ORT_ttfJHZRqOySrFp3YqMHPPLUkNoM47NI9ru-uhURhkdDHyfwIaAoNgeSOZf0m_EojUx1UkqkP_r8R-g"
       }
       
     ```

6. you can use following user account to login the application.
   * Username = eslam1, password = password, Role = Admin
   * Username = eslam2, password = password, Role = User
   * Username = admin, password = admin, Role = Admin


7. Create User resource.


  ``` 
     curl -X POST   http://localhost:8080/api/1.0/user -H 'Authorization: Bearer  <JWT TOKEN>'   \
    
      -d '{
    	 "lastName": "elshenawy",
    	 "firstName" : "eslam",
    	 "age" : 29,
    	 "colourId" : "0b3db7e0-9c4c-4936-98e0-24ef27fd5824",
    	 "hobbies" : ["010d961a-d6fc-40d2-8cfd-350b6ff1e34f", "0215299c-b300-4bf1-a60c-f108d531b1db","05645502-4a47-4d18-9dc9-c058d6712061"]
         }'
         
  ```
   You will get following format eslam response.
  
  ```
    {
      "status": "SUCCESS",
      "message": null,
      "data": {
          "id": "12aee7e6-b32a-45c1-aefc-cc910b4e81dd",
          "firstName": "eslam",
          "lastName": "elshenawy",
          "colourId": "0b3db7e0-9c4c-4936-98e0-24ef27fd5824",
          "age": 29,
          "dateOfBirth": "1991-01-01",
      }
    }
  ```
     
10. Get All Active Users 

    ``` 
    curl -X GET   'http://localhost:8080/api/1.0/user/pagination?page=0&pageSize=20' -H 'Authorization: Bearer  <JWT TOKEN>'
     ``` 

   You will get following format Json response.
   
  ```
  {
    "status": "SUCCESS",
    "message": null,
    "data": [
        {
            "firstName": "eslam",
            "lastName": "elshenawy",
            "favouriteColour": "Green",
            "age": 29,
        }
    ],
    "totalElement": 0
}
  
  ```
   
> **Note:** Dockerfile and docker-compose.yml files are in project root dir.


