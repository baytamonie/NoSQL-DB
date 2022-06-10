package driver;

import driver.documentEntities.DocumentEntity;
import driver.documentEntities.DocumentEntityType;

import java.util.HashMap;

public class JSONDocument {
    private final HashMap<String, DocumentEntity> data;

    public JSONDocument(HashMap<String, DocumentEntity> data) {
        this.data = data;
    }

    public HashMap<String, DocumentEntity> getData() {
        return data;
    }
    public DocumentEntity getEntity(String key){
        return data.get(key);
    }
    public DocumentEntityType getEntityType(String key){
        return data.get(key).getEntityType();
    }
}
