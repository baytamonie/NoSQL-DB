package driver.documentEntities;


public class StringEntity extends DocumentEntity{

    private final String value;

    public StringEntity(String value) {
        this.value = value;
    }
    public int stringLength(){
        return this.value.length();
    }

    public String getValue() {
        return value;
    }

    @Override
    public DocumentEntityType getEntityType() {
        return DocumentEntityType.STRING;
    }

    @Override
    protected int compareToSameType(DocumentEntity entity) {
        String str1 = value;
        String str2 = ((StringEntity)entity).getValue();
        int l1 = str1.length();
        int l2 = ((StringEntity)entity).getValue().length();
        int lmin = Math.min(l1, l2);
        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }
        if (l1 != l2) {
            return l1 - l2;
        }
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "StringEntity{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        StringEntity stringEntity = (StringEntity) obj;
        return value.equals(stringEntity.value);
    }

}
