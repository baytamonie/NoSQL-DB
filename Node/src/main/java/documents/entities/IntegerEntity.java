package documents.entities;

public class IntegerEntity extends DocumentEntity{

    private final long value;

    public IntegerEntity(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public DocumentEntityType getEntityType() {
        return DocumentEntityType.INT;
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {
            return Long.compare(value, ((IntegerEntity) entity).value);
    }

    @Override
    public String toString() {
        return "IntegerEntity{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        else{
            return this.value ==  ((IntegerEntity) obj) .getValue();
        }

    }
}
