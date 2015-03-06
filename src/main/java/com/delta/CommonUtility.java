package com.delta;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CommonUtility {
    public static String extractString(String input, String regexp) {
      Pattern pattern = Pattern.compile(regexp);
      Matcher matcher = pattern.matcher(input);
      matcher.find();
      return matcher.group(1);
    }
    
    public static JSONObject parseJSON(String JSONFilePath) throws Exception {
      JSONParser parser = new JSONParser();
      return (JSONObject)parser.parse(new FileReader(JSONFilePath));
    }
}
