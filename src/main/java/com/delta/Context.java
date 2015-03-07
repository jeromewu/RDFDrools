package com.delta;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Context {
  private Model model;
  private Resource target;
  private Map<String, String> abbrMap;

  Context() {
    ;
  }
  Context(Model model, Resource target, Map<String, String> abbrMap) {
    this.model = model;
    this.target = target;
    this.abbrMap = abbrMap;
  }

  public float getLiteralFloat(String propertyURI) {
    return getLiteral(propertyURI).getFloat();
  }

  public boolean getLiteralBoolean(String propertyURI) {
    return getLiteral(propertyURI).getBoolean();
  }

  public String getResourceURI(String propertyURI) {
    Resource resource = getStatementObject(propertyURI).asResource();
    String resourceNameSpace = abbrMap.get(resource.getNameSpace());
    String resourceLocalName = resource.getLocalName();
    return resourceNameSpace + ":" + resourceLocalName;
  }

  private Literal getLiteral(String propertyURI) {
    Literal literal = getStatementObject(propertyURI).asLiteral();
    return literal;
  }

  private RDFNode getStatementObject(String propertyURI) {
    String propertyNameSpace = propertyURI.split(":")[0];
    String propertyLocalName = propertyURI.split(":")[1];
    Property property = model.getProperty(abbrMap.get(propertyNameSpace), propertyLocalName);
    Statement statement = target.getProperty(property);
    return statement.getObject();
  }
}
