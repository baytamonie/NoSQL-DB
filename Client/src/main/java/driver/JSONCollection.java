package driver;

import java.util.List;

public class JSONCollection  {
    private final String name;
    private List<JSONDocument> jsonDocuments;

    public JSONCollection(String name) {
        this.name = name;
    }

    public void setJsonDocuments(List<JSONDocument> jsonDocuments) {
        if(jsonDocuments==null)
            throw new IllegalArgumentException();
        this.jsonDocuments = jsonDocuments;
    }

    public String getName() {
        return name;
    }

    public List<JSONDocument> getJsonDocuments() {
        return jsonDocuments;
    }
}
