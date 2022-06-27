<h1 align="center">Order-Processing-Management v1</h1>

<div align="center">
The admin layer for OSP, which manages all the orders and we need to interact with kafka to push order details to message queue.
<br/>
<br/>
<img src="https://img.shields.io/maintenance/yes/2022.svg" alt="maintained">
<img src="https://img.shields.io/static/v1.svg?label=java&message=11&color=informational" alt="java version">
</div>

### App Links

| env  | api |
| ---- | --- |
| Local | http://localhost:8080 |

## Local Setup

You will need Gradle 5, JDK 8 or newer, kafka and MongoDB installed. We suggest managing installations with Homebrew or
SDKMAN!

You can set this either via properties or via the environment

## kafka Setup

To setup kafka locally on your system, Run homebrew installer

```
brew install kafka
```

Now start Zookeeper and Kafka:

Start Zookeeper:

```
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties
```

Start Kafka server:

```
kafka-server-start /usr/local/etc/kafka/server.properties
```

WARNING:

During server start, you might be facing connection broken issue. To fix this issue, we need to change the
server.properties file.

```
vim /usr/local/etc/kafka/server.properties
```

Here uncomment the server settings of listeners line and update the value of listeners to

```
listeners = PLAINTEXT://your.host.name:9092
```

and restart the server and it will work great.

Create Kafka Topic:

```
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
```

Initialize Producer console:
Now we will initialize the Kafka producer console, which will listen to localhost at port 9092 at topic test :

```
kafka-console-producer --broker-list localhost:9092 --topic test
```

Initialize Consumer console:

Now we will initialize the Kafka consumer console, which will listen to bootstrap server localhost at port 9092 at topic
test from beginning:

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning
```

### Working with OPM

To start the OPM application, use IntelliJ, your chosen IDE, or Terminal, and type:

```
./gradlew clean build
./gradlew run
```

You should see the project build, run unit tests, and run quality control before finally ending in successful build
after the first command. After the second, the application should be started on localhost:8080. You will use this link
with your MongoDB collection to test API calls.

You can also run this command if you only want to check tests.

```
./gradlew test
```

## Structure

The default file structure looks like this:

```
app
├── gradle/                  # optimized, production-ready code
├── src/                     # the app source code goes here
    ├── main                 # all things relating to the code
        ├── ...api/       # the code
            ├── config/      # configuration setups
            ├── constants/   # constants
            ├── controller/  # API endpoints
            ├── dto/         # objects used to transfer data
            ├── services/    # the code
            ├── App.groovy   # the main application
        ├── resources/       # the settings for the code
    ├── test/                # all things relating to testing
├── .gitignore               # what files to ignore within version control
├── build.gradle             # gradle configuration
├── Dockerfile               # docker configuration
├── gradlew                  # gradle wrapper
├── gradlew.bat              # a batch file used on Windows
└── README.md                # this file
```