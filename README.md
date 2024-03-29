Shorten URL is a modern scalable application written in Java using Redis and Docker to containerize

<B>#Tech Stack</B>
JDK 1.8
Docker
Redis
Springboot
maven

<B>#Key Features</B>
Powered with Docker Containerization
MicroServices/Springboot
Redis
Analytics 

<B>#Run the project</B>

1. Clone from Git
2. Create a Docker Environment(install docker-compose)--I used CentOS
3. Build the code using:-
mvn clean install 
4. Copy project in Docker
5. Under the docker folder, execute below command:-
  <br>docker-compose build</br>
  <br>docker-compose up</br>
<br>This step will start 2 container, one for Redis and other for the Application</br>
Application is exposed at port 8080 by default which can be changed in docker.yml and DockerFile
6. Execute below APIs which have been exposed:-
<br>#Get Short URL
http://<Docker/localhost>:8080/shorten/<LongUrl>
<br>#Get Long URL for the short URL returned above
http://<Docker/localhost>:8080/ShortUrl
<br>#Get Count for the Hits for Short URL
http://<Docker/localhost>:8080/stats/<ShortUrl>
7. You can also use Curl commands to test the APIs
  
