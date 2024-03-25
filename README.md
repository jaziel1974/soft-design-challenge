# Desafio técnico de backend da Softdesign

### Documentation

This application uses spring boot with:
- Spring Web
- Spring Data MongoDB
- Lombok
- JUnit 5
- OpenAPI 3.0

Architecturally, I tried to use SOLID principles and just a bit of CLEAN CODE, since the application is very small and avoiding over-engineering was mandatory.

So, I created regular entity, service, controller layers with dtos in order to avoid tight coupling among the layers.

To avoid over-engineering, I didn't implement interfaces for the conversation between the service layer and the model layer, although it would be a good practice.

Also, in spite of using a NoSql database such as MongoDb, it's a good practice as well to add indexes to the collections to speed up the process of querying them. For the simplicity of the code, I skipped such approach.

I implemented a small bunch of integration tests that can be automatically started when deploying the application via CI/CD approach. I didn't implement unit tests for the model layer, since it is very simple.

I used the *SonarLint* plugin to help me with code quality.

You can find the api specs (Swagger style) at the /api-docs endpoint.

Also, if you use Postman to test your apis, you'll find both a collection and an environments example files at the /postman folder.

### Extra task one
For the extra task one (validate CPF), it seems that the validation service is not working properly. So, I configured a variable in the application.properties file to enable or disable the validation. The default value is false. To enable the validation, set the value to true.

### Extra task two
Regarding the extra task two (how to test performance of the application), an excellent choice for that is to use the JMeter application. With that application, we can create a test plan and configure how many threads we want to simulate to access the apis simultaneously, how many calls in each thread, pauses between the calls and so on.

### Extra task three
Finally, answering the extra task three, the best approach to version RestFul apis is providing a /v<<n>> between the base url and name of the service. Ideally, versioning occurs when, somehow, the contract data is broken. When, for example, a api starts demanding a new mandatory field that it didn't before. Versioning is important to avoid breaking the clients that are already using the api. The versioning should be done in a way that the clients can choose to migrate to the new version when they are ready.

There are some improvements I could implement targeting performance enhancement. Among them:
- change to webflux, since asynchronous calls are proved to be more efficient than synchronous;
- as said before, index points of interest in the database;
- pagination in the getAll apis;


### How to run the application LOCALLY
Having a docker and docker-compose installed, just run the following command in the root folder of the project (linux):

```
bash +x start.sh
bash ./start.sh
```

After running the command, all the apis will be available at http://localhost:8080
You also can find the PostmanCollection folder at the root folder with a postman collection and environment configuration to test the apis.

### How to run the application remotely
Alternatively, if you change and commit anything the *Render Cloud* based version will be automatically redeployed.
In order to access the cloud based apis you have to change the url to https://soft-design-challenge.onrender.com
The cloud version may take a while to start at first time, since it is a free tier version.

NOTES:
I didn't implement the part of the program where some kind of metadata for automation of form generation is returned for the mobile consumer. I talked with the interviewer and told her that I would return the proper JSON data from the apis as I usually do in my applications. I hope this will be enough.