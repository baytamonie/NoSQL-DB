package driver.documentEntities;


public class NullEntity extends DocumentEntity {


    private static final NullEntity nullEntity = new NullEntity();

    private NullEntity(){

    }

    public static NullEntity getNullEntity(){
        return  nullEntity;
    }

    @Override
    public DocumentEntityType getEntityType() {
        return DocumentEntityType.NULL;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return "Null";
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {
        return 0;
    }
}
