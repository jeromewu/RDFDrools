package com.delta;

import com.delta.OntUtility;
import com.delta.Context;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;
import java.lang.Integer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

public class RDFDrools
{
  final static String targetNameSpace = "http://www.delta.com.tw/runtime#";
  final static String targetLocalName = "target";

  public static void main( String[] args ) throws Exception
  {
    String rdfFilePath = args[0];
    String abbrFilePath = args[1];
    String JSONFilePath = args[2];
    String sleepTime = args[3];
    
    KieServices ks = KieServices.Factory.get();
    KieContainer kc = ks.getKieClasspathContainer();
    KieSession ksession = kc.newKieSession("RDFDroolsKS");
    ksession.setGlobal("gVar", new HashMap<String, Object>());
    //ksession.addEventListener( new DebugAgendaEventListener() );
    //ksession.addEventListener( new DebugRuleRuntimeEventListener() );

    Map<String, String> abbrMap = OntUtility.getAbbrMap(abbrFilePath);

    Model model = OntUtility.getModel(rdfFilePath);
    

    while(true) {
      Resource target = OntUtility.createResourceByJSONFile(
          model, 
          OntUtility.getURI(targetNameSpace, targetLocalName), 
          JSONFilePath, 
          abbrMap
          );

      Context context = new Context(model, target, abbrMap);

      FactHandle contextFactHandle = ksession.insert(context);
      ksession.fireAllRules();
      ksession.delete(contextFactHandle);

      model.removeAll(target, null, null);

      Thread.sleep(Integer.parseInt(sleepTime));
    }
    
    //ksession.dispose();
  }
}
