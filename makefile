jarfile=./target/rdfdrools-1.0-SNAPSHOT-jar-with-dependencies.jar
log4jproperties=./src/main/resources/log4j.properties
main=com.delta.RDFDrools
rdffile=./data/rdf/accontrol.rdf
abbrfile=./data/abbr/abbr.dat
jsonfile=./data/json/args.json
sleeptime=3000

all: compile

compile:
	mvn clean compile assembly:single

run:
	java -cp ${jarfile} -Dlog4j.configuration=file:${log4jproperties} ${main} ${rdffile} ${abbrfile} ${jsonfile} ${sleeptime}

clean:
	mvn clean
