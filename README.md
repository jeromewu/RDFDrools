# RDFDrools

## Introduction

RDFDrools is a program to combine the power of ontology and drools rule engine, so far it is still in an early stage.

## Build

The program is built by [maven](http://maven.apache.org/) build tool, just simple key in

```
$ mvn clean compile assembly:single
```

It will built into a signle jar file with all dependencies, so you don't have to add lots of jar file in the class path to run the program :)


## Execution

To execute the program, you will need to define rdf file, abbr file, rule file, json file and a sleep time

```
$ java -cp <jarfile> com.delta.RDFDrools <rdffile> <abbrfile> <jsonfile> <sleeptime>
```

* rdffile : the ontology in RDF/XML format
* abbrfile : the abbr file define the abbr. of the namespace, need to start with @prefix, a quick example
```
@prefix rdfs: <http://url.to.the.rdfs/rdfs#>
```
* jsonfile : the json file to define the input data, an example here
```
{
  "args": [
    {"property":"subClassOf", "propertyNS":"rdfs", "value":"Thing", "valueNS":"owl"},
    ...
    ]
}
```
* sleeptime : the sleep time is in ms, to specify how long the program will read the json file
* rulefile : the rule file is included in the jar file, the path is src/main/resources/com/delta/rule.drl, please check drools official website to write a rule

You can check makefile in the root directory to see the full example of Build and Execution, and the sample data is in the data directory

## Contact

Please feel free to contact jeromewus@gmail.com
