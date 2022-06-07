package driver.documentEntities;


import java.util.List;

public class ArrayEntity extends DocumentEntity {
    @Override
    public DocumentEntityType getEntityType() {
        return DocumentEntityType.ARRAY;
    }

    private final List<DocumentEntity> entities;


    public ArrayEntity(List<DocumentEntity> entities) {
        this.entities = entities;
    }


    public List<DocumentEntity> getEntities() {
        return entities;
    }



    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {

        return 0;
    }
}
