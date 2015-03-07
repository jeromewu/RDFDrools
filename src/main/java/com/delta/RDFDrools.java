package com.delta;

import com.delta.OntUtility;
import com.delta.Context;

import java.util.ArrayList;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class RDFDrools
{
  final static String targetNameSpace = "http://www.delta.com.tw/runtime#";
  final static String targetLocalName = "target";

  public static void main( String[] args ) throws Exception
  {
    String rdfFilePath = args[0];
    String abbrFilePath = args[1];
    String JSONFilePath = args[2];

    Map<String, String> abbrMap = OntUtility.getAbbrMap(abbrFilePath);

    Model model = OntUtility.getModel(rdfFilePath);
    Resource target = OntUtility.createResourceByJSONFile(
        model, 
        OntUtility.getURI(targetNameSpace, targetLocalName), 
        JSONFilePath, 
        abbrMap
        );

    Context context = new Context(model, target, abbrMap);

    KieServices ks = KieServices.Factory.get();
    KieContainer kc = ks.getKieClasspathContainer();
    KieSession ksession = kc.newKieSession("RDFDroolsKS");
    //ksession.addEventListener( new DebugAgendaEventListener() );
    //ksession.addEventListener( new DebugRuleRuntimeEventListener() );
    //ksession.setGlobal("list", new ArrayList<Object>());

    ksession.insert(context);

    ksession.fireAllRules();
    ksession.dispose();
    
    model.removeAll(target, null, null);
  }
}
