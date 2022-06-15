package documents.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class JSONSchema {

    private final JSONObject jsonObject;


    public JSONSchema(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Class getPropertyType(String key){
        String value = (String) jsonObject.get(key);
        switch (value){
            case "integer":
                return Integer.class;
            case "string":
                return String.class;
            case "long":
                return Long.class;
            case "double":
                return Double.class;
            case "float":
                return Float.class;
            case "boolean":
                return Boolean.class;
            case "char":
                return Character.class;
            default:
                return null;
        }
    }


}
