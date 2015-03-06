jarfile=./target/rdfdrools-1.0-SNAPSHOT-jar-with-dependencies.jar
log4jproperties=./src/main/resources/log4j.properties

all: compile

compile:
	mvn clean compile assembly:single

run:
	java -cp ${jarfile} -Dlog4j.configuration=file:${log4jproperties} com.delta.rdfdrools.RDFDrools

clean:
	mvn clean
