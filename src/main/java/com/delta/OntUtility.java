package com.delta;

import com.delta.CommonUtility;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;

import java.lang.Float;
import java.lang.Double;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Short;
import java.lang.Byte;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.Boolean;
import java.lang.String;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;

import com.hp.hpl.jena.util.FileManager;

import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

import com.hp.hpl.jena.util.PrintUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OntUtility {
    
    public static Map<String, String> getAbbrMap(String abbrFilePath) throws Exception {
      Map<String, String> abbrMap = new HashMap<String, String>();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(abbrFilePath));
      String line = new String("");
      while((line = bufferedReader.readLine()) != null) {
        if(line.startsWith("@prefix")) {
          String[] seg = line.split(" ");
          String abbr = seg[1].substring(0, seg[1].length()-1);
          String uri = CommonUtility.extractString(seg[2], ".*\\< *(.*) *\\>*.");
          abbrMap.put(abbr, uri);
        }
      }
      registerAbbr(abbrMap);
      return abbrMap;
    }
    
    public static void registerAbbr(Map<String, String> abbrMap) {
      for(String key : abbrMap.keySet()) {
        PrintUtil.registerPrefix(key, abbrMap.get(key));
      }
    }

    public static String getURI(String nameSpace, String localName) {
      return nameSpace + localName;
    }

    public static Model getModel(String filePath) {
      return FileManager.get().loadModel("file:"+filePath);
    }

    public static Reasoner getReasoner(String ruleFilePath) {
      Model model = ModelFactory.createDefaultModel();
      Resource config = model.createResource();
      config.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
      config.addProperty(ReasonerVocabulary.PROPruleSet, ruleFilePath);
      return GenericRuleReasonerFactory.theInstance().create(config);
    }

    public static Literal getLiteral(Model model, String valueNS, String value) {
      Literal literal = null;
      if(valueNS.contains("float")) {
        literal = model.createTypedLiteral(new Float(Float.parseFloat(value)));
      } else if(valueNS.contains("double")) {
        literal = model.createTypedLiteral(new Double(Double.parseDouble(value)));
      } else if(valueNS.contains("int")) {
        literal = model.createTypedLiteral(new Integer(Integer.parseInt(value)));
      } else if(valueNS.contains("long")) {
        literal = model.createTypedLiteral(new Long(Long.parseLong(value)));
      } else if(valueNS.contains("short")) {
        literal = model.createTypedLiteral(new Short(Short.parseShort(value)));
      } else if(valueNS.contains("byte")) {
        literal = model.createTypedLiteral(new Byte(Byte.parseByte(value)));
      } else if(valueNS.contains("integer")) {
        literal = model.createTypedLiteral(new BigInteger(value));
      } else if(valueNS.contains("decimal")) {
        literal = model.createTypedLiteral(new BigDecimal(value));
      } else if(valueNS.contains("boolean")) {
        literal = model.createTypedLiteral(new Boolean(Boolean.parseBoolean(value)));
      } else if(valueNS.contains("string")) {
        literal = model.createTypedLiteral(value);
      } else {
        literal = model.createTypedLiteral(value);
      }
      return literal;
    }

    /* Call registerAbbr() first to get abbreviation of the namespace*/
    public static void printAllStatement(Model model) {
      StmtIterator iter = model.listStatements();
      while(iter.hasNext()) {
        System.out.println(PrintUtil.print(iter.nextStatement()));
      }
    }

    /*
     * JSON format
     * {
     *   "args":[
     *      {"property":"drinkWine", "propertyNS":"wine", "value":"RedWine", "valueNS":"wine"},
     *      {...}
     *   ]
     * }
     *
     * The abbrevation of the Namespace need to be defined in abbrMap
     *
     */
    @SuppressWarnings("unchecked")
    public static Resource createResourceByJSONFile(Model model, String resourceURI, String JSONFilePath, Map<String, String> abbrMap) throws Exception{
      Resource target = model.createResource(resourceURI);
      JSONObject jsonObject = CommonUtility.parseJSON(JSONFilePath);

      JSONArray args = (JSONArray)jsonObject.get("args");
      Iterator<JSONObject> iterator = args.iterator();
      while(iterator.hasNext()) {
        JSONObject jsonObj = iterator.next();

        String propertyNS = (String)jsonObj.get("propertyNS");
        String property = (String)jsonObj.get("property");
        String valueNS = (String)jsonObj.get("valueNS");
        String value = (String)jsonObj.get("value");

        Property ontProperty = model.getProperty(abbrMap.get(propertyNS), property);
        if(!valueNS.contains("xsd")) {
          Resource resource = model.getResource(getURI(abbrMap.get(valueNS), value));
          target.addProperty(ontProperty, resource);
        } else {
          Literal literal = getLiteral(model, valueNS, value);
          target.addProperty(ontProperty, literal);
        }
      }
      return target;
    }
}
