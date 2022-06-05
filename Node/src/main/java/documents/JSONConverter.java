package documents;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import documents.entities.*;

import javax.print.Doc;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

public class JSONConverter extends TypeAdapter<DocumentEntity>{

    private JSONConverter(){

    }
    private  static final JSONConverter converter = new JSONConverter();

    public static JSONConverter getConverter(){
        return converter;
    }

    @Override
    public void write(JsonWriter jsonWriter, DocumentEntity documentEntity) throws IOException {
        if(documentEntity == null){
            jsonWriter.nullValue();
            return;
        }
        DocumentEntityType type = documentEntity.getEntityType();
        switch (type){
            case INT:
                jsonWriter.value(((IntegerEntity) documentEntity).getValue());
                break;
            case DECIMAL:
                jsonWriter.value(((DecimalEntity) documentEntity).getValue());
                break;
            case STRING:
                jsonWriter.value(((StringEntity) documentEntity).getValue());
                break;
            case BOOL:
                jsonWriter.value(((BoolEntity) documentEntity).getValue());
                break;
            case ARRAY:
                jsonWriter.beginArray();
                for (DocumentEntity entity : ((ArrayEntity) documentEntity).getEntities()) {
                    write(jsonWriter, entity);
                }
                jsonWriter.endArray();
                break;
            case OBJECT:
                jsonWriter.beginObject();
                for(Map.Entry<DocumentEntity,DocumentEntity> entry: ((ObjectEntity) documentEntity).getEntityMap().entrySet()){
                    jsonWriter.name(((StringEntity) entry.getKey()).getValue());
                    write(jsonWriter, entry.getValue());
                }
                jsonWriter.endObject();
                break;
            default:
                throw new IllegalArgumentException();
        }



    }

    @Override
    public DocumentEntity read(JsonReader jsonReader) throws IOException {
        switch (jsonReader.peek()){
            case STRING:
                return new StringEntity(jsonReader.nextString());
            case NUMBER:
                String number = jsonReader.nextString();
                Pattern decimalOrInteger = Pattern.compile("[.eE]");
                if(decimalOrInteger.matcher(number).find()){
                    return new DecimalEntity(new Double(number));
                }
                else
                    return new IntegerEntity(Long.parseLong(number));
            case BOOLEAN:
                return new BoolEntity(jsonReader.nextBoolean());
            case NULL:
                jsonReader.nextNull();
                return NullEntity.getNullEntity();
            case BEGIN_ARRAY:
                LinkedList<DocumentEntity> entities = new LinkedList<>();
                jsonReader.beginArray();
                while(jsonReader.hasNext()){
                    entities.add(read(jsonReader));
                }
                jsonReader.endArray();
                return new ArrayEntity(entities);

            case BEGIN_OBJECT:
                HashMap<DocumentEntity, DocumentEntity> entityHashMap = new HashMap<>();
                jsonReader.beginObject();
                while(jsonReader.hasNext()){
                    entityHashMap.put(new StringEntity(jsonReader.nextName()),read(jsonReader));
                }
                jsonReader.endObject();
                return new ObjectEntity(entityHashMap);


            default:
                throw new IllegalArgumentException();

        }

    }

}
