package cache;

import org.json.simple.JSONObject;

public interface Cache {

    Object get(String key);


    void put(String key, JSONObject value);
}
