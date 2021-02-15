package com.inovisionsoftware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class ObjectParser {

    final static String ROOT = "root";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    final private String objectJsonStr;
    final Map<String, String> valueMap = new HashMap<>();
    protected List<String> excludeList = new ArrayList<String>(5);
    final private Stack<String> stack = new Stack<String>();

    public ObjectParser(Object object, List<String> excludeList) throws JsonProcessingException {
        if(object == null) throw new IllegalArgumentException("Object cannot be null");
        this.objectJsonStr = OBJECT_MAPPER.writeValueAsString(object);
        this.excludeList = excludeList;
        parse();
    }

    public String getValue(String xpath) {
        return valueMap.get(ROOT + "." + xpath);
    }

    private void parse() {
        JsonElement lhsRoot = JsonParser.parseString(objectJsonStr);
        stack.push(ROOT);
        if(isArray(lhsRoot)) {
            parse(lhsRoot.getAsJsonArray());
        } else if(isObject(lhsRoot)) {
            parse(lhsRoot.getAsJsonObject());
        } else if(isPrimitive(lhsRoot)) {
            valueMap.put(getXPath(), lhsRoot.getAsString());
        }
    }

    private void parse(JsonArray lhsArr) {
        Iterator<JsonElement> lhsItr = lhsArr.iterator();
        int count = 0 ;
        String name = stack.peek();
        while(lhsItr.hasNext()) {
            stack.pop();
            stack.push(String.format(name + "[%d]",  count++));
            JsonElement lhsEle = lhsItr.next();
            if(isArray(lhsEle)) {
                parse(lhsEle.getAsJsonArray());
            } else if(isObject(lhsEle)) {
                parse(lhsEle.getAsJsonObject());
            } else if(isPrimitive(lhsEle)) {
                valueMap.put(getXPath(), lhsEle.getAsString());
            }
        }
    }

    private void parse(JsonObject lhsObject) {
        Set<Entry<String, JsonElement>> lhsSet = lhsObject.entrySet();
        Iterator<Entry<String, JsonElement>> lhsKeyIterator = lhsSet.iterator();
        Set<String> masterKeyList = new TreeSet<String>();
        while(lhsKeyIterator.hasNext()) {
            masterKeyList.add(lhsKeyIterator.next().getKey());
        }

        Iterator<String> keyiterator = masterKeyList.iterator();//lhsKeys.iterator();
        while(keyiterator.hasNext()) {
            String key = keyiterator.next();
            if(excludeList != null && excludeList.contains(key)) {
                //skip nodes which are on exclude list
                continue;
            }
            stack.push(key);
            JsonElement ele1 = lhsObject.get(key);
            if(isArray(ele1)) {
                parse(ele1.getAsJsonArray());
            } else if(isObject(ele1)) {
                parse(ele1.getAsJsonObject());
            } else if(isPrimitive(ele1)) {
                valueMap.put(getXPath(), ele1.getAsString());
            }
            stack.pop();
        }
    }

    private String getXPath() {
        StringBuilder buf = new StringBuilder();
        if(!stack.isEmpty()) {
            Iterator<String> itr = stack.iterator();
            buf.append(itr.next());
            while(itr.hasNext()) {
                buf.append(".");
                buf.append(itr.next());
            }
        }
        return buf.toString();
    }

    private boolean isObject(JsonElement ele) {
        return (ele != null && ele.isJsonObject() );
    }

    private boolean isPrimitive(JsonElement ele) {
        return (ele != null && ele.isJsonPrimitive() );
    }

    private boolean isArray(JsonElement ele) {
        return (ele != null && ele.isJsonArray() );
    }

}
