package com.delta.rdfdrools;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RDFDrools
{
  private static final String query =
    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
    +"SELECT ?s ?o "
    +"WHERE { "
    +"?s rdfs:subClassOf ?o "
    +"}";
  public static void main( String[] args )
  {
    QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/deltaiabg/query", query);
    ResultSet results = qe.execSelect();
    Resource res = results.next().get("?s").asResource();
    System.out.println("NameSpace: " + res.getNameSpace());
    System.out.println("getLocalName: "+res.getLocalName());
    qe.close();

    KieServices ks = KieServices.Factory.get();
    KieContainer kc = ks.getKieClasspathContainer();
    KieSession ksession = kc.newKieSession("RDFDroolsKS");
    //ksession.addEventListener( new DebugAgendaEventListener() );
    //ksession.addEventListener( new DebugRuleRuntimeEventListener() );
    ksession.setGlobal("list", new ArrayList<Object>());

    ksession.insert(res);

    ksession.fireAllRules();
    ksession.dispose();
  }
}
