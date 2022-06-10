package documents;

import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JSONDocument implements Iterable<JSONObject> {

  private final List<JSONObject> jsonDocument;
  private int currentSize;

  public JSONDocument() {
    jsonDocument = new LinkedList<>();
    currentSize = 0;
  }

  public List<JSONObject> getJsonDocument() {
    return jsonDocument;
  }

  public void addJsonProperty(JSONObject jsonObject) {
    if (jsonObject == null) throw new IllegalArgumentException();
    jsonDocument.add(jsonObject);
    currentSize++;
  }


  public JSONDocument(List<JSONObject> jsonObjects) {
    if (jsonObjects == null) throw new IllegalArgumentException();
    this.jsonDocument = jsonObjects;
    this.currentSize = jsonDocument.size();
  }

  @Override
  public Iterator<JSONObject> iterator() {
    Iterator<JSONObject> it =
        new Iterator<JSONObject>() {

          private int currentIndex = 0;

          @Override
          public boolean hasNext() {
            return currentIndex < currentSize && jsonDocument.get(currentIndex) != null;
          }

          @Override
          public JSONObject next() {
            return jsonDocument.get(currentIndex++);
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
    return it;
  }
}