# Pin Generation System

This application generates pin 

## Prerequisites
1. Java 8
2. Maven
3. Docker
4. Docker-compose (if not included in docker)
5. Rest client (postman or ARC)

## Installation

After cloning the repository, cd to the parent folder.

```bash
mvn package
docker build -f Dockerfile -t pin_manager_app .
docker-compose up
```

while running mvn package, there would be some errors in logs which are expected as few exceptions are also being tested in unit/integration tests. Also, Spring Scheduler will throw exception of not finding DB Schema as it comes-up before even setting up H2 In-memory datbase.
while docker-compose up, in logs there would be errors as database takes some time to be ready to take calls. after couple restarts, the app would be up and running and ready to take calls.

Run src\test\resources\schema.sql to set-up database schema.

Database Client set-up.

Host: 127.0.0.1 

Port: 3306 

Login User: root

password: my-secret-pw 

## API Calls
The input and output is Base64 encoded.
Please go to [here](https://www.base64encode.org/) to encode and [here](https://www.base64decode.org/) to decode.

API Input Validations:
MSISDN must start with + and may contain hyphen('-') in between.
it should start with + and may contain 1 to 3 digits for country code and then an optional hyphen('-') and then 3 to 10 digits of actual mobile phone number.
Regular expression used for validation:

^\+[1-9]{1,3}\-{0,1}[0-9]{3,10}$

PIN should be 4 digit number.

Examples for generate pin call:

### Generate Pin:

URL:http://localhost:8080/generatePin

Body:
{
    "msisdn" :  "KzM0OTUxNzg0NDMzMQ=="
}

Header:
CallerId:testCaller

### Validate Pin:

URL:http://localhost:8080/validatePin

Body:{
    "msisdn": "KzM0OTUxNzg0NDMzMQ==",
    "pin": <Pin returned from previous call>
}

Header:

CallerId:testCaller
   
    
### Behaviour Assumptions:

There is no column like validated in database, if a PIN is validated it would be deleted from database rightaway, assuming its OTP.
    

