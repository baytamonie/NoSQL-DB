package documents.entities;

public class BoolEntity extends DocumentEntity{

    private  final boolean value;

    public BoolEntity(boolean value) {
        this.value = value;
    }
    public boolean getValue(){
        return value;
    }
    @Override
    public DocumentEntityType getEntityType() {
        return DocumentEntityType.BOOL;
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {
        if(this == entity)
            return 0;
        else if(value)
            return 1;
        else return -1;

    }
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String toString() {
        return "BoolEntity{" +
                "value=" + value +
                '}';
    }
}
