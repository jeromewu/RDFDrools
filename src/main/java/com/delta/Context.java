package com.delta;

import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Statement;

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

  public float getLiteralFloat(String propertyNameSpace, String propertyLocalName) {
    return getLiteral(propertyNameSpace, propertyLocalName).getFloat();
  }

  public boolean getLiteralBoolean(String propertyNameSpace, String propertyLocalName) {
    return getLiteral(propertyNameSpace, propertyLocalName).getBoolean();
  }

  private Literal getLiteral(String propertyNameSpace, String propertyLocalName) {
    Property property = model.getProperty(abbrMap.get(propertyNameSpace), propertyLocalName);
    Statement statement = target.getProperty(property);
    Literal literal = statement.getObject().asLiteral();
    return literal;
  }
}
