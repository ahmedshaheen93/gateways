# This sample project is managing gateways

## master devices that control multiple peripheral devices.

### requirement

    - java 11 
    - maven
    - postman to import contract.yaml as postman collection 
        to test the application
    - MYSQL as DBMS

### Step 1

    update 'src/resources/application.yaml' with your connection prameters
    such as database url , username and password

### Step 2

    build the application using maven 

```bash
    mvn package
```

### Step 3

```bash
    java -jar target/gateways-0.0.1-SNAPSHOT.jar
```

### Step 4

- import openApi/contract.yaml to postman as postman collection
- change the baseUrl var on postman with your server url and running port number example
  http://localhost:8088/api/v1
- test all endpoints

### Step 5

- you can review api specs openApi/contract.yaml on swagger editor 
  
