package driver.documentEntities;


public class DecimalEntity extends DocumentEntity{
    private final double value;

    public DecimalEntity(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public DocumentEntityType getEntityType() {
        return DocumentEntityType.DECIMAL;
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {
        return Double.compare(value, ((DecimalEntity) entity).value);
    }

    @Override
    public String toString() {
        return "DecimalEntity{" +
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