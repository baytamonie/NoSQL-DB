package driver.documentEntities;
import java.util.Map;

public class ObjectEntity extends DocumentEntity {

    private final Map<DocumentEntity,DocumentEntity> entityMap;

    public ObjectEntity(Map<DocumentEntity, DocumentEntity> entityMap) {
        this.entityMap = entityMap;
    }

    public Map<DocumentEntity, DocumentEntity> getEntityMap() {
        return entityMap;
    }

    @Override
    public DocumentEntityType getEntityType() {
        return null;
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {
        return 0;
    }
}